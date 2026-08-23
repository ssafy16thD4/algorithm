// T2-1 인덱서. 저장소를 스캔해서 사이트가 읽을 site/data/index.json 을 만든다.
// 매핑 규칙은 scripts/lib/map.mjs 한 곳에만 있다.
//   node scripts/scan.mjs           -> 리포트 + site/data/index.json
//   node scripts/scan.mjs --reviews -> 리뷰가 없는 풀이 목록까지 출력
import fs from 'node:fs';
import path from 'node:path';
import { ROOT, authors, walkRepo, resolveSource } from './lib/map.mjs';

const files = walkRepo();
const results = files.map(resolveSource);
const solutions = results.filter((r) => r.ok);

const problems = new Map();
for (const s of solutions) {
  if (!problems.has(s.key)) {
    problems.set(s.key, {
      key: s.key, platform: s.platform, problemId: s.problemId,
      title: s.title, url: s.url, verified: s.verified,
      weeks: new Set(), entries: [],
    });
  }
  const p = problems.get(s.key);
  if (s.week) p.weeks.add(s.week);
  p.entries.push({
    author: s.author, authorName: s.authorName, week: s.week, variant: s.variant,
    source: s.source, target: s.solutionTarget, review: s.reviewTarget,
    hasReview: fs.existsSync(path.join(ROOT, s.reviewTarget)),
    lines: s.lines, bytes: s.bytes,
  });
}

const list = [...problems.values()].sort(
  (a, b) => a.platform.localeCompare(b.platform) || Number(a.problemId) - Number(b.problemId)
);
const authorIds = authors.map((a) => a.id);
const byReason = (reason) => results.filter((r) => !r.ok && r.reason === reason);
const pad = (s, n) => String(s ?? '').padEnd(n);

console.log('\n=== 스캔 결과 ===');
console.log(`파일 ${files.length}개 스캔 / 풀이로 인식 ${solutions.length}개 / 문제 ${list.length}개\n`);

console.log('=== 문제 × 작성자 (O=풀이, ✓=리뷰까지 있음) ===');
console.log(`${pad('문제', 26)}${pad('식별자', 20)}${authorIds.map((a) => pad(a, 10)).join('')}열수`);
console.log('-'.repeat(26 + 20 + authorIds.length * 10 + 4));
for (const p of list) {
  const cells = authorIds.map((id) => {
    const mine = p.entries.filter((e) => e.author === id);
    if (!mine.length) return pad('·', 10);
    const mark = mine.every((e) => e.hasReview) ? '✓' : 'O';
    return pad(mine.length > 1 ? `${mark}(+${mine.length - 1})` : mark, 10);
  });
  const n = new Set(p.entries.map((e) => e.author)).size;
  console.log(`${pad((p.title ?? '(제목없음)') + (p.verified ? ' ' : '?'), 26)}${pad(p.key, 20)}${cells.join('')}${n}`);
}

console.log('\n=== 3열 비교 가능 여부 (T2-4) ===');
const byCount = { 1: [], 2: [], 3: [], 4: [] };
for (const p of list) byCount[new Set(p.entries.map((e) => e.author)).size].push(p.key);
for (const n of [4, 3, 2, 1]) console.log(`  ${n}명 제출: ${byCount[n].length}문제`);

const reviewed = solutions.filter((s) => fs.existsSync(path.join(ROOT, s.reviewTarget)));
console.log('\n=== 리뷰 진행률 (T1) ===');
console.log(`  ${reviewed.length} / ${solutions.length}개 (${Math.round((reviewed.length / solutions.length) * 100)}%)`);

console.log('\n=== 제외된 파일 ===');
for (const [reason, label] of [
  ['unmapped-title', '매핑 없는 파일명'],
  ['not-java', '.java 아님'],
  ['not-a-solution', '풀이 아님'],
  ['empty', '빈 파일'],
  ['commented-out', '전체 주석'],
  ['unknown-author', '작성자 불명'],
]) {
  const hits = byReason(reason);
  if (hits.length) console.log(`  ${pad(label, 16)}: ${hits.length}개  ${hits.map((h) => h.rel).join(', ')}`);
}
const noExt = solutions.filter((s) => s.noExtension);
if (noExt.length) console.log(`  ${pad('확장자 누락', 16)}: ${noExt.length}개  ${noExt.map((s) => s.rel).join(', ')}`);

if (process.argv.includes('--reviews')) {
  // 졸업생은 앞으로 제출할 일이 없으므로 리뷰 대상에서 뺀다.
  // 이미 붙은 리뷰와 코드는 그대로 남고 사이트에서도 '졸업생 포함' 토글로 볼 수 있다.
  const active = new Set(authors.filter((a) => a.active !== false).map((a) => a.id));
  const all = solutions.filter((s) => !fs.existsSync(path.join(ROOT, s.reviewTarget)));
  const pending = all.filter((s) => active.has(s.author));
  const skipped = all.length - pending.length;
  console.log(`\n=== 리뷰 없는 풀이 ${pending.length}개 ===`);
  for (const s of pending) console.log(`  /review ${s.source}`);
  if (skipped) console.log(`  (졸업생 ${skipped}건 제외 — 보려면 --all)`);
}

const out = {
  generatedAt: new Date().toISOString(),
  authors: authors.map(({ id, displayName, verified }) => ({ id, displayName, verified })),
  problems: list.map((p) => ({ ...p, weeks: [...p.weeks].sort((a, b) => a - b) })),
};
const outPath = path.join(ROOT, 'site/data/index.json');
fs.mkdirSync(path.dirname(outPath), { recursive: true });
fs.writeFileSync(outPath, JSON.stringify(out, null, 2) + '\n');
console.log(`\n-> site/data/index.json (${list.length} problems, ${solutions.length} solutions)`);
