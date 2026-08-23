---
platform: programmers
problemId: "49191"
author: seongil
source: 이성일/week2/순위.java
week: 2
compiles: true
verdict: good
tags: [duplicate-code, dead-code, magic-branch]
complexity:
  time: O(n(n+m))
  space: O(n+m)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 순위 (programmers/49191) — seongil

## 접근

선수를 `Node` 객체로 만들고, 각 노드가 `stronger` / `weaker` 리스트로 **다른 노드를 직접 참조**하게 했습니다. 인덱스 그래프가 아니라 객체 그래프입니다. 그 위에서 DFS로 도달 가능한 노드 수를 세고, 합이 `n-1` 이면 순위 확정으로 봅니다.

세 사람 중 유일하게 객체 참조 방식을 골랐습니다. `enroll.get(result[0]-1).weaker.add(enroll.get(result[1]-1))` 한 줄에 "이긴 사람의 약한 목록에 진 사람을 넣는다" 가 그대로 드러나는 건 인덱스 배열보다 읽기 좋은 지점입니다. 대신 노드에서 인덱스를 다시 꺼내야(`n.idx - 1`) 방문 배열을 쓸 수 있어서, 방문 체크 쪽은 오히려 번거로워졌습니다.

**검증**: 김준수·안찬웅 코드와 함께 플로이드-워셜 브루트포스를 기준으로 무작위 20,000건(n=2~10, 간선 밀도 무작위)을 대조했습니다. 불일치 0건입니다.

## 개선점

### 1. (사소) `sdfs` 와 `wdfs` 가 재귀에서 타는 필드만 다르고 같은 코드다 — duplicate-code

`sdfs` 는 `n.stronger` 로, `wdfs` 는 `n.weaker` 로 내려가는 것 말고 차이가 없습니다. 노드에서 다음 목록을 꺼내는 방법을 인자로 받으면 하나로 합쳐집니다.

```java
public static int dfs(List<Node> list, boolean[] visited, boolean up) {
    int count = 0;
    for (Node n : list) {
        if (visited[n.idx - 1]) continue;
        visited[n.idx - 1] = true;
        count += 1 + dfs(up ? n.stronger : n.weaker, visited, up);
    }
    return count;
}
```

호출부는 `dfs(enroll.get(i).stronger, new boolean[n], true) + dfs(enroll.get(i).weaker, new boolean[n], false)` 가 됩니다. 같은 문제를 푼 안찬웅 코드가 그래프를 인자로 받아 이 중복을 처음부터 안 만들었습니다.

### 2. (사소) 쓰이지 않는 import 와 즉시 덮어쓰는 초기화 — dead-code

- `import java.util.Collections;` — 파일 어디에서도 `Collections` 를 쓰지 않습니다.
- `int scnt = 0, wcnt = 0;` 로 초기화한 뒤 바로 다음 두 줄에서 `scnt = sdfs(...)`, `wcnt = wdfs(...)` 로 덮어씁니다. 선언과 대입을 붙이면 됩니다.

```java
boolean[] svisited = new boolean[n];
boolean[] wvisited = new boolean[n];
int scnt = sdfs(enroll.get(i).stronger, svisited);
int wcnt = wdfs(enroll.get(i).weaker, wvisited);
```

### 3. (사소) `if (...) { continue; } else { ... }` 의 `else` 는 필요 없다 — magic-branch

`continue` 로 이미 흐름이 끊기므로 `else` 블록의 들여쓰기가 한 단계 낭비됩니다.

```java
if (visited[n.idx - 1]) continue;
visited[n.idx - 1] = true;
count++;
count += sdfs(n.stronger, visited);
```

### 4. (사소) 접근 계획 주석이 코드보다 뒤에 남아 있다

`// 각 리스트별로 나보다 쎈선수 dfs로 구하고...` 아래 6줄은 구현 전에 세운 계획으로 보이는데, 바로 아래 코드가 그 계획을 이미 그대로 실행하고 있습니다. 계획 주석을 남기는 건 나쁘지 않지만, 구현이 끝난 뒤에는 "왜 이렇게 했는가" 만 남기고 "무엇을 할 것인가" 는 지우는 편이 읽기 좋습니다.

## 복잡도

- 시간: `O(n(n+m))` — 선수마다 양방향 DFS. n ≤ 100, m ≤ 4,500 이므로 약 46만 연산입니다.
- 공간: `O(n+m)` — 노드 n개와 각 노드의 참조 리스트. 재귀 깊이는 최대 n(=100)이라 스택 오버플로 위험은 없습니다.

## 요약

객체 그래프로 관계를 표현한 선택이 명확하고, 무작위 대조에서도 정확했습니다. 지적은 전부 정답이 아니라 정리 문제입니다 — `sdfs`/`wdfs` 중복 하나만 합쳐도 파일이 20줄 가까이 짧아지고, 나머지(미사용 import, 불필요한 `else`, 계획 주석)는 한 번에 치울 수 있는 것들입니다.
