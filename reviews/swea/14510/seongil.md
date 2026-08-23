---
platform: swea
problemId: "14510"
author: seongil
source: 이성일/week1/SWEA14510.java
week: 1
compiles: true
verdict: good
tags: [naming]
complexity:
  time: O(N + gapMax)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 나무 높이 (swea/14510) — 이성일

## 접근

가장 큰 나무를 기준으로 각 나무가 자라야 할 총량(`gap`)을 홀수 몫(`one`)과 2단위 몫(`two`)으로 나눠 집계한 뒤, `two`를 얼마나 `one` 쪽으로 전환할지(`split`)를 0부터 `two`까지 전부 시도해서 `max(홀수 처리 일수, 짝수 처리 일수)`를 최소화하는 지점을 찾는다. 두 그룹으로 나눠 완전탐색하되 그 안에서는 결정적 공식(`2*newone-1`, `2*newtwo`)으로 답을 계산하는 구조라 로직 자체는 깔끔하다.

이 파일은 `data/problems.json`에 URL이 등록돼 있지 않아(`url: null`) 문제 지문을 확인하지 못했다. 코드와 변수명만으로 판단했고, 홀수/짝수 그룹으로 나눠 자라는 방식이라는 전제가 맞다면 로직은 일관돼 보인다. **정답 여부는 검증 못 함** — 공식 예제 입출력을 구하지 못해 실행 대조를 하지 못했다.

## 개선점

### 1. (사소) 변수명이 값의 의미를 드러내지 않음 — `naming`

`one`, `two`, `newone`, `newtwo`가 무엇을 세는 값인지 이름만으로는 알 수 없다. `oddNeedCount`, `pairNeedCount`처럼 "무엇의 개수/합"인지 드러나는 이름이었다면 위 접근 설명을 코드만 보고도 재구성할 수 있었을 것이다.

## 복잡도

- 시간: `O(N + gapMax)` — `gapMax`(= `two`의 최댓값)만큼 split 후보를 전부 순회
- 공간: `O(N)` — `trees` 리스트

## 요약

구조는 일관되고 완전탐색 부분도 결정적 공식과 잘 결합돼 있다. 다만 문제 URL이 없어 실제 예제로 정답을 대조하지 못했다는 한계가 있다 — 이름을 조금만 더 설명적으로 바꾸면 코드만으로도 검증하기 쉬워진다.
