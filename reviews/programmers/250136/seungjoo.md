---
platform: programmers
problemId: "250136"
author: seungjoo
source: 이승주/pccp/석유 시추(박진우).java
week: null
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(n*m)
  space: O(n*m)
generatedBy: claude-code-local
generatedAt: 2026-08-21
---

# [PCCP 기출문제] 2번 / 석유 시추 (programmers/250136) — seungjoo

## 접근

DFS로 덩어리를 한 번씩만 찾은 뒤, 그 덩어리가 걸치는 열 범위 `[minX, maxX]` 를 구해서 **그 범위의
모든 열에 덩어리 크기를 한 번씩만 더한다.** 안찬웅 풀이와 달리 "행 전이"가 아니라 "열 범위" 단위로
더하기 때문에, 같은 덩어리가 한 열을 여러 구간으로 지나가도 그 열엔 정확히 한 번만 더해진다 —
같은 반례(`{{1,1,0,0},{0,1,0,0},{0,1,0,0},{1,1,0,0},{0,1,0,0}}`)로 교차 검증했고 정확히 7을 반환했다.
공식 예제 2개도 실제로 돌려서 확인했다.

## 개선점

지적할 만한 문제가 없다. `getOilNum` 을 열 범위에 통째로 더하는 아이디어가 이 문제의 핵심(연결
덩어리는 어느 열을 뚫어도 통째로 얻는다)을 정확히 반영한다.

## 복잡도

- 시간: `O(n*m)` — DFS 한 번 + 덩어리별 열 범위 갱신.
- 공간: `O(n*m)` — visited, getOilNum.

## 요약

열 범위 단위 합산이 이 문제의 "연결 덩어리는 통째로 얻는다"는 조건을 정확히 반영한 좋은 설계.
2개 예제 전부 통과, 같은 문제를 틀린 안찬웅 풀이의 반례에서도 정답을 냈다.
