---
platform: ssafy
problemId: "14-3-1"
author: chanung
source: 안찬웅/week6/14기 3차 1번.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [time-complexity, overflow]
complexity:
  time: O(N·K + N^2)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 최대 부분 수열 (14기 3차 1번) (ssafy/14-3-1) — chanung
SSAFY 14기 3차 A형 시험 1번으로 보인다. 공개 문제 페이지가 없어 `problems.json` 에 못 넣었다.
주석대로 **"길이 K 인 연속 부분 수열 두 개를 겹치지 않게 골라 합을 최대로"** 로 읽고 리뷰했다.

## 접근

두 단계다. 먼저 모든 시작점 `i` 의 K칸 합 `arrSum[i]` 를 만들고, 그다음 `j ≥ i+K` 인 쌍
`(i, j)` 를 전부 돌며 `arrSum[i] + arrSum[j]` 의 최댓값을 잡는다. "겹치지 않음 = 두 번째 시작점이
첫 번째 시작점 + K 이상" 이라는 조건을 `j = i+k` 한 줄로 정확히 옮겼다.

**실제로 돌려본 검증**: 삼중 루프 완전탐색과 무작위 900건(`N ≤ 20`, 값 −50~50)을 대조해
**불일치 0건**. 답은 맞는다.

## 개선점

### 1. (중요) 쌍 열거가 O(N²) 이라 N 이 커지면 시간초과 — 누적합 + 접미사 최댓값으로 O(N)

`arrSum` 을 만드는 데 `O(N·K)`, 쌍을 도는 데 `O(N²)` 이다. **N 제약을 확인하지 못했지만**
실측하면 감이 온다.

| N | K | 원본 | 아래 수정안 |
| --- | --- | --- | --- |
| 100,000 | 1,000 | 2.0초 | 0.1초 |
| 300,000 | 1,000 | 34.4초 | 0.1초 |

N 이 수천이면 문제없고, 10⁵ 급이면 못 쓴다. 시험 1번에서 N 이 작았다면 이 코드로 통과했을 것이고,
컸다면 이 지점이 원인이다. 어느 쪽이든 **O(N) 풀이가 더 짧으므로** 그쪽을 기본형으로 두는 게 낫다.

핵심은 두 가지. 구간 합은 누적합으로 O(1) 에 뽑고, "`j ≥ i+K` 중 최대" 는 **뒤에서부터 접미사
최댓값** 을 한 번 만들어 두면 `i` 마다 O(1) 이다.

```java
long[] pre = new long[n + 1];
for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + arr[i];

int w = n - k + 1;                       // 시작점 개수
long[] win = new long[w];
for (int i = 0; i < w; i++) win[i] = pre[i + k] - pre[i];

long[] sufMax = new long[w + 1];         // sufMax[i] = max(win[i..w-1])
sufMax[w] = Long.MIN_VALUE / 4;
for (int i = w - 1; i >= 0; i--) sufMax[i] = Math.max(win[i], sufMax[i + 1]);

long best = Long.MIN_VALUE;
for (int i = 0; i + k < w; i++) best = Math.max(best, win[i] + sufMax[i + k]);
```

**검증함** — 위 코드를 원본·완전탐색과 같은 900건에 대조해 불일치 0건, 위 표의 시간도 이 코드로 잰 것이다.

### 2. (중요) 합이 `int` 를 넘길 수 있다 — 값 범위 확인 필요

`sum` 과 `twoSum` 이 `int` 다. 원소가 10⁹ 급이면 K=3 만 돼도 넘친다. 값 제약을 모르니 확정은
못 하지만, 누적합 문제는 **합을 담는 변수는 `long`** 으로 두는 게 비용 0 인 습관이다. 위 수정안은 그렇게 했다.

### 3. (사소) `n < 2K` 이면 `Integer.MIN_VALUE` 가 찍힌다

쌍이 하나도 없을 때 `maxSum` 초기값이 그대로 출력된다. 문제가 `2K ≤ N` 을 보장하면 무관하다.
보장이 없다면 그 경우 답이 뭔지(0? 불가능 표기?) 지문에서 확인해야 한다.

## 복잡도

- 시간: `O(N·K + N²)` — 쌍 열거가 지배적. 수정안은 `O(N)`
- 공간: `O(N)`

## 요약

겹치지 않는 조건을 `j = i+K` 로 옮긴 것과 두 단계 구조는 맞고, 무작위 대조에서 전부 정답이다.
문제는 규모다 — 쌍을 전부 도는 대신 **접미사 최댓값 배열 하나**를 두면 O(N) 이고 코드도 더 짧다.
"두 구간 중 하나를 고정하면 나머지는 범위 최댓값 질의" 라는 틀은 이 유형에서 계속 나오니 익혀 두면 좋다.
