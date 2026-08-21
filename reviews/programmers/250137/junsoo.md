---
platform: programmers
problemId: "250137"
author: junsoo
source: 김준수/PCCP/붕대 감기.java
week: null
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(공격 시간 최댓값)
  space: O(공격 시간 최댓값)
generatedBy: claude-code-local
generatedAt: 2026-08-21 (갱신: 소스 변경 반영)
---

# [PCCP 기출문제] 1번 / 붕대 감기 (programmers/250137) — junsoo

## 접근

매초 이벤트(공격/무공격)를 `turnList[턴] = 피해량` 배열로 미리 펼쳐놓고, 1턴부터 끝까지 한 번만 순회한다.
"공격이 있는 턴이면 피해, 없으면 회복"이라는 조건만 남아서 붕대 회복 로직과 공격 로직이 섞이지 않는다.
공격 턴에 `healCnt = 0`(연속 성공 리셋)을 명시적으로 넣어서, 공격을 맞은 뒤 곧바로 이어지는 회복이
직전 연속 횟수에 이어 붙는 걸 막았다 — 이전 버전엔 이 리셋이 빠져 있었는데(공식 예제로는 드러나지
않았다) 이번에 고쳐졌다. 공식 예제 4개 모두 실제로 돌려서 확인했고 전부 일치했다.

## 개선점

지적할 만한 문제가 없다. `turnList` 를 배열로 펼치는 대신 정렬된 `attacks` 를 포인터로 순회해도
같은 로직을 공간 O(1)로 짤 수 있었겠지만, 제약(공격 시간 ≤ 1000)에서는 차이가 없다.

## 복잡도

- 시간: `O(공격 시간 최댓값)` — turnList 생성 + 1회 순회.
- 공간: `O(공격 시간 최댓값)` — turnList 배열. 제약상 최대 1000이라 문제없다.

## 요약

이벤트를 배열로 펼쳐 순회를 단순화한 깔끔한 풀이. 4개 예제 전부 통과, 지적할 결함 없음.
