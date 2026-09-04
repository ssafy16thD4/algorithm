---
platform: swea
problemId: "1952"
author: seongil
source: 이성일/week5/SWEA1952.java
week: 5
compiles: false
lang: java
verdict: needs-fix
tags: [missing-return, good-complexity, good-readability]
complexity:
  time: O(T·12)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 수영장 (swea/1952) — seongil

## 접근

12개월을 1차원 DP 로 놓고 `dp[i]` = 1월부터 i월까지의 최소 비용으로 정의했다.
전이가 두 갈래뿐이라는 걸 정확히 봤다 — **3개월권을 쓰면 `dp[i-3]`에서 건너뛰고, 아니면 `dp[i-1]`에
그 달 요금(1일권 × 이용일수 vs 1달권 중 싼 것)을 더한다.** 마지막에 1년권과 한 번 비교하고 끝낸다.

`Math.min(pees[0]*months[i], pees[1])` 로 "그 달을 어떻게 끊을지" 를 전이 안에서 즉시 접은 게 좋다.
1일권/1달권을 별도 상태로 두면 상태가 늘어나는데, 어차피 그 달 안에서 독립적인 선택이라 접어도 된다는
판단이 정확하다. 이 문제에서 상태를 과하게 늘려 헤매는 경우가 많다.

**정확성은 확인했다.** 정석 DP(`dp[i] = min(dp[i-1] + 그달요금, dp[max(0,i-3)] + 3개월권)`)와
무작위 4,000건을 대조해 **전부 일치**했다. 아래 2번에서 우려했던 `dp[2]` 도 결과에 영향이 없었다.

## 개선점

### 1. (치명) 눈에 안 보이는 공백 문자 443개 때문에 컴파일이 안 된다 — <missing-return>

```
이성일/week5/SWEA1952.java:3: error: illegal character: ' '
```

파일 전체에 **U+00A0 (non-breaking space)** 가 443개 들어 있다. 웹 페이지나 블로그에서 코드를 복사할 때
따라오는 문자로, 화면에는 평범한 공백처럼 보이지만 `javac` 는 식별자로 쓸 수 없는 문자로 판정한다.
**로직과 무관하게 이 파일은 지금 상태로는 제출해도 컴파일 단계에서 떨어진다.**

고치는 법은 에디터에서 전부 일반 공백으로 바꾸는 것이다.

- VS Code: `Ctrl+H` → 정규식 모드(`.*` 아이콘) 켜고 ` ` → 공백
- IntelliJ: `Ctrl+Shift+R` → Regex 체크 → `\x{00A0}` → 공백

앞으로 붙여넣기할 때 한 번 확인하는 게 좋다. 같은 문자가 섞이면 매번 같은 에러가 난다.
(실제로 이 파일도 치환하고 나니 바로 컴파일됐고, 그 상태로 위 4,000건 대조를 돌렸다.)

### 2. (사소) `dp[1]`, `dp[2]` 만 손으로 펼친 게 실제로는 안전했다 — 기록

```java
dp[1] = Math.min(pees[0]*months[1], pees[1]);
dp[2] = Math.min(pees[0]*months[2] + dp[1], pees[1] + dp[1]);
for (int i = 3; ...) dp[i] = Math.min(dp[i-3] + pees[2], ...);
```

`dp[1]`, `dp[2]` 에는 3개월권 선택지가 빠져 있다. 3개월권 하나가 1달권 두 장보다 쌀 수 있으므로
(`month=100, three=150`) 이론상 `dp[2]` 는 실제 최솟값보다 클 수 있다.

**그런데 답에는 영향이 없다.** 3개월권을 2월에 끊으면 어차피 4월까지 덮으므로 그 경우는 `dp[4]` 에서
`dp[1] + three` 로 잡히고, 12가 3의 배수라 최적 경로가 `dp[2]` 를 거쳐야만 하는 상황이 안 생긴다.
무작위 4,000건에서도 한 건도 어긋나지 않았다.

다만 경계를 손으로 펼치면 이런 확인을 매번 해야 한다. `dp[Math.max(0, i-3)]` 한 줄로 통일하면
특수 케이스 자체가 사라진다.

```java
int[] dp = new int[13];
for (int i = 1; i <= 12; i++) {
    dp[i] = dp[i - 1] + Math.min(pees[0] * months[i], pees[1]);
    dp[i] = Math.min(dp[i], dp[Math.max(0, i - 3)] + pees[2]);
}
int answer = Math.min(dp[12], pees[3]);
```

**검증함** — 위 정석 DP 가 바로 4,000건 대조에 쓴 레퍼런스이고, 원본과 전부 같은 답을 냈다.

### 3. (사소) 출력이 테스트케이스마다 `System.out.println` — <io-performance>

`StringBuilder` 를 매 케이스마다 새로 만들어서 한 줄 찍고 버린다.
`sb` 를 루프 밖에 하나 두고 마지막에 `System.out.print(sb)` 한 번이면 된다.
T 가 작아서 이 문제에서 시간 초과가 나진 않지만, `StringBuilder` 를 쓰는 이유 자체가 그거라 습관 차원이다.

## 복잡도

- 시간: `O(T·12)` — 케이스당 12칸 DP. 사실상 상수
- 공간: `O(1)` — `dp[13]`, `months[13]` 고정 크기

## 요약

전이를 두 갈래로 줄이고 그 달 요금을 `min` 하나로 접은 판단이 정확해서, DP 자체는 흠잡을 데가 없다.
정석 DP 와 무작위 4,000건 전부 일치했다. 문제는 알고리즘이 아니라 **파일에 섞여 들어간 U+00A0 443개**로,
지금 상태로는 컴파일조차 안 된다. 그것만 치환하면 바로 통과한다.
