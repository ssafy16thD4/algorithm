---
platform: programmers
problemId: "67258"
author: seongil
source: 이성일/week4/보석쇼핑.java
week: 4
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N)
  space: O(K)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 보석 쇼핑 (programmers/67258) — seongil

## 접근

투 포인터를 `extendRight`/`shrinkLeft` 두 메서드로 나눴다. `leftIdx` 를 `shrinkLeft` 의 반환값으로 다음 호출에 넘겨 포인터 상태를 이어가는 방식이라 전체적으로 O(N) 이 유지된다.

`shrinkLeft` 안에서 매 반복마다 정답을 갱신하지 않고, 왼쪽 원소를 지웠을 때 그 보석의 개수가 정확히 0이 되는(=윈도우가 깨지기 직전인) 순간에만 정답을 갱신하는 점이 눈에 띈다 — 같은 `rightIdx` 에 대해 만들 수 있는 가장 작은 윈도우가 정확히 그 순간이라는 걸 이용한 것으로, 매 스텝 갱신하는 것과 결과가 같으면서 불필요한 비교를 줄인다. 무작위 2만 건 대조와 공식 예제(`[3, 7]`)로 확인했을 때 결과가 브루트포스와 정확히 일치했다.

## 개선점

지적할 것이 없다. `실패` 파일(`보석쇼핑실패.java`, 옛 시간초과 버전)의 O(N^2) 문제를 O(N)으로 완전히 해결했다.

## 복잡도

- 시간: `O(N)` — `leftIdx`, `rightIdx` 가 각각 배열을 한 번씩만 지난다.
- 공간: `O(K)` — K는 보석 종류 수.

## 요약

투 포인터를 두 메서드로 분리하면서도 상태(leftIdx)를 정확히 이어받아 O(N)을 유지했다. "언제 정답을 갱신해도 되는가"를 정확히 짚어서 불필요한 비교를 없앤 점이 좋다.
