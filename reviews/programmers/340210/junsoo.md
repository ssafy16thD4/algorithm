---
platform: programmers
problemId: "340210"
author: junsoo
source: 김준수/PCCP/수식 복원하기.java
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

# [PCCP 기출문제] 4번 / 수식 복원하기 (programmers/340210) — junsoo

주석에 적힌 계획(진법 2~9 후보 중 각 수식이 성립하는 진법 판별 → 정답 수식들의 공통 후보 진법 → 미지수 수식에 그 진법들을 대입해 결과가 하나면 채우고 여러 개면 `?`)은 방향이 맞다. `Exp` 클래스로 수식을 파싱하는 부분까지는 구현했고, 각 진법 후보에 대해 `left`/`right` 를 구성하는 자릿수가 그 진법 범위를 넘는지 거르는 로직도 있다.

하지만 그 다음 — 진법에 맞춰 계산한 값을 실제 `result` 와 비교해서 "이 진법이 유효한가"를 판정하는 부분, 그리고 그 유효 진법들로 미지수 수식의 결과를 계산해 답을 채우는 부분이 전혀 없다. `answer` 는 끝까지 빈 배열(`{}`)로 초기화된 채 그대로 반환되어, 입력이 무엇이든 항상 빈 배열이 나온다(공식 예제 어느 것도 통과 못 함 — 직접 실행해 확인).

**다음에 볼 것**: 진법 변환은 `Integer.parseInt(Integer.toString(n, d))` 로 이미 시도했던 방향을 살려서, (1) 정답 있는 수식들의 유효 진법 집합을 먼저 구하고 (2) 그 집합을 전체 수식에 대해 교집합 낸 뒤 (3) 미지수 수식마다 남은 진법으로 계산한 결과값 집합을 만들어 크기가 1이면 그 값, 아니면 `?` 로 채우면 된다.
