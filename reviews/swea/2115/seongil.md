---
platform: swea
problemId: "2115"
author: seongil
source: 이성일/week6/SWEA2115.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [dead-code, redundant-loop, good-complexity]
complexity:
  time: O(N⁴ · M!)
  space: O(N² + M)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 벌꿀채취 (swea/2115) — seongil

## 접근

첫 번째 일꾼의 자리 `(i, j)` 를 고정하고, 두 번째 일꾼을 **같은 행이면 `j+M` 부터, 다음 행부터는 0열부터**
훑는 `while(a < N)` 루프로 겹치지 않는 배치만 정확히 열거한다. `b >= N-M+1` 이면 다음 행으로 넘기는
조건 덕분에 M칸이 안 들어가는 자리는 시작칸에서 자연히 빠진다 — 김준수 풀이가 걸린 경계 문제
두 개(딱 붙은 배치 누락, M칸 미만 자리를 시작칸으로 셈)가 여기엔 없다.

벌통 하나의 최대 수익은 백트래킹으로 구하는데, **`honeyCrop[i] + cnt > C` 이면 아예 들어가지 않는
가지치기**와 "M개를 다 못 골라도 지금까지의 수익을 기록" 하는 처리가 맞다. 큰 벌통을 다 담는 탐욕이
아니라 부분집합을 전부 보는 쪽을 고른 것이 이 문제의 핵심이고, 그 판단이 정확하다.

**실제로 돌려본 검증**: 완전탐색(부분집합 비트마스크 + 구간 겹침 판정)과 무작위 1,000건
(`N ≤ 8`, `M ≤ 5`, `C ≤ 30`, 벌통 0~9)을 대조해 **불일치 0건**. 최대 입력(`N=10, M=5`) 50케이스는
0.14초에 끝난다.

## 개선점

### 1. (치명) `package com.ssafy.swb;` + `public class SWEA2115` 로는 SWEA 에 제출이 안 된다 — <dead-code>

SWEA 는 default package 에 `public class Solution` 을 요구한다. 로컬 `javac` 는 클래스명에 맞춘 임시
파일로 돌리기 때문에 통과하지만, 채점 서버에서는 로직과 무관하게 컴파일 단계에서 떨어진다.
`SWEA1953`, `SWEA2105` 리뷰에서도 같은 지적이 있었다 — 6주차에서 계속 반복되는 패턴이니
**제출 직전에 맨 위 두 줄만 확인하는 습관**을 들이면 끝난다.

```java
// package com.ssafy.swb;   ← 지운다
public class Solution {     // SWEA2115 → Solution
```

### 2. (사소) 같은 자리의 최대 수익을 N² 번씩 다시 계산한다 — <redundant-loop>

첫 일꾼 자리 하나마다 뒤쪽 모든 자리에서 `dfs` 를 새로 돌린다. 자리 수가 `N(N-M+1)` 이므로
각 자리의 `dfs` 가 최대 그 횟수만큼 반복된다. 게다가 `dfs` 가 `visited` 로 **순서를 구분하는 순열
탐색**이라 같은 부분집합을 `k!` 번씩 다시 본다(M=5 이면 부분집합 32개를 326번 방문).

N ≤ 10, M ≤ 5 라 시간은 전혀 문제가 안 된다. 다만 "자리별 최대 수익을 한 번만 구해 표에 넣고,
조합은 표만 더한다" 로 나누면 코드가 두 단계로 갈려 읽기도 쉽다. 바뀌는 부분만:

```java
int[][] best = new int[N][N];
for (int i = 0; i < N; i++)
    for (int j = 0; j + M <= N; j++) {
        answer = 0;
        honeyCrop = Arrays.copyOfRange(honeyContainer[i], j, j + M);
        dfs(0, 0, 0, new boolean[M]);
        best[i][j] = answer;
    }
// 이후 while 루프 안에서는 dfs 대신 best[a][b] 만 더한다
maximum = Math.max(maximum, best[i][j] + best[a][b]);
```

순열을 부분집합으로 바꾸려면 `visited` 대신 "idx 번째 칸을 담는다 / 안 담는다" 두 갈래로 내려가면
된다(`dfs(idx+1, cnt+v, earning+v*v)` / `dfs(idx+1, cnt, earning)`). 같은 주차 안찬웅 풀이가 그 형태다.
**검증 안 함** — 위 코드는 구조 설명용이고 실제로 돌려보지는 않았다. 원본이 이미 정답이므로
고치지 않아도 된다.

## 복잡도

- 시간: `O(N⁴ · M!)` — 배치 쌍 `N²(N-M+1)²/2` 마다 순열 백트래킹. N ≤ 10, M ≤ 5 라 케이스당 60만 번 안쪽
- 공간: `O(N² + M)` — 벌통 판과 백트래킹 보조 배열

## 요약

겹치지 않는 두 자리를 "같은 행이면 `j+M` 부터, 아니면 다음 행부터" 로 열거한 것이 정확하고,
벌통 하나의 수익도 가지치기 붙인 백트래킹으로 맞게 구한다 — **무작위 1,000건 전부 정답이다.**
남은 건 알고리즘 밖의 제출 형식 하나(`package` + 클래스명)뿐이고, 수익을 자리마다 다시 계산하는 건
N 이 작아 실전에서는 영향이 없다.
