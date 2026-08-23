---
platform: swea
problemId: "5656"
author: junsoo
source: 김준수/week1/SWEA5656.java
week: 1
compiles: true
verdict: needs-fix
tags: [duplicate-code, redundant-loop]
complexity:
  time: O(W^N · H·W)
  space: O(H·W)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 벽돌 깨기 (swea/5656) — junsoo

## 접근

매 턴 모든 열에 공을 던지는 경우를 완전탐색하고, 맞은 벽돌부터 큐로 십자 연쇄 제거(BFS 방식)한 뒤
중력을 적용한다. 웹에서 찾은 다른 풀이들과 대조해보니 "벽돌이 깨질 때 연쇄적으로 계속 퍼진다"는
규칙 이해가 정확하다 — `curr[2] >= 2`인 벽돌을 큐에서 꺼낼 때마다 그 벽돌 기준으로 다시 십자 범위를
검사하는 게 바로 이 연쇄 규칙이다.

**빈 열을 만나면 바로 결과를 확정하고 더 깊이 탐색하지 않는 가지치기(93~96행)** 도 실제로 정답에
영향이 없다는 걸 확인했다 — 이미 빈 열에 공을 던지는 건 공을 낭비하는 선택이라, 같은 깊이에서
다른(비어있지 않은) 열을 고르는 형제 가지들이 항상 이 가지보다 같거나 더 나은 결과를 만든다.
`Math.min`으로 전체 리프 중 최솟값을 취하기 때문에, 이 가지치기로 결과가 바뀌는 경우는 없다.
주석에 남긴 "0 0 0 1인데 공 3개" 예시로 이 가지치기의 필요성을 미리 짚어둔 것도 좋다.

`gravity()`는 아래부터 위로(`H-1`→`0`) 훑으면서 각 칸을 필요한 만큼 아래로 떨어뜨리는데,
직접 두 칸 이상 뜬 경우(연속으로 두 칸이 비어 있는 경우)까지 손으로 따라가봤을 때 정상적으로
바닥까지 쌓였다. `System.arraycopy`로 얕은 복사만 하면 안 되고 행마다 순회해야 한다는 걸 시행착오로
직접 깨달아 고쳤다는 주석도, 실제로 2차원 배열 복사에서 자주 나오는 함정을 정확히 짚었다.

## 개선점

### 1. (사소) board 백업/복구 코드가 두 곳(main, backtrack)에 그대로 중복된다 — `duplicate-code`

```java
int[][] prevBoard = new int[H][W];
for(int i = 0; i < H; i++) {
    System.arraycopy(board[i], 0, prevBoard[i], 0, W);
}
```

이 패턴이 53~56행과 134~138행에 그대로 반복되고, 복구 쪽(63~65행, 145~148행)도 마찬가지다.
`copyBoard(int[][] src)` 처럼 메서드 하나로 뽑으면 네 군데가 두 번의 메서드 호출로 줄어든다.

```java
static int[][] copyBoard() {
    int[][] copy = new int[H][W];
    for (int i = 0; i < H; i++) System.arraycopy(board[i], 0, copy[i], 0, W);
    return copy;
}
static void restoreBoard(int[][] saved) {
    for (int i = 0; i < H; i++) System.arraycopy(saved[i], 0, board[i], 0, W);
}
```

### 2. (사소) `gravity()`의 `i == H - 1` 검사가 매 열마다 반복된다 — `redundant-loop`

```java
for (int i = H - 1; i >= 0; i--) {
    for (int j = W - 1; j >= 0; j--) {
        if(i == H - 1) continue;   // i에만 의존하는 조건을 W번 반복 검사
```

이 조건은 `j`와 무관하므로 바깥 루프를 `for (int i = H - 2; i >= 0; i--)` 로 시작하면 매 열마다
반복 검사할 필요 없이 아예 없앨 수 있다. `H`, `W`가 작아 성능에 체감되는 차이는 없지만, 조건 자체가
바깥 루프의 범위로 흡수될 수 있다는 걸 보여주는 편이 코드 의도가 더 분명하다.

## 복잡도

- 시간: `O(W^N · H·W)` — 매 턴 `W`갈래로 분기하는 백트래킹이 지배적이고, 각 리프/분기마다 보드 복사와
  십자 제거·중력 계산에 `O(H·W)`가 붙는다. `data/problems.json`에 URL이 없어 `N`, `W`, `H`의 정확한
  상한은 확인 못 했다.
- 공간: `O(H·W)` — `board`, `prevBoard`, `visited` 류 배열.

## 요약

연쇄 폭발 규칙 이해, 중력 적용, 빈 열 가지치기까지 손으로 재현해봤을 때 로직에서 어긋나는 부분을
찾지 못했다. 백업/복구 코드가 두 곳에 그대로 중복돼 있고 `gravity()`의 경계 검사 하나를 루프 범위로
옮길 수 있는 정도가 남은 정리거리다.
</content>
