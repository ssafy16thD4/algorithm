// 이번 push/PR 에서 바뀐 .java/.cpp 만 골라 컴파일한다.
//   node scripts/compile-changed.mjs <base-ref>
//
// 전체를 검사하지 않는 이유: 기존에 깨져 있는 파일 3건 때문에 모든 PR 이 빨간불이 된다.
// "새로 넣은 코드가 깨지는지"만 막으면 충분하고, 기존 파일은 작성자가 알아서 고치면 된다.
import fs from 'node:fs';
import os from 'node:os';
import path from 'node:path';
import { execFileSync } from 'node:child_process';
import { ROOT, resolveSource } from './lib/map.mjs';

const ZERO = /^0{7,40}$/; // 브랜치를 새로 push 하면 github.event.before 가 전부 0 이다

function git(args) {
  // core.quotepath=false 가 없으면 git 이 한글 경로를 "\354\225..." 로 뱉어서 파일을 못 연다.
  return execFileSync('git', ['-c', 'core.quotepath=false', ...args], {
    cwd: ROOT,
    encoding: 'utf8',
    stdio: ['ignore', 'pipe', 'pipe'],
  });
}

const exists = (ref) => {
  try { git(['rev-parse', '--verify', '--quiet', `${ref}^{commit}`]); return true; }
  catch { return false; }
};

/** 쓸 만한 비교 기준을 순서대로 찾는다. */
function pickBase() {
  const wanted = process.argv[2];
  const candidates = [
    wanted && !ZERO.test(wanted) ? wanted : null,
    'origin/main', 'main', 'HEAD~1',
  ].filter(Boolean);
  for (const c of candidates) if (exists(c)) return c;
  return null;
}

function changedFiles() {
  const base = pickBase();
  if (!base) {
    console.log('비교할 기준 커밋을 못 찾았습니다 — 검사를 건너뜁니다.');
    return [];
  }
  if (base !== process.argv[2]) console.log(`기준: ${base} (요청값 대신 사용)\n`);
  try {
    return git(['diff', '--name-only', '--diff-filter=ACMR', `${base}...HEAD`])
      .split('\n').map((s) => s.trim()).filter(Boolean);
  } catch (e) {
    console.error(`git diff 실패 (기준 ${base}) — 검사를 건너뜁니다.`);
    console.error(String(e.stderr ?? e.message).trim());
    return [];
  }
}

const COMPILERS = {
  java: { bin: 'javac', args: (out, file) => ['-nowarn', '-encoding', 'UTF-8', '-d', out, file] },
  cpp: { bin: 'g++', args: (out, file) => ['-std=c++17', '-O0', '-o', path.join(out, 'a.out'), file] },
};

const changed = changedFiles().filter((f) => f.endsWith('.java') || f.endsWith('.cpp'));
if (!changed.length) {
  console.log('바뀐 .java/.cpp 없음 — 통과');
  process.exit(0);
}

console.log(`바뀐 .java/.cpp ${changed.length}개\n`);

const outDir = fs.mkdtempSync(path.join(os.tmpdir(), 'compile-'));
let failed = 0;
let skipped = 0;

for (const rel of changed) {
  const info = resolveSource(rel);
  if (!info.ok) {
    console.log(`SKIP  ${rel}  (${info.reason})`);
    skipped++;
    continue;
  }
  const compiler = COMPILERS[info.lang];
  try {
    execFileSync(compiler.bin, compiler.args(outDir, path.join(ROOT, rel)), {
      stdio: ['ignore', 'pipe', 'pipe'],
    });
    console.log(`OK    ${rel}`);
  } catch (e) {
    if (e.code === 'ENOENT') {
      console.log(`SKIP  ${rel}  (${compiler.bin} 없음)`);
      skipped++;
      continue;
    }
    failed++;
    console.log(`FAIL  ${rel}`);
    const msg = String(e.stderr ?? '')
      .replace(/\r/g, '')
      .split('\n')
      .map((l) => l.replace(/^.*?(?=\d+: (?:error|warning):)/, `      ${rel}:`))
      .filter((l) => l.trim())
      .slice(0, 10)
      .join('\n');
    console.log(msg);
  }
}
fs.rmSync(outDir, { recursive: true, force: true });

console.log(`\n통과 ${changed.length - failed - skipped} / 실패 ${failed} / 건너뜀 ${skipped}`);
if (failed) {
  console.log('\n컴파일이 안 되는 파일이 있습니다. 채점 사이트에서도 동일하게 막힙니다.');
  process.exit(1);
}
