---
platform: programmers
problemId: "67258"
author: junsoo
source: 김준수/week4/보석 쇼핑.java
week: 4
compiles: true
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(N)
  space: O(K)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 보석 쇼핑 (programmers/67258) — junsoo

## 접근

투 포인터로 윈도우를 관리하는 슬라이딩 윈도우 최적 풀이다. `window` 해시맵으로 현재 구간에 포함된 보석 종류별 개수를 세고, `window.keySet().size() == type` 으로 "모든 종류를 포함했는지"를 판단한다. 조건을 만족하면 결과를 갱신하고 시작 인덱스를 당겨 축소를, 만족 못 하면 끝 인덱스를 밀어 확장을 하는 전형적인 O(N) 투 포인터 패턴이다.

파일 상단 주석에 소수(prime) 곱으로 포함 여부를 판단하려던 첫 시도, 그리고 윈도우 길이를 1부터 순회하는 O(N^2) 시도가 시간초과 났다는 시행착오까지 남겨둔 점이 좋다 — 이번 `.failed` 파일이 바로 그 두 번째 시도이고, 실제로 검증해보니 시간 문제 외에 로직 자체에도 결함이 있었다 (`reviews/programmers/67258/junsoo.failed.md` 참고).

## 개선점

지적할 것이 없다. 브루트포스와 무작위 2만 건 대조(입력 길이 1~15, 알파벳 5종) 결과 전부 일치했고, 공식 예제(`["DIA","RUBY","RUBY","DIA","DIA","EMERALD","SAPPHIRE","DIA"]` → `[3, 7]`)도 그대로 재현됐다.

## 복잡도

- 시간: `O(N)` — 시작/끝 포인터가 각각 배열을 한 번씩만 오간다.
- 공간: `O(K)` — K는 보석 종류 수(해시맵 크기), N을 넘지 않는다.

## 요약

투 포인터 슬라이딩 윈도우를 정확하게 구현했다. 실패했던 이전 접근을 주석으로 남겨서 왜 이 구조를 택했는지가 코드만 봐도 드러난다.
