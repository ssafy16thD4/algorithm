---
platform: programmers
problemId: "42861"
author: junsoo
source: 김준수/week4/섬 연결하기.java
week: 4
compiles: true
verdict: needs-fix
tags: [off-by-one, nonstatic-inner-class]
complexity:
  time: O(E log E)
  space: O(N+E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 섬 연결하기 (programmers/42861) — junsoo

## 접근

표준적인 크루스칼(간선 비용 오름차순 정렬 + 유니온 파인드)로, 경로 압축까지 들어간 `find`와
간선 수가 `n-1`이 되면 조기 종료하는 것까지 정확하다. 상단 주석에 "템플릿을 아직 못 외웠다"고
적었는데, 실제 구현은 표준 템플릿과 다를 게 없을 만큼 정확하게 재현했다.

## 개선점

### 1. (사소) `parent` 배열 크기와 초기화 범위가 실제 섬 번호 범위와 어긋난다 — `off-by-one`

```java
parent = new int[n + 1];
for(int i = 1; i <= n; i++){
    parent[i] = i;
}
```

이 문제의 섬 번호는 `0`부터 `n-1`까지인데(`costs[i] = [섬1, 섬2, 비용]`), 이 초기화는 `1`부터
`n`까지를 채운다. 실제로 쓰이는 `parent[0]`은 이 루프가 아니라 **자바 배열의 기본값 `0`**이
`parent[0] == 0`(자기 자신을 부모로 갖는 루트 상태)과 우연히 맞아떨어져서 정답이 나온 것이고,
반대로 `parent[n]`은 명시적으로 초기화까지 됐지만 실제로는 아무 섬도 인덱스 `n`을 쓰지 않아
전혀 참조되지 않는다.

지금 코드로 정답이 나오는 이유를 직접 확인해봤다 — 자바가 `int[]`를 항상 `0`으로 채워준다는 사실에
기대고 있을 뿐, 코드 스스로 "섬 `0`의 부모는 `0`이다"를 명시하고 있지 않다. 배열을 `new int[n]`으로
줄이고 `for(int i = 0; i < n; i++) parent[i] = i;`로 바꾸면 실제 섬 번호 범위와 정확히 맞고, 이런
암묵적 우연에 기대지 않게 된다.

### 2. (사소) `Edge`가 `static`이 아닌 중첩 클래스다 — `nonstatic-inner-class`

```java
class Solution {
    static int[] parent;

    class Edge implements Comparable<Edge>{ ... }   // static 아님
```

`Edge`는 `Solution`의 인스턴스 멤버를 쓰지 않으므로 `static class Edge`가 맞다.

## 복잡도

- 시간: `O(E log E)` — 간선 정렬이 지배적.
- 공간: `O(N+E)` — `parent` 배열과 간선 리스트.

## 요약

크루스칼 알고리즘 자체는 정확하고 반례를 찾지 못했다. 다만 `parent` 배열의 초기화 범위가 실제 섬
번호 범위와 하나씩 밀려 있는데 자바 배열 기본값 덕에 우연히 맞는 상태라, 명시적으로 고쳐두는 게
나중에 비슷한 코드를 재사용할 때 안전하다.
</content>
