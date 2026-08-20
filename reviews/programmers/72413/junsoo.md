---
platform: programmers
problemId: "72413"
author: junsoo
source: 김준수/week2/합승 택시 요금.java
week: 2
compiles: true
verdict: needs-fix
tags: [nonstatic-inner-class]
complexity:
  time: O(3 · (V+E) log V)
  space: O(V+E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 합승 택시 요금 (programmers/72413) — junsoo

## 접근

`s`, `a`, `b` 세 지점에서 각각 다익스트라를 한 번씩 돌려 최단거리 배열 세 개를 구하고, 모든 지점
`i`(`s` 제외)에 대해 `distS[i] + distA[i] + distB[i]`(같이 `i`까지 간 뒤 갈라짐)와 초기값
`distA[s] + distB[s]`(합승 안 함)를 비교해 최소값을 찾는다. 무방향 그래프이므로 `distA[i] == i→a`
라는 대칭성을 주석으로 짚어둔 것도 정확하고, `PriorityQueue` + `dist[target]`보다 큰 항목은
건너뛰는 지연 삭제(lazy deletion) 다익스트라도 표준적으로 잘 구현했다. 코드를 읽으면서 반례를
찾지 못했다 — 공식 풀이와 접근 자체가 거의 같다.

## 개선점

### 1. (사소) `Node`가 `static`이 아닌 중첩 클래스다 — `nonstatic-inner-class`

```java
class Solution {
    static List<List<Node>> graph;

    class Node implements Comparable<Node>{   // static 아님
```

`Node`는 `target`/`cost`만 갖고 `Solution`의 인스턴스 멤버를 전혀 쓰지 않는데, `static`이 없어서
매 `Node` 인스턴스가 바깥 `Solution` 참조를 하나씩 더 들고 다닌다. `graph`가 `static List<List<Node>>`
로 선언돼 있어 정적 문맥에서도 `new Node(...)` 호출이 되긴 하지만(프로그래머스 채점 환경은 `Solution`
인스턴스를 통해 `solution`을 호출하므로 컴파일·실행에는 문제없다), `static class Node`로 바꾸는 게
의도에 맞고 메모리도 아주 조금 덜 쓴다.

## 복잡도

- 시간: `O(3·(V+E) log V)` — 다익스트라 세 번. 이 문제의 `n <= 200`, `fares.length <= 8000` 기준으로
  충분히 빠르다.
- 공간: `O(V+E)` — 인접 리스트와 세 거리 배열.

## 요약

다익스트라 세 번 + 합승 지점 순회라는 표준 접근을 정확하게 구현했고, 로직에서 지적할 거리를 찾지
못했다. `Node`를 `static`으로 바꾸는 정도가 유일하게 남는 정리거리다.
</content>
