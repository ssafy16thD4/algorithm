---
platform: boj
problemId: "17471"
author: chanung
source: 안찬웅/week6/게리맨더링 실패.java
week: 6
compiles: true
lang: java
verdict: unattempted
tags: []
complexity:
  time: O(N + E)
  space: O(N + E)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 게리맨더링 (boj/17471) — chanung (실패 버전)

## 접근

주석 그대로 "입력 받기만 완료" 상태다. 구역 수 `n`, 인구 `pop[]`, 인접 리스트 `graph[]` 까지 읽고
`main` 이 끝난다. 출력이 없으므로 알고리즘 리뷰 대상이 아니다.

## 다음에 볼 것

N ≤ 10 이라 **부분집합 전수조사**가 정답 방향이다. 구역마다 A/B 를 정하는 재귀(2^N 가지)를 돌리고,
리프에서 두 선거구가 각각 연결인지 BFS 로 확인한 뒤 인구 차 최솟값을 갱신한다.
같은 문제를 푼 `김준수/week6/게리맨더링.java`(합을 재귀 안에서 증감으로 유지) 와
`이성일/week6/게리멘더링.java`(연결 판정 하나를 A/B 파라미터로 재사용) 가 둘 다 검증된 정답이다.

제출할 때는 `package practive;` 를 지우고 `class Solution` 을 `public class Main` 으로 바꿔야 한다
(BOJ 규칙).

## 요약

미제출 스텁. 입력 파싱은 맞으니 그 위에 2^N 배치 + BFS 두 번을 얹으면 된다.
