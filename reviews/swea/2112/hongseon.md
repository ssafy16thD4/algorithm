---
platform: swea
problemId: "2112"
author: hongseon
source: 전홍선/week6/swea2112.cpp
week: 6
compiles: null
lang: cpp
verdict: needs-fix
tags: [dead-code, redundant-collection, good-complexity]
complexity:
  time: O(T·3^D·D·W)
  space: O(D·W·K)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 보호 필름 (swea/2112) — hongseon

> `compiles: null` — 이 환경에 g++ 가 없어 **컴파일·실행을 하지 못했다.** 컴파일 실패라는 뜻이 아니라
> 판정을 못 했다는 뜻이다. 아래는 전부 코드를 읽고 쓴 것이고, 반례를 돌려본 것은 없다.

## 접근

`ans = k` 로 시작하는 게 이 풀이의 핵심이다. **"K개 행을 연속으로 골라 전부 같은 약품을 넣으면
모든 열이 반드시 K개 연속을 갖는다"** 는 사실에서 나오는 상한이라, 탐색을 시작하기도 전에
답의 범위가 `0 ~ K` 로 닫힌다. 그 덕에 `if (n >= ans) return;` 한 줄이 곧바로 깊이 제한이 되고,
탐색이 `K` 단계 아래로 내려가지 않는다.

가지치기가 세 겹으로 들어가 있는 것도 정확하다.

```cpp
if (n >= ans) return;              // 지금까지 최선보다 이미 나쁘다
if (check(cur_board)) { ans = min(ans, n); return; }   // 됐으면 더 넣을 이유가 없다
if (n + 1 == ans) return;          // 한 발 더 넣어봐야 동점이다
```

특히 **성공하면 즉시 `return`** 하는 게 중요하다. 이미 통과한 상태에서 약품을 더 넣어봐야 답이
나빠지기만 하므로 그 아래 가지 전체가 잘린다. 그리고 행 선택을 `last + 1` 부터로 제한해
같은 조합을 순서만 바꿔 다시 세는 일이 없다.

`check()` 도 열마다 연속 길이를 세다가 `k` 에 닿는 순간 `break` 하고, 한 열이라도 실패하면
즉시 `return false` 한다 — 불필요한 순회가 없다.

## 개선점

### 1. (중요) `is_used` 배열은 아무 데서도 읽히지 않는다 — <dead-code>

```cpp
int is_used[20];
...
is_used[i] = true;
... backtrack ...
is_used[i] = false;
```

값을 쓰기만 하고 **읽는 곳은 주석 처리된 디버그 블록뿐**이다(`if (is_used[2] && is_used[5])`).
지금은 탐색 노드마다 대입 두 번을 헛돌고 있고, 읽는 사람에게는 "이 배열이 탐색에 쓰이나?" 하는
잘못된 신호를 준다. 전역 선언과 `fill` 까지 같이 지우면 된다.

`backtrack` 맨 위의 주석 처리된 보드 출력 블록(14줄)도 함께 정리 대상이다.

### 2. (중요) 가지마다 보드를 통째로 복사한다 — <redundant-collection>

```cpp
vector<vector<int>> next_board;
for (int i = last + 1; i < d; i++) {
    next_board = cur_board;                          // D×W 복사
    fill(next_board[i].begin(), next_board[i].end(), 0);
    backtrack(n + 1, i, next_board);
    fill(next_board[i].begin(), next_board[i].end(), 1);   // 여기서도 한 번 더
    backtrack(n + 1, i, next_board);
}
```

노드 수가 `3^D` 급(`D ≤ 13` → 약 160만)인데, 노드마다 `D×W` (최대 260칸) 복사가 붙는다.
게다가 `vector<vector<int>>` 라 **행마다 힙 할당**이라서 복사 비용이 단순 260칸 이상이다.

이 문제는 **한 행만 통째로 바꿨다가 되돌리면** 되므로 복사가 필요 없다. 원본 행을 백업했다가
되돌리는 쪽이 훨씬 싸다.

```cpp
static int board[13][20];                 // 전역 고정 배열

void backtrack(int n, int last) {
    ...
    for (int i = last + 1; i < d; i++) {
        int backup[20];
        copy(board[i], board[i] + w, backup);       // 한 행만 백업
        for (int med = 0; med <= 1; med++) {
            fill(board[i], board[i] + w, med);
            backtrack(n + 1, i);
        }
        copy(backup, backup + w, board[i]);         // 되돌리기
    }
}
```

`vector<vector<int>>` 를 `int[13][20]` 고정 배열로 바꾸면 `check()` 의 열 순회도 캐시에 훨씬 잘 맞는다.

**검증 안 함** — g++ 가 없어 돌려보지 못했다. 반영하면 원본과 같은 답을 내는지 확인이 필요하다.

### 3. (사소) `if (n + 1 == ans) return;` 이 세 곳에 흩어져 있다 — <duplicate-code>

`check` 직후에 한 번, `for` 루프 안에 한 번, 합쳐서 같은 조건이 세 번 나온다.
루프 안의 것은 재귀 도중 `ans` 가 줄어들 수 있어서 **의미가 있지만**, `check` 직후의 것은
바로 위 `if (n >= ans) return;` 과 합쳐서 `if (n + 1 >= ans) return;` 한 줄로 정리된다.
가지치기 조건이 흩어져 있으면 나중에 하나만 고치고 나머지를 놓치기 쉽다.

### 4. (사소) `check()` 가 `d < k` 인 경우를 매번 다시 확인한다

`k > d` 면 어떤 열도 `k` 연속을 만들 수 없다. 문제 제약이 `K ≤ D` 라 실제로는 안 생기지만,
`check` 는 그걸 모르고 매번 전체를 훑는다. 지금 코드로 문제가 되지는 않는다.

## 복잡도

- 시간: `O(T·3^D·D·W)` — 행마다 (안 넣음 / A / B) 세 갈래, 노드마다 `check` 와 보드 복사가 `O(D·W)`.
  가지치기로 실제 깊이는 `K` 이하로 잘린다
- 공간: `O(D·W·K)` — 재귀 깊이 `K` 만큼 보드 사본이 스택에 쌓인다. 2번을 적용하면 `O(D·W)`

## 요약

`ans = k` 라는 상한을 먼저 세워 탐색 깊이를 닫고, 성공 즉시 `return` 으로 아래 가지를 통째로 자른
설계가 정확하다. 조합 중복도 `last + 1` 로 막아 두어 탐색 자체에는 군더더기가 없다.
남은 건 두 가지다 — **읽히지 않는 `is_used` 배열**이 노드마다 헛대입을 만들고,
가지마다 `vector<vector<int>>` 를 통째로 복사하는 탓에 실제 비용의 상당 부분이 보드 복사에 쓰인다.
한 행만 백업·복원하는 방식으로 바꾸면 둘 다 사라진다.
