---
platform: swea
problemId: "2105"
author: junsoo
source: 김준수/week5/디저트 카페.java
week: 5
compiles: false
verdict: good
tags: [collection-choice, long-method]
complexity:
  time: O(N^4 × D)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 디저트 카페 (swea/2105) — junsoo

## 접근

시작점마다 네 방향을 순서대로(`dir 0→1→2→3`) 진행하면서, 한 방향으로 **갈 수 있는 데까지 while 로 밀고 나가되 매 칸마다 다음 방향으로 꺾는 재귀를 호출**한다. `dir == 4` 에 도달했을 때 시작점으로 정확히 돌아왔으면 사각형이 완성된 것으로 보고 길이를 갱신한다.

주석에 남긴 시행착오가 정확하다. "첫 지점을 `visited=true` 로 막아버리면 마지막에 시작점으로 돌아올 수 없다" 는 걸 짚고, 시작 좌표를 `start` 로 따로 들고 있다가 `nx == start[0] && ny == start[1]` 로 **복귀를 정상 종료로 처리**했다. 대각선 이동을 `dx/dy` 배열로 일반 격자탐색과 똑같이 다룬 것도 좋은 정리다.

`newCafes` 에 이번 while 루프에서 새로 방문한 칸만 모아두었다가 한꺼번에 되돌리는 백트래킹도 빠짐없이 맞다.

**검증**: 안찬웅 통과 코드와 무작위 입력 60건(N=4~9, 디저트 번호 1~12)을 대조했고 **두 구현이 전부 일치**했다.

## 개선점

### 1. (중요) 디저트 중복 검사를 `List.contains` 로 한다 — `collection-choice`

```java
static List<Integer> cafes;   // LinkedList
...
if(visited[nx][ny] || cafes.contains(board[nx][ny])) break;
```

`cafes` 는 `LinkedList<Integer>` 라 `contains` 가 **선형 탐색**이다. 게다가 `Integer` 라 비교마다 언박싱이 걸린다. 경로가 길어질수록 검사 한 번이 비싸지는데, 이 검사는 탐색의 가장 안쪽 루프에서 매번 돈다.

디저트 번호는 최대 100까지라 **번호를 인덱스로 쓰는 boolean 배열**이면 `O(1)` 이다:

```java
static boolean[] hasDessert;   // new boolean[101], 테스트케이스마다 초기화
...
if (visited[nx][ny] || hasDessert[board[nx][ny]]) break;
...
hasDessert[board[x][y]] = true;    // 방문 시
hasDessert[board[p[0]][p[1]]] = false;   // 백트래킹 복원 시
```

되돌릴 때도 `cafes.remove(cafes.size()-1)` 를 `newCafes.size()` 번 도는 대신, 좌표별로 `false` 만 찍으면 된다. 안찬웅 코드가 `vis = new boolean[101]` 로 이 형태다.

덤으로 **`visited` 배열이 아예 필요 없어진다.** 같은 칸을 두 번 밟으면 그 칸의 디저트 번호가 이미 `true` 라서 자동으로 걸리기 때문이다.

### 2. (사소) `dfs` 하나가 while 루프·재귀·백트래킹 복원을 전부 안고 있다 — `long-method`

`while(true)` 안에서 좌표 `x, y` 를 직접 갱신하면서 동시에 재귀를 호출하기 때문에, "지금 `x,y` 가 어디를 가리키는가"가 루프 회차마다 달라진다. 시작점 복귀 처리(`if(nx == start[0] ...)`)가 `inRange` 검사보다 **앞에** 와야 하는 이유도 이 뒤엉킴에서 나온다 — 읽는 사람이 순서의 의미를 재구성해야 한다.

안찬웅 코드처럼 `dfs(x, y, cnt, dir)` 이 **한 칸만 움직이고 `dir` 또는 `dir+1` 두 갈래로 재귀**하는 형태로 바꾸면 while 루프와 `newCafes` 복원 로직이 통째로 사라진다. 같은 탐색인데 함수가 10줄이 된다.

## 복잡도

- 시간: `O(N^4 × D)` — 시작점 `N^2` × 사각형 모양(두 변 길이) `N^2` 에, 중복 검사 `D`(경로 길이)가 곱해진다. 개선점 1을 적용하면 `D` 가 빠져 `O(N^4)`.
- 공간: `O(N^2)` — `visited` + `cafes`.

## 요약

시작점 복귀를 종료 조건으로 삼는 이 문제의 핵심을 정확히 잡았고, 백트래킹 복원도 새지 않는다. 답은 맞다. 남은 건 두 가지 정리다 — 중복 검사를 `List.contains` 에서 **번호 인덱스 배열**로 바꾸면 상수배가 크게 줄고, 겸사겸사 `visited` 도 필요 없어진다.
