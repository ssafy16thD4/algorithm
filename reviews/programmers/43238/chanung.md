---
platform: programmers
problemId: "43238"
author: chanung
source: 안찬웅/week2/입국심사.java
week: 2
compiles: true
verdict: good
tags: [overflow, good-readability]
complexity:
  time: O(M log(max·n))
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 입국심사 (programmers/43238) — chanung

## 접근

"시간 t 안에 n 명을 처리할 수 있나?"가 t 에 대해 단조라는 점을 이용한 답 이분탐색이다.
상한을 `가장 느린 심사관 × n` 으로 잡은 것이 정확하다 — 그보다 큰 답은 나올 수 없다.
`sum >= n` 이면 `high` 를 당기면서 `time` 에 기록하는 형태도 안전하게 최솟값을 남긴다.

## 개선점

### 1. (수정 완료 / 중요) 누적 합이 long 도 넘길 수 있었다 — `overflow`

```java
for(int x : times) sum += mid / x;   // 끊지 않으면 계속 더한다
```

`mid` 는 최대 10^9 × 10^9 = 10^18 근처까지 간다. 심사관이 1분짜리면 `mid / 1` 이 그대로 10^18 이고,
심사관이 10만 명이면 합이 10^23 이 되어 long(약 9.2×10^18)을 넘긴다. 넘치면 음수가 되고
`sum >= n` 이 거짓이 되어 **탐색이 반대쪽으로 간다.**

```java
for(int x : times) {
    sum += mid / x;
    if(sum >= n) break;   // 넘는 순간 끊는다
}
```

`n` 이상인지만 알면 되므로 넘는 순간 끊는 것으로 충분하고, 덤으로 빨라진다.

### 2. (사소) `time` 대신 `low` 를 반환할 수 있다

루프가 끝나면 `low` 가 곧 최소 답이라 별도 변수 없이 `return low;` 로 끝난다. 다만 지금 방식이
"답을 만난 순간 기록" 이라 읽기 쉬워서, 굳이 바꾸지 않아도 되는 취향 차이다.

### 3. (사소) `Arrays.sort(times)` 는 이 풀이에 필요 없다

이분탐색 판정이 전체 합이라 순서와 무관하다. 상한 계산에 최댓값이 필요할 뿐이라
`Arrays.stream(times).max()` 로 충분하다. 지금도 정렬 비용이 지배적이진 않다.

## 복잡도

- 시간: `O(M log(max·n))` — M = 심사관 수. 조기 종료 덕분에 실제로는 더 짧다.
- 공간: `O(1)`

## 요약

이분탐색의 상한 잡기와 최솟값 기록이 정확한, 뼈대가 좋은 풀이다. 유일한 위험이 "판정 함수 안의
누적 합"이었는데, 이건 **결과가 임계값을 넘는지만 알면 되는 계산에서 끝까지 다 더하는** 흔한 습관에서
나온다. 임계값이 있으면 그 자리에서 끊는다 — 정확성과 속도를 동시에 얻는다.
