---
platform: ssafy
problemId: "balloon-shooting"
author: junsoo
source: 김준수/week6/풍선 사격 게임.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [time-complexity, overflow, good-decomposition]
complexity:
  time: O(N!)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 풍선 사격 게임 (ssafy/balloon-shooting) — junsoo
## 접근

풍선을 터뜨릴 때 이웃 값이 바뀌므로 **이중 연결 리스트를 배열 두 개(`prev`, `next`)로 직접 구현**하고,
백트래킹으로 모든 순서를 시도한다. 삭제·복구가 대칭이라 되돌리기가 정확하고, 양 끝(-1) 처리도 빠짐없다.
점수 규칙 네 갈래(양쪽·왼쪽만·오른쪽만·없음)도 문제 그대로다.

**실제로 돌려본 검증**: 구간 DP 레퍼런스와 무작위 1,500건(N ≤ 8) 대조해 **불일치 0건**.
점수 계산과 리스트 조작은 맞다.

## 개선점

### 1. (중요) `O(N!)` 이라 N 이 조금만 커져도 못 쓴다 — 구간 DP 로 `O(N³)`

파일 주석에 "N 최대값을 몰라서 안전하게 진행" 이라고 적혀 있는데, 실측하면 안전한 쪽이 아니다.

| N | 이 코드 | 구간 DP |
| --- | --- | --- |
| 9 | 6 ms | 1 ms 미만 |
| 10 | 73 ms | 1 ms 미만 |
| 11 | 805 ms | 1 ms 미만 |
| 200 | 사실상 불가능 | 2 ms |

**핵심 전환은 "무엇을 먼저 터뜨릴까" 대신 "무엇을 마지막에 터뜨릴까" 를 고르는 것이다.**
구간 `[l, r]` 안에서 마지막에 터지는 풍선을 `k` 로 잡으면, 그 시점에 `k` 의 이웃은 반드시
`l-1` 과 `r+1`(구간 밖) 이다. 즉 `k` 의 점수가 구간 안의 순서와 무관하게 정해지고,
양옆이 독립된 부분 문제로 갈린다.

```java
long[][] dp = new long[n + 1][n + 1];
for (int len = 1; len <= n; len++)
    for (int l = 0; l + len - 1 < n; l++) {
        int r = l + len - 1;
        long best = 0;
        for (int k = l; k <= r; k++) {
            long v;
            boolean L = l > 0, R = r < n - 1;
            if (L && R)  v = (long) b[l - 1] * b[r + 1];
            else if (L)  v = b[l - 1];
            else if (R)  v = b[r + 1];
            else         v = b[k];
            long left  = k > l ? dp[l][k - 1] : 0;
            long right = k < r ? dp[k + 1][r] : 0;
            best = Math.max(best, left + right + v);
        }
        dp[l][r] = best;
    }
// 답은 dp[0][n - 1]
```

**검증함** — 위 DP 가 이 파일의 백트래킹과 무작위 1,500건에서 불일치 0건이고, 위 표의 시간도 이 코드로 쟀다.

### 2. (중요) 점수 합이 `int` 를 넘길 수 있다 — 제약 확인 필요

`score` 가 `int` 다. 풍선 값이 크고 N 이 길면 곱의 합이 넘친다. 위 DP 는 `long` 으로 뒀다.

## 복잡도

- 시간: `O(N!)` — 모든 순서. DP 는 `O(N³)`
- 공간: `O(N)` — 연결 리스트 배열. DP 는 `O(N²)`

## 요약

연결 리스트 직접 구현과 되돌리기는 정확하고, 작은 입력에서는 답도 맞다.
막히는 지점은 순서를 전부 시도한다는 것 하나다. **"마지막에 터질 풍선"** 을 기준으로 구간을 쪼개면
같은 점수 규칙을 그대로 쓰면서 `O(N³)` 이 된다.
