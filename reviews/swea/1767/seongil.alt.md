---
platform: swea
problemId: "1767"
author: seongil
source: 이성일/week6/SWEA1767.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [uninitialized-state, logic-edge-case]
complexity:
  time: O(T · 5^C · N)
  space: O(N² + C)
generatedBy: claude-code-local
generatedAt: 2026-09-09
---

# 프로세서 연결하기 (swea/1767) — seongil (6주차 재풀이)

> 1주차의 `이성일/week1/SWEA1767.java` 와는 다른 코드다. 파일명이 같아 대표 리뷰 경로가 겹쳐서
> 이 파일은 `.alt` 로 붙였다 (`scripts/lib/map.mjs` 의 `PATH_VARIANT_OVERRIDES`).

## 접근

코어 좌표를 입력받으며 미리 모아두고(`idxs`), 코어를 하나씩 깊이로 소비하면서 4방향 전선을 시도하는
백트래킹이다. **연결한 코어 개수를 인덱스로 쓰는 `answer[cellCnt]` 배열**을 둬서
"개수 최대 → 그중 길이 최소" 라는 2단 목표를 배열 하나로 눌러 담은 게 이 풀이의 좋은 판단이다.

```java
int minCost = Integer.MAX_VALUE;
for (int i = 0; i < answer.length; i++)
    if (answer[i] != Integer.MAX_VALUE) minCost = answer[i];   // 마지막으로 살아남은 칸 = 최대 개수
```

앞에서부터 훑어 마지막으로 살아있는 칸을 집으므로, "가장 많이 연결한 경우의 최소 길이" 가 자연히 나온다.
가장자리 코어를 `cellCnt + 1` 로 세고 바로 다음 깊이로 넘기는 처리도 맞다.

## 개선점

### 1. (치명) `backward()` 가 단 한 번도 실행되지 않는다 — 되돌리기가 통째로 빠졌다 — <uninitialized-state>

```java
static void backward(int dir, int nx, int ny, int x, int y) {
    while (nx != x && ny != y) { ... }      // ← && 가 아니라 || 여야 한다
}
```

전선은 **한 축으로만** 뻗는다. 위/아래로 깔면 `ny == y` 가 끝까지 유지되고, 좌/우로 깔면 `nx == x` 가
유지된다. 그래서 `nx != x && ny != y` 는 **항상 처음부터 false** 이고 루프 본문이 한 번도 안 돈다.
결과적으로 `visited` 에 칠한 전선이 영원히 남아, 형제 가지들이 앞 가지가 깔아 둔 전선 위에서 탐색한다.

**실제로 돌려본 반례** (5×5, 코어 2개):

```
1
5
0 0 0 0 0
0 0 0 0 0
0 1 0 1 0
0 0 0 0 0
0 0 0 0 0
```

`(2,1)` 은 왼쪽으로 1칸, `(2,3)` 은 오른쪽으로 1칸이면 둘 다 연결되고 길이 합은 **2** 다.
이 코드는 **3** 을 출력한다.

무작위 대조도 같은 그림이다 — 5×5 코어 2개 전수 300건 중 **11건 불일치**,
6×8 코어 4~9개 1,500건 중 **947건 불일치**.

고치는 건 연산자 하나다.

```java
while (nx != x || ny != y) { visited[nx][ny] = false; nx -= dx[dir]; ny -= dy[dir]; }
```

### 2. (치명) 전선을 끝까지 깐 분기의 복구 시작점이 판 밖이다 — <logic-edge-case>

```java
while (isInRange(nx, ny)) { ... nx += dx[i]; ny += dy[i]; }   // 빠져나온 시점의 nx,ny 는 판 밖
...
else { dfs(...); backward(i, nx, ny, x, y); }                  // 판 밖 좌표에서 복구를 시작한다
```

1번을 고쳐서 `backward` 가 실제로 돌기 시작하면 이 분기가 곧바로 터진다.
위 반례를 1번만 고친 코드로 돌리면 **`ArrayIndexOutOfBoundsException: Index -1 out of bounds for length 5`**
가 난다 (실제로 돌려서 확인). 한 칸 되돌린 자리에서 시작해야 한다.

```java
else { dfs(depth + 1, cellCnt + 1, sum + dirSum); backward(i, nx - dx[i], ny - dy[i], x, y); }
```

막힌 분기는 이미 `nx -= dx[i]; ny -= dy[i];` 로 되돌린 뒤 호출하고 있으니 그대로 두면 된다.

**1·2번만 고쳐서 검증했다.** 정석 백트래킹 레퍼런스와 대조해
**5×5 전수 300건 + 무작위 480건 + 조밀 1,500건 + 6×6 코어 3개 392건, 합계 2,672건 불일치 0건**이다.
알고리즘 뼈대는 건드리지 않아도 된다.

### 3. (치명) 답 앞에 디버그 출력이 그대로 나간다 — <logic-edge-case>

`minCost` 를 고르는 루프 안의 `cell:` 출력과, 그 뒤 `visited` 격자를 찍는 이중 루프가
**주석 처리되지 않은 채 살아 있다.** 채점 서버가 보는 표준출력이 이렇게 된다.

```
 cell: 0 answer[cellCnt]: 0
 cell: 1 answer[cellCnt]: 1
 cell: 2 answer[cellCnt]: 3

01010
01010
11111
01010
01010
#1 3
```

`#1 …` 만 있어야 할 출력이 오염되므로, 1·2번을 고쳐도 이것 때문에 오답 처리된다.
(주석 처리된 디버그 출력은 리뷰 대상이 아니지만, 이건 실행되는 코드다.)

## 복잡도

- 시간: `O(T · 5^C · N)` — 코어 `C` 개마다 4방향 + 포기, 방향마다 전선 깔기가 `O(N)`.
  `C ≤ 12` 제약 안에서는 충분하다. 복잡도는 이 풀이의 문제가 아니다.
- 공간: `O(N² + C)` — `maxinos` · `visited` 격자와 재귀 깊이

## 요약

`answer[cellCnt]` 로 "개수 최대 → 길이 최소" 를 배열 하나에 담은 설계와 복잡도 선택은 맞다.
무너진 건 백트래킹의 기본기 쪽이다 — `backward()` 의 `&&` 때문에 **되돌리기가 아예 안 돌고**,
그걸 고치면 복구 시작점이 판 밖이라 예외가 나며, 디버그 출력이 살아서 답을 가린다.
두 줄을 고치고 출력을 지운 코드는 2,672건 대조에서 전부 정답이었다.

> 참고로 "4방향이 전부 뚫린 코어를 포기하는 선택지" 는 코드에 없다(막힌 방향이 있을 때만 건너뛴다).
> 이론상 빈틈으로 보여서 조밀한 배치 위주로 2,672건을 돌려봤지만 답이 갈리는 사례는 나오지 않았다.
> 확인된 결함이 아니므로 개선점에 올리지 않는다.
