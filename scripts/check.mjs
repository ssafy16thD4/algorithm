// 무결성 검사. 데이터 파일과 리뷰 마크다운이 규칙을 지키는지 확인한다.
//   node scripts/check.mjs
// 실패가 하나라도 있으면 exit 1 (나중에 CI에 그대로 붙일 수 있게).
import fs from 'node:fs';
import path from 'node:path';
import { ROOT, authors, walkRepo, resolveSource } from './lib/map.mjs';

const fail = [];
const warn = [];
const ok = [];
const check = (cond, msg, list = fail) => (cond ? ok.push(msg) : list.push(msg));

// ---------- 1. 데이터 파일 ----------
const problems = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/problems.json'), 'utf8')).problems;
const tagDefs = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/review-tags.json'), 'utf8')).tags;
const validTags = new Set(tagDefs.map((t) => t.id));
const authorIds = new Set(authors.map((a) => a.id));

const keys = problems.map((p) => `${p.platform}/${p.problemId}`);
check(new Set(keys).size === keys.length, `problems.json 키 중복 없음 (${keys.length}개)`);

// 같은 문제 안의 표기 변형(`가장 긴 팰린드롬` / `가장긴팰린드롬`)은 정상 — 정규화하면 같아진다.
// 문제가 되는 건 하나의 alias 가 서로 '다른' 문제 두 개를 가리키는 경우뿐이다.
const norm = (a) => a.normalize('NFC').replace(/\s+/g, '').toLowerCase();
const aliasOwners = new Map();
for (const p of problems) {
  const key = `${p.platform}/${p.problemId}`;
  for (const a of p.aliases) {
    if (!aliasOwners.has(norm(a))) aliasOwners.set(norm(a), new Set());
    aliasOwners.get(norm(a)).add(key);
  }
}
const ambiguous = [...aliasOwners].filter(([, owners]) => owners.size > 1);
check(
  ambiguous.length === 0,
  `alias 모호성 0건${ambiguous.length ? ` -> ${ambiguous.map(([a, o]) => `${a}(${[...o].join(' vs ')})`).join(', ')}` : ''}`
);

// 한 문제 안에서 정규화 후 같아지는 alias 는 지워도 되는 잉여물이다 (동작에는 영향 없음).
for (const p of problems) {
  const seen = new Map();
  for (const a of p.aliases) {
    if (seen.has(norm(a))) warn.push(`problems.json ${p.platform}/${p.problemId}: '${a}' 는 '${seen.get(norm(a))}' 와 중복 (지워도 됨)`);
    else seen.set(norm(a), a);
  }
}

check(new Set(tagDefs.map((t) => t.id)).size === tagDefs.length, `태그 id 중복 없음 (${tagDefs.length}개)`);
check(
  problems.every((p) => p.platform !== 'programmers' || p.url),
  'programmers 문제는 모두 url 보유'
);

// ---------- 2. 소스 스캔 ----------
const files = walkRepo();
const results = files.map(resolveSource);
const solutions = results.filter((r) => r.ok);
const unmapped = results.filter((r) => !r.ok && r.reason === 'unmapped-title');
// 매핑 안 된 파일은 버그가 아니라 "아직 alias를 안 붙인 새 풀이"일 뿐이다 — 그냥 인덱싱에서
// 빠질 뿐 사이트는 멀쩡히 돈다. 문제 제목에 무슨 문자가 들어있든(띄어쓰기·영어·한글 등) 배포
// 파이프라인이 이것 때문에 죽으면 안 되므로 경고로만 남긴다. 실제 alias 등록은 각자 편할 때.
check(unmapped.length === 0, `매핑 실패 0건${unmapped.length ? ` -> ${unmapped.map((u) => u.rel).join(', ')}` : ''}`, warn);

const targets = solutions.map((s) => s.solutionTarget);
const dupTarget = targets.filter((t, i) => targets.indexOf(t) !== i);
check(dupTarget.length === 0, `풀이 경로 충돌 0건${dupTarget.length ? ` -> ${[...new Set(dupTarget)].join(', ')}` : ''}`);

const reviewTargets = solutions.map((s) => s.reviewTarget);
const dupReview = reviewTargets.filter((t, i) => reviewTargets.indexOf(t) !== i);
check(dupReview.length === 0, `리뷰 경로 충돌 0건${dupReview.length ? ` -> ${[...new Set(dupReview)].join(', ')}` : ''}`);

// ---------- 2.5 로테이션 ----------
const rot = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/rotation.json'), 'utf8'));
const problemKeys = new Set(keys);
const STATUS = new Set(['done', 'upcoming', 'skipped']);

const activeIds = new Set(authors.filter((a) => a.active !== false).map((a) => a.id));
const alumni = authors.filter((a) => a.active === false);

check(
  rot.order.every((id) => authorIds.has(id)),
  `rotation.order 가 모두 유효한 author (${rot.order.length}명)`
);
// 떠난 사람이 발표 순번에 남아 있으면 로테이션이 그 사람 차례에서 멈춘다
check(
  rot.order.every((id) => activeIds.has(id)),
  `rotation.order 에 졸업생 없음${rot.order.filter((id) => !activeIds.has(id)).map((id) => ` -> ${id}`).join('')}`
);
if (alumni.length) {
  ok.push(`졸업생 ${alumni.length}명 (${alumni.map((a) => `${a.displayName}/${a.leftAt ?? '날짜미상'}`).join(', ')}) — 코드·리뷰는 보존`);
}

const asg = rot.assignments ?? [];
for (const [i, a] of asg.entries()) {
  const at = `rotation.assignments[${i}]`;
  if (!problemKeys.has(a.problem)) fail.push(`${at}: 문제 '${a.problem}' 가 problems.json 에 없음`);
  if (!authorIds.has(a.presenter)) fail.push(`${at}: 발표자 '${a.presenter}' 가 authors.json 에 없음`);
  else if (!activeIds.has(a.presenter) && a.status === 'upcoming') {
    fail.push(`${at}: 졸업생 '${a.presenter}' 에게 예정된 발표가 잡혀 있음`);
  }
  if (!STATUS.has(a.status)) fail.push(`${at}: status '${a.status}' 는 허용값 아님 (${[...STATUS].join('|')})`);
  if (a.date && !/^\d{4}-\d{2}-\d{2}$/.test(a.date)) fail.push(`${at}: date '${a.date}' 형식은 YYYY-MM-DD`);
}
// 한 문제에 발표자가 둘이면 "문제당 담당 1명" 규칙이 깨진다
const asgProblems = asg.map((a) => a.problem);
const dupAsg = asgProblems.filter((k, i) => asgProblems.indexOf(k) !== i);
check(dupAsg.length === 0, `문제당 담당자 1명${dupAsg.length ? ` -> 중복: ${[...new Set(dupAsg)].join(', ')}` : ''}`);
check(true, `rotation.assignments ${asg.length}건 검사`);
if (asg.length === 0) warn.push('rotation.assignments 가 비어 있음 — 팀이 발표 담당을 채워야 로테이션 보드가 완성됩니다');

// ---------- 3. 리뷰 마크다운 ----------
const reviewDir = path.join(ROOT, 'reviews');
const reviewFiles = fs.existsSync(reviewDir)
  ? walkDir(reviewDir).filter((f) => f.endsWith('.md') && !f.endsWith('team-feedback.md'))
  : [];

function walkDir(dir, acc = []) {
  for (const e of fs.readdirSync(dir, { withFileTypes: true })) {
    const p = path.join(dir, e.name);
    if (e.isDirectory()) walkDir(p, acc);
    else acc.push(path.relative(ROOT, p).split(path.sep).join('/'));
  }
  return acc;
}

const REQUIRED = ['platform', 'problemId', 'author', 'source', 'compiles', 'verdict', 'tags'];
const VERDICTS = new Set(['good', 'needs-fix', 'wrong', 'unattempted']);

for (const rel of reviewFiles) {
  const text = fs.readFileSync(path.join(ROOT, rel), 'utf8');
  const m = text.match(/^---\r?\n([\s\S]*?)\r?\n---/);
  if (!m) {
    fail.push(`${rel}: 프론트매터 없음`);
    continue;
  }
  const fm = m[1];
  const get = (k) => fm.match(new RegExp(`^${k}:\\s*(.+)$`, 'm'))?.[1]?.trim().replace(/^["']|["']$/g, '');

  for (const key of REQUIRED) {
    if (!new RegExp(`^${key}:`, 'm').test(fm)) fail.push(`${rel}: '${key}' 누락`);
  }

  const verdict = get('verdict');
  if (verdict && !VERDICTS.has(verdict)) fail.push(`${rel}: verdict '${verdict}' 는 허용값 아님 (${[...VERDICTS].join('|')})`);

  const author = get('author');
  if (author && !authorIds.has(author)) fail.push(`${rel}: author '${author}' 가 authors.json 에 없음`);

  // 태그가 고정 어휘 안에 있는지 — 여기가 깨지면 "반복 지적 패턴 Top 5" 집계가 무너진다
  const tagsRaw = get('tags') ?? '[]';
  const tags = tagsRaw.replace(/^\[|\]$/g, '').split(',').map((t) => t.trim()).filter(Boolean);
  for (const t of tags) {
    if (!validTags.has(t)) fail.push(`${rel}: 태그 '${t}' 가 review-tags.json 에 없음`);
  }

  // 리뷰가 가리키는 source 가 실제로 그 리뷰 경로로 해석되는지 (역방향 검증)
  const source = get('source');
  if (source) {
    const back = resolveSource(source.normalize('NFC'));
    if (!back.ok) fail.push(`${rel}: source '${source}' 해석 불가 (${back.reason})`);
    else if (back.reviewTarget !== rel) fail.push(`${rel}: source 와 경로 불일치 -> ${back.reviewTarget}`);
  }
}
check(true, `리뷰 파일 ${reviewFiles.length}개 검사`);

// 리뷰가 있는데 대응하는 풀이가 없는 경우 (고아 리뷰)
const validReviewPaths = new Set(reviewTargets);
const orphans = reviewFiles.filter((r) => !validReviewPaths.has(r));
check(orphans.length === 0, `고아 리뷰 0건${orphans.length ? ` -> ${orphans.join(', ')}` : ''}`);

// ---------- 4. 인덱스 산출물 ----------
const indexPath = path.join(ROOT, 'site/data/index.json');
if (fs.existsSync(indexPath)) {
  const idx = JSON.parse(fs.readFileSync(indexPath, 'utf8'));
  const idxSolutions = idx.problems.reduce((a, p) => a + p.entries.length, 0);
  check(idxSolutions === solutions.length, `index.json 풀이 수 일치 (${idxSolutions} = ${solutions.length})`);
  check(idx.problems.every((p) => p.title), 'index.json 제목 누락 0건');
  check(idx.problems.every((p) => p.verified), 'index.json 미검증 문제 0건');
} else {
  warn.push('site/data/index.json 없음 — node scripts/scan.mjs 를 먼저 실행하세요');
}

// ---------- 출력 ----------
console.log('\n=== 통과 ===');
for (const m of ok) console.log(`  OK   ${m}`);
if (warn.length) {
  console.log('\n=== 경고 ===');
  for (const m of warn) console.log(`  WARN ${m}`);
}
if (fail.length) {
  console.log('\n=== 실패 ===');
  for (const m of fail) console.log(`  FAIL ${m}`);
}
console.log(`\n통과 ${ok.length} / 경고 ${warn.length} / 실패 ${fail.length}`);
process.exit(fail.length ? 1 : 0);
