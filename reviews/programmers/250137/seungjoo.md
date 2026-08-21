---
platform: programmers
problemId: "250137"
author: seungjoo
source: 이승주/pccp/붕대감기(박진우).java
week: null
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(공격 시간 최댓값)
  space: O(공격 횟수)
generatedBy: claude-code-local
generatedAt: 2026-08-21
---

# [PCCP 기출문제] 1번 / 붕대 감기 (programmers/250137) — seungjoo

## 접근

공격 목록을 `Deque` 에 넣고 매초 `peek()` 로 다음 공격 시각과 비교하는 방식. 공격 시점엔 `successTime`
을 0으로 리셋하고 `continue` 로 그 초의 회복 로직을 건너뛴다. 공식 예제 4개 모두 실제로 돌려서
확인했고 전부 일치했다.

## 개선점

지적할 만한 문제가 없다.

## 복잡도

- 시간: `O(공격 시간 최댓값)` — 매초 순회, 공격 여부만 큐 peek으로 확인.
- 공간: `O(공격 횟수)` — Deque.

## 요약

Deque + peek 조합으로 공격 판정을 짧게 처리했다. 4개 예제 전부 통과, 지적할 결함 없음.
