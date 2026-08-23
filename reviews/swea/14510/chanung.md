---
platform: swea
problemId: "14510"
author: chanung
source: 안찬웅/week1/SWEA14510.java
week: 1
compiles: true
verdict: needs-fix
tags: [overflow]
complexity:
  time: O(N log(maxDay))
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 나무 높이 (swea/14510) — chanung

## 접근

"필요한 날 수"에 대한 이분탐색이라는 방향은 정확하다. `check(day)`에서 홀수 개수(odd, `+1` 필요)와
짝수날 처리 가능량(`b`)을 비교하고, `+2` 초과분을 `+1` 두 장으로 환산하는 부분까지 문제의 카드 교환
규칙을 잘 반영했다.

## 개선점

### 1. (치명) `low + high` 오버플로로 큰 입력에서 무한루프에 빠진다 — `overflow`

```java
int low = 0;
int high = Integer.MAX_VALUE;
...
int mid = (low + high) / 2;
```

`high`를 `Integer.MAX_VALUE`로 잡아둔 상태에서 탐색이 진행되다가 `low`가 커지면
`low + high`가 `int` 범위를 넘어 음수로 오버플로된다. `mid`가 음수가 되면 `check(mid)`의
`b = day/2`, `a = day-b`도 음수가 되고, `if(odd > a) return false;`가 항상 걸려 `check`가 계속
`false`를 반환한다 — 그 결과 `low`가 음수 쪽으로 잘못 움직여 탐색 구간 갱신이 깨진다.

실제로 돌려서 확인한 반례: `n=2`, 나무 높이 `[1, 2000000000]` (두 번째 나무가 최댓값)을 넣으면
20초 타임아웃까지 응답이 없었다(무한루프). 같은 입력을 `mid = low + (high - low) / 2`로만 바꾼
사본에서는 즉시 `1333333333`을 반환했다 — 오버플로가 원인임을 확인했다.

다만 높이 값이 1,000,000,000(1e9) 정도로 작을 때는 문제없이 빠르게 정답을 낸다
(`n=2`, `[1, 1000000000]` → `666666666`, 즉시 반환). `high = Integer.MAX_VALUE`로 잡아
설계상 그보다 큰 차이도 받아들이려 한 것으로 보이는데, 이 문제의 실제 높이 제약이 1e9 안쪽이면
현재 테스트 데이터에서는 안 걸릴 수도 있다 — 다만 그 경우여도 `high`를 `Integer.MAX_VALUE`로 둘
이유가 없으므로, 아래처럼 오버플로에 안전한 형태로 바꾸는 게 맞다.

```java
int mid = low + (high - low) / 2;
```

## 복잡도

- 시간: `O(N log(maxDay))` — 이분탐색 각 단계에서 배열을 한 번 순회.
- 공간: `O(N)` — `d[]` 배열.

## 요약

이분탐색 대상과 `check` 로직 자체는 올바르다. 다만 `mid` 계산에 고전적인 오버플로 버그가 있고,
`high = Integer.MAX_VALUE` 근처까지 몰리면 실제로 무한루프로 재현된다 — 실측으로 확인했다.
`low + (high-low)/2`로 바꾸면 같은 입력에서 즉시 정답을 낸다는 것도 확인했다.
