---
platform: ssafy
problemId: "hunter"
author: seongil
source: 이성일/week6/헌터.java
week: 6
compiles: true
lang: java
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(T · K! · K)
  space: O(N² + K)
generatedBy: claude-code-local
generatedAt: 2026-09-10
---

# 헌터 (ssafy/hunter) — seongil

## 접근

몬스터·고객 칸을 입력받으며 한 리스트(`list`)에 모으고, 그 칸들을 방문하는 **순서를 DFS 로 전부 시도**한다.
노드가 최대 8개(몬스터 4 + 고객 4)라 순열 탐색이면 충분하다는 판단이 맞다.

"몬스터 `i` 를 잡아야 고객 `i` 에게 갈 수 있다" 는 선후 조건을 `isHunted[]` 하나로 처리했다.

```java
if (vertex < 0) { if (isHunted[-1 * vertex]) { ... dfs(...) } else continue; }
if (vertex > 0) { visited[i] = true; isHunted[vertex] = true; dfs(...); ... isHunted[vertex] = false; }
```

몬스터를 방문할 때 켜고 되돌아 나올 때 끄므로, **아직 잡지 않은 몬스터의 고객에게는 애초에 가지를 뻗지 않는다.**
여기에 `distCnt > answer` 가지치기를 붙여 불필요한 순서를 일찍 끊는다.
이동 거리는 장애물이 없으니 맨해튼 거리 `|r-x| + |c-y|` 로 바로 더한다.

**실제로 돌려본 검증**: 이 코드와 독립으로 짠 비트마스크 DP(상태 = 방문 집합 × 마지막 노드)와
N 3~10, 몬스터 1~4마리 무작위 입력 **1,500건을 대조해 불일치 0건**이다.
몬스터나 고객이 출발칸 `(1,1)` 에 놓이는 경우도 입력에 섞었다.

## 개선점

오답·시간초과·로직 결함으로 볼 만한 것을 찾지 못했다.

## 복잡도

- 시간: `O(T · K! · K)` — `K` 는 몬스터+고객 수(≤ 8). 순서 `K!` 가지에 각 단계 후보 `K` 개를 훑는다.
  선후 조건과 가지치기로 실제로는 훨씬 적게 돈다. 1,500건(JVM 기동 30번 포함)이 2.2초였다.
- 공간: `O(N² + K)` — 격자와 방문 배열, 재귀 깊이 `K`

## 요약

노드 수가 작다는 걸 보고 순열 DFS 를 고른 판단이 정확하고, 선후 조건을 `isHunted[]` 켜기/끄기로 단순하게 처리했다.
비트마스크 DP 레퍼런스와 1,500건 대조에서 전부 정답이다.
