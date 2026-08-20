---
platform: programmers
problemId: "49189"
author: junsoo
source: 김준수/week4/가장 먼 노드.java
week: 4
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(N+E)
  space: O(N+E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 가장 먼 노드 (programmers/49189) — junsoo

## 접근

1번 노드에서 BFS로 모든 노드까지의 최단거리를 구하면서 그 과정에서 최대 거리(`maxN`)까지 같이
갱신하고, 마지막에 그 최대 거리와 같은 노드 수를 센다. 가중치 없는 그래프의 최단거리는 BFS로
정확히 구해진다는 전제와 문제 조건이 정확히 맞고, `dist[next] == -1`로 방문 여부와 최단거리
미계산 여부를 한 번에 판단한 것도 군더더기가 없다.

## 개선점

특별히 지적할 부분을 찾지 못했다. 짧고 표준적인 BFS 구현이고, 반례도 찾지 못했다.

## 복잡도

- 시간: `O(N+E)` — 표준 BFS.
- 공간: `O(N+E)` — 인접 리스트, `dist` 배열.

## 요약

가장 기본적인 BFS 최단거리 문제를 정확하고 간결하게 풀었다. 지적할 결함이 없다.
</content>
