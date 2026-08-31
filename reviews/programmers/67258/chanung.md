---
platform: programmers
problemId: "67258"
author: chanung
source: 안찬웅/week4/보석 쇼핑.java
week: 4
compiles: true
verdict: good
tags: [good-readability, comment-noise]
complexity:
  time: O(N)
  space: O(K) — K는 보석 종류 수
generatedBy: claude-code-local
generatedAt: 2026-08-25
---

# 보석 쇼핑 (programmers/67258) — chanung

## 접근

투 포인터(슬라이딩 윈도우)로 "모든 종류를 포함하는 최소 구간"을 찾는다. `map`으로 전체 종류 수를 먼저 세고, `right`를 늘리며 `mapCnt`에 등장 종류 수(`curType`)를 채운 뒤, `curType == diaType`(모든 종류 포함)인 동안 `left`를 당기면서 최소 길이를 갱신한다. 표준적인 "최소 윈도우" 패턴을 정확히 구현했고, 윈도우가 유효한 시점(줄이기 전)에 정답 후보를 기록하는 순서도 맞다.

실제로 컴파일해 프로그래머스 예시(`[3,7]`)와 자체 케이스(단일 종류, 종류별 1회만 등장) 3건을 추가로 돌려봤고 전부 정답과 일치했다.

## 개선점

### 1. (사소) 주석의 시간복잡도가 실제 구현과 다름 — `comment-noise`

파일 상단 주석엔 "시간복잡도: NlogN"이라고 적혀 있지만, 실제 구현은 정렬이나 로그 요소 없는 순수 투 포인터 + HashMap이라 `O(N)`이다. 채점 결과엔 영향 없지만, 나중에 이 주석만 보고 복잡도를 다시 판단하면 헷갈릴 수 있다.

## 복잡도

- 시간: `O(N)` — `left`, `right`가 각각 최대 N번만 이동하는 투 포인터
- 공간: `O(K)` — K는 보석 종류 수 (`map`, `mapCnt` 크기)

## 요약

정확하고 깔끔한 슬라이딩 윈도우 구현. 실측 테스트 4건 전부 통과했고, 지적할 만한 로직 결함은 없다. 상단 주석의 시간복잡도 표기만 실제 구현과 어긋나 있다.
