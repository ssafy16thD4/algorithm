---
platform: ssafy
problemId: "interview"
author: junsoo
source: 김준수/week6/면접 실패.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [logic-edge-case, time-complexity]
complexity:
  time: O(N! · N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 면접 (ssafy/interview) — junsoo (실패 버전)
파일 주석이 이미 `O(N!)` 이라 시간초과라고 진단하고 있다. 맞는 진단이지만, **그 전에 입력을 못 읽는다.**

## 개선점

### 1. (치명) 입력 파싱이 첫 줄부터 예외로 죽는다

```java
String[] line = br.readLine().trim().split("");
N = Integer.parseInt(line[0]);
M = Integer.parseInt(line[1]);
```

`split("")` 은 **문자 하나씩** 쪼갠다. `"5 3 2"` 는 `["5", " ", "3", " ", "2"]` 가 되고
`line[1]` 이 공백이라 바로 터진다. 실제로 돌려본 결과다.

```
입력  1 / 5 3 2
실행  java.lang.NumberFormatException: For input string: " "
```

두 자리 수 이상이면 `split(" ")` 로 바꿔도 자리가 밀리므로, 다른 파일에서 쓰는
`StringTokenizer` 를 그대로 쓰는 게 맞다.

```java
StringTokenizer st = new StringTokenizer(br.readLine().trim());
N = Integer.parseInt(st.nextToken());
M = Integer.parseInt(st.nextToken());
K = Integer.parseInt(st.nextToken());
```

### 2. (치명) 서로 같은 값을 자리만 바꿔 세느라 `O(N!)` 이다 — 사실은 배치를 구성하면 끝난다

`dfs` 가 N개 자리에 N개 원소를 **순열**로 깔고 리프에서 점수를 잰다. `interview` 안의 값은
`true` M개 + `false` N-M개뿐이라 서로 구분되지 않는데, 같은 배치를 `M! × (N-M)!` 번 다시 센다.
N ≤ 500 이면 절망적이고, 조합(`C(N,M)`)으로 줄여도 마찬가지다.

**이 문제는 탐색이 아니라 구성 문제다.** 틀린 문제는 연속 카운터를 끊는 칸막이고, 덩어리 하나에
`K-1` 개까지는 2배가 안 터진다. 덩어리를 최대한 많이 만들고, 정원을 넘는 몫은 **맨 앞 덩어리에 몰아서**
2배가 작은 점수에 걸리게 하면 최소가 된다. 같은 주차의 `안찬웅/week6/면접 실패.java` 와
`이성일/week6/면접.java` 가 그 구성을 O(N) 으로 짠 코드다.

**검증함** — `n ≤ 12` 의 모든 `(n, m, k)` 조합 728건에서 그 구성 방식과 완전탐색이 **불일치 0건**이었다.
이 파일의 완전탐색도 파싱만 고치면 작은 입력에서는 같은 답을 낸다(`n ≤ 8` 240건 대조, 0건 불일치).
즉 **점수 계산 로직(`calc`)은 맞고, 틀린 건 입력 파싱과 규모 대응 두 가지다.**

## 복잡도

- 시간: `O(N! · N)` — 순열 전개. `N ≤ 500` 이면 불가능. 구성 방식은 `O(N)`
- 공간: `O(N)`

## 요약

`calc()` 의 점수 규칙 해석은 정확하다. 죽은 건 입력 파싱(`split("")`)과 접근 규모다.
"최소 점수를 만드는 배치는 이미 정해져 있다" 를 알아채면 순열이 통째로 사라진다.
