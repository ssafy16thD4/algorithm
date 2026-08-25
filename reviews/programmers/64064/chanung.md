---
platform: programmers
problemId: "64064"
author: chanung
source: 안찬웅/week4/불량 사용자.java
week: 4
compiles: true
verdict: wrong
tags: [wrong-algorithm]
complexity:
  time: O(banned_id.length × user_id.length × L)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-25
---

# 불량 사용자 (programmers/64064) — chanung

## 접근

각 `banned_id`마다 매칭되는 `user_id` 개수를 세고(`verse`로 와일드카드 비교), 그 개수들 중 최댓값을 답으로 반환한다.

## 개선점

### 1. (치명) 문제를 잘못 풀었다 — 정답이 아니라 "가장 많이 매칭되는 banned_id 하나의 매칭 수"를 반환 — `wrong-algorithm`

이 문제는 "**서로 다른 banned_id들에 서로 다른 user_id를 하나씩 배정하는 방법(조합) 자체가 몇 가지인지**"를 묻는다. `banned_id` 각각을 독립적으로 세서 max를 취하는 방식으로는 여러 `banned_id`가 겹치는 `user_id` 후보를 공유할 때 실제 조합 수를 반영하지 못한다.

**실제로 돌려본 반례** — 프로그래머스 공식 예시 3번:
```java
user_id   = {"frodo","fradi","crodo","abc123","frodoc"}
banned_id = {"fr*d*","*rodo","******","******"}
```
기대 출력: `3`
실제 출력: `2`

원인: `banned_id[0]="fr*d*"`는 `frodo`,`fradi`에 매칭(cnt=2), `banned_id[1]="*rodo"`는 `frodo`,`crodo`에 매칭(cnt=2)이라 `maxCnt=2`로 끝난다. 하지만 실제로는 `{frodo, crodo, abc123, frodoc}` 같은 서로 다른 4명을 4개의 `banned_id`에 중복 없이 배정하는 조합이 3가지 존재해서 정답은 3이다. `banned_id`끼리 후보를 공유할 때의 조합 폭발을 전혀 계산하지 않은 게 원인이다.

**올바른 방향**: `banned_id` 개수(최대 8개)만큼 깊이의 DFS/백트래킹으로 "각 `banned_id`에 서로 다른 `user_id` 인덱스를 하나씩 배정"하는 모든 조합을 만들고, 배정된 인덱스 집합을 정렬해 `Set<List<Integer>>`에 넣어 중복 제거 후 개수를 센다. (팀원 `김준수` 풀이가 이 방식으로 정확히 통과했다 — `dfs(depth, banned)`로 `banned_id[depth]`가 매칭 가능한 `user_id` 인덱스들을 순회하며 재귀, `depth == banned_id.length`에서 중복 인덱스 여부 확인 후 정렬해서 `Set`에 저장.)

## 복잡도

- 시간: `O(banned_id.length × user_id.length × L)` — 각 `banned_id`마다 전체 `user_id`를 훑으며 길이 L 문자열 비교. 다만 이 복잡도는 **틀린 알고리즘의 복잡도**이며 정답 자체가 산출되지 않는다.
- 공간: `O(1)` (누적 카운터만 사용)

## 요약

와일드카드 문자열 매칭(`verse`) 자체는 맞게 짰지만, 문제의 핵심인 "banned_id별 서로 다른 user_id 배정 조합 수 세기"를 하지 않고 단순히 최대 매칭 수만 반환해서 오답이다. 공식 예시 3번에서 기대값 3, 실제값 2로 확인했다. DFS로 조합을 만들고 `Set`으로 중복 제거하는 방식으로 다시 짜야 한다.
