---
platform: programmers
problemId: "340211"
author: chanung
source: 안찬웅/pccp/충돌위험 찾기.java
week: null
compiles: true
verdict: unattempted
tags: []
complexity:
  time: null
  space: null
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [PCCP 기출문제] 3번 / 충돌위험 찾기 (programmers/340211) — chanung

`solution` 메서드 본문이 `int answer = 0; return answer;` 뿐이라 알고리즘이 들어가기 전 뼈대만 있다(공식 예제 기대값 `1`/`9`/`0` 중 `1`, `9` 는 당연히 실패, 직접 실행해 확인).

**다음에 볼 것**: 로봇마다 웨이포인트 구간을 "행 좌표 이동을 열 좌표 이동보다 먼저" 규칙대로 좌표 리스트로 펼친 뒤, 모든 로봇을 같은 타임스텝 기준으로 맞춰 같은 좌표에 2대 이상 모이는 타임스텝을 세면 된다. 격자 크기가 100×100 이하로 작으니 BFS든 직접 경로를 계산하든 시간 제약은 크게 문제 되지 않는다 — 같은 문제를 다룬 `reviews/programmers/340211/junsoo.md` 에 실제로 검증된 접근과 성능 관련 주의점이 있으니 참고하면 된다.
