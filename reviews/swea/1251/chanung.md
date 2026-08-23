---
platform: swea
problemId: "1251"
author: chanung
source: 안찬웅/week1/SWEA1251.java
week: 1
compiles: true
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(N^2 log N)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 하나로 (swea/1251) — chanung

## 접근

모든 섬을 최소 비용으로 연결하는 문제를 MST(크루스칼)로 인식하고, 거리 제곱 값 자체를 간선 비용으로 써서
불필요한 `sqrt` 호출을 피했다. 좌표를 `long` 배열로 받아 `dx*dx+dy*dy` 오버플로도 미리 막아뒀다.
경로 압축이 들어간 `find`, N-1개 채택되면 조기 종료하는 `break`까지 크루스칼의 표준 형태를 잘 갖췄다.

## 개선점

지적할 만한 문제는 찾지 못했다. `union`에 rank/size 비교가 없어 트리가 한쪽으로 치우칠 수 있지만
N≤1000 규모(간선 약 50만개)에서는 경로 압축만으로도 충분하다.

## 복잡도

- 시간: `O(N^2 log N)` — 간선 O(N^2)개를 정렬하는 비용이 지배적. 주석의 `O(N^2 log N^2)`도 상수 차이일 뿐 같은 오더다.
- 공간: `O(N^2)` — 완전그래프 간선 배열이 지배적.

## 요약

MST 문제를 크루스칼로 정확히 매핑했고, 오버플로 방지·조기 종료까지 챙긴 깔끔한 구현이다. 고칠 것이 없다.
