---
platform: swea
problemId: "5656"
author: chanung
source: 안찬웅/week5/벽돌 깨기 실패.java
week: 5
compiles: true
verdict: wrong
tags: [wrong-algorithm, logic-edge-case, dead-code]
complexity:
  time: O(N × W × H × max)
  space: O(H × W)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 벽돌 깨기 (swea/5656) — chanung (실패 버전)

## 접근

주석에 적어둔 설계(`1. n번 던진다 / 2. 가장 큰 수를 부순다 / 3. 부순 뒤 위 벽돌을 내린다`)는 방향이 맞다. 다만 코드가 그 설계를 따라가지 못했다 — **탐색이 없고, 중력이 없고, 연쇄가 없다.** 세 조각 모두 미구현이라 뼈대만 잡힌 상태에 가깝다.

## 개선점

### 1. (치명) 어느 열에 떨어뜨릴지 고르는 탐색이 없다 — `wrong-algorithm`

```java
while(n > 0) {
    for(int i=0; i<w; i++) {
        for(int j=0; j<h; j++) {
            if(arr[j][i] != 0) {
                bun(j, i);        // 조건에 맞으면 전부 터뜨린다
            }
        }
    }
    n--;
}
```

이 문제는 "구슬 N개를 **각각 어느 열에 떨어뜨려야** 남는 벽돌이 최소인가"를 묻는다. 즉 `W^N` 가지 선택을 전부 시도하고 최솟값을 취해야 한다.

그런데 이 코드는 열을 고르는 대신 **모든 칸을 순회하며 0이 아닌 칸을 전부 터뜨린다.** 구슬 한 개로 판 전체를 없애는 동작이라 문제와 다른 것을 계산한다. `n--` 는 아무 의미 없이 바깥 루프를 N번 돌 뿐이고, 두 번째 반복부터는 이미 판이 비어 있다.

또 열마다 **가장 위쪽 벽돌 하나만** 맞아야 하는데(`break` 필요), 세로 루프가 끝까지 돌며 그 열의 모든 벽돌을 차례로 터뜨린다.

### 2. (치명) 연쇄 폭발이 없다 — `wrong-algorithm`

```java
static void bun(int x, int y) {
    int size = arr[x][y];
    for(int i=1; i<=size-1; i++) { ... arr[x-i][y] = 0; ... }
    arr[x][y] = 0;
}
```

숫자 `k` 인 벽돌이 터지면 범위 안의 벽돌이 사라지는데, **그 범위 안에 있던 2 이상짜리 벽돌도 자기 범위만큼 다시 터져야 한다.** 지금은 범위 안의 칸을 `0` 으로 지우기만 하고 재귀·큐 처리가 없어서 연쇄가 한 단계에서 멈춘다.

이성일 코드(`reviews/swea/5656/seongil.md`)처럼 값이 2 이상인 칸은 큐에 넣어 나중에 자기 범위를 펼치게 해야 한다.

### 3. (치명) 범위 검사 `continue` 가 네 방향을 한꺼번에 건너뛴다 — `logic-edge-case`

```java
for(int i=1; i<=size-1; i++) {
    if(x-i < 0 || x+i >= h || y-i < 0 || y+i >= w) continue;
    if(arr[x-i][y] >= 1) arr[x-i][y] = 0;
    ...
}
```

네 방향의 경계를 **하나의 조건으로 묶어** 검사한다. 그래서 위쪽만 판을 벗어나도 `continue` 가 걸려 **아래·왼쪽·오른쪽까지 전부 건너뛴다.** 가장자리 근처 벽돌은 터뜨려야 할 칸을 놓친다.

방향마다 따로 봐야 한다:

```java
if (x - i >= 0)  arr[x - i][y] = 0;
if (x + i < h)   arr[x + i][y] = 0;
if (y - i >= 0)  arr[x][y - i] = 0;
if (y + i < w)   arr[x][y + i] = 0;
```

### 4. (치명) 중력 처리가 주석 한 줄로만 남아 있다 — `wrong-algorithm`

```java
// 빈공간들 땡기기
```

벽돌이 사라지면 위쪽 벽돌이 아래로 내려와야 다음 구슬의 결과가 달라진다. 이 처리가 통째로 비어 있어서, 설령 위 문제들을 고치더라도 답이 맞지 않는다. 열마다 아래에서부터 채워 넣는 함수가 필요하다:

```java
static void down() {
    for (int j = 0; j < w; j++) {
        int floor = h - 1;
        for (int i = h - 1; i >= 0; i--) {
            if (arr[i][j] > 0) { int v = arr[i][j]; arr[i][j] = 0; arr[floor--][j] = v; }
        }
    }
}
```

### 5. (사소) `System.out.println(sb)` 로 끝나 줄바꿈이 하나 더 붙는다 — `dead-code`

`sb` 에 이미 케이스마다 `\n` 을 넣었으므로 `print` 가 맞다. 다른 풀이들은 전부 `System.out.print(sb)` 다.

## 복잡도

- 시간: `O(N × W × H × max)` — 탐색이 없어서 오히려 빠르다. **틀린 것을 빠르게 계산한다.**
- 공간: `O(H × W)` — 판 한 장. 탐색이 없으니 복사본도 없다.

## 요약

주석의 설계는 문제를 제대로 읽은 것이고, 방향 자체는 맞다. 다만 구현이 거기까지 가지 못했다 — **열 선택 탐색·연쇄 폭발·중력** 세 가지가 전부 빠져 있어서, 지금은 "판의 모든 벽돌을 지우고 0을 출력하는" 코드에 가깝다. 하나씩 채운다면 순서는 중력(`down`) → 연쇄(큐) → 열 선택 DFS가 만들기 쉽다. 이성일 풀이가 그 세 조각을 각각 함수로 분리해두어 대조하기 좋다.
