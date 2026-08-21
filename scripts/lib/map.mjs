// 소스 파일 경로 -> {문제, 작성자, 리뷰 파일 경로} 매핑.
// scan.mjs(인덱서)와 resolve.mjs(/review 커맨드)가 같은 규칙을 쓰도록 여기 한 곳에 둔다.
// 둘이 어긋나면 리뷰가 엉뚱한 경로에 쓰이므로 절대 복붙하지 말 것.
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

export const ROOT = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');

// 팀원 풀이가 아닌 디렉터리 (생성물 + 도구)
const SKIP_DIRS = new Set([
  '.git', '.github', '.claude', 'data', 'scripts', 'site', 'reviews', 'references', 'node_modules',
]);

// 결과물이 아닌 부산물
// 헌터 문제: 이성일/week4 — `public class 헌터 문제 {}` 뿐인 빈 스캐폴드. 실제 문제와
// 매칭할 근거가 코드에 전혀 없어(추측 금지 원칙, T0-2) 풀이로 세지 않는다.
const NON_SOLUTION = [/^Test$/i, /^Testcode$/i, /^ttt$/i, /^헌터\s*문제$/];

// 같은 문제의 실패/연습 버전. 대표 풀이와 구분해서 variant로 붙인다.
// keep: true 면 매칭된 문자열을 stem에서 지우지 않는다 (별칭 조회에 그대로 써야 할 때).
const VARIANT_RULES = [
  { re: /\s*실패\s*$/, tag: 'failed' },
  { re: /\s*잘못된\s*풀이\s*$/, tag: 'failed' },
  { re: /FailVersion$/i, tag: 'failed' },
  { re: /\s*시간초과\s*$/, tag: 'failed' },
  { re: /(?<=[가-힣])2$/, tag: 'alt' },
  // 이성일/week4/가장긴징검다리.java: "징검다리 건너기"(64062)를 다른 이름으로 부른 별도 시도.
  // 같은 폴더의 징검다리건너기.java(대표 풀이)와 경로가 겹치지 않도록 alt로 구분한다.
  { re: /^가장긴징검다리$/, tag: 'alt', keep: true },
];

const problemsFile = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/problems.json'), 'utf8'));
const authorsFile = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/authors.json'), 'utf8'));

export const authors = authorsFile.authors;

const aliasToProblem = new Map();
const problemByKey = new Map();
for (const p of problemsFile.problems) {
  for (const a of p.aliases) aliasToProblem.set(normalize(a), p);
  problemByKey.set(`${p.platform}/${p.problemId}`, p);
}
const folderToAuthor = new Map();
for (const a of authorsFile.authors) {
  for (const f of a.folders) folderToAuthor.set(f.normalize('NFC'), a);
}

function normalize(s) {
  return s.normalize('NFC').replace(/\s+/g, '').toLowerCase();
}

function parseWeek(segment) {
  const m = segment?.match(/(\d+)/);
  return m ? Number(m[1]) : null;
}

/** 저장소 안의 팀원 풀이 파일들을 훑는다 (레포 상대경로, `/` 구분자). */
export function walkRepo(dir = ROOT, acc = []) {
  for (const entry of fs.readdirSync(dir, { withFileTypes: true })) {
    if (entry.isDirectory()) {
      if (SKIP_DIRS.has(entry.name)) continue;
      walkRepo(path.join(dir, entry.name), acc);
    } else {
      acc.push(path.relative(ROOT, path.join(dir, entry.name)).split(path.sep).join('/'));
    }
  }
  return acc;
}

/**
 * 레포 상대경로 하나를 해석한다.
 * 성공하면 {ok:true, ...}, 실패하면 {ok:false, reason} 을 돌려준다.
 * reason: not-a-solution | unknown-author | not-java | empty | commented-out | unmapped-title
 */
export function resolveSource(rel) {
  const segs = rel.split('/');
  if (segs.length < 2) return { ok: false, reason: 'not-a-solution', rel };

  const [folder, weekSeg, ...rest] = segs;
  const author = folderToAuthor.get(folder.normalize('NFC'));
  if (!author) return { ok: false, reason: 'unknown-author', rel };

  const base = rest[rest.length - 1];
  const ext = path.extname(base);
  const stemRaw = ext ? base.slice(0, -ext.length) : base;

  if (ext && ext !== '.java') return { ok: false, reason: 'not-java', rel };
  if (NON_SOLUTION.some((re) => re.test(stemRaw))) return { ok: false, reason: 'not-a-solution', rel };

  const abs = path.join(ROOT, rel);
  if (!fs.existsSync(abs)) return { ok: false, reason: 'missing', rel };
  const buf = fs.readFileSync(abs);
  const text = buf.toString('utf8');

  if (!text.trim()) return { ok: false, reason: 'empty', rel };
  if (text.split('\n').every((l) => !l.trim() || l.trim().startsWith('//'))) {
    return { ok: false, reason: 'commented-out', rel };
  }

  // variant 추출 (실패/대안 풀이)
  let stem = stemRaw.normalize('NFC').trim();
  let variant = null;
  for (const rule of VARIANT_RULES) {
    if (rule.re.test(stem)) {
      variant = rule.tag;
      if (!rule.keep) stem = stem.replace(rule.re, '').trim();
      break;
    }
  }

  // 문제 식별. SWEA는 번호를 파일명에서 뽑고 제목만 매핑에서 붙인다.
  const swea = stem.match(/^SWEA\s*(\d+)$/i);
  const hit = swea ? problemByKey.get(`swea/${swea[1]}`) : aliasToProblem.get(normalize(stem));

  let platform, problemId, title, url, verified;
  if (hit) {
    ({ platform, problemId, title, verified } = hit);
    url = hit.url ?? null;
  } else if (swea) {
    platform = 'swea';
    problemId = swea[1];
    title = null;
    url = null;
    verified = false;
  } else {
    return { ok: false, reason: 'unmapped-title', rel, stem };
  }

  const suffix = variant ? `.${variant}` : '';
  return {
    ok: true,
    rel,
    source: rel,
    platform,
    problemId,
    key: `${platform}/${problemId}`,
    title,
    url,
    verified,
    author: author.id,
    authorName: author.displayName,
    week: parseWeek(weekSeg),
    variant,
    lines: text.split('\n').length,
    bytes: buf.length,
    crlf: buf.includes('\r\n'),
    noExtension: !ext,
    solutionTarget: `solutions/${platform}/${problemId}/${author.id}${suffix}.java`,
    reviewTarget: `reviews/${platform}/${problemId}/${author.id}${suffix}.md`,
  };
}
