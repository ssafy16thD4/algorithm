---
platform: programmers
problemId: "49191"
author: junsoo
source: 김준수/week2/순위.java
week: 2
compiles: true
verdict: needs-fix
tags: [duplicate-code]
complexity:
  time: O(N(N+M))
  space: O(N+M)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 순위 (programmers/49191) — junsoo

## 접근

`results[i] = [승자, 패자]`를 `strongs[패자]에 승자 추가`, `weaks[승자]에 패자 추가`로 정확히 방향을
맞춰 그래프 두 개를 만들고, 각 선수마다 BFS로 "확정적으로 나보다 센 사람 수"와 "확정적으로 나보다
약한 사람 수"를 transitive하게(간접 승패까지) 센다. 합이 `n-1`이면 그 선수는 전원과의 상대 순위가
확정된다는 판정도 문제 조건과 정확히 일치한다. `김준수/week1/SWEA5643.java`(키 순서)와 같은 유형의
문제인데, 그쪽에서는 그래프 이름과 실제 저장 내용이 반대였던 반면 여기는 `strongs`/`weaks`가
이름 그대로 정확하게 채워져 있다.

## 개선점

### 1. (사소) `countStrong`과 `countWeak`가 그래프만 다르고 완전히 같은 BFS 코드를 반복한다 — `duplicate-code`

```java
private int countStrong(int i){ ... strongs.get(curr) ... }
private int countWeak(int i){ ... weaks.get(curr) ... }
```

두 메서드가 순회하는 리스트(`strongs`/`weaks`)만 다르고 나머지 큐·집합·반복문은 토씨 하나 안 다르다.
그래프를 인자로 받는 메서드 하나로 합치면 20줄 가까이 줄어든다.

```java
private int countReachable(int i, List<List<Integer>> graph) {
    int result = 0;
    Queue<Integer> queue = new ArrayDeque<>();
    Set<Integer> visited = new HashSet<>();
    queue.offer(i);
    visited.add(i);

    while (!queue.isEmpty()) {
        int curr = queue.poll();
        for (int next : graph.get(curr)) {
            if (visited.add(next)) {   // add()가 새로 추가됐는지 여부를 boolean으로 반환
                result++;
                queue.offer(next);
            }
        }
    }
    return result;
}
```

호출부는 `countReachable(i, strongs) + countReachable(i, weaks)`로 바뀐다.

## 복잡도

- 시간: `O(N(N+M))` — 선수마다 그래프 전체를 BFS. 이 문제의 `n <= 100`, `results.length <= 4500`
  기준으로 여유 있게 통과한다.
- 공간: `O(N+M)` — 인접 리스트 두 개.

## 요약

방향 설정과 transitive 도달 판정 모두 정확하고, 반례를 찾지 못했다. 두 BFS 메서드를 하나로 합치는
정도만 남은 정리거리다.
</content>
