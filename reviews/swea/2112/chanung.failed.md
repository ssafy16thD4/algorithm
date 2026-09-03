---
platform: swea
problemId: "2112"
author: chanung
source: 안찬웅/week6/보호 필름 실패.java
week: 6
compiles: false
lang: java
verdict: needs-fix
tags: [missing-return]
complexity:
  time: O(3^D * D * W)
  space: O(D * W)
generatedBy: claude-code-local
generatedAt: 2026-09-03
---

# 보호 필름 — 실패 버전 (swea/2112) — chanung

## 접근

행마다 "그대로 둔다 / 0으로 덮는다 / 1로 덮는다" 3갈래로 뻗는 DFS 백트래킹이다. `check()`는 각 열에서 세로로 연속된 동일값이 k 이상인 구간이 하나라도 있는지 검사하고, 하나라도 없는 열이 있으면 즉시 실패 처리한다 — 문제의 "각 열마다 연속 k 이상 구간이 필요하다"는 조건을 정확히 구현했다. `answer`의 초기값을 `k`로 잡은 것도 좋은 판단이다 — 아무 연속된 k개 행을 통째로 한 색으로 덮으면 항상 모든 열이 통과하므로, `k`는 항상 실행 가능한 상한이면서 동시에 강한 가지치기 기준이 된다(`cnt >= answer`에서 곧바로 컷). 0/1로 덮은 뒤 `arr[row] = backup`으로 원상복구하는 백트래킹 처리도 정확하다.

## 개선점

### 1. (치명) public 클래스명이 파일명과 달라 컴파일이 안 된다 — `missing-return`

```java
public class algo {
```

파일명은 `보호 필름 실패.java`인데 `public class`는 `algo`로 선언돼 있다. 자바는 `public` 클래스명과 파일명이 일치해야 하므로 그 자리에서 컴파일 실패한다:

```
안찬웅/week6/보호 필름 실패.java:17: error: class algo is public, should be declared in a file named algo.java
```

클래스명을 파일명과 맞추거나(비ASCII라 어렵다면 `algo.java`로 파일명을 바꾸거나), `public`을 떼면 해결된다. 검증한 바로는 이 한 줄 문제를 빼면 DFS·백트래킹·`check()` 로직 자체에서 확신을 갖고 지적할 오류는 찾지 못했다 — 그래서 `wrong`이 아니라 `needs-fix`로 남긴다.

## 복잡도

- 시간: `O(3^D · D·W)` — 행마다 3갈래로 뻗는 DFS에 `cnt >= answer` 가지치기가 걸리고, 매 노드마다 `check()`가 `O(D·W)`.
- 공간: `O(D·W)` — `arr` 배열 + 재귀 깊이 D, 백업용 `int[]` 하나.

## 요약

클래스명-파일명 불일치라는 순수 컴파일 오류 하나 때문에 채점이 안 되는 상태다. DFS 설계와 `check()` 판정, `answer = k` 상한 설정까지 코드만 읽어서는 문제 조건과 잘 들어맞아 보여서, 이름만 고치면 통과할 가능성이 있어 보인다 — 다만 실제 채점 데이터로 돌려본 것은 아니라 "검증 안 함"으로 남긴다.
