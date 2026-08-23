---
platform: programmers
problemId: "42884"
author: seongil
source: 이성일/week3/연습/단속카메라.java
week: 3
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(N log N)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 단속카메라 (programmers/42884) — 이성일

## 접근

차량 경로를 시작 지점(같으면 끝 지점) 기준으로 정렬한 뒤, 현재 카메라가 커버하는 구간(`area`)과 겹치면 구간을 교집합으로 좁히고, 안 겹치면 카메라를 하나 더 놓는 표준 그리디다. 겹칠 때 `area[1] = Math.min(route[1], area[1])`로 커버 구간을 좁히는 부분이 핵심인데 정확히 구현했다.

**공식 예제**(`routes=[[-20,-15],[-14,-5],[-18,-13],[-5,-3]]`)로 직접 컴파일·실행해 기대값 `2`와 일치하는 것을 확인했다.

## 개선점

지적할 만한 문제를 찾지 못했다.

## 복잡도

- 시간: `O(N log N)` — 정렬이 지배적, 이후 순회는 `O(N)`
- 공간: `O(1)` — 정렬은 제자리에서 이뤄지고 추가 자료구조 없음

## 요약

짧고 정확한 그리디 구현이다. 지적할 게 따로 없다.
