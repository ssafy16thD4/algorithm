---
platform: programmers
problemId: "340212"
author: junsoo
source: 김준수/PCCP/퍼즐 게임 챌린지.java
week: null
compiles: true
verdict: good
tags: [good-complexity]
complexity:
  time: O(N log(max(diffs)))
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [PCCP 기출문제] 2번 / 퍼즐 게임 챌린지 (programmers/340212) — junsoo

## 접근

정답(최소 숙련도 레벨)이 커질수록 총 소요시간이 단조 감소한다는 점(레벨이 오르면 각 퍼즐의 실수 횟수만 줄어들거나 그대로다)에 착안해 매개변수 탐색(이분 탐색)으로 레벨을 찾는다. `isValid(level)` 은 문제가 정의한 공식(`(time_cur + time_prev) * (diff - level) + time_cur`)을 그대로 옮겨서 O(N)에 계산한다.

## 개선점

지적할 것이 없다. 공식 예제 4건(`3`/`2`/`294`/`39354`) 전부 통과했고, 최대 크기(N=300,000, diffs 최댓값 100,000, limit=1로 강제 최악 케이스)로 직접 돌려봤을 때 **18ms** 만에 끝났다.

## 복잡도

- 시간: `O(N log(max(diffs)))` — 이분 탐색 각 스텝마다 배열을 한 번씩 훑는다.
- 공간: `O(1)`

## 요약

단조성 관찰과 이분 탐색 선택이 정확하다. 같은 문제를 선형 탐색(레벨을 1씩 증가)으로 짠 chanung 풀이는 논리는 같지만 최대 크기에서 15초 이상 걸려 시간 초과가 확실한 반면, 이 풀이는 여유 있게 통과한다.
