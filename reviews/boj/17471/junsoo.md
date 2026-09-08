---
platform: boj
problemId: "17471"
author: junsoo
source: 김준수/week6/게리맨더링.java
week: 6
compiles: true
lang: java
verdict: good
tags: [naming, good-decomposition, good-complexity]
complexity:
  time: O(2^N * (N + E))
  space: O(N + E)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 게리맨더링 (boj/17471) — junsoo

## 접근

N ≤ 10 을 보고 "모든 구역을 A 또는 B 에 배치 → 2^10 = 1024 가지" 로 바로 잡았다. `dfs(idx)` 가
구역 하나씩 A(0)/B(1) 를 정하고, 리프에서 두 선거구의 대표 노드 하나씩 골라 `bfs()` 로
연결 여부를 확인한 뒤 인구 차를 갱신한다.

**리프에서 합을 다시 세지 않는 게 이 코드의 장점이다.** `typeSums[]`/`typeCnts[]` 를 재귀 내려갈 때
더하고 올라올 때 빼는 식으로 유지해서, 리프마다 N 번 도는 합산이 없다. 연결 판정도 정석이다 —
같은 선거구 노드만 따라가며 방문 수를 세고, 그 수가 `typeCnts[선거구]` 와 같으면 연결이다.
빈 선거구는 `aStart == -1 || bStart == -1` 에서 걸러지므로 "각 선거구는 적어도 한 구역" 조건도 지켜진다.

**실제로 돌려본 검증**: 부분집합 완전탐색을 독립적으로 짜서 무작위 500건(N ≤ 8, 간선 확률 0~0.7,
불가능 케이스 포함)과 대조해 **불일치 0건**.

마스크 `S` 와 그 여집합이 같은 분할이라 모든 분할이 두 번 평가되지만, 1024 → 2048 번 수준이라
성능에는 영향이 없다. 1번 구역을 A 에 고정하면 절반으로 줄긴 한다.

## 개선점

### 1. (중요) `package algorithm;` + `public class 게리맨더링` 은 BOJ 에 제출이 안 된다 — <naming>

BOJ Java 는 **default package 의 `public class Main`** 을 요구한다. 로컬 `javac` 는 클래스명에 맞춘
임시 파일로 돌리기 때문에 `compiles: true` 지만, 채점 서버에서는 컴파일 단계에서 막힌다.
같은 폴더의 `다리만들기2.java`, `최대부분수열.java` 도 같은 상태다.

```java
// package algorithm;   ← 제출 시 제거
public class Main {      ← 게리맨더링 → Main
```

## 복잡도

- 시간: `O(2^N * (N + E))` — 배치 2^N 가지마다 BFS 두 번. N ≤ 10 이라 약 2만 스텝
- 공간: `O(N + E)` — 인접 리스트 + 재귀 깊이 N. `visited` 를 BFS 마다 새로 잡지만 N 이 작아 무의미

## 요약

접근·구현 둘 다 맞고, 무작위 500건 대조에서 전부 정답이다. 합을 재귀에서 증감으로 유지한 것과
"방문 수 == 선거구 크기" 연결 판정이 깔끔하다. 실제로 걸리는 건 제출 형식 하나 — 패키지 선언을 지우고
클래스명을 `Main` 으로 바꾸면 그대로 통과할 코드다.
