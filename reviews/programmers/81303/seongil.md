---
platform: programmers
problemId: "81303"
author: seongil
source: 이성일/week3/표편집.java
week: 3
compiles: true
verdict: good
tags: [good-decomposition]
complexity:
  time: O(N + sum of U/D distances)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — 이성일

## 접근

각 행을 이중 연결 리스트 노드로 두고, `C`(삭제) 시점의 노드를 `spares` 스택에 저장했다가 `Z`(복구) 시 꺼내 `prev`/`next`를 다시 이어 붙이는 표준 풀이다. 삭제 시 커서를 다음(없으면 이전) 노드로 옮기는 4가지 경우(양끝 모두 있음/`next`만 없음/`prev`만 없음/유일한 행)를 빠짐없이 나눈 것도 정확하다.

**공식 예제 1**(`n=8, k=2, cmd=["D 2","C","U 3","C","D 4","C","U 2","Z","Z"]`)을 직접 컴파일·실행해 기대값 `OOOOXOOO`와 정확히 일치했다.

문제 제약상 `U X`/`D X`의 `X` 총합이 전체적으로 `O(N)`으로 보장되므로, `while(x>0) currNode=currNode.next`처럼 한 칸씩 이동하는 방식이 시간복잡도상 문제되지 않는다 — 배열 기반이 아니라 연결 리스트로 짠 이유이기도 하다.

## 개선점

지적할 만한 문제를 찾지 못했다.

## 복잡도

- 시간: `O(N + ΣX)` — 초기 리스트 구성 `O(N)`, 이동 명령 총 이동 거리(문제에서 `O(N)`으로 보장)
- 공간: `O(N)` — 노드 배열과 `spares` 스택

## 요약

이중 연결 리스트 + undo 스택이라는 정석 구조를 정확히 구현했고 공식 예제로 확인했다. 지적할 부분이 없다.
