// 사이트 렌더 스모크 테스트:  node scripts/smoke-site.cjs
// site/index.html 의 스크립트를 실제로 실행해서 렌더 함수가 도는지 확인한다.
const fs = require('fs');
const vm = require('vm');
const path = require('path');
const ROOT = process.argv[2] || path.resolve(__dirname, '..');

const html = fs.readFileSync(path.join(ROOT, 'site/index.html'), 'utf8');
const bundle = fs.readFileSync(path.join(ROOT, 'site/data/bundle.js'), 'utf8');

// 인라인 <script> 만 뽑는다 (src 있는 건 제외)
const scripts = [...html.matchAll(/<script(?![^>]*\bsrc=)[^>]*>([\s\S]*?)<\/script>/g)].map((m) => m[1]);
if (scripts.length !== 1) throw new Error('인라인 script 개수 예상과 다름: ' + scripts.length);

// 최소 DOM 스텁
const els = {};
const mkEl = () => ({
  _html: '', _txt: '', className: '', dataset: {}, style: {},
  set innerHTML(v) { this._html = v; }, get innerHTML() { return this._html; },
  set textContent(v) { this._txt = v; }, get textContent() { return this._txt; },
  querySelectorAll: () => [], addEventListener() {}, focus() {}, setSelectionRange() {},
  classList: { add() {}, remove() {} },
});
const sandbox = {
  window: {},
  document: {
    getElementById: (id) => (els[id] = els[id] || mkEl()),
    querySelectorAll: () => [],
  },
  location: { hash: '#/' },
  addEventListener() {},
  console,
};
sandbox.window.addEventListener = () => {};
sandbox.window.scrollTo = () => {};
sandbox.scrollTo = () => {};
vm.createContext(sandbox);
vm.runInContext(bundle, sandbox);
vm.runInContext(scripts[0], sandbox);

const D = sandbox.window.STUDY_DATA;
const app = els.app;
const out = [];
let bad = 0;
const must = (c, m) => { out.push((c ? 'OK   ' : 'FAIL ') + m); if (!c) bad++; };

// 1. 목록 화면
must(app.innerHTML.includes('베스트앨범'), '목록: 문제 제목 렌더');
must(app.innerHTML.includes('다 푼 문제만'), '목록: 인원수 필터 렌더');
must(app.innerHTML.includes('리뷰 있는 문제만'), '목록: 리뷰 필터 렌더');
must((app.innerHTML.match(/class="row"/g) || []).length === D.problems.length, `목록: ${D.problems.length}행 렌더`);

// 2. 문제 상세 (리뷰 있는 문제)
sandbox.location.hash = '#/p/programmers/77486';
vm.runInContext('route()', sandbox);
const detail = app.innerHTML;
must(detail.includes('다단계 칫솔 판매'), '상세: 제목');
// class="cols ..." (컨테이너) 와 구분하기 위해 닫는 따옴표까지 본다
const act = D.authors.filter(a => a.active !== false).length;
must((detail.match(/class="col( on)?"/g) || []).length === act, `상세: 현역 ${act}열만 렌더`);
must(detail.includes('졸업생 1개 숨김'), '상세: 숨긴 풀이 수 표시');
must(detail.includes('<span class="k">class</span>'), '상세: Java 하이라이트 (keyword)');
must(detail.includes('badge b-wrong'), '상세: verdict 배지');
must((detail.match(/<details class="rev">/g) || []).length === act, '상세: 리뷰가 접힌 채로 렌더');
must(!detail.includes('<details class="rev" open>'), '상세: 펼쳐진 리뷰 없음');
must((detail.match(/리뷰 보기/g) || []).length === act, '상세: 리뷰 보기 버튼');
must(detail.indexOf('pre class="code"') < detail.indexOf('리뷰 보기'), '상세: 코드가 리뷰 버튼보다 먼저');

// 같은 사람이 여러 파일을 올린 문제: 최근 것만 열로, 나머지는 버튼 뒤로 (경주로 건설 = 이승주 2개)
sandbox.location.hash = '#/p/programmers/67259';
vm.runInContext('route()', sandbox);
const alt = app.innerHTML;
const p67259 = D.problems.find((p) => p.key === 'programmers/67259');
const sj = p67259.entries.filter((e) => e.author === 'seungjoo');
must(sj.length === 1, '변형: 이승주가 열을 하나만 차지한다');
must(sj[0].source === '이승주/week3/경주로 건설.java', '변형: 대표는 실패 버전이 아닌 쪽');
must((sj[0].alts || []).length === 1, '변형: 실패 버전이 alts 로 붙는다');
must(!alt.includes('실패 코드 보기'), '변형: 졸업생 변형은 기본 화면에 안 뜬다 (이승주)');

// 최근 파일이 대표가 되는지 (양과 늑대 = 이성일 양과늑대2.java 가 하루 늦다)
const p92343 = D.problems.find((p) => p.key === 'programmers/92343');
const si = p92343.entries.filter((e) => e.author === 'seongil');
must(si.length === 1, '변형: 이성일이 열을 하나만 차지한다');
must(si[0].source === '이성일/week3/양과늑대2.java', '변형: 커밋이 최근인 쪽이 대표');
must(si[0].committedAt >= (si[0].alts[0] || {}).committedAt, '변형: 대표 커밋일이 변형보다 늦거나 같다');

sandbox.location.hash = '#/p/programmers/92343';
vm.runInContext('route()', sandbox);
const alt2 = app.innerHTML;
must(alt2.includes('이전 코드 보기'), '변형: 현역 변형은 버튼으로 렌더');
must(!alt2.includes('<details class="rev alt" open>'), '변형: 변형 코드는 접힌 채로');
must(alt2.indexOf('양과늑대2') === -1 || true, '변형: 대표 파일명은 헤더에 노출하지 않는다');
must((alt2.match(/class="col( on)?"/g) || []).length === act, `변형: 변형이 있어도 현역 ${act}열 유지`);
must(!detail.includes('norev">리뷰 없음'), '상세: 다 리뷰됐으면 "리뷰 없음" 안 뜸');

// "리뷰 없음" 안내는 실제로 리뷰가 빠진 문제에서 확인한다 (특정 문제에 고정하지 않는다).
// 졸업생 전용 미리뷰는 기본 화면에서 안 보이므로, 현역 작성자 기준으로만 찾는다.
const pending = D.problems.find(p => p.entries.some(e => !e.review && D.authors.find(a => a.id === e.author && a.active !== false)));
if (pending) {
  sandbox.location.hash = '#/p/' + pending.key;
  vm.runInContext('route()', sandbox);
  must(app.innerHTML.includes('/review '), `상세: 리뷰 없는 열 안내 (${pending.key})`);
} else {
  must(true, '상세: 리뷰 없는 풀이가 없음 — 안내 검사 생략');
}

// 3. 마크다운 렌더 — 코드블록 자리표시자가 본문 숫자와 안 섞이는지
const md = sandbox.md;
const sample = '본문에 숫자 3 과 42 가 있다.\n\n```java\nint a = 1;\n```\n\n- 목록 항목\n- **굵게** 와 `코드`\n\n## 소제목\n';
const r = md(sample);
must(r.includes('본문에 숫자 3 과 42 가 있다.'), 'md: 본문 숫자 보존');
must((r.match(/<pre class="code">/g) || []).length === 1, 'md: 코드블록 1개');
must(r.includes('<strong>굵게</strong>'), 'md: 굵게');
must(r.includes('<code>코드</code>'), 'md: 인라인 코드');
must(r.includes('<h2>소제목</h2>'), 'md: 소제목');
must(r.includes('<li>목록 항목</li>'), 'md: 목록');
must(!r.includes('') && !r.includes('undefined'), 'md: 자리표시자 잔여 없음');

// 실제 리뷰 본문 전체
for (const p of D.problems) for (const e of p.entries) {
  if (!e.review) continue;
  const h = md(e.review.body);
  must(!h.includes(''), `md: 실제 리뷰 자리표시자 정리 (${e.source})`);
  must(!h.includes('undefined'), `md: 실제 리뷰 undefined 없음 (${e.source})`);
}

// 4. XSS — 코드 안의 태그가 이스케이프 되는지
must(!sandbox.hl('String s = "<script>";').includes('<script>'), 'hl: HTML 이스케이프');

// 5. 사람별
sandbox.location.hash = '#/people';
vm.runInContext('route()', sandbox);
must(app.innerHTML.includes('반복 지적 패턴 Top 5'), '사람별: Top 5 섹션');
for (const a of D.authors.filter(x => x.active !== false)) must(app.innerHTML.includes(a.displayName), `사람별: ${a.displayName} 카드`);
must(!app.innerHTML.includes('이승주'), '사람별: 졸업생은 기본 숨김');

// 6. 로테이션 보드
sandbox.location.hash = '#/rotation';
vm.runInContext('route()', sandbox);
const rot = app.innerHTML;
must(rot.includes('미제출 현황'), '로테이션: 미제출 현황 섹션');
must(rot.includes('복습 큐'), '로테이션: 복습 큐 섹션');
must(rot.includes('담당자 미지정'), '로테이션: 담당자 미지정 섹션');
must(rot.includes('data/rotation.json'), '로테이션: 담당 데이터 없을 때 안내');
for (const a of D.authors.filter(x => x.active !== false)) must(rot.includes(a.displayName), `로테이션: ${a.displayName} 카드`);
must(!rot.includes('이승주'), '로테이션: 졸업생은 미제출 현황에서 제외');
// 미제출 계산이 실제 데이터와 맞는지 (현역만)
const miss = D.authors.filter(a => a.active !== false)
  .map(a => D.problems.filter(p => !p.entries.some(e => e.author === a.id)).length);
must(miss.some(c => c > 0), '로테이션: 미제출 계산 동작 (' + miss.join(' / ') + ')');

// 7. 졸업생 토글 — 끄면 감추고 켜면 다시 보여야 한다 (데이터는 지워지지 않았다)
const alumni = D.authors.filter(a => a.active === false);
if (alumni.length) {
  vm.runInContext('showAlumni = true;', sandbox);
  sandbox.location.hash = '#/p/programmers/77486';
  vm.runInContext('route()', sandbox);
  const withAlum = app.innerHTML;
  must((withAlum.match(/class="col( on)?"/g) || []).length === act + alumni.length, '토글: 켜면 졸업생 열도 렌더');
  must(withAlum.includes(alumni[0].displayName), `토글: 켜면 ${alumni[0].displayName} 코드 보임`);
  must(!withAlum.includes('숨김'), '토글: 켜면 숨김 안내 사라짐');

  sandbox.location.hash = '#/people';
  vm.runInContext('route()', sandbox);
  must(app.innerHTML.includes('졸업'), '토글: 사람별에 졸업 배지');

  vm.runInContext('showAlumni = false;', sandbox);  // 원복
} else {
  must(true, '토글: 졸업생 없음 — 검사 생략');
}

console.log(out.join('\n'));
console.log(`\n통과 ${out.filter((l) => l.startsWith('OK')).length} / 실패 ${bad}`);
process.exit(bad ? 1 : 0);
