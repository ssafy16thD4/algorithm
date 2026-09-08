---
platform: ssafy
problemId: "apple-game"
author: seongil
source: 이성일/week6/사과먹기게임.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [logic-edge-case, wrong-algorithm]
complexity:
  time: O(T · (N^2 + A))
  space: O(N^2 + A)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 사과 먹기 게임 (ssafy/apple-game) — seongil
## 접근

사과 번호 순서대로 좌표를 모아 두고, 다음 사과까지 **필요한 방향**(가로 성분·세로 성분)을 8가지 분기로 뽑은 뒤
`rotateCounter()` 로 시계 회전 수를 더한다. "전진은 공짜, 회전만 센다" 를 격자 탐색 없이 좌표 계산으로
바꾼 판단 자체는 맞다 — 이 문제는 실제로 탐색 없이 풀린다.

## 개선점

### 1. (치명) 두 방향이 필요할 때 도는 순서를 고르지 않는다

`rotateCounter(currDir, {a, b})` 는 `a` 를 먼저 돌고 `b` 를 나중에 도는 한 가지만 계산한다.
그런데 대각선 이동은 **어느 축을 먼저 맞추느냐에 따라 총 회전 수가 달라진다.**
시계 회전만 가능하므로 `현재→a→b` 와 `현재→b→a` 가 서로 다른 값이 나온다.

또 `flag` 분기가 그 위에 얹혀 있다. 필요한 방향 중 하나가 이미 현재 방향과 같으면
`tempCnt`(그때까지 시험 삼아 센 값)를 그대로 반환하는데, `tempDir` 은 루프를 돌며 이미 갱신된 상태라
반환값이 어느 경로의 비용인지 코드만으로는 정해지지 않는다.

실제로 돌려본 반례 (3×3, 사과 1이 `(1,0)`, 2가 `(2,1)`):

```
000
100
020
```

기대 출력 `#1 4`, 이 코드 출력 `#1 5`.
`(0,0)` 우향 → 아래로 1회 회전해 `(1,0)` 도달 → 아래로 전진해 `(2,0)` → 우향까지 3회 회전 → `(2,1)`. 합 4다.

무작위 1,500건 대조에서 **252건이 불일치**했다. 더 큰 예도 하나 남긴다(5×5, 기대 14, 출력 18).

```
10400
53702
00060
08000
00000
```

**고치는 방법**: 방향이 4개뿐이므로 **직전 사과에서 어느 방향을 보고 있었는지 4가지를 전부 들고 간다.**
사과마다 4×(순서 2가지)만 계산하면 되고 전체가 `O(사과 수 × 4)` 다.

```java
int[] dp = {0, INF, INF, INF};              // 시작은 우향(0)
for (int i = 0; i + 1 < pts.length; i++) {
    int dr = pts[i+1][0] - pts[i][0], dc = pts[i+1][1] - pts[i][1];
    List<Integer> need = new ArrayList<>();
    if (dc != 0) need.add(dc > 0 ? 0 : 2);   // 우 / 좌
    if (dr != 0) need.add(dr > 0 ? 1 : 3);   // 하 / 상
    int[] nd = {INF, INF, INF, INF};
    for (int d = 0; d < 4; d++) {
        if (dp[d] >= INF) continue;
        if (need.isEmpty()) { nd[d] = Math.min(nd[d], dp[d]); continue; }
        for (int f = 0; f < need.size(); f++) {          // 어느 축을 먼저 맞출지
            int a = need.get(f), cost = dp[d] + cw(d, a), end = a;
            if (need.size() == 2) { int b = need.get(1 - f); cost += cw(a, b); end = b; }
            nd[end] = Math.min(nd[end], cost);
        }
    }
    dp = nd;
}
// 답은 min(dp)
// cw(from, to) = (to - from + 4) % 4   // 시계 회전 수
```

**검증함** — 이 DP 를 독립 다익스트라 레퍼런스와 무작위 3,000건 대조해 **불일치 0건**이다.
같은 문제의 `안찬웅/week6/사과 먹기 게임.java`(0-1 BFS)도 1,500건에서 0건이라, 두 방식 모두 정답이다.

### 2. (중요) 사과 번호가 9 를 넘으면 좌표 배열이 어긋난다

`board[i][j] = row.charAt(j) - '0'` 이라 한 자리 숫자만 읽는다. `appleNum` 을 "0 이 아닌 칸 수" 로 세고
`points[board[i][j]]` 에 좌표를 넣는 구조라, 두 자리 번호가 있으면 인덱스가 밀리거나 범위를 벗어난다.
**→ 사과 개수 제약 확인 필요.**

## 복잡도

- 시간: `O(T · (N² + A))` — 격자 스캔이 지배적. `A` 는 사과 수
- 공간: `O(N² + A)`

## 요약

탐색 없이 좌표만으로 회전 수를 세겠다는 방향은 맞고, 그게 이 문제의 정석에 가깝다.
틀린 건 **두 축을 맞추는 순서를 고르지 않은 것** 하나다. 방향 4개를 상태로 들고 가면
`rotateCounter` 의 `flag` 분기까지 통째로 사라진다.
