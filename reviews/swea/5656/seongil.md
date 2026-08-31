---
platform: swea
problemId: "5656"
author: seongil
source: 이성일/week5/SWEA5656.java
week: 5
compiles: true
verdict: good
tags: [good-decomposition, redundant-loop]
complexity:
  time: O(W^N × H × W)
  space: O(N × H × W)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 벽돌 깨기 (swea/5656) — seongil

## 접근

`dfs(구슬번호, board)` 로 **어느 열에 떨어뜨릴지** N개를 전부 시도하고, 단계마다 board를 복사해 갈래끼리 간섭이 없게 했다. 이 문제의 세 조각이 각각 함수로 분리되어 있다 — `build`(연쇄 폭발), `clean`(중력), `countBrick`(집계). 시뮬레이션 문제에서 이 분리가 되어 있으면 디버깅이 훨씬 쉬워진다.

**연쇄 폭발을 큐로 처리한 것이 정확하다.** 숫자 `k` 인 벽돌은 상하좌우로 `k-1` 칸까지 부수는데, 그 범위 안에 다시 `2` 이상인 벽돌이 있으면 그것도 터져야 한다. 값이 1인 칸은 즉시 `0` 으로 지우고, `2` 이상인 칸만 큐에 넣어 나중에 자기 범위를 펼치게 한 구분이 맞다:

```java
if (temp[i][y] > 1) deq.offerLast(new int[] {i, y});
else { temp[i][y] = 0; visited[i][y] = true; }
```

`clean` 의 중력도 맞다. 아래에서부터 처음 만나는 빈칸을 `floor` 로 잡고, 그 위의 벽돌을 차례로 내리면서 `floor--` 하는 방식이라 순서가 뒤엉키지 않는다.

빈 열을 골랐을 때(`flag == false`) 판이 변하지 않은 채 구슬만 소모하도록 `dfs(n + 1, board)` 를 호출하는 처리도 빠뜨리지 않았다.

## 개선점

### 1. (중요) 빈 열이 여러 개면 똑같은 탐색을 그만큼 반복한다 — `redundant-loop`

```java
for (int i = 0; i < W; i++) {
    ...
    if (!flag) {
        dfs(n + 1, board);     // for 루프 안에 있다
    }
}
```

`if (!flag)` 가 열 루프 **안에** 있어서, 비어 있는 열마다 한 번씩 `dfs(n + 1, board)` 를 호출한다. 그런데 빈 열은 어느 것을 골라도 결과가 완전히 같다 — **같은 상태를 빈 열 개수만큼 중복 탐색**하는 셈이다.

판이 거의 비워진 뒤에 특히 나쁘다. 모든 열이 비면 한 단계에서 `W`번 갈라지고, 남은 구슬 수만큼 `W^남은구슬` 로 불어난다.

빈 열 처리는 루프 밖에서 한 번만 하면 된다:

```java
boolean anyBrick = false;
for (int i = 0; i < W; i++) {
    int r = 첫 벽돌 행(i);
    if (r == -1) continue;          // 빈 열은 건너뛴다
    anyBrick = true;
    int[][] temp = copy(board);
    build(r, i, temp);  clean(temp);
    dfs(n + 1, temp);
}
if (!anyBrick) {                     // 판 전체가 비었을 때만, 한 번
    answer = Math.min(answer, 0);
    return;
}
```

### 2. (중요) 판이 다 비면 더 볼 필요가 없다 — `redundant-loop`

`answer` 가 0이 되면 그보다 작아질 수 없으므로 즉시 끝낼 수 있다. `dfs` 첫 줄에 한 줄이면 된다:

```java
if (answer == 0) return;
```

1번과 함께 적용하면 판이 일찍 비워지는 입력에서 탐색이 급격히 줄어든다.

### 3. (사소) board 복사를 2중 루프로 한다 — `redundant-loop`

```java
int[][] temp = new int[H][W];
for (int x = 0; x < H; x++)
    for (int y = 0; y < W; y++)
        temp[x][y] = board[x][y];
```

행 단위 `clone()` 이 더 짧고 빠르다:

```java
int[][] temp = new int[H][];
for (int x = 0; x < H; x++) temp[x] = board[x].clone();
```

### 4. (사소) `build` 에서 같은 칸이 큐에 두 번 들어갈 수 있다 — `redundant-loop`

`visited` 를 `offer` 시점이 아니라 `poll` 시점에 찍기 때문에, 값이 2 이상인 칸이 서로 다른 폭발 범위에 겹치면 큐에 중복으로 들어간다. 두 번째로 꺼낼 때는 `temp[x][y]` 가 이미 0이라 범위가 빈 구간이 되어 **결과는 바뀌지 않는다** — 낭비일 뿐이다. `offerLast` 직전에 `visited` 를 찍으면 없어진다.

## 복잡도

- 시간: `O(W^N × H × W)` — 구슬마다 `W`개 열, 리프마다 복사·폭발·중력이 `H × W`. N ≤ 4, W ≤ 12라 통과한다.
- 공간: `O(N × H × W)` — 재귀 깊이만큼 복사본이 살아 있다.

## 요약

폭발·중력·집계를 각각 함수로 떼어낸 구조가 좋고, 연쇄 폭발에서 값 1과 2 이상을 구분해 큐에 넣는 처리도 정확하다. 로직상 결함은 보이지 않는다. 다듬을 곳은 탐색 낭비 쪽인데, 특히 **빈 열 처리가 열 루프 안에 있어 같은 상태를 여러 번 탐색하는 것**이 제일 크다 — 루프 밖으로 한 번만 빼면 된다.
