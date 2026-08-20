---
platform: programmers
problemId: "92343"
author: junsoo
source: 김준수/week3/양과 늑대.java
week: 3
compiles: true
verdict: needs-fix
tags: [dead-code, nonstatic-inner-class]
complexity:
  time: O(N!) 최악 (가지치기로 실제로는 훨씬 적음)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 양과 늑대 (programmers/92343) — junsoo

## 접근

방문 배열 대신 "지금 접근 가능한 노드 목록(`canGo`)"을 매 재귀 호출마다 새로 복사해서 넘기는
방식으로 상태를 관리한다. 트리 간선을 부모→자식 단방향으로만 저장해서 역주행을 원천 차단한
것도 좋은 판단이다 — 상단 주석에 "양방향으로 저장했다가 방문 처리가 꼬여서 단방향으로
바꿨다"는 시행착오가 남아 있는데, 그 판단이 맞다. `sheep == wolf`를 종료 조건으로 쓴 것도
정확하다 — 한 번에 동물이 하나씩만 늘어나므로 `wolf > sheep`으로 건너뛸 일이 없어
`==` 검사만으로 "늑대 수가 양 수 이상" 조건을 정확히 잡아낸다.

`answer = Math.max(answer, sheep)`를 재귀 진입 시점마다 매번 부르는 것도, 늑대를 추가해
`sheep == wolf`가 된 직후 호출에서는 부모 호출 때 이미 기록한 것과 같은 `sheep` 값을
한 번 더 기록하는 셈이라 중복이긴 하지만(늑대를 추가해도 양의 수는 안 바뀌므로) 정답에는
영향이 없다.

## 개선점

### 1. (사소) `visited` 배열이 선언·초기화만 되고 실제로는 쓰이지 않는다 — `dead-code`

```java
static boolean[] visited;
...
visited = new boolean[info.length];
```

`dfs`도, 다른 어떤 메서드도 `visited`를 읽거나 쓰지 않는다. 주석에 적힌 시행착오("양방향 간선 +
방문 처리가 꼬여서" → "단방향 간선 + canGo 복사"로 전환)를 보면, 초기 설계에서 쓰던 방문 배열이
전환 후에 지우지 않고 남은 것으로 보인다. 지금은 `canGo` 리스트 자체가 "미방문 + 접근 가능"을
표현하므로 이 필드는 지워도 된다.

### 2. (사소) `Node`가 `static`이 아닌 중첩 클래스다 — `nonstatic-inner-class`

```java
class Solution {
    List<Node> nodes;
    ...
    class Node{ ... }   // static 아님
```

`Node`는 `isWolf`, `child`만 갖고 `Solution`의 인스턴스 멤버를 쓰지 않으므로 `static class Node`가
맞다.

## 복잡도

- 시간: `O(N!)` 최악 — 매 단계 `canGo` 크기만큼 분기하는 완전탐색이라 트리 모양에 따라 최악은
  이 정도까지 갈 수 있지만, `sheep == wolf` 가지치기와 `info.length <= 17`이라는 이 문제의
  작은 제약(익히 알려진 상한) 덕에 실제 탐색량은 훨씬 적다.
- 공간: `O(N^2)` — 재귀 깊이 `N`마다 `canGo` 리스트를 새로 복사하므로 깊이×리스트 크기.

## 요약

핵심 로직(단방향 트리, `canGo` 복사, 종료 조건)이 모두 정확하고 반례를 찾지 못했다. 안 쓰이는
`visited` 필드와 non-static `Node` 정도가 남은 정리거리다.
</content>
