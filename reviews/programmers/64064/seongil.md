---
platform: programmers
problemId: "64064"
author: seongil
source: 이성일/week4/불량사용자.java
week: 4
compiles: true
verdict: good
tags: [good-decomposition, redundant-loop]
complexity:
  time: O(nodeSize! ) worst case, 실질적으로는 매칭 가능한 조합 수에 비례
  space: O(userSize)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 불량 사용자 (programmers/64064) — 이성일

## 접근

**참고**: `CLAUDE.md`에는 이 파일이 "접근만 주석으로 적어두고 `return 0`인 뼈대"라고 적혀 있는데, 실제 현재 파일 내용은 완결된 DFS 백트래킹 구현이다 — 주석에 적힌 계획(제재 아이디 순으로 DFS, 어순 비교, 방문 관리, `Set<Set<Integer>>`로 조합 중복 제거)이 실제로 그대로 구현돼 있었다. 문서가 최신 상태를 반영하지 못한 것으로 보여 실제 코드를 기준으로 리뷰한다.

제재 아이디 각각(`idx`)에 대해 아직 안 쓴 응모자(`visited[i]==false`) 중 패턴이 일치하는 사람을 하나씩 배정해보는 DFS다. `*`는 건너뛰고 나머지 문자를 그대로 비교하는 매칭 로직, 배정이 끝나면(`idx==nodeSize`) 결과 집합(`states`)을 `Set<Set<Integer>>`에 넣어 서로 다른 조합만 세는 것 모두 이 문제의 표준 풀이와 일치한다.

**공식 예제 세 개를 직접 컴파일·실행해 확인했다:**

- `user=["frodo","fradi","crodo","abc123","frodoc"], banned=["fr*d*","abc1**"]` → 기대값 `2`, 실제 `2`
- `user=["frodo","fradi","crodo","abc123","frodoc"], banned=["*rodo","*rodo","******"]` → 기대값 `2`, 실제 `2`
- `user=["frodo","fradi","crodo","abc123","frodoc"], banned=["fr*d*","*rodo","******","******"]` → 기대값 `3`, 실제 `3`

세 예제 모두 일치했다.

## 개선점

### 1. (사소) `HashSet`이 이미 하는 중복 제거를 수동으로 한 번 더 함 — `redundant-loop`

```java
for (Set<Integer> candidate: lists) {
    if (candidate.equals(states)) {
        return;
    }
}
Set<Integer> candidate = new HashSet<>();
candidate.addAll(states);
lists.add(candidate);
```

`lists`가 `Set<Set<Integer>>`이므로 `lists.add(new HashSet<>(states))`만 호출해도 `Set`의 `equals`/`hashCode` 계약에 따라 이미 있는 조합이면 자동으로 무시된다. 위 수동 순회(`for (Set<Integer> candidate: lists) ...`)는 매번 지금까지 찾은 조합 수만큼 전체 비교를 반복하는 불필요한 추가 비용이다. `lists.add(new HashSet<>(states));` 한 줄로 줄여도 동작은 같다(제거해도 결과가 바뀌지 않는 코드이므로 별도 대조 검증은 필요 없었다 — `Set.add`의 표준 동작이다).

## 복잡도

- 시간: `O(제재 수 * 응모자 수)` 가지의 DFS 분기 — 문제 제약(`user_id.length, banned_id.length <= 8`)에서 완전탐색이 충분히 빠름
- 공간: `O(userSize)` — `visited` 배열과 재귀 깊이

## 요약

문서(`CLAUDE.md`)의 예상과 달리 실제로는 완결되고 정확한 DFS 백트래킹 구현이며 공식 예제 3개를 모두 통과한다. `HashSet`이 이미 처리하는 중복 제거를 수동으로 한 번 더 하는 부분만 정리하면 된다.
