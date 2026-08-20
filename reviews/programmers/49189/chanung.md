---
platform: programmers
problemId: "49189"
author: chanung
source: 안찬웅/week4/가장 먼 노드.java
week: 4
compiles: true
verdict: good
tags: [uninitialized-state]
complexity:
  time: O(N+E)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 가장 먼 노드 (programmers/49189) — chanung

## 접근

트리(무방향 그래프)에서 1번 노드부터 BFS로 거리 배열을 채우고, 정렬 후 뒤에서부터 최댓값과
같은 개수를 세는 방식이다. 실제로 컴파일해서 돌려봤다.

```
n=6, edge=[[3,6],[4,3],[3,2],[1,3],[1,2],[2,4],[5,2]] → 3 (기대값과 일치)
n=2, edge=[[1,2]] → 1
```

두 경우 모두 정확한 결과를 냈다.

## 개선점

### 1. (사소) `dist` 배열의 0번 인덱스가 미사용인데 정렬에 같이 섞인다 — `uninitialized-state`

```java
dist = new int[n+1];   // 0번 인덱스는 노드 번호로 안 쓰임(노드는 1..n)
...
Arrays.sort(dist);
```

노드 번호가 1부터 시작하는데 배열은 `n+1` 크기라 `dist[0]`이 기본값 0인 채로 정렬에 섞인다.
지금은 문제없이 동작한다 — `n≥2`인 트리에서는 최대 거리가 항상 1 이상이라, 뒤에서부터 세는 루프가
`dist[0]`에 도달하기 전에 항상 먼저 멈춘다(실측으로 확인). 다만 이건 "우연히 안 걸리는" 구조라,
`dist = new int[n+1]`로 잡을 거면 `i=n`부터 `1`까지만 도는 지금 로직을 유지하되, 애초에
`Arrays.sort(dist, 1, n+1)`로 0번 인덱스를 정렬 대상에서 빼두면 이 우연에 기대지 않아도 된다.

## 복잡도

- 시간: `O(N+E)` — BFS 한 번 + 정렬 O(N log N). (정렬이 실제로는 지배적이지만 N 규모에서 문제없음)
- 공간: `O(N)` — `dist`, `vis`, 인접 리스트.

## 요약

BFS로 거리를 구하고 최댓값 개수를 세는 접근이 정확하고, 실제 테스트에서도 맞는 답을 냈다.
0번 인덱스를 정렬에 섞는 부분은 지금 제약에서는 안전하지만 근본적으로는 피하는 게 좋다.
