---
platform: programmers
problemId: "72413"
author: seongil
source: 이성일/week2/합승택시요금.java
week: 2
compiles: true
verdict: good
tags: [good-decomposition, good-complexity]
complexity:
  time: O(E log V) * 3
  space: O(V^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 합승 택시 요금 (programmers/72413) — 이성일

## 접근

시작점 `s`, 목적지 `a`, `b` 세 곳에서 각각 다익스트라를 한 번씩 돌려 `dist`, `distA`, `distB`를 구한 뒤, 모든 정점 `k`를 "합승을 끝내는 분기점" 후보로 놓고 `dist[k] + distA[k] + distB[k]`의 최솟값을 찾는다. 이 문제의 표준 풀이(분기점 완전탐색 × 3-다익스트라)를 정확히 그대로 구현했고, `djikstra`를 함수로 분리해 세 번 재사용하는 구조도 깔끔하다.

**공식 예제**(`n=6, s=4, a=6, b=2, fares=[[4,1,10],[3,5,24],[5,6,2],[3,1,41],[5,1,24],[4,6,50],[2,4,66],[2,3,22],[1,6,25]]`)를 직접 컴파일·실행해 기대값 `82`와 정확히 일치하는 것을 확인했다.

## 개선점

지적할 만한 문제를 찾지 못했다.

## 복잡도

- 시간: `O(E log V)` 다익스트라 3회 — 분기점 탐색은 `O(V)`로 무시할 만함
- 공간: `O(V^2)` — 인접행렬(`imap`)이 `V^2`, `V <= 200` 제약에서는 문제 없음

## 요약

분기점 완전탐색 + 3회 다익스트라라는 정석 풀이를 정확히 구현했고 공식 예제로 확인했다. 인접행렬을 써서 정점 수가 커지면 공간이 아쉬울 수 있지만 이 문제 제약(`n<=200`)에서는 전혀 문제 없다.
