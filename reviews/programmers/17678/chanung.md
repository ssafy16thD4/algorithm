---
platform: programmers
problemId: "17678"
author: chanung
source: 안찬웅/week3/셔틀버스.java
week: 3
compiles: true
verdict: good
tags: [magic-number]
complexity:
  time: O(N + M log M)
  space: O(M)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [1차] 셔틀버스 (programmers/17678) — chanung

## 접근

크루 도착 시각을 정렬한 뒤, 버스마다 정원(m)까지 순서대로 태워보는 시뮬레이션으로 마지막 버스의
탑승 상태(자리가 남았는지/꽉 찼는지)를 판정하는 정석적인 접근이다. 자리가 남으면 막차 시각 그대로,
꽉 찼으면 마지막으로 탄 크루보다 1분 먼저 도착한다는 로직이 정확하고, 분→시각 문자열 변환도
자리수 맞춰 잘 처리했다.

## 개선점

### 1. (사소) `540`이 09:00를 의미하는 매직 넘버다 — `magic-number`

```java
bus[i] = 540 + t * i;
```

`540`이 몇 시인지 코드만 봐서는 바로 안 드러난다. `static final int START = 9 * 60;`처럼 이름을
붙이면 이 문제 도메인(첫차 09:00)이 코드에 드러난다.

## 복잡도

- 시간: `O(N + M log M)` — 크루 정렬이 지배적(N=버스 수, M=크루 수).
- 공간: `O(M)` — 정렬된 크루 배열.

## 요약

시뮬레이션 로직이 정확하고 깔끔하다. 매직 넘버 하나만 이름을 붙이면 더 좋아진다.
