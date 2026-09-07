---
platform: swea
problemId: "14510"
author: chanung
source: 안찬웅/week1/SWEA14510.java
week: 1
compiles: true
lang: java
verdict: good
tags: [overflow]
complexity:
  time: O(N log(sum d))
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 나무 높이 (swea/14510) — chanung

## 접근

"날짜 d 로 가능한가?"가 d 에 대해 단조라는 점을 잡아 이분탐색으로 최소 날을 찾는다. `check(day)` 가
좋다 — 홀수날 개수 `a`(+1 카드), 짝수날 개수 `b`(+2 카드)를 세고, **홀수인 `d[i]` 는 +1 을 반드시
한 장 써야 한다**는 관찰에서 `odd > a` 를 먼저 걸러낸 뒤, 남은 양을 +2 로 채우고 모자라면
+2 한 장을 +1 두 장으로 환산한다. 문제의 제약이 세 줄 안에 그대로 들어가 있다.

## 개선점

### 1. (수정 완료 / 치명) `low + high` 오버플로로 큰 입력에서 무한루프에 빠졌다 — `overflow`

```java
int high = Integer.MAX_VALUE;
...
int mid = (low + high) / 2;   // low 가 커지면 int 를 넘겨 mid 가 음수가 된다
```

`mid` 가 음수가 되면 `check(음수)` 가 항상 false → `low = mid + 1` 로 다시 커지고, 탐색 범위가
줄지 않아 루프가 끝나지 않는다. 두 군데를 고쳤다.

```java
int high = 0;
for(int i=0; i<n; i++) high += d[i];
high = high * 2 + 2;                    // 모자란 양을 +1 카드로만 채워도 충분한 상한
...
int mid = low + (high - low) / 2;       // 넘치지 않는 중간값
```

상한 근거: 하루 평균 1.5씩 채우므로 필요한 날은 대략 `2·total/3`. `2·total + 2` 는 넉넉한 상한이다.
`(low + high) / 2` 는 이분탐색에서 가장 흔한 오버플로 지점이니 **처음부터 `low + (high-low)/2` 로
쓰는 습관**을 들이는 게 좋다.

## 검증

수정 후 실제로 돌렸다.

```
5 / 2 4 3 1 3            -> #1 5
6 / 3 1 2 3 3 1          -> #2 4
8 / 1 2 3 4 3 2 1 1      -> #3 10
```

세 케이스 모두 손계산과 일치한다. 예를 들어 1번은 d=[2,0,1,3,1], total=7, odd=3 이라
day=4(a=2)에서는 `odd > a` 로 막히고 day=5(a=3, b=2, need2=2)에서 처음 통과한다.

## 복잡도

- 시간: `O(N log(sum d))` — 이분탐색 한 번에 `check` 가 배열을 한 번 훑는다.
- 공간: `O(N)` — arr, d.

## 요약

관찰(홀수는 +1 을 반드시 쓴다 / +2 는 +1 두 장으로 바꿀 수 있다)이 정확해서 판정 함수가 짧고
정확하다. 유일한 결함이 이분탐색의 고전적인 오버플로였다. `check` 가 매번 `total`·`odd` 를
다시 세는데, 이 값들은 day 와 무관하니 루프 밖에서 한 번만 구해도 된다(성능에 큰 차이는 없다).
