---
platform: swea
problemId: "5643"
author: chanung
source: 안찬웅/week1/SWEA5643.java
week: 1
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N * (N+M))
  space: O(N+M)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 키 순서 (swea/5643) — chanung

## 접근

"자신의 키 순서를 아는 사람 = (조상 수 + 자손 수) == n-1"이라는 부등호 그래프의 성질을 정확히 짚었다.
`dfs(node, graph)`가 그래프를 파라미터로 받게 만들어서 `up`/`down` 양방향 탐색에 같은 함수를 재사용한
게 좋다 — 방향별로 dfs를 복붙하지 않아도 된다. 이 패턴은 나중에 프로그래머스 49191(순위)에도
거의 그대로 다시 쓰인다.

## 개선점

지적할 만한 문제는 찾지 못했다.

## 복잡도

- 시간: `O(N * (N+M))` — 노드마다 그래프 전체를 두 번(up/down) 순회.
- 공간: `O(N+M)` — 인접 리스트.

## 요약

부등호 관계를 그래프로 바꾸고 도달 가능성으로 판정하는 접근이 정확하고, `dfs` 재사용도 깔끔하다.
