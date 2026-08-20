---
platform: programmers
problemId: "92343"
author: chanung
source: 안찬웅/week3/양과 늑대.java
week: 3
compiles: true
verdict: good
tags: [good-decomposition, space-complexity]
complexity:
  time: O(2^N)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 양과 늑대 (programmers/92343) — chanung

## 접근

방문 가능한 후보(`candidates`)를 프론티어로 유지하면서, 각 후보를 다음 방문 노드로 시도해보고
늑대 수가 양 수 이상이 되면 그 분기만 포기하는 백트래킹이다. 재귀 호출마다 `candidates`를
`new ArrayList<>(candidates)`로 복사해서 넘기기 때문에, 원본 리스트를 건드리지 않고도 다음 반복에서
원래 후보 집합으로 되돌아간다 — 수동으로 `add`/`remove`를 짝 맞춰 되돌리다 실수하는 흔한
백트래킹 버그를 구조적으로 피한 점이 좋다.

## 개선점

### 1. (사소) 재귀마다 후보 리스트를 통째로 복사한다 — `space-complexity`

```java
List<Integer> nextCandidates = new ArrayList<>(candidates);
```

매 호출마다 O(후보 수)만큼 복사가 발생한다. N≤17 제약에서는 문제없지만, 명시적으로
`add` 후 재귀, 반환 후 `remove`로 되돌리는 방식이면 복사 없이 같은 결과를 얻을 수 있다
(다만 그 경우 인덱스 기반 순회를 다시 신경 써야 해서 지금 방식이 덜 실수하기 좋다는 트레이드오프는 있다).

## 복잡도

- 시간: `O(2^N)` — 최악의 경우 각 노드를 방문/미방문으로 가지치기하며 탐색. N≤17 제약에서 통상 허용되는 오더.
- 공간: `O(N^2)` — 재귀 깊이 O(N) × 매 단계 후보 리스트 복사 O(N).

## 요약

프론티어를 리스트 복사로 관리해 백트래킹 undo 실수를 구조적으로 막은 설계가 인상적이다.
복사 비용은 이 문제 제약에서는 무시할 만하다.
