---
platform: swea
problemId: "2115"
author: chanung
source: 안찬웅/week6/벌꿀채취.java
week: 6
compiles: true
lang: java
verdict: good
tags: [missing-return]
complexity:
  time: O(N^2 · 2^M + N^4)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 벌꿀채취 (swea/2115) — chanung

## 접근

두 단계로 나눈 것이 이 풀이의 좋은 점이다. 먼저 시작 좌표마다 **덩어리 하나의 최대 수익**을
부분집합 DFS 로 구해 `bestArr` 에 저장하고(`sum > c` 가지치기 포함), 그다음 `bestArr` 에서 겹치지
않는 두 좌표를 골라 합의 최댓값을 구한다. 두 벌꿀통이 독립이라는 사실을 이용해 문제를 반으로
쪼갠 판단이 정확하다.

겹침 판정도 깔끔하다.

```java
for(int i2=i1; i2<n; i2++) {
    int start = (i1 == i2) ? j1 + m : 0;   // 같은 행이면 j1+m 부터, 다른 행이면 0부터
```

`i2` 를 `i1` 부터 돌려서 같은 쌍을 두 번 세지 않고, 같은 행일 때만 열 조건을 거는 한 줄로
"겹치면 안 된다"를 전부 처리한다.

## 개선점

### 1. (수정 완료 / 치명) 중괄호가 모자라 컴파일이 안 됐다 — `missing-return`

`i1`, `j1`, `i2`, `j2` 4중 for 를 열어놓고 닫는 `}` 가 2개뿐이었다. `sb.append(...)` 가 `j1` 루프
안에, `System.out.print(sb)` 가 `i1` 루프 안에 갇히면서 파일 끝까지 브레이스가 밀렸고,
`static void dfs(...)` 선언이 메서드 안의 문장처럼 취급돼 이렇게 났다.

```
안찬웅/week6/벌꿀채취.java:67: error: illegal start of expression
	static void dfs(int x, int y, int idx, int sum, int score) {
```

`j1`·`i1` 을 닫는 `}` 두 개를 `sb.append(...)` 앞에 넣어 해결했다. 나머지 로직은 손대지 않았다.

### 2. (수정 완료 / 중요) `import` 두 줄과 `public` 이 빠져 있었다

`BufferedReader`/`StringTokenizer` 를 쓰면서 `java.io.*`, `java.util.*` import 가 없어 컴파일이 안 됐다.
두 줄을 넣어 해결했다.

## 검증

브레이스와 import 를 고친 뒤 실제로 컴파일해서 표준 예제를 돌렸다.

```
입력  5 3 10 / 7 2 6 9 2 / 1 1 1 1 1 / 8 5 5 5 5 / 1 1 1 1 1 / 1 1 1 1 1
출력  #1 145
손계산 81(2행 [2,6,9]에서 9만 채취) + 64(4행 [8,5,5]에서 8만 채취) = 145 로 일치
```

**채점 데이터로 돌려본 것은 아니다** — 전 범위 검증은 안 했다.

## 복잡도

- 시간: `O(N^2 · 2^M)`(덩어리별 부분집합) + `O(N^4)`(두 덩어리 조합). N ≤ 10, M ≤ 5 라 여유 있다.
- 공간: `O(N^2)` — graph, bestArr.

## 요약

접근과 구조는 처음부터 맞았고 막힌 건 브레이스 하나였다. 4중 for 처럼 중첩이 깊어지면
**안쪽 루프를 먼저 완성해 닫고 바깥으로 나오는** 순서로 쓰는 게 안전하다. 지금은
"덩어리 최대 수익 계산 → 조합" 두 단계가 함수 경계로 갈려 있어 읽기 좋다.
