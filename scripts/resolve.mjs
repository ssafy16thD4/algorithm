// /review 슬래시 커맨드용. 소스 경로 하나를 받아 리뷰에 필요한 메타데이터를 뱉는다.
//   node scripts/resolve.mjs "안찬웅/week4/베스트앨범.java"
//   node scripts/resolve.mjs "..." --no-compile   (javac 생략)
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { execFileSync } from 'node:child_process';
import { ROOT, resolveSource } from './lib/map.mjs';

const REASONS = {
  'unknown-author': 'data/authors.json 의 folders 에 없는 최상위 폴더입니다.',
  'unmapped-title': 'data/problems.json 의 aliases 에 이 파일명이 없습니다. alias를 추가하세요.',
  'unsupported-lang': '.java 또는 .cpp 파일이 아닙니다.',
  'not-a-solution': '풀이 파일이 아닙니다 (Test.java, ttt.txt 등).',
  empty: '빈 파일입니다.',
  'commented-out': '전체가 주석 처리되어 있습니다.',
  missing: '파일이 존재하지 않습니다.',
};

const raw = process.argv[2];
if (!raw) {
  console.error('사용법: node scripts/resolve.mjs "<풀이 파일 경로>"');
  process.exit(2);
}

// 절대경로로 줘도, 백슬래시로 줘도 받아준다.
let rel = raw.replace(/\\/g, '/');
if (path.isAbsolute(rel)) rel = path.relative(ROOT, rel).split(path.sep).join('/');
rel = rel.replace(/^\.\//, '');

const info = resolveSource(rel);
if (!info.ok) {
  console.error(`해석 실패 (${info.reason}): ${rel}`);
  console.error(REASONS[info.reason] ?? '알 수 없는 이유입니다.');
  process.exit(1);
}

// 컴파일 여부는 리뷰 프론트매터의 compiles 필드에 그대로 들어간다.
// compiles: true=통과, false=실패, null=판정 못함(그 이유는 compileError에).
let compiles = null;
let compileError = null;

const COMPILERS = {
  java: { bin: 'javac', args: (out, file) => ['-nowarn', '-encoding', 'UTF-8', '-d', out, file], missing: 'javac 없음 (JDK 미설치)' },
  cpp: { bin: 'g++', args: (out, file) => ['-std=c++17', '-O0', '-o', path.join(out, 'a.out'), file], missing: 'g++ 없음 (MinGW/빌드 도구 미설치)' },
};

if (process.argv.includes('--no-compile')) {
  compileError = '건너뜀 (--no-compile)';
} else if (info.noExtension) {
  // javac/g++ 는 확장자가 없으면 파일로 취급하지 않는다 (invalid flag).
  // 컴파일 실패가 아니라 판정 불가로 남긴다.
  compileError = '확장자가 없어 컴파일러로 확인 불가';
} else {
  const compiler = COMPILERS[info.lang];
  const outDir = fs.mkdtempSync(path.join(os.tmpdir(), 'review-compile-'));
  try {
    execFileSync(compiler.bin, compiler.args(outDir, path.join(ROOT, rel)), {
      stdio: ['ignore', 'pipe', 'pipe'],
    });
    compiles = true;
  } catch (e) {
    if (e.code === 'ENOENT') {
      compileError = compiler.missing; // 컴파일 실패가 아니다
    } else {
      compiles = false;
      compileError = cleanCompilerError(String(e.stderr ?? ''));
    }
  } finally {
    fs.rmSync(outDir, { recursive: true, force: true });
  }
}

// 컴파일러는 stderr 를 콘솔 코드페이지로 쓴다. 한글 경로가 섞이면 깨져서 나오므로
// 파일 경로 부분을 잘라내고 `줄번호: error: 내용` 만 남긴다.
function cleanCompilerError(stderr) {
  return stderr
    .replace(/\r/g, '')
    .split('\n')
    .map((l) => l.replace(/^.*?(?=\d+: (?:error|warning):)/, `${rel}:`))
    .filter((l) => l.trim())
    .slice(0, 8)
    .join('\n');
}

const existingReview = fs.existsSync(path.join(ROOT, info.reviewTarget));

console.log(JSON.stringify({
  source: info.source,
  platform: info.platform,
  problemId: info.problemId,
  title: info.title,
  url: info.url,
  author: info.author,
  authorName: info.authorName,
  week: info.week,
  variant: info.variant,
  lang: info.lang,
  lines: info.lines,
  compiles,
  compileError,
  reviewTarget: info.reviewTarget,
  reviewExists: existingReview,
}, null, 2));
