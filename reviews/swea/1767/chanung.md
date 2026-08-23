---
platform: swea
problemId: "1767"
author: chanung
source: 안찬웅/week1/SWEA1767.java
week: 1
compiles: true
verdict: good
tags: [good-decomposition, duplicate-code]
complexity:
  time: O(5^K * N)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 프로세서 연결하기 (swea/1767) — chanung

## 접근

가장자리 코어는 이미 연결된 것으로 보고 탐색 대상에서 빼는 전처리가 깔끔하다. 내부 코어마다
"4방향 중 하나로 연결 or 포기"의 5지선다 완전탐색을 재귀로 짜고, `cnt + 남은 코어 수 < maxCore`면
가지치기하는 것도 이 문제의 표준적인 접근을 정확히 구현했다. `check`로 가능 여부만 먼저 보고
`fill`로 실제 칠하는 두 단계 분리도 읽기 좋다.

## 개선점

### 1. (사소) `check`와 `fill`이 같은 이동 루프를 반복한다 — `duplicate-code`

두 함수 모두 `nx, ny`를 같은 방향으로 전진시키는 `while` 루프를 각각 따로 들고 있다.
방향만 정해지면 지나가는 칸 목록은 한 번만 계산해도 되므로, 좌표 리스트를 만들어
`check`에서 유효성만 보고 `fill`에서 그 리스트로 칠하면 중복이 줄어든다. 코어 개수가 적어
실행 시간에 영향은 없지만, 로직이 바뀔 때 두 곳을 같이 고쳐야 하는 부담은 남는다.

## 복잡도

- 시간: `O(5^K * N)` — K는 내부 코어 수, 각 리프에서 O(N) 방향 체크. 완전탐색 문제라 이 오더가 맞다.
- 공간: `O(N^2)` — 격자 배열이 지배적.

## 요약

가지치기가 들어간 표준적인 백트래킹 구현이고 정확해 보인다. `check`/`fill` 중복은 스타일 문제일 뿐
동작에는 영향이 없다.
