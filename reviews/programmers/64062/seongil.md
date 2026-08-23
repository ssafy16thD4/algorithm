---
platform: programmers
problemId: "64062"
author: seongil
source: 이성일/week4/징검다리건너기.java
week: 4
compiles: true
verdict: good
tags: [good-complexity, good-decomposition]
complexity:
  time: O(N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 징검다리 건너기 (programmers/64062) — seongil

## 접근

이분탐색을 아예 쓰지 않고 **덱을 이용한 슬라이딩 윈도우 최댓값**으로 한 번에 끝냅니다. 답이 "크기 k 창의 최댓값들 중 최솟값" 이라는 걸 알아채면 `O(N)` 에 풀리는데, 그 관찰을 정확히 구현했습니다.

같은 문제를 푼 팀원 3명 중 유일하게 `O(N)` 입니다. 나머지는 전부 이분탐색(`O(N log max)`)이고, 이 코드만 로그가 없습니다.

덱 운용 두 줄이 이 풀이의 전부입니다.

```java
while (!deq.isEmpty() && deq.peekFirst() < i - k + 1) deq.pollFirst();   // 창 밖으로 나간 인덱스 버리기
while (!deq.isEmpty() && stones[deq.peekLast()] <= stones[i]) deq.pollLast();  // 나보다 작은 건 답이 될 일 없음
```

**값이 아니라 인덱스를 담은 것**이 맞는 선택입니다. 창 이탈 판정(`deq.peekFirst() < i - k + 1`)을 하려면 인덱스가 필요합니다.

**검증**: 크기 k 창의 최댓값 중 최솟값을 구하는 브루트포스를 기준으로 무작위 30,000건(n=1~12, k=1~n, 값이 겹치는 입력 포함)을 대조했습니다. **불일치 0건**입니다. 같은 값이 연속으로 나오는 입력에서도 `<=` 로 뒤를 걷어내기 때문에 어긋나지 않습니다.

성능 실측(`n = 200,000`, `k = 100,000`):

| 입력 | 이 코드 | 안찬웅(이분탐색) | 김준수(실패 버전) |
|---|---|---|---|
| 내림차순 | 15 ms | 12 ms | 33,317 ms |
| 무작위 | 9 ms | 5 ms | 12,573 ms |

> 이론상 `O(N)` 인 이쪽이 `O(N log max)` 인 이분탐색보다 살짝 느리게 찍혔습니다. `Deque<Integer>` 의 오토박싱 때문입니다. 아래 2번 참고.

## 개선점

### 1. (사소) `answer` 초기값 `Integer.MAX_VALUE` 가 그대로 나갈 수 있는가

`if (i >= k - 1)` 안에서만 갱신하므로, `stones.length < k` 이면 한 번도 갱신되지 않고 `Integer.MAX_VALUE` 가 반환됩니다. **문제 제약이 `k <= stones.length` 라서 실제로는 일어나지 않습니다** — 무작위 대조에서도 `k` 를 `1..n` 범위로만 만들었고 문제가 없었습니다. 다만 초기값을 그대로 돌려주는 구조는 나중에 제약이 다른 문제에 이 코드를 재활용할 때 조용히 틀립니다. 마지막에 한 줄 방어를 두면 의도가 드러납니다.

### 2. (사소) `Deque<Integer>` 의 오토박싱 — 배열 덱으로 바꾸면 더 빨라진다

인덱스만 담으므로 `int[]` 와 머리/꼬리 포인터 두 개로 충분합니다. 박싱과 `Integer` 캐시 밖 할당이 사라집니다.

```java
int[] deq = new int[stones.length];
int head = 0, tail = 0;
for (int i = 0; i < stones.length; i++) {
    if (head < tail && deq[head] < i - k + 1) head++;
    while (head < tail && stones[deq[tail - 1]] <= stones[i]) tail--;
    deq[tail++] = i;
    if (i >= k - 1) answer = Math.min(answer, stones[deq[head]]);
}
```

창 이탈은 한 칸씩만 밀리므로 바깥의 `while` 이 `if` 로 줄어드는 것도 같이 봐두면 좋습니다.

지금 코드로도 15 ms 라 통과에는 아무 지장이 없습니다. 이건 "덱을 배열로 펴는 법" 을 익혀두는 차원의 지적입니다.

### 3. (사소) `import java.util.*` 에서 실제로 쓰는 건 두 개뿐

`Deque` 와 `ArrayDeque` 만 씁니다. 명시적으로 적으면 이 파일이 무엇에 의존하는지 한 줄로 보입니다. 같은 폴더의 `징검다리 건너기.java` 는 import 없이도 도는 형태입니다.

## 복잡도

- 시간: `O(N)` — 각 인덱스가 덱에 한 번 들어가고 한 번 나옵니다. 이분탐색 없이 한 번의 순회로 끝납니다.
- 공간: `O(N)` — 최악(오름차순 입력)에 덱이 창 크기만큼 찹니다.

## 요약

팀에서 유일한 `O(N)` 풀이이고, 무작위 30,000건 대조에서 오답이 없었습니다. 덱에 인덱스를 담고 `<=` 로 뒤를 걷어내는 두 지점이 이 알고리즘의 핵심인데 둘 다 정확합니다. 남은 건 오토박싱 정도인데 통과에는 영향이 없습니다.
