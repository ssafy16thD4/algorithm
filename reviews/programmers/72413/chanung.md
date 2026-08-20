---
platform: programmers
problemId: "72413"
author: chanung
source: 안찬웅/week2/합승 택시 요금.java
week: 2
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N log N + E log N)
  space: O(N+E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 합승 택시 요금 (programmers/72413) — chanung

## 접근

"갈라지는 지점 k를 정하면 총요금 = dist(s,k) + dist(k,a) + dist(k,b)"라는 이 문제의 핵심 관찰을
정확히 코드로 옮겼다. `s`, `a`, `b` 각각에서 다익스트라를 한 번씩 돌려 거리 배열 세 개를 얻고
`k`를 완전탐색하며 합을 최소화하는 구조가 깔끔하다. `dijkstra`를 `start`만 받는 함수로 분리해
세 번 재사용한 것과, `PriorityQueue` pop 시 `cur.cost > dist[cur.v]`로 이미 처리된 stale 항목을
건너뛰는 것까지 다익스트라의 표준 형태를 정확히 갖췄다.

## 개선점

지적할 만한 문제는 찾지 못했다.

## 복잡도

- 시간: `O(N log N + E log N)` — 다익스트라 3회, 각각 우선순위 큐 기반이라 이 오더.
- 공간: `O(N+E)` — 인접 리스트 + dist 배열 3개.

## 요약

"분기점 k 완전탐색 + 다익스트라 3회"라는 정석적인 접근을 정확하고 재사용 가능한 형태로 구현했다.
