---
platform: programmers
problemId: "250137"
author: chanung
source: 안찬웅/pccp/붕대 감기.java
week: null
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(공격 시간 최댓값)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-21
---

# [PCCP 기출문제] 1번 / 붕대 감기 (programmers/250137) — chanung

## 접근

1초 단위 시뮬레이션. `attacksIdx` 로 다음 공격 시각을 캐싱해두고, 매 초마다 "지금이 공격 시각인가"만
분기해서 처리한다. 연속 성공 횟수(`cnt`)를 공격 시점에 즉시 0으로 리셋하는 처리와, 회복 시 최대체력
캡을 두 군데(초당 회복, 추가 회복) 모두 거는 처리가 빠짐없이 들어가 있다.

실제로 컴파일해서 공식 예제 4개(`[5,1,5],30,...→5`, `[3,2,7],20,...→-1`, `[4,2,7],20,...→-1`, `[1,1,1],5,...→3`) 모두 확인했고, 4개 다 일치했다.

## 개선점

지적할 만한 문제가 없다. 마지막 공격 시각까지만 루프를 도는 `time < maxTime` 종료 조건도
"모든 공격이 끝난 뒤의 체력"이라는 문제 요구와 정확히 맞아떨어진다.

## 복잡도

- 시간: `O(공격 시간 최댓값)` — 초 단위로 순회. 제약상 최대 1000이라 여유 있다.
- 공간: `O(1)`

## 요약

의사코드 주석 그대로 구현이 따라간다. 4개 예제 전부 통과, 지적할 결함 없음.
