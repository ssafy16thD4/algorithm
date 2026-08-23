---
platform: programmers
problemId: "60059"
author: chanung
source: 안찬웅/week2/자물쇠와 열쇠.java
week: 2
compiles: true
verdict: good
tags: [naming, comment-noise]
complexity:
  time: O((N+M)^2 * N^2)
  space: O(M^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 자물쇠와 열쇠 (programmers/60059) — chanung

## 접근

4방향 회전 × 모든 이동 오프셋을 완전탐색하면서, 자물쇠 전체 칸에 대해 `(열쇠 겹침값 + 자물쇠값) == 1`
인지 한 번에 검사하는 방식이다. 이 판정 하나로 "홈이 안 채워짐(합 0)"과 "돌기끼리 겹침(합 2)"을
동시에 걸러내는 이 문제의 표준적인 트릭을 정확히 구현했다. 이동 범위를 `-(m-1)..n-1`로 잡은 것도
열쇠와 자물쇠가 한 칸이라도 겹칠 수 있는 범위를 정확히 커버한다. 답을 찾자마자 `return`하는 조기
종료도 잘 들어가 있다.

## 개선점

### 1. (사소) 지역 변수 `rotate`가 메서드 `rotate`와 이름이 같다 — `naming`

```java
static void rotate(int[][] key, int[][] lock, int degree) {
    int[][] rotate = new int[m][m];
    ...
```

컴파일은 되지만 메서드 이름과 회전된 배열 변수 이름이 같아서 읽을 때 헷갈린다. `rotated`처럼
다른 이름을 쓰는 게 낫다.

### 2. (사소) 도입부 주석이 실제 구현과 다르다 — `comment-noise`

```java
// 1.0도 이동 -> 오른쪽 한칸, 왼쪽 한칸, 위로 한칸 아래로 한칸
// 1.90도 이동 -> 오른쪽 한칸, 왼쪽 한칸, 위로 한칸 아래로 한칸
```

이 네 줄은 이동을 "한 칸씩"으로 설명하는데, 실제 `moveKey`는 `-(m-1)`부터 `n-1`까지 전체 범위를
훑는 완전탐색이다. 시행착오 중 남은 메모로 보이는데, 최종 로직과 안 맞아 다음에 읽을 때 혼란을 준다.

## 복잡도

- 시간: `O((N+M)^2 * N^2)` — 4방향 × 이동 오프셋 O((N+M)^2) × 매번 자물쇠 전체 O(N^2) 검사.
- 공간: `O(M^2)` — 회전된 열쇠 배열.

## 요약

"합이 1인지"로 홈/겹침을 동시에 판정하는 핵심 트릭을 정확히 구현했다. 지적한 두 개는 모두
가독성 문제일 뿐 동작에는 영향이 없다.
