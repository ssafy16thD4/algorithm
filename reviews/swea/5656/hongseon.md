---
platform: swea
problemId: "5656"
author: hongseon
source: 전홍선/week5/breakthewall.cpp
week: 5
compiles: null
lang: cpp
verdict: needs-fix
tags: [dead-code, good-decomposition, redundant-collection]
complexity:
  time: O(T·W^N·H·W)
  space: O(N·H·W)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 벽돌 깨기 (swea/5656) — hongseon

> `compiles: null` — 이 환경에 g++ 가 없어 **컴파일·실행을 하지 못했다.** 컴파일 실패라는 뜻이 아니라
> 판정을 못 했다는 뜻이다. 아래는 전부 코드를 읽고 쓴 것이고, 반례를 돌려본 것은 없다.

## 접근

세 가지 일을 세 함수로 정확히 갈랐다 — `shoot()`(한 발 쏘고 연쇄 폭발), `gravity()`(낙하),
`solution()`(N발의 조합 탐색). 이 문제는 세 가지가 섞이면 디버깅이 사실상 불가능해지는데, 경계가 분명하다.

**연쇄 폭발을 BFS 로 처리한 판단이 좋다.** 폭발한 벽돌이 또 다른 벽돌을 터뜨리는 구조라 재귀로 짜면
스택 상태와 보드 상태가 엉키기 쉬운데, 큐에 `{x, y, 위력}` 을 담고 **넣는 순간 0으로 지워서**
중복 폭발을 원천 차단했다.

```cpp
for (int step = 1; step < range; step++) {
    ...
    if (board[nx][ny] == 0) continue;   // 빈 칸을 만나도 폭발 범위는 계속 뻗는다
    q.push({nx, ny, board[nx][ny]});
    board[nx][ny] = 0;
}
```

`step < range` 로 "위력 N인 벽돌은 상하좌우 N-1칸" 을 맞췄고, 빈 칸에서 `break` 가 아니라 `continue` 를
쓴 것도 정확하다. 여기서 `break` 를 쓰면 이미 터진 자리 너머의 벽돌이 안 터진다 — 이 문제의 대표적인 오답이다.

답을 "남은 벽돌" 이 아니라 **"깬 벽돌의 최댓값"** 으로 두고 마지막에 `block_total - ans` 로 뒤집은 것도
탐색 가지치기(`cnt == block_total` 이면 즉시 종료)와 잘 맞는다.

## 개선점

### 1. (중요) 203줄 중 60줄 가까이가 주석 처리된 코드다 — <dead-code>

`draw()` 오버로드 두 개(28줄), `history[10]` / `flag1,2,3` 전역, `solution()` 맨 위의 중첩 디버그
블록(22줄), `// vector<vector<bool>> visited(...)`, 중간중간 `// cout << ...` 여러 줄.
**파일의 30% 가까이가 죽은 코드다.**

디버깅 흔적이 이 정도로 남아 있으면 실제 로직이 어디서 시작하는지 찾는 데만 시간이 든다.
특히 `history` / `flag` 는 전역 선언까지 남아 있어서, 읽는 사람이 "이게 지금 쓰이나?" 를
한 번 확인해야 한다. 지우는 게 맞다 — 필요하면 git 이력에 있다.

`week5/2105.cpp` 에도 같은 패턴이 있다. 이번 주차에서 반복되는 유일한 문제라, 제출·커밋 전에
`cout` 과 `//` 를 한 번 훑는 것만으로 정리된다.

### 2. (중요) `gravity()` 가 열마다 `vector` 를 새로 할당한다 — <redundant-collection>

```cpp
for (int col = 0; col < w; col++) {
    vector<int> col_vector;          // 열마다 힙 할당
    ...
}
```

`gravity()` 는 `shoot()` 마다 불리고, `shoot()` 은 탐색 노드마다 불린다.
노드 수가 `W^N` (최대 `12⁴ ≈ 2만`) 이므로 **`vector` 할당이 2만 × W = 24만 번** 일어난다.

두 포인터로 제자리에서 내리면 할당이 아예 없어진다.

```cpp
for (int col = 0; col < w; col++) {
    int write = h - 1;
    for (int read = h - 1; read >= 0; read--) {
        if (board[read][col] > 0) board[write--][col] = board[read][col];
    }
    while (write >= 0) board[write--][col] = 0;
}
```

**검증 안 함** — g++ 가 없어 돌려보지 못했다. 반영하면 원본과 같은 답을 내는지 확인이 필요하다.

### 3. (사소) 탐색 가지에서 보드를 통째로 복사한다 — <space-complexity>

```cpp
vector<vector<int>> next_board = cur;   // H×W 복사
int broken = shoot(next_board, col);
```

`W^N` 개 노드마다 `H×W` (최대 180칸) 복사가 일어난다. `N ≤ 4, W ≤ 12` 라 실제로는 통과하지만,
`shoot` 이 이미 보드를 파괴적으로 바꾸는 함수라 복사 말고는 방법이 없는 구조이기도 하다.
지금 규모에서는 **바꾸지 않아도 되는 지점**이고, 굳이 고친다면 되돌리기(undo) 로그를 쌓는 쪽인데
연쇄 폭발이라 로그가 복잡해진다. 지금 선택이 합리적이다.

### 4. (사소) 파일명이 문제 번호와 연결되지 않는다

`breakthewall.cpp` 는 `week6` 의 `swea2112.cpp` 식 규칙과 다르다. 저장소 alias 매핑이 흡수해서
사이트에는 `swea/5656` 으로 정상 인식되지만, `swea5656.cpp` 로 통일하면 번호로 파일을 찾을 수 있다.

## 복잡도

- 시간: `O(T·W^N·H·W)` — 노드 `W^N` 개마다 보드 복사 + `shoot` + `gravity` 가 각각 `O(H·W)`.
  `N ≤ 4, W ≤ 12` 라 노드는 2만 개 수준
- 공간: `O(N·H·W)` — 재귀 깊이 N 만큼 보드 사본이 스택에 쌓인다

## 요약

연쇄 폭발을 BFS + 즉시 0으로 지우기로 처리하고, 빈 칸에서 `break` 대신 `continue` 를 쓴 것 —
이 문제에서 제일 자주 틀리는 두 곳을 정확히 짚었다. `shoot`/`gravity`/`solution` 의 역할 분리도 좋다.
알고리즘에는 손댈 곳이 없고, 실제 문제는 **파일의 30%를 차지하는 주석 처리된 디버그 코드**다.
`gravity` 의 열별 `vector` 할당까지 두 포인터로 걷어내면 훨씬 읽기 좋아진다.
