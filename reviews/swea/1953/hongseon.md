---
platform: swea
problemId: "1953"
author: hongseon
source: 전홍선/week5/1953.cpp
week: 5
compiles: null
lang: cpp
verdict: good
tags: [good-readability, good-complexity, redundant-loop]
complexity:
  time: O(T·N·M)
  space: O(N·M)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 탈주범 검거 (swea/1953) — hongseon

> `compiles: null` — 이 환경에 g++ 가 없어 **컴파일·실행을 하지 못했다.** 컴파일 실패라는 뜻이 아니라
> 판정을 못 했다는 뜻이다. 아래 지적은 전부 코드를 읽고 쓴 것이고, 반례를 돌려본 것은 없다.

## 접근

터널 구조물 7종의 연결 방향을 **2차원 진리표 하나로 못 박았다.**

```cpp
bool tunnel_can_go[8][4] = {
    {false, false, false, false},   // 0: 빈 칸
    {true,  true,  true,  true },   // 1: 십자
    ...
};
int opposite_dir[4] = {1, 0, 3, 2};
```

덕분에 BFS 본문에 구조물 관련 분기가 **한 줄도 없다.** 나갈 수 있는지(`tunnel_can_go[현재][dir]`),
상대가 받아줄 수 있는지(`tunnel_can_go[이웃][opposite_dir[dir]]`) 두 번의 배열 조회로 끝난다.
0번 행을 전부 `false` 로 채워 빈 칸까지 같은 표에 넣은 것도 좋다 — 빈 칸 예외 처리가 따로 없어진다.

`dist` 배열을 `-1` 로 초기화해 **방문 여부와 거리를 한 배열로 겸한 것**도 깔끔하다.
`visited` 를 따로 두면 두 배열의 갱신 시점이 어긋나는 실수가 생기는데 그 여지가 없다.

`opposite_dir` 옆에 `//(5 - dir) % 4;` 로 규칙까지 적어둬서, 표가 어떻게 나왔는지 나중에 검산할 수 있다.

## 개선점

### 1. (사소) BFS 가 L 을 넘어서도 계속 퍼진다 — <redundant-loop>

```cpp
if (dist[cur.first][cur.second] + 1 <= l) answer++;
```

세는 건 `L` 이내로 정확히 걸렀지만, **큐에 넣는 것은 안 막았다.** 그래서 시작점이 속한 파이프
연결요소 전체를 끝까지 훑고 나서야 BFS 가 끝난다. `L` 이 작고 터널망이 크면 그만큼 헛돈다.

```cpp
if (dist[cur.first][cur.second] + 1 >= l) continue;   // 더 뻗을 필요 없다
```

한 줄 넣으면 `L` 레벨에서 멈춘다. `N, M ≤ 50` 이라 지금도 통과하겠지만, 세는 조건과 뻗는 조건이
같은 자리에 나란히 있는 편이 읽기에도 낫다.

### 2. (사소) `answer` 가 전역인데 케이스마다 초기화가 필요하다 — <uninitialized-state>

`answer`, `board`, `dist` 가 전부 전역이라 매 케이스 `answer = 0` 과 `fill` 두 번을 잊으면 안 된다.
지금은 세 줄 다 제대로 들어가 있지만, 케이스별 상태를 전역에 두면 이런 초기화 누락이 가장 흔한 오답 원인이다.
`board` 는 `n, m` 만큼만 다시 읽어 덮어쓰니 `fill` 이 사실 필요 없고, `dist` 초기화만 있으면 된다.

### 3. (사소) 파일명이 다른 주차와 규칙이 다르다

`week5` 는 `1953.cpp`, `2105.cpp`, `breakthewall.cpp` 인데 `week6` 는 `swea2112.cpp` 처럼 접두사가 붙어 있다.
저장소의 alias 매핑이 흡수하고 있어 사이트에는 정상으로 잡히지만, `swea1953.cpp` 로 통일해두면
나중에 문제 번호로 파일을 찾기 쉽다.

## 복잡도

- 시간: `O(T·N·M)` — 각 칸을 최대 한 번 방문. `L` 은 곱해지지 않는다
- 공간: `O(N·M)` — `dist` 와 큐

## 요약

구조물별 연결 방향을 `bool[8][4]` 표로 데이터화해서 BFS 본문에서 분기를 전부 없앤 게 이 풀이의 값어치다.
이 문제에서 제일 실수가 잦은 "양쪽 다 뚫려 있어야 한다" 를 배열 조회 두 번으로 처리했고,
`dist` 로 방문 여부까지 겸했다. 알고리즘·자료구조 선택에 손댈 곳이 없고, 남은 건 `L` 이후로도
계속 퍼지는 BFS 를 한 줄로 끊는 정도다.

> 참고: 같은 문제를 푼 `이성일/week5/SWEA1953.java` 는 같은 발상을 `int[][] blocks` 목록으로 구현해서
> 안쪽에 `for` + `break` 가 한 겹 더 있다. 이 코드의 `bool[8][4]` 표 쪽이 더 낫다.
