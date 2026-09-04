---
platform: boj
problemId: "17472"
author: seongil
source: 이성일/week6/다리만들기2.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [dead-code, good-decomposition, good-complexity]
complexity:
  time: O(N·M·(N+M) + E log E)
  space: O(N·M + I²)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 다리 만들기 2 (boj/17472) — seongil

## 접근

이 문제는 "격자 시뮬레이션" 처럼 생겼지만 실제로는 **그래프 문제로 번역할 수 있느냐**가 전부다.
이 코드는 파일 맨 위 주석에서 그 번역을 먼저 적어놓고 시작한다 — 섬을 노드로, 다리를 간선으로,
답은 MST 의 가중치 합. 그리고 코드가 그 주석 그대로 4단계로 나뉜다.

1. `bfs()` — 섬 덩어리마다 번호를 매기고 그 섬의 좌표 목록을 반환
2. `getEdges()` — 각 섬의 모든 칸에서 4방향 직선을 쏴서 다른 섬까지의 최소 길이를 `edges[i][j]` 에 기록
3. 우선순위 큐 + `union/find` 로 크루스칼
4. 간선이 `섬수-1` 개 안 모이면 `-1`

**번호 매기기를 `board` 에 덮어쓴 판단이 좋다.** 별도 `id` 배열을 두지 않고 원본 격자의 1 을 섬 번호로
갈아끼우기 때문에, 2단계에서 직선을 쏘다 만난 칸의 값이 곧 상대 섬 번호가 된다. 조회 테이블이 하나 줄었다.

간선 중복도 `initialized[]` 로 정리했다. i 를 처리할 때 이미 끝난 섬(j < i)으로 향하는 간선은 건너뛰므로,
같은 다리가 양쪽에서 두 번 들어오지 않는다.

**정확성은 확인했다.** 섬 라벨링 → 간선 수집 → 크루스칼을 독립적으로 짠 레퍼런스와
무작위 600건(`N,M ≤ 9`, 섬이 하나뿐이거나 연결 불가인 경우 포함)을 대조해 **전부 일치**했다.
`-1` 을 내야 하는 경우도 같았다.

## 개선점

### 1. (치명) `package com.ssafy.swb;` + `public class 다리만들기2` 로는 BOJ 에 제출이 안 된다 — <dead-code>

BOJ 의 Java 채점은 **default package 의 `public class Main`** 을 요구한다. 지금 파일은 패키지 선언이
있는 데다 클래스명이 한글이라 두 가지 모두 어긋난다. 로컬 `javac` 는 클래스명에 맞춘 임시 파일로
복사해 돌리기 때문에 `compiles: true` 로 나오지만, **BOJ 에서는 컴파일 단계에서 막힌다.**

```java
// 지우고
package com.ssafy.swb;
public class 다리만들기2 {

// 이렇게
public class Main {
```

같은 주차의 `SWEA1953.java`(`package com.ssafy.swb`), `SWEA2105.java`(`package coding`) 도 같은 상태다.
IDE 패키지 안에서 작업한 파일들이 그대로 올라온 것으로 보인다. 제출 직전에 한 번 훑으면 세 개가 같이 해결된다.

### 2. (중요) 섬이 자기 자신을 가리키는 간선이 만들어진다 — <dead-code>

`getEdges()` 에서 직선을 쏘다 **같은 섬의 다른 칸**에 닿는 경우가 걸러지지 않는다.
`initialized[i]` 는 i 를 처리하는 동안에는 아직 `false` 라서, 아래 분기로 들어간다.

```java
if (initialized[board[nx][ny]]) break;
else {
    edges[i][board[nx][ny]] = Math.min(edges[i][board[nx][ny]], cost);   // i == board[nx][ny] 가능
    break;
}
```

그래서 `edges[i][i]` 에 값이 들어가고, 그게 우선순위 큐에도 `{i, i, cost}` 로 올라간다.
`mst()` 의 `find(edge[0]) == find(edge[1])` 에서 걸러지므로 **답은 틀리지 않는다** (600건 대조에서 확인).
다만 큐에 쓸모없는 원소가 섬 개수만큼 섞이고, 그중 하나가 가장 짧은 간선이면 `poll` 을 한 번 헛돌게 된다.
한 줄이면 막힌다.

```java
int target = board[nx][ny];
if (target == i) break;              // 같은 섬이면 다리가 아니다
if (initialized[target]) break;
edges[i][target] = Math.min(edges[i][target], cost);
break;
```

### 3. (사소) 주석 처리된 코드 두 곳 — <dead-code>

```java
//        if (answer == 0) answer = -1;
...
//            System.out.println("edge n " + edgeCnt + ...);
```

앞엣것은 `mst()` 안의 `if (edgeCnt != islandSize - 1) return -1;` 로 이미 해결돼서 필요 없어진 코드고,
뒤엣것은 디버그 출력이다. 남겨두면 나중에 읽을 때 "이게 왜 꺼져 있지" 를 한 번 더 생각하게 된다. 지우는 게 낫다.

### 4. (사소) `edges` 인접 행렬을 큐로 옮기는 이중 루프 — <redundant-loop>

```java
int[][] edges = new int[islandSize + 1][islandSize + 1];   // 최소 길이 갱신용
...
for (int i = 1; i <= islandSize; i++)
    for (int j = 1; j <= islandSize; j++)
        if (edges[i][j] < Integer.MAX_VALUE) edgesList.offer(...);
```

섬은 최대 36개(`10×10` 격자에 체스판 모양)라 `36²` 은 아무 문제가 없다.
다만 인접 행렬로 최솟값을 모은 뒤 다시 큐로 옮기는 2단계 대신, `edges[i][j]` 를 그대로 두고
**리스트에 담아 `Arrays.sort` 로 정렬**하면 `PriorityQueue` 와 그 이동 루프가 같이 없어진다.
크루스칼은 어차피 전체 간선을 오름차순으로 한 번 훑는 알고리즘이라 힙이 필요 없다.

## 복잡도

- 시간: `O(N·M·(N+M) + E log E)` — 섬의 각 칸에서 4방향 직선(최대 `N+M`)을 쏘는 게 지배적.
  간선 수 `E` 는 섬 개수 `I ≤ 36` 에 대해 `I²` 이하라 정렬 비용은 무시할 수준
- 공간: `O(N·M + I²)` — 격자와 섬별 좌표 목록, 그리고 인접 행렬

## 요약

문제를 "섬 = 노드, 다리 = 간선, 답 = MST" 로 번역한 판단이 정확하고, 그 4단계가 함수 경계와 그대로 맞아떨어져
읽기 좋다. 섬 번호를 원본 격자에 덮어써서 조회 테이블을 하나 줄인 것도 좋은 선택이다.
무작위 600건 대조에서 `-1` 케이스까지 전부 일치했으므로 알고리즘은 맞다.
실제로 손봐야 할 건 **BOJ 제출 형식**(패키지 + 한글 클래스명으로는 컴파일이 안 된다) 하나이고,
자기 자신으로 향하는 간선은 답에는 영향이 없지만 한 줄로 막아두는 게 좋다.
