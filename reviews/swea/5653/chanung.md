---
platform: swea
problemId: "5653"
author: chanung
source: 안찬웅/week5/줄기세포배양.java
week: 5
compiles: true
lang: java
verdict: good
tags: [naming, magic-branch, space-complexity]
complexity:
  time: O(K · 활성세포수 log)
  space: O((N+2K)^2)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 줄기세포배양 (swea/5653) — chanung

## 접근

세포마다 "생명력만큼 비활성 → 활성화되어 같은 시간만큼 번식 → 사망" 이라는 상태 전이를
시간 순서대로 처리한다. 배열을 중앙에 두고 사방으로 K 만큼 여유를 준 좌표계로 잡아 경계 검사를
없앤 것도 이 문제에서 자주 쓰는 정석이다.

## 개선점

### 1. (해결 / 사소) `compiles` 판정이 false 로 남아 있었다 — `naming`

파일명이 `줄기세포배양.java` 인데 안에 `public class SWEA5653` 이 있어서, 예전 도구가 제자리에서
`javac` 를 돌리면 `class X is public, should be declared in a file named X.java` 로 실패했다.
**코드 문제가 아니라 검사 방식 문제였다.** 지금은 `resolve.mjs` 가 `public class` 이름에 맞춘 임시
파일로 복사해 컴파일하므로 `compiles: true` 로 정정한다.

다만 제출 관점의 지적은 그대로 유효하다 — **SWEA 는 default package 의 `public class Solution`** 을
요구한다. 제출할 때 클래스명을 `Solution` 으로 바꿔야 한다.

### 2. (사소) `time + 1 > k` 검사를 루프 조건으로 올릴 수 있다 — `magic-branch`

루프 안에서 다음 시간이 K 를 넘는지 보는 대신 `for(int time = 0; time < k; time++)` 로 두면
종료 조건이 한 곳에만 있게 된다.

### 3. (사소) `size` 가 필요한 것보다 조금 넉넉하다 — `space-complexity`

세포는 K 시간 동안 한 방향으로 최대 K 칸 퍼지므로 `N + 2K` 면 충분하다. 지금 값도 동작에는
문제가 없고 메모리도 여유가 있어 급히 고칠 것은 아니다.

## 복잡도

- 시간: `O(K · 활성세포수 log)` — 시간마다 활성 세포를 우선순위대로 처리한다.
- 공간: `O((N+2K)^2)`

## 요약

상태 전이(비활성 → 활성 → 사망)와 번식 우선순위를 정확히 다룬 풀이다. 지적할 것은 제출 형식과
사소한 정리뿐이다. `compiles: false` 는 코드 결함이 아니라 검사 도구의 오탐이었고 지금은 정정됐다.
