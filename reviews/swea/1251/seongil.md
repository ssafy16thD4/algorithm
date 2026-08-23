---
platform: swea
problemId: "1251"
author: seongil
source: 이성일/week1/SWEA1251.java
week: 1
compiles: true
verdict: good
tags: [good-decomposition, collection-choice]
complexity:
  time: O(N^2 log N)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 하나로 (swea/1251) — 이성일

## 접근

정석적인 Kruskal MST 구현이다. 모든 간선쌍(N*(N-1)/2개)을 비용순 정렬하고, union-find로 사이클을 걸러가며 N-1개 간선을 고른다. `find`에 경로 압축(`parent[(int) d] = find(parent[(int) d])`)이 들어 있어서 `union`이 랭크/크기 기준이 아니라 단순 `a<b` 비교인데도 트리가 심하게 기울지 않는다 — 경로 압축만으로도 상각 성능이 충분히 나오는 조합을 정확히 알고 쓴 것으로 보인다. 테스트케이스마다 `parent`, `graph`, `ans`를 새로 초기화해서 TC 간 상태 오염도 없다.

## 개선점

### 1. (사소) 정수 인덱스를 double 배열에 저장 — `collection-choice`

`graph`가 `double[][]`인데 0,1번 열은 사실 정점 번호(정수)다. `find(double d)`처럼 매번 `(int) d` 캐스팅을 거치는데, 정점 수가 적어(최대 100 내외) 정밀도 문제는 없지만 읽는 사람은 왜 정수 인덱스가 double인지 한 번 더 생각해야 한다. `int[][] edgeIdx`와 `double[] edgeCost`를 분리하거나, `Edge` 클래스 하나를 만들어 정렬하면 캐스팅 없이 더 명확해진다.

## 복잡도

- 시간: `O(N^2 log N)` — 간선 개수가 `N^2` 수준이라 정렬이 지배적
- 공간: `O(N^2)` — 간선 배열

## 요약

교과서적인 Kruskal 구현이고 지적할 만한 오답 포인트는 없었다. double 배열에 정수 인덱스를 욱여넣은 것만 스타일 문제로 남는다.
