---
platform: swea
problemId: "2117"
author: seongil
source: 이성일/week6/SWEA2117.java
week: 6
compiles: false
lang: java
verdict: needs-fix
tags: [missing-return, uninitialized-state, good-complexity]
complexity:
  time: O(N⁴)
  space: O(N²)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 홈 방범 서비스 (swea/2117) — seongil

## 접근

모든 칸을 중심으로 잡고 BFS 를 한 번씩 돌리는데, **거리별 집 개수를 `homeByDepth[k]` 에 쌓아두고
마지막에 누적합**으로 바꾼다. 운영 범위 `K` 의 마름모는 "중심에서 맨해튼 거리 ≤ K-1" 이고,
4방향 BFS 의 깊이가 정확히 맨해튼 거리이므로 **K 를 1부터 다시 세지 않고 BFS 한 번으로 모든 K 를
동시에 처리**한다. 중심 하나당 BFS 를 K 번 돌리는 흔한 풀이보다 한 단계 낫다.

손익 판정 `homes * M - (K² + (K-1)²) >= 0` 도 문제 조건 그대로다.

**실제로 돌려본 검증**: 완전탐색(중심 × K × 전체 칸 맨해튼 거리 계산)과 무작위 800건
(`N ≤ 10`, `M ≤ 15`, 집 밀도 0.05~0.75, 집이 1개 이상인 판)을 대조해 **불일치 0건**.
알고리즘 자체는 맞다.

## 개선점

### 1. (치명) 파일에 non-breaking space 가 섞여 있어 컴파일이 안 된다 — <missing-return>

3행부터 들여쓰기와 빈 줄 자리에 `U+00A0`(non-breaking space)가 들어가 있다. javac 가 그대로 막는다:

```
SWEA2117.java:3: error: illegal character: ' '
SWEA2117.java:5: error: illegal character: ' '
```

웹 에디터에서 코드를 복사해 붙이면 자주 생긴다. 공백으로 일괄 치환하면 그대로 컴파일된다
(**치환 후 실제로 컴파일·실행해서 위 검증을 돌렸다**). IDE 에서 "공백 문자 표시" 를 켜면 눈에 보인다.

### 2. (중요) `maxHome` 이 테스트케이스마다 초기화되지 않는다 — <uninitialized-state>

`maxHome` 은 static 필드인데 `answer` 와 달리 tc 루프 안에서 0 으로 되돌리지 않는다.
그래서 두 번째 케이스부터는 **이전 케이스들의 집 개수까지 합쳐진 값**이 들어 있다.

```java
answer = 1;
maxHome = 0;   // 이 줄이 빠져 있다
```

정답에는 영향이 없다 — 이 값은 `if (answer == maxHome) break;` 라는 조기 종료에만 쓰이고,
값이 커지면 조건이 안 걸려 그냥 끝까지 돌 뿐이다(게다가 이 `break` 는 안쪽 `j` 루프만 끊는다).
다만 "모든 집을 다 덮었으면 그만" 이라는 최적화가 첫 케이스 말고는 전혀 동작하지 않는다.

### 3. (사소) 집이 하나도 없는 판에서 1 을 출력한다

`answer = 1` 로 시작하는데, 집이 0개면 어떤 배치도 수익이 0 이라 답은 0 이어야 한다.
무작위 대조에서 갈린 57건이 전부 이 경우였다(집이 1개 이상이면 800건 0건 불일치).
`answer = 0` 으로 시작하면 된다 — `M ≥ 1` 이므로 집이 하나라도 있으면 K=1 로 항상 1 이 나온다.

## 복잡도

- 시간: `O(N⁴)` — 중심 N² 개마다 판 전체를 BFS(N²). N ≤ 20 이라 케이스당 16만 번 안쪽
- 공간: `O(N²)` — 판 + BFS 방문 배열 + 거리별 집 개수(`2N`)

## 요약

**BFS 깊이 = 맨해튼 거리라는 성질로 모든 K 를 한 번에 처리한 것이 이 풀이의 핵심이고, 정확하다** —
집이 있는 판 800건에서 완전탐색과 완전히 일치한다. 남은 건 알고리즘 밖의 두 가지다:
붙여넣기로 섞여 들어간 `U+00A0` 때문에 **지금 상태로는 컴파일이 안 되고**,
`maxHome` 초기화 누락으로 조기 종료 최적화가 첫 케이스에서만 동작한다.
