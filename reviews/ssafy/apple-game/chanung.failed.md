---
platform: ssafy
problemId: "apple-game"
author: chanung
source: 안찬웅/week6/사과 먹기 게임 실패.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [wrong-algorithm, uninitialized-state, collection-choice]
complexity:
  time: O(N^2)
  space: O(N^2 × 4)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 사과 먹기 게임 (ssafy/apple-game) — chanung (실패 버전)
## 왜 실패했나 — 대표 풀이와 무엇이 달랐나

이 파일과 위 대표 풀이의 차이는 **"상태를 어디에 두었는가"** 하나로 요약된다.

| | 실패 버전 | 대표 풀이 |
| --- | --- | --- |
| 방문 관리 | `boolean[n][n][4] vis` | `int[n][n][4] dist` (비용) |
| 목표 진행 | `static int num` 을 BFS 안에서 `num++` | 목표마다 BFS 를 다시 시작 |
| 회전 비용 | 없음 | 우회전 = 비용 1 |
| 큐 | 일반 `Queue` | `Deque` + offerFirst/offerLast |

**핵심**: `boolean vis` 로는 이 문제를 풀 수 없다. 같은 칸·같은 방향이라도 **몇 번 회전해서
왔는지**가 다르면 이후 최적값이 달라진다. "방문했는가"가 아니라 "몇 번에 왔는가"를 들고 있어야
하고, 그 순간 자료구조는 `boolean` 이 아니라 `int dist` 가 된다. 대표 풀이는 이 전환을 해냈다.

## 그 외 실제 결함

1. **(치명) `bfs` 가 한 번도 호출되지 않는다.** `main` 은 격자를 읽고 `num = 1;` 한 뒤
   `sb.append(num)` 을 찍는다. **어떤 입력에도 `1` 만 출력한다.**
2. **(치명) `maxNum` 이 테스트케이스마다 초기화되지 않는다.** `static int maxNum;` 이
   루프 밖에서 한 번만 0 이라, 두 번째 케이스부터는 이전 케이스의 최댓값이 남아 `endX/endY`
   갱신이 안 된다. **static 필드는 케이스 시작마다 리셋한다** — 멀티 테스트케이스 문제의
   1번 함정이다.
3. **(치명) `num++` 을 BFS 안에서 한다.** 큐에 여러 갈래가 동시에 들어 있는데 전역 카운터를
   증가시키면, 어느 갈래가 먼저 처리되느냐에 따라 목표가 제멋대로 앞당겨진다.
   목표 진행은 BFS 밖에서 관리해야 한다 — 대표 풀이가 그렇게 고쳤다.
4. **(치명) 디버그 출력이 루프 안에 있다.** `System.out.println("nx: " + nx + " ny: " + ny)` 는
   채점 출력에 그대로 섞인다. 알고리즘이 맞아도 오답 처리된다.
5. **(중요) `vis[nx][ny][(dir + 1) % 4] = true;` 뒤에 그 방향으로 큐에 넣지 않는다.**
   방문 표시만 하고 갈래를 만들지 않으니, 회전한 경로가 아예 탐색되지 않는다.

## 이 파일에서 가져갈 것

주석에 적은 계획(`1.1 가장 큰 수의 인덱스를 만나면 return` / `1.2 1을 향해 이동하면서 방향을
회전할때마다 카운팅`)이 이미 문제를 정확히 요약하고 있다. **막힌 지점은 "회전 비용을 어떻게
셀 것인가"였고, 그 답이 가중치 0/1 그래프**라는 것을 알아챈 순간 대표 풀이가 나왔다.
"비용이 0 과 1 뿐이면 0-1 BFS" 는 한 번 외워 두면 계속 쓰는 패턴이다.
