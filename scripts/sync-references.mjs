// T4-1. data/references.json 에 등록된 외부 레퍼런스 코드를 내려받는다.
//   node scripts/sync-references.mjs          없는 파일만 받는다
//   node scripts/sync-references.mjs --force  이미 있어도 다시 받는다 (pinnedCommit 을 올렸을 때)
//
// 원본을 그대로 저장한다. 주석 헤더를 얹지 않는 이유는 references.json 의 _comment 참고.
// 지금은 GitHub 만 지원한다(kind 를 늘릴 일이 생기면 여기서 분기).
import fs from 'node:fs';
import path from 'node:path';
import { ROOT } from './lib/map.mjs';
import { rawUrl } from './lib/refs.mjs';

const force = process.argv.includes('--force');
const reg = JSON.parse(fs.readFileSync(path.join(ROOT, 'data/references.json'), 'utf8'));
const providers = new Map(reg.providers.map((p) => [p.id, p]));

// 한 문제의 notes 는 entry 여러 개가 같은 파일을 가리킬 수 있으므로 중복 요청을 접는다.
const jobs = new Map();
for (const e of reg.entries) {
  const provider = providers.get(e.provider);
  if (!provider) throw new Error(`알 수 없는 provider: ${e.provider} (${e.file})`);
  jobs.set(e.file, { provider, sourcePath: e.sourcePath, dest: e.file });
  if (e.notes && e.notesSourcePath !== null) {
    // notes 의 원본 경로는 코드와 같은 폴더의 solve.md 라고 본다. 다르면 entry 에 notesSourcePath 로 적는다.
    const guess = e.notesSourcePath || e.sourcePath.replace(/\/[^/]+$/, '/solve.md');
    jobs.set(e.notes, { provider, sourcePath: guess, dest: e.notes });
  }
}

let got = 0, kept = 0, failed = 0;
for (const job of jobs.values()) {
  const abs = path.join(ROOT, job.dest);
  if (!force && fs.existsSync(abs)) { kept++; continue; }
  const url = rawUrl(job.provider, job.sourcePath);
  const res = await fetch(url);
  if (!res.ok) { console.error(`  실패 ${res.status}  ${job.sourcePath}`); failed++; continue; }
  const text = (await res.text()).replace(/\r\n/g, '\n');
  fs.mkdirSync(path.dirname(abs), { recursive: true });
  fs.writeFileSync(abs, text, 'utf8');
  console.log(`  받음  ${job.dest}  (${text.split('\n').length}줄)`);
  got++;
}

console.log(`-> 레퍼런스 ${jobs.size}개 · 받음 ${got} · 이미 있음 ${kept}${failed ? ` · 실패 ${failed}` : ''}`);
if (failed) process.exitCode = 1;
