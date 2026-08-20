---
platform: programmers
problemId: "49191"
author: chanung
source: 안찬웅/week2/순위.java
week: 2
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N * (N+E))
  space: O(N+E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 순위 (programmers/49191) — chanung

## 접근

"정확한 순위를 아는 사람 = (이긴 사람 수 + 진 사람 수) == n-1"이라는 이 문제의 핵심을 정확히
짚었고, `win`/`lose` 양방향 인접 리스트에 대해 같은 `dfs(node, graph)`를 재사용해 중복 없이
도달 가능 노드 수를 센다. SWEA5643(키 순서)과 동일한 패턴을 여기서도 일관되게 적용한 점이 좋다.

## 개선점

지적할 만한 문제는 찾지 못했다.

## 복잡도

- 시간: `O(N * (N+E))` — 노드마다 그래프 전체를 두 번(win/lose) 순회. N≤100 제약에서 충분히 빠르다.
- 공간: `O(N+E)` — 인접 리스트.

## 요약

부등호(승패) 관계를 그래프 도달 가능성으로 바꾸는 접근이 정확하고, 코드도 SWEA5643과 같은 재사용
가능한 형태로 깔끔하게 짜여 있다.
