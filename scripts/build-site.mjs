// T2-2. 사이트가 읽을 데이터 번들을 만든다.
//   node scripts/build-site.mjs  ->  site/data/bundle.js
//
// 왜 .json 이 아니라 .js 인가:
// file:// 로 열면 브라우저가 fetch 를 막는다. <script src> 는 막지 않으므로,
// 번들을 전역 변수에 담아두면 GitHub Pages 에서도, 로컬에서 index.html 을 더블클릭해도 똑같이 돈다.
import fs from 'node:fs';
import { execSync } from 'node:child_process';
import path from 'node:path';
import { ROOT, authors, walkRepo, resolveSource } from './lib/map.mjs';

const tagDefs = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/review-tags.json'), 'utf8')).tags;
const rotation = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/rotation.json'), 'utf8'));

// T4-1. 정석 코드 레퍼런스. references/{platform}/{problemId}/*.java 를 그대로 스캔한다
// (중앙 manifest 없음 — 파일 맨 위 `// ref-*:` 주석이 메타를 담는다. javac 로도 그대로 컴파일된다).
function readReferences(platform, problemId) {
  const dir = path.join(ROOT, 'references', platform, problemId);
  if (!fs.existsSync(dir)) return [];
  return fs.readdirSync(dir).filter((f) => f.endsWith('.java')).sort().map((f) => {
    const raw = fs.readFileSync(path.join(dir, f), 'utf8').replace(/\r\n/g, '\n');
    const lines = raw.split('\n');
    const meta = {};
    let i = 0;
    while (i < lines.length && /^\/\/\s*ref-\w+:/.test(lines[i])) {
      const m = lines[i].match(/^\/\/\s*ref-(\w+):\s*(.*)$/);
      if (m) meta[m[1]] = m[2].trim();
      i++;
    }
    return {
      refId: f.replace(/\.java$/, ''),
      sourceTitle: meta.title || '',
      url: meta.url || '',
      note: meta.note || '',
      code: lines.slice(i).join('\n').replace(/^\n+/, '').replace(/\s+$/, ''),
    };
  });
}

function readReview(rel) {
  const abs = path.join(ROOT, rel);
  if (!fs.existsSync(abs)) return null;
  const text = fs.readFileSync(abs, 'utf8').replace(/\r\n/g, '\n');
  const m = text.match(/^---\n([\s\S]*?)\n---\n?/);
  if (!m) return { meta: {}, body: text };

  const meta = {};
  for (const line of m[1].split('\n')) {
    const kv = line.match(/^(\w+):\s*(.*)$/);
    if (!kv) continue;
    const [, k, raw] = kv;
    const v = raw.trim().replace(/^["']|["']$/g, '');
    if (k === 'tags') meta.tags = v.replace(/^\[|\]$/g, '').split(',').map((s) => s.trim()).filter(Boolean);
    else if (v) meta[k] = v;
  }
  // complexity 는 중첩이라 별도로 긁는다
  const time = m[1].match(/^\s+time:\s*(.+)$/m)?.[1]?.trim();
  const space = m[1].match(/^\s+space:\s*(.+)$/m)?.[1]?.trim();
  if (time || space) meta.complexity = { time, space };

  return { meta, body: text.slice(m[0].length).trim() };
}

// 파일별 마지막 커밋 날짜. git 을 파일마다 부르면 느려서 한 번에 훑어 표를 만든다.
// 한글 경로가 octal escape 로 나오지 않게 core.quotepath=false 가 반드시 필요하다.
const commitDates = (() => {
  const table = new Map();
  try {
    const out = execSync('git -c core.quotepath=false log --format=%x00%ad --date=short --name-only',
      { cwd: ROOT, encoding: 'utf8', maxBuffer: 64 * 1024 * 1024 });
    let date = null;
    for (const line of out.split('\n')) {
      if (line.startsWith('\0')) { date = line.slice(1).trim(); continue; }
      const file = line.trim();
      if (!file || !date) continue;
      if (!table.has(file)) table.set(file, date);   // 최신 커밋부터 나오므로 첫 등장이 마지막 수정일
    }
  } catch {
    // git 이 없거나 저장소가 아니면 날짜 없이 동작한다 (변형 정렬만 파일명 순으로 떨어진다)
  }
  return table;
})();
const lastCommitDate = (src) => commitDates.get(src) || '';

const solutions = walkRepo().map(resolveSource).filter((r) => r.ok);
const problems = new Map();

for (const s of solutions) {
  if (!problems.has(s.key)) {
    problems.set(s.key, {
      key: s.key, platform: s.platform, problemId: s.problemId,
      title: s.title, url: s.url, weeks: new Set(), entries: [],
    });
  }
  const p = problems.get(s.key);
  if (s.week) p.weeks.add(s.week);

  const review = readReview(s.reviewTarget);
  p.entries.push({
    author: s.author,
    authorName: s.authorName,
    week: s.week,
    variant: s.variant,
    source: s.source,
    lines: s.lines,
    code: fs.readFileSync(path.join(ROOT, s.source), 'utf8').replace(/\r\n/g, '\n').replace(/\s+$/, ''),
    review: review ? { ...review.meta, body: review.body } : null,
    committedAt: lastCommitDate(s.source),
  });
}

// 같은 사람이 한 문제에 여러 파일을 올렸으면 **가장 최근에 올린 것**이 대표다.
// 나머지(실패/대안 버전)는 대표에 alts 로 붙여서 사이트에서 버튼 뒤에 둔다.
for (const p of problems.values()) {
  const byAuthor = new Map();
  for (const e of p.entries) {
    if (!byAuthor.has(e.author)) byAuthor.set(e.author, []);
    byAuthor.get(e.author).push(e);
  }
  const kept = [];
  for (const group of byAuthor.values()) {
    // 최근 커밋이 먼저. 날짜가 같으면 변형이 아닌 쪽(대표 이름)이 먼저.
    group.sort((a, b) =>
      (b.committedAt || '').localeCompare(a.committedAt || '') ||
      (a.variant ? 1 : 0) - (b.variant ? 1 : 0) ||
      a.source.localeCompare(b.source));
    const [primary, ...alts] = group;
    primary.alts = alts;
    kept.push(primary);
  }
  p.entries = kept;
}

// T3-4. 발표 후 팀 피드백. 문제 단위이므로 개별 리뷰와 달리 문제당 한 개.
for (const p of problems.values()) {
  const fb = path.join(ROOT, `reviews/${p.platform}/${p.problemId}/team-feedback.md`);
  p.teamFeedback = fs.existsSync(fb)
    ? fs.readFileSync(fb, 'utf8').replace(/\r\n/g, '\n').replace(/^---\n[\s\S]*?\n---\n?/, '').trim()
    : null;
  p.references = readReferences(p.platform, p.problemId);
}

const list = [...problems.values()]
  .map((p) => ({ ...p, weeks: [...p.weeks].sort((a, b) => a - b) }))
  .sort((a, b) => a.platform.localeCompare(b.platform) || Number(a.problemId) - Number(b.problemId));

// 작성자 순서를 고정해서 비교 뷰의 열 순서가 매번 같게 한다
const order = authors.map((a) => a.id);
for (const p of list) {
  p.entries.sort((a, b) => order.indexOf(a.author) - order.indexOf(b.author) || (a.variant ? 1 : -1));
}

const data = {
  generatedAt: new Date().toISOString().slice(0, 10),
  authors: authors.map(({ id, displayName, active }) => ({ id, displayName, active: active !== false })),
  tags: tagDefs,
  rotation: { order: rotation.order, cadence: rotation.cadence, assignments: rotation.assignments },
  problems: list,
};

const outPath = path.join(ROOT, 'site/data/bundle.js');
fs.mkdirSync(path.dirname(outPath), { recursive: true });
fs.writeFileSync(outPath, `window.STUDY_DATA = ${JSON.stringify(data)};\n`);

const reviewed = list.flatMap((p) => p.entries).filter((e) => e.review).length;
const total = list.reduce((a, p) => a + p.entries.length, 0);
const refs = list.reduce((a, p) => a + p.references.length, 0);
const kb = Math.round(fs.statSync(outPath).size / 1024);
console.log(`-> site/data/bundle.js  (${list.length}문제 / ${total}풀이 / 리뷰 ${reviewed}건 / 정석 코드 ${refs}건 / ${kb}KB)`);
