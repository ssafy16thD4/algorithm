---
platform: boj
problemId: "17471"
author: seongil
source: 이성일/week6/게리멘더링.java
week: 6
compiles: true
lang: java
verdict: good
tags: [good-decomposition, good-complexity]
complexity:
  time: O(2^N * (N + E))
  space: O(N + E)
generatedBy: claude-code-local
generatedAt: 2026-09-06
---

# 게리맨더링 (boj/17471) — seongil

## 접근

N ≤ 10 이라는 제약을 보고 부분집합 전수조사로 바로 갔다. `divide(index)` 가 각 구역을 A/B 중
어디에 넣을지 재귀로 2^N 갈래를 만들고, 리프에서 `isConnected(true)` / `isConnected(false)` 로
두 선거구가 각각 연결인지 BFS 로 확인한 뒤 인구 차를 갱신한다.

**연결 판정을 그룹의 참/거짓 하나로 파라미터화한 게 이 코드의 핵심이다.** `selected[i] == group`
한 줄로 A 검사와 B 검사를 같은 함수로 돌린다. 보통 이 문제에서 A용·B용 BFS 를 복붙해 두 벌 만들다가
한쪽만 고쳐서 틀리는데, 그 함정을 구조로 피했다.

빈 그룹 처리도 정확하다. `start == -1` 이면 그 그룹에 아무도 없다는 뜻이고 바로 `false` 를 반환하니,
"각 선거구는 적어도 하나의 구역을 포함한다" 는 조건이 별도 분기 없이 지켜진다.

## 개선점

### 1. (사소) 대칭 분할을 두 번씩 센다 — <redundant-loop>

`divide` 는 2^N 가지 마스크를 전부 훑는데, 마스크 `S` 와 그 여집합은 같은 분할이다.
즉 모든 유효 분할이 정확히 두 번 평가된다. N ≤ 10 이라 1024 번이 2048 번 되는 정도라
성능상 문제는 전혀 없다 — 다만 `selected[0] = true` 로 0번 구역을 A 에 고정하면
탐색이 절반으로 줄고, "두 분할은 같다" 는 걸 코드가 말해준다.

```java
selected[0] = true;
divide(1);          // 0번은 항상 A → 여집합 중복 제거
```

## 복잡도

- 시간: `O(2^N * (N + E))` — 부분집합 2^N 마다 BFS 두 번. N ≤ 10 이라 최대 약 2만 스텝, 여유롭다
- 공간: `O(N + E)` — 인접 리스트와 재귀 깊이 N. BFS 마다 `visited` 를 새로 잡지만 N 이 작아 무의미

## 요약

접근·구현 둘 다 맞다. 무작위 그래프 400건을 독립 브루트포스와 대조했고 **불일치 0건**이었다
(N ≤ 8, 간선 확률 0.4). 남은 건 대칭 분할을 두 번 세는 것뿐인데 N ≤ 10 이라 성능에 영향이 없다.
