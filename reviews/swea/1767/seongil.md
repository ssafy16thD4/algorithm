---
platform: swea
problemId: "1767"
author: seongil
source: 이성일/week1/SWEA1767.java
week: 1
compiles: true
verdict: good
tags: [dead-code, good-decomposition]
complexity:
  time: O(4^K) (K = 내부 노드 수)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 프로세서 연결하기 (swea/1767) — 이성일

## 접근

각 코어(내부 셀)에 대해 4방향 중 하나로 전선을 놓거나 아예 연결하지 않는 두 선택지를 모두 탐색하는 DFS 백트래킹이다. `isSearchable`로 해당 방향이 보드 끝까지 뚫려 있는지 먼저 확인하고, `draw(-1)`로 표시했다가 재귀 후 `draw(0)`으로 되돌리는 undo 패턴이 정확하다. 주석에 "연결 가능해도 미연결 탐색이 필요할 수 있다"고 직접 남긴 것도 맞는 지적이다 — 모두 연결하는 게 항상 최적은 아니기 때문이다.

## 개선점

### 1. (사소) 의미 없는 분기 — `dead-code`

```java
if (!flag) dfs(nIdx+1, searchCnt, totalWires, board, nodes);
// *연결가능함에도 미연결하는 탐색이 필요할 수 있다.*
else dfs(nIdx+1, searchCnt, totalWires, board, nodes);
```

`flag`(연결 가능 여부)에 따라 분기했지만 양쪽 분기의 실행문이 완전히 동일하다. 즉 `flag` 값과 무관하게 항상 "이 노드를 연결하지 않고 다음으로 넘어가는" 탐색을 한 번 더 실행하는 셈이라, `if/else` 없이

```java
dfs(nIdx+1, searchCnt, totalWires, board, nodes);
```

한 줄로 줄여도 동작이 같다. 의도(미연결 탐색이 필요하다는 것)는 맞지만 `flag`로 분기하는 형태가 오해를 준다.

## 복잡도

- 시간: `O(4^K)` — 내부 노드 K개마다 "4방향 중 하나 연결 또는 미연결" 선택을 완전탐색
- 공간: `O(N^2)` — 보드 배열

## 요약

백트래킹 뼈대(선택-재귀-undo)는 정확하고, "미연결도 탐색해야 한다"는 문제의 핵심을 정확히 짚었다. 다만 그 결론을 구현한 `if/else`가 사실상 죽은 분기라 코드만 보면 의도가 잘 안 보인다.
