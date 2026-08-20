---
platform: programmers
problemId: "49191"
author: seongil
source: 이성일/week2/순위.java
week: 2
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N^2)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 순위 (programmers/49191) — 이성일

## 접근

각 선수에 "나를 이긴 선수(stronger)"와 "내가 이긴 선수(weaker)" 리스트를 만들고, 선수마다 두 방향으로 DFS를 돌려 "확실히 나보다 강한 선수 수 + 확실히 나보다 약한 선수 수"가 `n-1`이면 순위가 확정된다고 판단한다. `sdfs`/`wdfs`가 완전히 대칭 구조라 읽기 쉽고, 방향별로 독립된 `visited` 배열을 매번 새로 만들어 선수 간 탐색이 서로 오염되지 않는다.

**직접 손으로 검증한 예제**(`n=5, results=[[4,3],[4,2],[3,2],[1,2],[2,5]]`)로 그래프를 그려 정답을 유도(2)한 뒤 실제 컴파일·실행했고, 출력도 `2`로 일치했다.

## 개선점

지적할 만한 문제를 찾지 못했다. 전이 관계를 DFS로 직접 펼치는 접근이라 `O(N^2)`이 조금 아쉬울 수 있지만, `results.length <= 4500`, `n <= 100` 수준의 제약에서는 충분하다.

## 복잡도

- 시간: `O(N^2)` — 선수마다 최악의 경우 나머지 전원을 DFS로 순회
- 공간: `O(N^2)` — `stronger`/`weaker` 리스트 총합이 결과 간선 수에 비례

## 요약

정직한 그래프 탐색 구현이고 반례를 못 찾았다. `stronger`/`weaker`로 방향을 분리한 설계가 문제 조건을 그대로 반영해서 읽기도 쉽다.
