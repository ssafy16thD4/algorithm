---
platform: programmers
problemId: "17678"
author: seongil
source: 이성일/week3/셔틀버스.java
week: 3
compiles: true
verdict: good
tags: [magic-branch, good-decomposition]
complexity:
  time: O(N log N + M log M)
  space: O(N + M)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [1차] 셔틀버스 (programmers/17678) — 이성일

## 접근

버스 도착 시간표를 만들고, 크루 시간표를 정렬한 뒤 버스마다 앞에서부터 최대 `m`명씩 태우는 시뮬레이션을 한다. 마지막 버스가 꽉 찼으면 그 버스에 마지막으로 탄 크루보다 1분 먼저 도착하도록, 안 찼으면 마지막 버스 시각 그대로 도착하도록 나눈 것이 표준 풀이와 일치한다.

**직접 만든 세 가지 케이스로 검증했다** (공식 예제 대신 문제 규칙을 직접 손으로 계산해서 만든 케이스):
- `n=1, m=3`, `["08:59","08:59","09:00"]` → 마지막 버스가 꽉 참(3명) → 기대값 `08:59`, 실제 `08:59`
- `n=1, m=5`, `["08:00","08:10"]` → 안 참(2명) → 기대값 `09:00`, 실제 `09:00`
- `n=2, t=30, m=2`, `["08:00","08:05"]` → 두 명 다 첫 버스로 다 탐, 마지막 버스는 0명(안 참) → 기대값 `09:30`, 실제 `09:30`

세 케이스 모두 일치했다.

## 개선점

### 1. (사소) 시:분 zero-padding을 4단계 분기로 직접 구현 — `magic-branch`

```java
if (cone.hour >= 10 && cone.minute >= 10) { ... "%d:%d" }
else if (cone.hour >= 10 && cone.minute < 10) { ... "%d:%d%d" }
else if (cone.hour < 10 && cone.minute >= 10) { ... "%d%d:%d" }
else { ... "%d%d:%d%d" }
```

`String.format("%02d:%02d", cone.hour, cone.minute)` 한 줄로 네 분기가 하던 일을 그대로 대신한다. 위 세 케이스로 이 4분기 각각이 실제로 올바른 패딩을 만든다는 것도 확인했으니 동작 자체엔 문제가 없다 — 다만 분기 4개로 나눠 쓸 이유가 없다.

## 복잡도

- 시간: `O(N log N + M log M)` — 크루 정렬이 지배적, 버스-크루 매칭은 투 포인터 방식이라 `O(N+M)`
- 공간: `O(N + M)` — 버스/크루 시간표

## 요약

시뮬레이션 로직은 표준 풀이와 같고, 직접 만든 세 가지 케이스(꽉 찬 마지막 버스 / 안 찬 마지막 버스 / zero-padding)로 확인했다. zero-padding을 수동 4분기로 짠 것만 정리하면 된다.
