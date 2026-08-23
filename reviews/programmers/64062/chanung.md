---
platform: programmers
problemId: "64062"
author: chanung
source: 안찬웅/week4/징검다리 건너기.java
week: 4
compiles: true
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(N log(max))
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 징검다리 건너기 (programmers/64062) — chanung

## 접근

인원 수를 이분탐색하고, 각 후보에 대해 "죽은 돌이 k개 연속으로 나오는가"를 한 번의 선형 순회로 판정합니다. 판정 함수가 `O(N)` 이라 전체가 `O(N log(max))` 로 떨어집니다.

이 문제에서 가장 흔한 실패가 **판정 함수를 `O(N·k)` 로 짜는 것**인데(같은 문제의 `김준수/week4/징검다리 건너기 실패.java` 가 그 경우입니다), 여기서는 `cnt` 하나로 연속 개수를 세고 `k` 에 도달하면 즉시 `break` 합니다. 배열 복사도 하지 않고 `stones[i] < mid` 로 원본을 그대로 읽습니다 — 군더더기가 없습니다.

**검증**: 크기 k 창의 최댓값 중 최솟값을 구하는 브루트포스를 기준으로 무작위 30,000건(n=1~12, k=1~n, 값이 겹치는 입력 포함)을 대조했습니다. **불일치 0건**입니다.

성능도 실측했습니다. `n = 200,000`, `k = 100,000`.

| 입력 | 이 코드 | 이성일(덱) | 김준수(실패 버전) |
|---|---|---|---|
| 내림차순 | **12 ms** | 15 ms | 33,317 ms |
| 무작위 | **5 ms** | 9 ms | 12,573 ms |

## 개선점

### 1. (사소) `person` 변수 없이 `left` 를 그대로 반환할 수 있다

지금은 성공할 때마다 `person = mid` 로 따로 담아두는데, 이 이분탐색은 끝났을 때 `left` 가 "마지막으로 성공한 값 + 1" 이므로 `left - 1` 이 곧 답입니다. `right` 도 같은 값입니다.

```java
while (left <= right) {
    int mid = (left + right) / 2;
    if (canCross(stones, k, mid)) left = mid + 1;
    else right = mid - 1;
}
return right;      // == person
```

`person` 을 쓰는 지금 방식이 틀린 건 아니고, 오히려 "무엇을 반환하는지"가 눈에 보인다는 장점이 있습니다. 이분탐색 경계에 자신이 없다면 지금 형태를 유지해도 됩니다.

### 2. (사소) 판정부를 메서드로 빼면 이분탐색 뼈대만 남는다

지금은 `while` 안에 이분탐색과 판정이 같이 들어 있어 14줄입니다. 판정을 분리하면 각각이 한눈에 들어옵니다.

```java
private boolean canCross(int[] stones, int k, int people) {
    int dead = 0;
    for (int s : stones) {
        dead = (s < people) ? dead + 1 : 0;
        if (dead >= k) return false;
    }
    return true;
}
```

### 3. 참고 — `right` 초기값

`right` 를 `max(stones)` 로 잡은 것은 맞습니다. 답이 그보다 클 수 없기 때문입니다. 문제 제약상 `stones[i] <= 2×10^8` 이라 `int` 로 충분하고, `(left + right) / 2` 도 최대 `2×10^8` 이라 오버플로가 나지 않습니다. 값이 `10^9` 를 넘는 문제였다면 `left + (right - left) / 2` 로 바꿔야 합니다.

## 복잡도

- 시간: `O(N log(max))` — `log(2×10^8) ≈ 28` 회의 이분탐색 × `O(N)` 판정. 실측 5~12 ms.
- 공간: `O(1)` — 배열을 복사하지 않고 원본을 읽습니다.

## 요약

이 문제의 정석 형태입니다. 판정 함수를 `O(N)` 으로 유지한 것이 핵심이고, 무작위 30,000건 대조에서도 오답이 없었습니다. 지적은 전부 취향 수준이라 지금 그대로 둬도 됩니다.
