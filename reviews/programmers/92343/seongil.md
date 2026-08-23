---
platform: programmers
problemId: "92343"
author: seongil
source: 이성일/week3/양과늑대.java
week: 3
compiles: true
verdict: wrong
tags: [uninitialized-state, wrong-algorithm]
complexity:
  time: N/A (실행 즉시 예외 발생)
  space: N/A
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 양과 늑대 (programmers/92343) — 이성일

## 접근

이 파일은 트리를 DFS로 한 번 순회하면서 "늑대를 처음 만나면 필요한 양 2마리, 그 다음부터는 1마리씩 증가"라는 비용을 각 양의 인덱스에 누적(`lambState`)한 뒤, 그 결과를 별도로 순회하며 그리디하게 답을 찾으려는 시도다. 이후 같은 문제를 다시 푼 `양과늑대2.java`(대표 풀이로 채택된 쪽)와 비교하면, 이 버전은 방문 순서를 하나만 고정해서 훑는 방식이라 실제로 가능한 "여러 방문 순서 중 최선"을 탐색하지 못한다 — 애초에 접근 자체가 이 문제가 요구하는 백트래킹과 거리가 있다.

## 개선점

### 1. (치명) `graph` 배열 원소를 초기화하지 않아 NPE로 즉시 종료 — `uninitialized-state`

```java
graph = new ArrayList[n];
for (List<Integer> g : graph)
    g = new ArrayList<>();
```

`graph = new ArrayList[n]`은 길이 `n`짜리 배열만 만들 뿐 각 칸은 여전히 `null`이다. 그 다음 `for (List<Integer> g : graph) g = new ArrayList<>();`는 **루프 변수 `g`에 새 리스트를 대입할 뿐, `graph` 배열의 원소는 전혀 바뀌지 않는다**(자바에서 흔한 함정 — 향상된 for문의 변수 재대입은 원본 배열/컬렉션에 반영되지 않는다). 결과적으로 `graph[i]`는 계속 `null`이고, 곧이어 나오는 `graph[edge[0]].add(edge[1]);`에서 `NullPointerException`이 난다.

**공식 예제 1**(`info=[0,0,1,0,1,0,1,0,1,0,1,1], edges=[[0,1],[1,2],[1,4],[0,8],[8,7],[9,10],[9,11],[4,3],[6,5],[4,6],[8,9]]`)로 직접 컴파일·실행해서 재현했다:

```
threw exception: java.lang.NullPointerException:
Cannot invoke "java.util.List.add(Object)" because "Solution.graph[<local8>[0]]" is null
```

간선이 하나라도 있으면 무조건 터진다. 수정하려면:

```java
for (int i = 0; i < n; i++) {
    graph[i] = new ArrayList<>();
}
```

로 바꿔야 한다(같은 문제의 `양과늑대2.java`는 정확히 이 형태로 초기화돼 있다).

### 2. (치명) NPE를 고쳐도 알고리즘 자체가 이 문제에 맞지 않음 — `wrong-algorithm`

이 코드는 트리를 딱 한 가지 순서(자식 재귀 순서)로만 훑으면서 "양을 만난 시점의 늑대 수"를 기록하고, 그 기록을 나중에 그리디하게 조합해 답을 낸다. 하지만 이 문제는 "어떤 순서로 방문하느냐"에 따라 늑대/양 비율 제약을 지킬 수 있는지가 달라지고, 최적 순서를 찾으려면 각 시점에서 가능한 다음 후보(방문한 노드들의 아직 안 간 자식들) 전체를 두고 백트래킹해야 한다. 이 코드는 그 선택지 자체를 만들지 않는다 — 즉 NPE를 고쳐도 정답이 나온다는 보장이 없다. **이 부분은 NPE 때문에 실행 자체가 안 돼 실측 대조를 하지 못했고, 코드 구조만으로 판단한 것 — 검증 안 함.**

## 복잡도

- 시간: 측정 불가 (예외로 조기 종료)
- 공간: 측정 불가

## 요약

`graph` 배열 초기화 실수로 간선이 있으면 무조건 `NullPointerException`이 나서 정상적으로는 한 번도 답을 내지 못했을 것이다. 초기화를 고쳐도 접근 자체가 이 문제가 요구하는 백트래킹과 달라서 정답을 낼지는 별도로 의심스럽다 — 같은 이름으로 다시 시도한 `양과늑대2.java`(백트래킹 방식)가 실제로는 올바른 재도전이었다.
