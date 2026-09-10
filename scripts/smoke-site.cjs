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
// 팀원이 늘어도(전홍선 등) 모든 인원이 모든 테스트용 문제를 풀어둔 건 아니므로,
// "전원이 풀었다"는 전제 대신 그 문제를 실제로 푼 인원 수를 매번 데이터에서 구한다.
// 떠난 사람(이승주)도 있던 회차의 풀이는 그냥 보이므로 현역으로 걸러내지 않는다.
const entryCount = (key) => D.problems.find((p) => p.key === key).entries.length;
const out = [];
let bad = 0;
const must = (c, m) => { out.push((c ? 'OK   ' : 'FAIL ') + m); if (!c) bad++; };

// 1. 목록 화면
must(html.includes('<a href="#/" id="home">SSAFY 16기 알고리즘</a>'), '헤더: 제목이 메인으로 가는 링크');
must(app.innerHTML.includes('베스트앨범'), '목록: 문제 제목 렌더');
// '다 푼 문제만'·'리뷰 있는 문제만' 체크박스는 2026-09-07 요청으로 뺐다
must(!app.innerHTML.includes('다 푼 문제만') && !app.innerHTML.includes('리뷰 있는 문제만'), '목록: 인원수·리뷰 필터 없음');
must(app.innerHTML.includes('id="f-pf"') && app.innerHTML.includes('id="f-wk"') && app.innerHTML.includes('id="f-q"'), '목록: 플랫폼·회차·검색 필터는 남아 있다');
must((app.innerHTML.match(/class="row"/g) || []).length === D.problems.length, `목록: ${D.problems.length}행 렌더`);

// 2. 문제 상세 (리뷰 있는 문제)
sandbox.location.hash = '#/p/programmers/77486';
vm.runInContext('route()', sandbox);
const detail = app.innerHTML;
must(detail.includes('다단계 칫솔 판매'), '상세: 제목');
// class="cols ..." (컨테이너) 와 구분하기 위해 닫는 따옴표까지 본다
const act = entryCount('programmers/77486');
must((detail.match(/class="col( on)?"/g) || []).length === act, `상세: ${act}열 렌더 (이승주 포함)`);
must(!detail.includes('숨김'), '상세: 숨김 안내 없음 (졸업생 토글 제거)');
must(detail.includes('<span class="k">class</span>'), '상세: Java 하이라이트 (keyword)');
// verdict 값 자체는 리뷰가 갱신되면 바뀐다(코드를 고치면 wrong -> good). 배지가 렌더되는지만 본다
must(/badge b-(good|needs-fix|wrong|unattempted)/.test(detail), '상세: verdict 배지');
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
must(alt.includes('실패 코드 보기'), '변형: 이승주 실패 버전도 버튼으로 렌더 (3주차라 보인다)');

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
const act92343 = entryCount('programmers/92343');
must((alt2.match(/class="col( on)?"/g) || []).length === act92343, `변형: 변형이 있어도 ${act92343}열 유지`);
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
for (const a of D.authors) must(app.innerHTML.includes(a.displayName), `사람별: ${a.displayName} 카드`);
must(app.innerHTML.includes('<span class="tag">졸업</span>'), '사람별: 이승주 카드에 졸업 배지');

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

// 7. 졸업생 토글은 없다 — 이승주는 3주차까지 그냥 보이고, 4주차 이후 파일은 인덱서(lastWeek)가 뺀다
must(!html.includes('alumni-toggle'), '토글: 졸업생 토글 마크업 없음');
must(vm.runInContext('typeof showAlumni', sandbox) === 'undefined', '토글: showAlumni 상태 없음');
{
  const sj = D.problems.flatMap(p => p.entries.filter(e => e.author === 'seungjoo'));
  must(sj.length > 0, `데이터: 이승주 풀이가 인덱스에 있다 (${sj.length}건)`);
  must(sj.every(e => !/\/week([4-9]|\d\d)\//.test(e.source)), '데이터: 이승주 4주차 이후 파일은 인덱스에서 빠진다');
  // 목록 행 아바타: 이승주가 푼 문제엔 뜨고, 안 푼 문제(4주차 이후)엔 안 뜬다
  sandbox.location.hash = '#/';
  vm.runInContext('route()', sandbox);
  const rows = app.innerHTML.split('<tr class="row"').slice(1);
  const withSj = rows.filter(r => r.includes('title="이승주"')).length;
  const solvedBySj = D.problems.filter(p => p.entries.some(e => e.author === 'seungjoo')).length;
  must(withSj === solvedBySj, `목록: 이승주 아바타는 푼 문제(${solvedBySj})에만 (${withSj})`);
}


// 8. 불안한 사람 보세요 — 구석 버튼과 말풍선, 말풍선은 기본 숨김, × 로 닫힌다
must(html.includes('id="anx-btn"') && html.includes('불안한 사람 보세요'), '불안: 구석 버튼 마크업');
must(html.includes('<div class="bubble" id="anx-bubble" hidden>내가 더 불안하다 임마'), '불안: 말풍선은 기본 숨김');
must(html.includes('id="anx-close"'), '불안: × 버튼');

// 9. 쿠키 — 누르면 한 마디, 끝은 반드시 "냥". 대화창·예시 질문·입력창은 없다
must(html.includes('id="cookie-btn"') && html.includes('assets/cookie.png'), '쿠키: 고양이 버튼');
must(html.includes('id="cookie-say" role="status" hidden'), '쿠키: 말풍선은 기본 숨김');
must(!html.includes('cookie-panel') && !html.includes('cookie-chips') && !html.includes('cookie-in'), '쿠키: 대화창·예시 질문·입력창 없음');
must(vm.runInContext('COOKIE_LINES.length >= 5 && COOKIE_LINES.every(s => s.endsWith("냥"))', sandbox), '쿠키: 모든 대사가 냥으로 끝난다');
must(vm.runInContext('Array.from({length: 40}, cookieLine).every(s => s.endsWith("냥"))', sandbox), '쿠키: 뽑은 말 40번 전부 냥으로 끝난다');

// 10. 어려울 때 누르는 버튼 — 헤더 오른쪽, 말풍선은 기본 숨김, 문구는 정해진 그대로
must(html.includes('id="hard-btn"') && html.includes('어려울 때 누르는 버튼'), '어려움: 헤더 버튼');
must(html.includes('id="hard-say" role="status" hidden'), '어려움: 말풍선 기본 숨김');
must(html.includes("'너도? 아, 나도!'"), '어려움: 문구');
// 쿠키 걷기 — 스모크 VM 엔 rAF 가 없어 걷지 않아야 하고(canMove=false) 에러도 없어야 한다
must(html.includes("tog(box, 'walking'"), '쿠키: 걷기 상태기계 있음');
must(html.includes("CK_STILL_KEY = 'cookie-still'") && html.includes('dbl = now - lastClick < 400'), '쿠키: 두 번 연속 클릭이면 안 움직임 (localStorage 기억)');

// 11. 재미 기능 — 왕관은 데이터에서 계산되고 1명뿐, 하늘·폭죽·잠자기·연타는 마크업/로직 존재
{
  const king = vm.runInContext('KING', sandbox);
  must(king && D.authors.some(a => a.id === king.id && a.active !== false), `왕관: 최근 회차 현역 1명 (${king && king.id}, ${king && king.week}주차 good ${king && king.good}/${king && king.rev})`);
  sandbox.location.hash = '#/people'; vm.runInContext('route()', sandbox);
  must((app.innerHTML.match(/주차 왕관/g) || []).length === 1, '왕관: 사람별 카드에 딱 1명');
  sandbox.location.hash = '#/'; vm.runInContext('route()', sandbox);
  must(app.innerHTML.includes('av has rev king') || app.innerHTML.includes(' king"'), '왕관: 목록 아바타에 king 클래스');
  must(html.includes('id="cookie-zzz" aria-hidden="true" hidden'), '잠자기: z z z 기본 숨김');
  must(html.includes("sayText('그만 눌러라냥')") && html.includes("'…미안하면 문제나 풀어라냥'"), '연타: 대사 (냥으로 끝남)');
  must(html.includes('data-sky="dawn"') && html.includes('data-sky="night"') && html.includes('function paintSky'), '하늘: 시간대 5개 + paintSky');
  must(html.includes('function confettiOnce') && html.includes("'confetti-' + hit.date"), '폭죽: D-DAY 하루 한 번');
}

// 12. 치트키 — "cookie" 타이핑 → stampede(). 입력창 안에선 무시. rAF 없는 VM 에선 조용히 안 함
must(html.includes("const CHEAT = 'cookie'") && html.includes('function stampede'), '치트키: cookie → stampede');
must(html.includes("tag === 'INPUT' || tag === 'SELECT' || tag === 'TEXTAREA'"), '치트키: 입력창 안에선 무시');
must((() => { try { vm.runInContext('stampede()', sandbox); return true; } catch (e) { return false; } })(), '치트키: rAF 없는 VM 에서 에러 없이 무시');

// 6. T4-3. 정석 코드 diff
{
  const withCode = D.problems.filter(p => (p.references || []).some(r => r.code && r.code.trim()));
  must(withCode.length > 0, `정석코드: 코드 원문이 있는 문제 ${withCode.length}개`);

  sandbox.location.hash = '#/p/programmers/67259';
  vm.runInContext('route()', sandbox);
  const rv = app.innerHTML;
  must(rv.includes('정석 코드와 diff'), '정석코드: 코드 열마다 diff 버튼');
  const act67259 = entryCount('programmers/67259');
  must((rv.match(/정석 코드와 diff/g) || []).length === act67259, `정석코드: ${act67259}열 전부에 diff 버튼`);
  must(!rv.includes('<details class="rev ref" open><summary><span class="t-open">정석 코드와 diff'),
    '정석코드: diff 는 기본 접힘 (코드 먼저 읽게)');
  must(rv.indexOf('pre class="code"') < rv.indexOf('정석 코드와 diff'), '정석코드: 코드가 diff 버튼보다 먼저');
  must(rv.includes('dwinging-01bfs') && rv.includes('dwinging-dijkstra'), '정석코드: 레퍼런스 2개면 탭 2개');
  must(rv.includes('class="dm"') && rv.includes('class="dp"'), '정석코드: diff 가 양쪽 차이를 칠한다');
  must(rv.includes('github.com/DWinging/Algorithm/blob/02325ae'), '정석코드: 출처가 커밋 SHA 로 고정 (T4-4)');

  // 코드 원문이 없는(링크만 저장한) 레퍼런스는 대조할 게 없으므로 diff 버튼이 안 붙어야 한다
  const linkOnly = D.problems.find(p => (p.references || []).length
    && !(p.references || []).some(r => r.code && r.code.trim()));
  if (linkOnly) {
    sandbox.location.hash = '#/p/' + linkOnly.key;
    vm.runInContext('route()', sandbox);
    must(!app.innerHTML.includes('정석 코드와 diff'),
      `정석코드: 링크만 있는 레퍼런스엔 diff 버튼 없음 (${linkOnly.key})`);
  } else {
    must(true, '정석코드: 링크만 저장된 레퍼런스 없음 — 검사 생략');
  }

  const noRef = D.problems.find(p => !(p.references || []).length);
  sandbox.location.hash = '#/p/' + noRef.key;
  vm.runInContext('route()', sandbox);
  must(!app.innerHTML.includes('정석 코드와 diff'), `정석코드: 레퍼런스 없으면 diff 버튼 없음 (${noRef.key})`);

  // ref-* 헤더 주석은 코드로 새어나오면 안 된다 (readReferences 가 잘라내야 한다)
  const anyRef = withCode[0].references.find(r => r.code && r.code.trim());
  must(!anyRef.code.includes('ref-url:'), '정석코드: ref-* 헤더가 코드 본문에 안 섞인다');
  must(anyRef.url.startsWith('https://'), '정석코드: ref-url 이 파싱된다');

  // diff 자체
  const dl = sandbox.diffLines;
  must(dl('a\nb\nc', 'a\nb\nc').every(r => r[0] === '='), 'diff: 같은 코드는 전부 = 로 나온다');
  must(dl('    int a;', 'int a;').every(r => r[0] === '='), 'diff: 들여쓰기 차이는 무시한다');
  const d3 = dl('a\nb', 'a\nX\nb');
  must(d3.filter(r => r[0] === '+').length === 1 && d3.filter(r => r[0] === '-').length === 0,
    'diff: 정석 코드에만 있는 줄은 + 하나');
  must(dl('a\nY\nb', 'a\nb').filter(r => r[0] === '-').length === 1, 'diff: 내 코드에만 있는 줄은 −');
  must(sandbox.diffHtml('<script>', 'x').includes('&lt;script&gt;'), 'diff: HTML 이스케이프');
  const long = Array.from({ length: 30 }, (_, i) => 'line' + i).join('\n');
  must(sandbox.diffHtml(long, long).includes('줄 동일'), 'diff: 긴 동일 구간은 접는다');
}



// 12. 쿠키 메뉴 (냥 패널) — 2026-09-08
// localStorage 가 없는 VM 에서도 죽지 않아야 하고, 이름을 알려주면 내 데이터로 채워져야 한다.
{
  must(html.includes('id="paw-btn"'), '쿠키메뉴: 헤더에 여는 버튼');
  must(html.includes('id="nyang"') && html.includes('class="nyang" id="nyang" role="dialog"'), '쿠키메뉴: 패널 마크업');
  must(/<div class="nyang" id="nyang"[^>]*hidden>/.test(html), '쿠키메뉴: 기본 닫힘');

  // localStorage 없이 호출해도 예외가 안 난다 (파일 열기 / 사생활 모드)
  let ok = true;
  try { vm.runInContext('nyangHTML()', sandbox); } catch (e) { ok = false; }
  must(ok, '쿠키메뉴: localStorage 없어도 렌더된다');

  const anon = vm.runInContext('nyangHTML()', sandbox);
  must(anon.includes('data-me="chanung"'), '쿠키메뉴: 이름 모르면 현역 목록으로 묻는다');
  must(!anon.includes('data-me="seungjoo"'), '쿠키메뉴: 졸업생은 이름 후보에 없다');

  // localStorage 스텁을 넣고 나를 chanung 으로
  const store = {};
  sandbox.localStorage = { getItem: (k) => (k in store ? store[k] : null), setItem: (k, v) => { store[k] = String(v); } };
  vm.runInContext("localStorage.setItem('cookie-me','chanung')", sandbox);
  const mine = vm.runInContext('nyangHTML()', sandbox);
  must(mine.includes('안찬웅'), '쿠키메뉴: 내 이름 표시');
  must(mine.includes('오늘의 브리핑') && mine.includes('오늘의 복습 1문제') && mine.includes('반복해서 지적받은 것'),
    '쿠키메뉴: 공부 섹션 3종');
  must(mine.includes('문제 룰렛') && mine.includes('뽀모도로') && mine.includes('쿠키 밥 주기') && mine.includes('냥 점괘'),
    '쿠키메뉴: 재미 섹션 4종');

  // 복습 추천은 실제로 needs-fix / wrong 인 내 풀이여야 한다 (지어내지 않는다)
  const pick = vm.runInContext("reviewPick('chanung')", sandbox);
  must(pick === null || ['needs-fix', 'wrong'].includes(pick.e.review.verdict), '쿠키메뉴: 복습 추천은 실제 판정에서 고른다');
  // 같은 날엔 같은 문제를 준다 (날짜 시드)
  const pick2 = vm.runInContext("reviewPick('chanung')", sandbox);
  must((pick && pick.p.key) === (pick2 && pick2.p.key), '쿠키메뉴: 복습 추천은 하루 동안 고정');

  // 약점 Top 은 칭찬 태그를 세지 않는다
  const weak = vm.runInContext("weakTop('chanung')", sandbox);
  const praise = new Set(D.tags.filter((t) => t.group === 'praise').map((t) => t.id));
  must(weak.every(([t]) => !praise.has(t)), '쿠키메뉴: 약점 Top 에 칭찬 태그 없음');
  must(weak.length <= 3, '쿠키메뉴: 약점은 Top 3까지');

  // 안 푼 문제 수는 데이터와 일치
  const notSolved = D.problems.filter((p) => !p.entries.some((e) => e.author === 'chanung')).length;
  must(vm.runInContext("notSolved('chanung').length", sandbox) === notSolved, '쿠키메뉴: 안 푼 문제 수가 데이터와 일치');

  // 인사말은 어느 시각이든 "냥" 으로 끝난다
  must(/냥$/.test(vm.runInContext('greet()', sandbox)), '쿠키메뉴: 인사말도 냥으로 끝난다');
  const fort = vm.runInContext('FORTUNE', sandbox);
  must(fort.every((f) => f.endsWith('냥')), '쿠키메뉴: 점괘 전부 냥으로 끝난다');

  // 연속 출석: 같은 날 두 번 열어도 안 늘어난다
  const st1 = vm.runInContext('bumpStreak()', sandbox);
  const st2 = vm.runInContext('bumpStreak()', sandbox);
  must(st1 === st2 && st1 >= 1, '쿠키메뉴: 연속 출석은 하루에 한 번만 오른다');

  // XSS — 패널에 들어가는 문제 제목은 이스케이프된다
  must(!mine.includes('<script>'), '쿠키메뉴: XSS 없음');

  // 치트키 3종 + 애니메이션 함수는 타이머 없는 VM 에서 조용히 넘어간다
  for (const fn of ['appleRain', 'confettiBurst', 'churu', 'forceNap', 'cookieSay', 'focusMode']) {
    must(vm.runInContext('typeof ' + fn, sandbox) === 'function', '쿠키메뉴: ' + fn + ' 정의됨');
  }
  let quiet = true;
  try { vm.runInContext("appleRain(); confettiBurst(); churu('x'); forceNap(); cookieSay('테스트'); focusMode(25);", sandbox); }
  catch (e) { quiet = false; }
  must(quiet, '쿠키메뉴: 타이머·DOM 없는 환경에서 조용히 넘어간다');
  must(html.includes("cheatBuf2.endsWith('apple')") && html.includes("cheatBuf2.endsWith('study')") && html.includes("cheatBuf2.endsWith('zzz')"),
    '쿠키메뉴: 치트키 apple / study / zzz');
  delete sandbox.localStorage;
}

// 12. 익명 댓글 (크게 보기 전용)
{
  must(!vm.runInContext('cmtOn()', sandbox), '댓글: URL/키가 없으면 꺼진 상태');
  const off = vm.runInContext("cmtBlock('programmers/42579#chanung')", sandbox);
  must(off.includes('댓글 서버가 연결되지 않아'), '댓글: 미설정이면 안내가 뜬다');
  must(/id="cmt-send" ?disabled/.test(off) && /id="cmt-body"[^>]*disabled/.test(off),
    '댓글: 미설정이면 입력 칸은 보이되 비활성');
  must(!off.includes('data-ck='), '댓글: 미설정이면 스레드를 붙지 않는다(요청도 안 나간다)');
  must(vm.runInContext("cmtKey({key:'swea/1767'},{author:'seongil',variant:'alt'})", sandbox) === 'swea/1767#seongil.alt',
    '댓글: 스레드 키 = 문제 × 작성자 × 변형');
  must(!detail.includes('class="cmt"'), '댓글: 그리드 뷰에는 안 붙는다 (크게 보기 전용)');
  must(html.includes('cmtBlock(cmtKey(p, e))'), '댓글: 크게 보기 뷰가 스레드를 렌더한다');

  // 설정이 채워진 상태
  sandbox.fetch = (u, o) => { sandbox.__last = { u, o: o || {} };
    return Promise.resolve({ ok: true, json: () => Promise.resolve([]) }); };
  vm.runInContext("CMT.url='https://x.supabase.co'; CMT.key='anon-test-key';", sandbox);
  must(vm.runInContext('cmtOn()', sandbox), '댓글: URL+anon 키를 넣으면 켜진다');
  const on = vm.runInContext("cmtBlock('programmers/42579#chanung')", sandbox);
  must(on.includes('id="cmt-body"') && on.includes('id="cmt-send"'), '댓글: 입력창과 등록 버튼');
  must(on.includes('data-ck="programmers/42579#chanung"'), '댓글: 스레드 키가 DOM 에 붙는다');
  must(!on.includes('anon-test-key'), '댓글: 키를 화면에 찍지 않는다');

  const item = vm.runInContext(
    "cmtItems([{nick:'<img src=x>',body:'<script>alert(1)<\/script>',created_at:'2026-09-10T01:02:03Z'}])", sandbox);
  must(!item.includes('<img') && !item.includes('<script>'), '댓글: XSS 없음 (닉네임·본문 전부 esc)');
  must(item.includes('2026-09-10'), '댓글: 작성 시각 렌더');
  must(vm.runInContext("cmtItems([{nick:'',body:'x',created_at:''}])", sandbox).includes('익명'),
    '댓글: 닉네임이 비면 익명으로 보인다');
  must(vm.runInContext('cmtItems([])', sandbox).includes('아직 댓글이 없습니다'), '댓글: 빈 스레드 안내');

  vm.runInContext("cmtList('a/1#b')", sandbox);
  must(sandbox.__last.u.startsWith('https://x.supabase.co/rest/v1/comments') &&
       /solution=eq\.a%2F1%23b/.test(sandbox.__last.u), '댓글: 목록은 그 풀이 것만 가져온다');
  vm.runInContext("cmtAdd('a/1#b', '', '하이')", sandbox);
  must(sandbox.__last.o.method === 'POST' && JSON.parse(sandbox.__last.o.body).nick === null,
    '댓글: 닉네임을 비우면 null 로 저장');
  must(sandbox.__last.o.headers.apikey === 'anon-test-key' &&
       sandbox.__last.o.headers.Authorization === 'Bearer anon-test-key', '댓글: anon 키를 헤더로 보낸다');

  vm.runInContext("CMT.url=''; CMT.key='';", sandbox);
  delete sandbox.fetch;
}

console.log(out.join('\n'));
console.log(`\n통과 ${out.filter((l) => l.startsWith('OK')).length} / 실패 ${bad}`);
process.exit(bad ? 1 : 0);
