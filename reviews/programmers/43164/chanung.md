---
platform: programmers
problemId: "43164"
author: chanung
source: 안찬웅/week4/여행경로.java
week: 4
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(E^2) 최악
  space: O(E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 여행경로 (programmers/43164) — chanung

## 접근

티켓을 도착지 기준으로 전역 정렬해두면, 같은 출발지를 가진 티켓들도 그 정렬 순서 안에서 상대적으로
오름차순을 유지한다는 점을 이용해 별도의 출발지별 그룹핑 없이 사전순으로 가장 이른 경로를 찾는다.
`res != null`이면 즉시 리턴해서 첫 번째로 완성된 경로(=사전순 최솟값)에서 재귀를 멈추는 것도
이 문제의 표준 트릭을 정확히 구현한 것이다.

## 개선점

지적할 만한 문제는 찾지 못했다.

## 복잡도

- 시간: `O(E^2)` 최악 — 각 깊이에서 미방문 티켓 전체를 훑는다(E=티켓 수). 이 문제의 E 규모에서 충분히 빠르다.
- 공간: `O(E)` — `order`, `vis`, 재귀 깊이.

## 요약

전역 정렬 하나로 출발지별 정렬까지 해결한 깔끔한 구현이고, 백트래킹 종료 조건도 정확하다.
