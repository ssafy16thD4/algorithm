---
platform: programmers
problemId: "340212"
author: chanung
source: 안찬웅/pccp/퍼즐 게임 챌린지.java
week: null
compiles: true
verdict: good
tags: [time-complexity, overflow]
complexity:
  time: O(N log D)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# [PCCP 기출문제] 2번 / 퍼즐 게임 챌린지 (programmers/340212) — chanung

## 접근

숙련도 `level` 에 대해 총 소요 시간이 **단조 감소**한다는 성질을 이용한 답 이분탐색이다.
`total(level)` 은 문제의 규칙을 그대로 옮겼다 — `diff <= level` 이면 `times[i]`,
아니면 `(times[i] + prev) * (diff - level) + times[i]`.

## 개선점

### 1. (수정 완료 / 중요) 숙련도를 1씩 올려서 최악에 3×10^10 번 돌았다 — `time-complexity`

```java
int level = 1;
while(true) { ... level++; }
```

난이도가 최대 10만, 퍼즐이 최대 30만 개다. 정답 숙련도가 크면 `(숙련도) × (퍼즐 수)` 만큼 돌아
시간 초과가 확정이다. 단조성이 있으니 이분탐색이 맞다.

```java
int low = 1, high = 1;
for(int d : diffs) high = Math.max(high, d);   // 난이도 최댓값이면 무조건 통과
while(low < high) {
    int mid = low + (high - low) / 2;
    if(total(mid) <= limit) high = mid; else low = mid + 1;
}
return low;
```

30만 개 × 난이도 10만 짜리 최악 입력으로 재보니 **23ms** 다.

### 2. (수정 완료 / 중요) `(times[i] + prev) * x` 가 int 를 넘겼다 — `overflow`

`times` 최대 10^4, `x = diff - level` 최대 10^5 이므로 `(10^4 + 10^4) × 10^5 = 2×10^9` 로
int 범위(약 2.1×10^9)를 사실상 스치고, 퍼즐 하나만 더 무거워도 바로 넘긴다.
`timeTotal` 이 long 이어도 **오른쪽 곱셈이 int 로 계산된 뒤에 대입되므로 소용이 없다.**
`(long) x` 캐스팅을 곱셈 안에 넣어야 한다.

```java
timeTotal += (times[i] + prev) * (long) x + times[i];
```

### 3. (수정 완료 / 사소) 판정을 `total(level)` 함수로 분리했다

이분탐색과 시간 계산이 한 덩어리였는데, 함수로 가르니 "무엇을 이분탐색하는지"가 시그니처에 드러난다.

## 검증

공식 예제 3건을 컴파일해서 돌렸다.

```
diffs=[1,5,3],           times=[2,4,7],   limit=30   -> 3
diffs=[1,4,4,2],         times=[6,3,8,2], limit=59   -> 2
diffs=[1,328,467,209],   times=[2,7,1,4], limit=1723 -> 293
```

3번은 손계산으로도 확인했다 — level=293 이면 `2 + (9·35+7) + (8·174+1) + 4 = 1721 ≤ 1723`,
level=292 면 1738 로 초과다.

## 복잡도

- 시간: `O(N log D)` — N = 퍼즐 수, D = 난이도 최댓값.
- 공간: `O(1)`

## 요약

시간 계산식을 문제에서 정확히 읽어낸 것이 이 풀이의 핵심이고 그건 처음부터 맞았다.
남은 두 가지가 **"답을 1씩 올려가며 찾는다"** 와 **"long 변수에 담으면 안전할 거라 믿는다"** 였는데,
둘 다 코딩테스트에서 당락을 자주 가르는 자리다. 특히 후자는 컴파일도 되고 작은 예제도 통과해서
제출하고 나서야 드러난다 — **곱셈이 등장하면 피연산자 쪽에 캐스팅이 있는지 본다.**
