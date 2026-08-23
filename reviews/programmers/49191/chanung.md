---
platform: programmers
problemId: "49191"
author: chanung
source: 안찬웅/week2/순위.java
week: 2
compiles: true
verdict: good
tags: [collection-choice, good-decomposition, good-complexity]
complexity:
  time: O(n(n+m))
  space: O(n+m)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 순위 (programmers/49191) — chanung

## 접근

승/패 인접리스트 두 벌을 만들고, 각 선수에서 양방향으로 DFS를 돌려 도달 가능한 정점 수를 셌습니다. 합이 `n-1` 이면 순위가 확정된 것으로 봅니다.

세 사람 풀이 중 **가장 짧습니다(63줄).** 이유는 `dfs(int node, List<Integer>[] graph)` 가 그래프를 인자로 받기 때문입니다. 김준수·이성일은 방향만 다른 같은 탐색을 두 벌 적었는데, 여기서는 `dfs(i, win)` / `dfs(i, lose)` 두 번 호출로 끝납니다. 중복을 안 만든 게 이 코드에서 제일 잘한 판단입니다.

**검증**: 김준수·이성일 코드와 함께 플로이드-워셜 브루트포스를 기준으로 무작위 20,000건(n=2~10, 간선 밀도 무작위)을 대조했습니다. 불일치 0건입니다.

## 개선점

### 1. (사소) `new List[n+1]` 은 raw type 이라 컴파일러가 unchecked 경고를 낸다 — collection-choice

실측입니다. `javac` 가 이 파일에 대해서만 경고를 냈습니다.

```
Note: Solution.java uses unchecked or unsafe operations.
Note: Recompile with -Xlint:unchecked for details.
```

`List<Integer>[] win = new List[n+1];` 은 제네릭 배열을 못 만들어서 raw `List[]` 를 담은 것입니다. 동작에는 문제가 없지만 타입 안전성이 그 줄에서 끊깁니다. `List<List<Integer>>` 를 쓰면 경고가 사라집니다.

```java
List<List<Integer>> win = new ArrayList<>();
List<List<Integer>> lose = new ArrayList<>();
for (int i = 0; i <= n; i++) { win.add(new ArrayList<>()); lose.add(new ArrayList<>()); }
```

(태그는 `collection-choice` 를 붙였습니다. raw type 을 정확히 가리키는 태그가 `data/review-tags.json` 에 없어서 가장 가까운 것을 골랐습니다.)

### 2. (사소) `vis`, `cnt` 를 `static` 필드로 주고받는 대신 반환값으로

지금은 DFS가 결과를 `static int cnt` 에 쌓고, 호출부가 매번 `vis = new boolean[n+1]; cnt = 0;` 으로 초기화합니다. **매번 초기화하고 있으므로 상태 오염 버그는 없습니다.** 다만 초기화를 한 번이라도 빠뜨리면 바로 오답이 되는 구조이고, 이 초기화 4줄이 호출부를 차지하고 있습니다. 개수를 반환하게 바꾸면 그 위험과 4줄이 같이 사라집니다.

```java
static int dfs(int node, List<List<Integer>> graph, boolean[] vis) {
    int cnt = 0;
    for (int next : graph.get(node)) {
        if (vis[next]) continue;
        vis[next] = true;
        cnt += 1 + dfs(next, graph, vis);
    }
    return cnt;
}
```

호출부는 이렇게 줄어듭니다.

```java
int total = dfs(i, win, new boolean[n + 1]) + dfs(i, lose, new boolean[n + 1]);
if (total == n - 1) answer++;
```

### 3. (사소) `int total;` 선언 위치

`total` 이 바깥 루프 앞에 선언돼 있는데 쓰이는 곳은 루프 안뿐입니다. 위 2번을 적용하면 선언 자체가 사라집니다.

## 복잡도

- 시간: `O(n(n+m))` — 정점마다 두 그래프를 완전 탐색. n ≤ 100, m ≤ 4,500 이라 약 46만 연산입니다.
- 공간: `O(n+m)` — 인접리스트 두 벌. 재귀 깊이는 최대 n(=100)이라 스택도 안전합니다.

## 요약

세 풀이 중 구조가 가장 깔끔하고, 무작위 대조에서도 정확했습니다. 남은 건 타입 안전성(raw `List[]`)과 `static` 필드로 값을 주고받는 습관 두 가지인데, 둘 다 정답에는 영향이 없고 DFS가 개수를 반환하도록 바꾸면 한 번에 정리됩니다.
