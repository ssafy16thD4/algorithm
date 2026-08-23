---
platform: programmers
problemId: "64062"
author: seongil
source: 이성일/week4/징검다리 건너기.java
week: 4
compiles: true
verdict: good
tags: [good-complexity, naming]
complexity:
  time: O(N log(max))
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 징검다리 건너기 (programmers/64062) — seongil (대안 버전)

## 접근

같은 문제를 **이분탐색으로 다시 푼 판본**입니다. 대표 풀이(`이성일/week4/징검다리건너기.java`)는 덱 슬라이딩 윈도우 `O(N)` 이고, 이쪽은 `O(N log max)` 입니다. 두 접근을 다 짜본 것 자체가 좋습니다 — 사이트 문제 상세에서 `이전 코드 보기` 로 나란히 볼 수 있습니다.

탐색 구간을 `[min(stones), max(stones)]` 로 잡은 게 눈에 띕니다. 하한을 0이 아니라 최솟값으로 올린 건 맞는 최적화입니다 — 인원이 `min(stones)` 이면 어떤 돌도 죽지 않으므로 항상 건널 수 있고, 따라서 답은 반드시 `min` 이상입니다.

**검증**: 크기 k 창의 최댓값 중 최솟값을 구하는 브루트포스를 기준으로 무작위 30,000건(n=1~12, k=1~n)을 대조했습니다. **불일치 0건**입니다. `return end` 로 마지막 성공값을 돌려주는 부분도 어긋나지 않았습니다.

성능 실측(`n = 200,000`, `k = 100,000`): 내림차순 **11 ms**, 무작위 **6 ms**. 대표 풀이(덱)의 15 ms / 9 ms 보다 오히려 빠릅니다 — 덱 쪽의 `Deque<Integer>` 오토박싱 때문입니다.

## 개선점

### 1. (사소) `retry` 라는 이름이 무엇을 뜻하는지 드러나지 않는다 — naming

```java
boolean retry = false;
...
if (streak == k) {
    // 현재 숫자보다 작은 범위를 탐색 해야한다.
    retry = true;
    break;
}
```

이 변수는 "다시 시도" 가 아니라 **"이 인원으로는 못 건넌다"** 는 뜻입니다. 주석이 그걸 설명하고 있는데, 이름을 `cannotCross` 나 `blocked` 로 바꾸면 주석 없이도 읽힙니다.

```java
boolean blocked = false;
...
if (blocked) { end = mid - 1; continue; }
start = mid + 1;
```

### 2. (사소) `minimum` 초기값 `200000001` 은 설명이 필요한 상수다 — magic number

문제 제약 `stones[i] <= 2×10^8` 에서 온 값인데, 코드만 봐서는 알 수 없습니다. `Integer.MAX_VALUE` 를 쓰면 제약이 바뀌어도 안 깨지고 의도도 분명합니다. 어차피 첫 원소에서 갱신되므로 성능 차이는 없습니다.

### 3. (사소) 판정 루프를 메서드로 빼면 이분탐색 뼈대만 남는다

`bSearch` 안에 이분탐색과 "k개 연속 판정" 이 같이 들어 있어 25줄입니다. 판정을 떼면 각각 5줄, 8줄이 됩니다.

```java
static boolean canCross(int[] stones, int k, int people) {
    int streak = 0;
    for (int s : stones) {
        streak = (s < people) ? streak + 1 : 0;
        if (streak >= k) return false;
    }
    return true;
}
```

`streak == k` 대신 `streak >= k` 로 두면 `break` 를 놓쳐도 안전합니다.

### 4. 참고 — `min` 을 하한으로 쓰는 게 왜 안전한가

`stones[i] < mid` 로 죽음을 판정하므로, `mid = min(stones)` 일 때는 어떤 원소도 `mid` 보다 작지 않아 `streak` 이 0을 유지합니다. 즉 하한은 항상 성공하고, `return end` 가 하한 아래로 내려갈 일이 없습니다. 무작위 대조 30,000건이 이걸 뒷받침합니다.

## 복잡도

- 시간: `O(N log(max - min))` — 탐색 폭을 `min` 만큼 줄였지만 최악(값이 넓게 퍼진 입력)에는 여전히 `log(2×10^8) ≈ 28` 회입니다. 실측 6~11 ms.
- 공간: `O(1)` — 배열 복사가 없습니다.

## 요약

같은 문제를 두 번째 접근으로 푼 판본이고, 무작위 30,000건에서 정확했습니다. 하한을 `min(stones)` 로 올린 판단이 좋고, 실측으로는 대표 풀이(덱)보다 빠릅니다. 지적은 이름(`retry`)과 매직 상수(`200000001`) 정도로 전부 가독성 문제입니다.
