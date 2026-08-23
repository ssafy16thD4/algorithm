---
platform: programmers
problemId: "340213"
author: junsoo
source: 김준수/PCCP/동영상 재생기.java
week: null
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(N)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [PCCP 기출문제] 1번 / 동영상 재생기 (programmers/340213) — junsoo

## 접근

시간을 초 단위 정수로 변환해 계산하고, `commands` 를 한 번 순회하면서 매 스텝 후 세 가지 클램프(0 이상, 영상 길이 이하, 오프닝 구간 스킵)를 순서대로 적용한다. 루프 진입 전에도 초기 위치가 오프닝 구간에 있는지 한 번 검사해서 "처음부터 오프닝 안"인 경우까지 커버했다.

## 개선점

지적할 것이 없다. 공식 예제 3건(`13:00`/`06:55`/`04:17`) 전부 통과했고, "next 이후 남은 시간이 10초 미만이면 영상 끝으로" 규칙이 실제로 지켜지는지 별도 반례(`video_len=00:20, pos=00:15, next` → 기대 `00:20`)로 확인했을 때도 정확히 `00:20` 을 반환했다.

## 복잡도

- 시간: `O(N)` — N은 commands 길이, 한 번만 순회한다.
- 공간: `O(1)`

## 요약

경계 처리(영상 끝 클램프, 오프닝 스킵)가 빠짐없이 들어가 있다. 같은 문제의 chanung 풀이가 놓친 "next 후 영상 끝 클램프"를 이 코드는 명시적으로 갖고 있다.
