---
platform: swea
problemId: "1952"
author: chanung
source: 안찬웅/week5/수영장.java
week: 5
compiles: true
verdict: good
tags: [good-readability, time-complexity]
complexity:
  time: O(3^12)
  space: O(12)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 수영장 (swea/1952) — chanung

## 접근

달마다 `1일권 / 1달권 / 3개월권` 세 갈래를 그대로 재귀로 펼치고, 12월을 넘어가면 최솟값을 갱신한다. 마지막에 1년권과 한 번 비교한다. **그리디로 줄이려 하지 않고 전부 세어본** 판단이 이 문제에서는 정확하다 — 달이 12개뿐이라 완전탐색이 충분히 싸고, 3개월권 구간을 어떻게 끊을지는 그리디로 맞히기 까다롭기 때문이다.

`dfs(money + charge[2], depth+3)` 로 3개월권이 세 달을 한 번에 건너뛰게 한 것도 간결하다. 12를 넘겨도 `depth >= 12` 한 줄이 받아내므로 별도 경계 처리가 필요 없다.

같은 문제에서 그리디로 접근한 김준수 코드는 실제로 반례가 나왔다(`reviews/swea/1952/junsoo.md` 참고). 이 풀이는 그 반례를 포함해 무작위 60건에서 전부 최적값을 냈다.

**검증**: 별도로 작성한 DP(`dp[i] = min(1일권+dp[i+1], 1달권+dp[i+1], 3개월권+dp[i+3])`)와 대조해 값이 일치함을 확인했다.

## 개선점

### 1. (사소) 같은 상태를 여러 번 다시 센다 — `time-complexity`

`dfs(money, depth)` 에서 결과에 실제로 영향을 주는 것은 `depth` 뿐이고, `money` 는 지금까지의 누적일 뿐이다. 그래서 같은 `depth` 를 서로 다른 경로로 도달할 때마다 그 아래를 처음부터 다시 센다. 호출 수가 `O(3^12)` 약 53만 번이다.

12개월 고정이라 지금은 아무 문제 없지만, "남은 개월 수"로 상태를 잡으면 12번이면 끝난다:

```java
static int[] memo;   // memo[depth] = depth월부터 12월까지의 최소 추가 비용

static int dfs(int depth) {
    if (depth >= 12) return 0;
    if (memo[depth] != -1) return memo[depth];
    int best = month[depth] * charge[0] + dfs(depth + 1);   // 1일권
    best = Math.min(best, charge[1] + dfs(depth + 1));      // 1달권
    best = Math.min(best, charge[2] + dfs(depth + 3));      // 3개월권
    return memo[depth] = best;
}
```

`minMoney = Math.min(dfs(0), charge[3])` 로 받으면 된다. 호출이 53만 번에서 12번으로 줄고, 무엇보다 **누적값을 파라미터로 끌고 다니지 않아서** 함수가 "이 달부터 끝까지의 최소 비용"이라는 한 문장으로 읽힌다.

### 2. (사소) `charge[0..3]` 대신 이름을 붙이면 읽기 쉽다 — `magic-number`

`charge[0]` 이 1일권인지 1년권인지 알려면 입력 파싱까지 거슬러 올라가야 한다. `int dayFee = charge[0], monthFee = charge[1], threeFee = charge[2], yearFee = charge[3];` 한 줄이면 `dfs` 본문이 그대로 설명이 된다. 주석에 이미 "1일이용권, 1달이용권, 3달이용권, 1년이용권" 이라고 적어둔 걸 코드로 옮기는 셈이다.

## 복잡도

- 시간: `O(3^12)` ≈ 53만 — 달마다 3갈래. 메모이제이션을 넣으면 `O(12)`.
- 공간: `O(12)` — 재귀 깊이 최대 12.

## 요약

그리디의 유혹을 피하고 완전탐색으로 간 판단이 이 문제의 핵심이고, 실제로 그리디 풀이에서 나온 반례를 이 코드는 통과한다. 구조도 짧고 명확하다. 상태가 `depth` 하나뿐이므로 메모이제이션 한 줄이면 지수가 선형이 되는데, 그건 성능 문제라기보다 **함수의 의미가 더 선명해지는** 쪽의 이득이다.
