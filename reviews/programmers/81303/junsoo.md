---
platform: programmers
problemId: "81303"
author: junsoo
source: 김준수/week3/표 편집.java
week: 3
compiles: true
verdict: good
tags: [good-complexity]
complexity:
  time: O(N + Σmove)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — junsoo

## 접근

`ArrayList`/`LinkedList`로 시간초과가 났던 시행착오를 거쳐, 배열 두 개(`frontPointers`,
`backPointers`)로 직접 더블 링크드 리스트를 구현했다. 삭제는 앞뒤 포인터만 서로 이어붙이고
`current` 자신은 건드리지 않는 방식이라, 되돌리기(`Z`)할 때 `current`가 그대로 유지된다는 문제
규칙과 자연스럽게 맞아떨어진다. 삭제된 행의 인덱스를 스택에 쌓아두고, 되돌리기는 그 인덱스만
다시 앞뒤에 연결해주면 되는 것도 정확하다.

세부 로직을 하나씩 손으로 따라가봤다.

- `delete()`: 이웃을 먼저 연결한 뒤 `current`를 아래(`backPointers`)로, 없으면 위(`frontPointers`)로
  옮긴다 — 이때 참조하는 `backPointers[current]`/`frontPointers[current]`는 아직 갱신 전(자기 자신은
  안 건드림) 값이라 원래 이웃이 맞다.
- `recover()`: `current`를 바꾸지 않고 스택에서 꺼낸 행만 원래 자리에 다시 끼워 넣는다 — "되돌리기해도
  현재 선택된 행은 바뀌지 않는다"는 규칙과 정확히 일치한다.

배열 기반 링크드 리스트라 삭제/복구가 각각 `O(1)`이라 이 문제의 큰 제약(`n <= 1,000,000`)에서도
시간초과 없이 통과할 수 있는 구조다.

## 개선점

특별히 지적할 부분을 찾지 못했다. 링크드 리스트 연결·해제, 스택을 이용한 되돌리기, 최종 결과
문자열 구성까지 모두 손으로 재현했을 때 어긋나는 지점이 없었다.

## 복잡도

- 시간: `O(N + Σmove)` — 초기화 `O(N)`에, `U`/`D`는 이동 칸 수만큼, `C`/`Z`는 각각 `O(1)`.
- 공간: `O(N)` — 포인터 배열 두 개와 스택.

## 요약

시간초과 시행착오를 겪은 뒤 배열 기반 더블 링크드 리스트로 갈아탄 판단이 정확했고, 삭제·복구·이동
로직 모두 문제 규칙과 정확히 맞는다. 지적할 결함을 찾지 못했다.
</content>
