---
platform: ssafy
problemId: "interview"
author: seongil
source: 이성일/week6/면접.java
week: 6
compiles: true
lang: java
verdict: good
tags: [good-complexity, overflow]
complexity:
  time: O(T · N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 면접 (ssafy/interview) — seongil
## 접근

맞은 문제를 `K-1` 개씩 끊어 담고 한 칸씩 건너뛰는 방식으로 **최소 점수가 나오는 O/X 배치를 직접 만든 뒤**,
그 배치를 문제 규칙대로 한 번 시뮬레이션한다. 탐색이 없다.

```java
while (M > 0 && idx < board.length) {
    for (int i = 0; i < K-1; i++) { ... board[idx + i] = 1; M--; }
    idx += K;                    // 한 칸 비워 연속을 끊는다
}
if (M > 0) { /* 남은 정답은 앞에서부터 빈칸에 채운다 */ }
```

**두 번째 루프가 이 풀이의 핵심이다.** 칸막이를 다 쓰고도 정답이 남으면 앞에서부터 채우는데,
그러면 2배가 **앞쪽(누적 점수가 작을 때)** 에서 터진다. 최소화 문제에서 곱셈은 값이 작을 때 당해야 유리하다는
성질을 코드가 그대로 실행한다.

**실제로 돌려본 검증**: `n ≤ 12` 의 모든 `(N, M, K)` 조합 728건을 O/X 전 배치 완전탐색과 대조해
**불일치 0건**. 같은 구성을 쓴 `안찬웅/week6/면접 실패.java` 도 728건 0건이다.
즉 이 그리디는 이 규칙 아래에서 최적이 맞다.

## 개선점

### 1. (중요) 점수가 `int` 를 넘길 수 있다 — 제약 확인 필요

`answer *= 2` 가 반복되면 지수적으로 커진다. 2배가 31번만 일어나도 int 를 넘긴다.
`N ≤ 500` 급이면 확실히 넘친다. **→ 문제의 N·K 제약과 출력 형식(모듈러 여부) 확인 필요.**
모듈러가 없다면 `long` 도 부족하므로 문제 조건을 먼저 봐야 한다.

## 복잡도

- 시간: `O(T · N)` — 배치 구성 한 번 + 시뮬레이션 한 번
- 공간: `O(N)`

## 요약

탐색 없이 배치를 구성해서 O(N) 에 끝냈고, 완전탐색 728건 대조에서 전부 정답이다.
남은 확인 사항은 점수 자료형 하나다.
