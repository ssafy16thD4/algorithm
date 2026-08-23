---
platform: programmers
problemId: "49191"
author: junsoo
source: 김준수/week2/순위.java
week: 2
compiles: true
verdict: good
tags: [duplicate-code, boxing-cost, good-readability]
complexity:
  time: O(n(n+m))
  space: O(n+m)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 순위 (programmers/49191) — junsoo

## 접근

"i의 순위가 확정된다 = i보다 센 사람 수 + 약한 사람 수 == n-1" 이라는 조건을 잡아내고, 승패 관계를 정방향(`weaks`)·역방향(`strongs`) 인접리스트 두 개로 만든 뒤 각 정점에서 BFS로 도달 가능한 정점 수를 셌습니다.

핵심은 **전이성을 명시적으로 계산하지 않고 도달 가능성으로 대체한 것**입니다. A가 B를 이기고 B가 C를 이기면 A는 C보다 세다 — 이걸 플로이드-워셜로 폐포를 구해도 되지만, 여기서는 그래프 탐색 한 번으로 같은 답을 냅니다. 맨 위 주석에 전략 네 줄이 그대로 적혀 있어서 코드를 읽기 전에 방향을 알 수 있는 것도 좋습니다.

**검증**: 같은 문제를 푼 안찬웅·이성일 코드와 함께, 플로이드-워셜 기반 브루트포스를 기준으로 무작위 20,000건(n=2~10, 간선 밀도 무작위)을 대조했습니다. 불일치 0건입니다.

## 개선점

### 1. (사소) `countStrong` 과 `countWeak` 이 완전히 같은 코드다 — duplicate-code

두 메서드는 순회하는 리스트가 `strongs` 냐 `weaks` 냐만 다르고 나머지 24줄이 글자 단위로 동일합니다. 그래프를 인자로 받으면 하나로 합쳐집니다.

```java
private int countReachable(int start, List<List<Integer>> graph) {
    int result = 0;
    Queue<Integer> queue = new ArrayDeque<>();
    boolean[] seen = new boolean[graph.size()];
    queue.offer(start);
    seen[start] = true;

    while (!queue.isEmpty()) {
        for (int next : graph.get(queue.poll())) {
            if (seen[next]) continue;
            seen[next] = true;
            result++;
            queue.offer(next);
        }
    }
    return result;
}
```

호출부는 `countReachable(i, strongs) + countReachable(i, weaks)` 가 됩니다. 안찬웅 코드가 이 방식(`dfs(i, win)` / `dfs(i, lose)`)을 썼고, 덕분에 63줄로 끝났습니다.

### 2. (사소) 방문 체크에 `HashSet<Integer>` 대신 `boolean[]` — boxing-cost

정점 번호가 `1..n` 이고 `n <= 100` 으로 고정이라 `boolean[n+1]` 이면 충분합니다. `HashSet<Integer>` 는 `add`/`contains` 마다 `Integer` 오토박싱과 해시 계산이 붙습니다. 이 문제 규모(n ≤ 100)에서는 시간초과가 나지 않으므로 **정답에는 영향이 없고**, 습관 차원의 지적입니다. 위 코드 조각에 반영해 두었습니다.

### 3. (사소) `strongs`/`weaks` 가 `static` 일 필요가 없다

`solution()` 안에서 매번 새 리스트로 갈아끼우고 있어서 **테스트케이스 간 오염은 없습니다** — 그 부분은 제대로 처리했습니다. 다만 두 필드는 `solution()` 안의 지역변수로 두고 위 1번처럼 메서드 인자로 넘기면 `static` 자체가 사라집니다. 채점 서버가 클래스를 재사용해도 안전해집니다.

## 복잡도

- 시간: `O(n(n+m))` — 정점 n개 각각에서 두 그래프를 완전 탐색합니다. n ≤ 100, m ≤ 4,500 이므로 약 46만 연산으로 여유가 큽니다.
- 공간: `O(n+m)` — 인접리스트 두 벌. BFS 큐와 방문 집합은 `O(n)`.

## 요약

조건을 "도달 가능성 세기" 로 바꿔낸 판단이 정확하고, 무작위 대조에서도 오답이 없었습니다. 고칠 건 정답이 아니라 중복입니다 — 방향만 다른 같은 탐색을 두 번 적었으니 그래프를 인자로 받는 메서드 하나로 합치면 코드가 절반으로 줄어듭니다.
