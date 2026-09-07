---
platform: programmers
problemId: "42895"
author: chanung
source: 안찬웅/week3/N으로 표현.java
week: 3
compiles: true
verdict: good
tags: [overflow, good-complexity]
complexity:
  time: O(8 · |dp|^2)
  space: O(|dp|)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# N으로 표현 (programmers/42895) — chanung

## 접근

`dp.get(i)` = "N 을 i 번 써서 만들 수 있는 수의 집합" 으로 잡고,
`dp.get(i) = { N 을 i개 이어붙인 수 } ∪ { dp.get(j) (op) dp.get(i-j) }` 로 채운다.
집합 분할 DP 의 정석이고, 답이 8 이하라는 제약을 그대로 루프 상한으로 쓴 것도 맞다.

## 개선점

### 1. (수정 완료 / 중요) 곱셈이 int 를 넘겼다 — `overflow`

`dp.get(i).add(num1 * num2)` 가 int 곱이었다. 이어붙인 수(최대 99999999)끼리 곱하면 바로 넘치고,
넘친 값은 **음수나 엉뚱한 양수**가 되어 집합에 그대로 들어간다. 그 쓰레기 값이 다음 단계 연산에
다시 쓰이므로 오답이 나올 수 있다. long 으로 계산한 뒤 범위 검사 후 담도록 `add` 헬퍼로 뺐다.

```java
static void add(Set<Integer> set, long v) {
    if (v > 0 && v <= LIMIT) set.add((int) v);   // LIMIT = 100000000
}
```

### 2. (수정 완료 / 중요) 쓸모없는 값이 집합에 계속 쌓였다 — `space-complexity`

음수·0 은 답이 될 수 없고(`number` 는 1 이상), 상한도 없어서 집합이 단계마다 부풀었다.
지금은 `1 ~ 1억` 만 담는다.

> **상한을 32000(= number 의 최댓값)으로 잡지 않은 이유**가 있다. `55555 / 5 = 11111` 처럼
> **큰 수를 나눠서 목표를 만드는 경로**가 있어서, 32000 에서 자르면 그 길이 막힌다.
> N 을 8개 이어붙인 수(99999999)까지는 살려야 안전하다. 실제로 `N=5, number=11111` 이
> 이 경로로 6 이 나오는 것을 돌려서 확인했다.

### 3. (수정 완료 / 사소) 이어붙인 수를 만드는 줄이 한 번 더 돌았다

`new StringBuilder().append(N)` 로 한 번 넣고 루프에서 `j < i` 만큼 또 넣어서 자리수가 하나 많았다
(`i=2` 일 때 `NNN`). `repeat = repeat * 10 + N` 을 i 번 도는 정수 연산으로 바꿔 파싱도 없앴다.

## 검증

```
N=5, number=12    -> 4  (공식 예제, 기대 일치)
N=2, number=11    -> 3  (공식 예제, 기대 일치)
N=5, number=11111 -> 6  (55555/5 경로. 상한을 32000 으로 잡으면 안 나온다)
N=3, number=1     -> 2  (3/3)
N=9, number=32000 -> -1 (8개로 불가)
```

가장 오래 걸린 케이스가 10ms 다. 상한을 1억으로 늘려도 8단계에서 터지지 않는다.

## 복잡도

- 시간: `O(8 · |dp|^2)` — 단계마다 두 집합의 곱집합을 훑는다. 상한 필터가 `|dp|` 를 눌러준다.
- 공간: `O(|dp|)`

## 요약

DP 정의와 분할 방식이 정확해서 뼈대는 그대로 두고 산술만 안전하게 만들면 되는 코드였다.
**"집합에 담기 전에 범위를 검사한다"** 를 `add` 하나로 몰아넣으면 네 종류 연산에 같은 규칙이
자동으로 걸린다 — 연산마다 if 를 붙였으면 하나쯤 빠뜨렸을 것이다.
