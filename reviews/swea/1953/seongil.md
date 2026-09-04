---
platform: swea
problemId: "1953"
author: seongil
source: 이성일/week5/SWEA1953.java
week: 5
compiles: true
lang: java
verdict: needs-fix
tags: [dead-code, redundant-collection, good-decomposition, good-readability]
complexity:
  time: O(T·N·M)
  space: O(N·M)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 탈주범 검거 (swea/1953) — seongil

## 접근

이 문제의 어려운 부분은 "터널 구조물마다 뚫린 방향이 다르고, **양쪽 다** 뚫려 있어야 이동할 수 있다" 는
조건이다. 이 코드는 그걸 **두 개의 표로 완전히 데이터화했다.**

```java
static int[][] blocks = { {}, {0,1,2,3}, {0,2}, {1,3}, {0,1}, {1,2}, {2,3}, {0,3} };
static int[] dirMap = {2, 3, 0, 1};   // 반대 방향
```

덕분에 탐색부에 조건문이 하나도 없다 — 현재 칸이 뚫린 방향으로 나가보고(`blocks[board[x][y]]`),
그 방향의 반대가 이웃 칸에도 뚫려 있는지(`naxis == dirMap[axis]`)만 확인하면 끝난다.
구조물 7종을 `if/switch` 로 나열하면 금방 수십 줄이 되고 오타 하나 찾기가 어려운데, 그걸 피했다.
`blocks[0] = {}` 로 빈 칸까지 같은 표에 넣어 특수 케이스를 없앤 것도 깔끔하다.

**정확성은 확인했다.** 레벨별 BFS 레퍼런스와 무작위 1,500건(`N,M ≤ 9`, `L ≤ 12`)을 대조해 **전부 일치**했다.

## 개선점

### 1. (치명) `package com.ssafy.swb;` + `public class SWEA1953` 로는 SWEA 에 제출이 안 된다 — <dead-code>

SWEA 는 default package 에 `public class Solution` 을 요구한다. 로직과 무관하게 채점 서버의
컴파일 단계에서 막힌다. 로컬 `javac` 는 클래스명에 맞춘 임시 파일로 돌리기 때문에 `compiles: true` 로
나오는데, **이건 우리 도구의 판정이지 SWEA 의 판정이 아니다.**

```java
// 지우고
package com.ssafy.swb;
public class SWEA1953 {

// 이렇게
public class Solution {
```

같은 주차의 `SWEA1952.java` 는 package 없이 잘 돼 있으니, IDE 에서 패키지 안에 만든 파일만 이렇게 된 것 같다.
`SWEA2105.java`, `다리만들기2.java` 도 같은 상태다. **제출 전에 한 번 훑는 습관을 들이는 게 좋다.**

### 2. (사소) `breadthCnt` 배열은 `deq.size()` 로 대체된다 — <redundant-collection>

```java
int[] breadthCnt = new int[time+1];
breadthCnt[1] = 1;
...
for (int i = 0; i < breadthCnt[l]; i++) { ... breadthCnt[l+1]++; }
```

레벨별 개수를 따로 세고 있는데, 레벨 BFS 에서는 **큐에 그 레벨 원소만 남아 있는 시점**이 있으므로
그 순간의 `deq.size()` 가 곧 그 레벨의 개수다. 배열도, `l+1` 인덱싱도 필요 없어진다.

```java
int l = 1, cnt = 1;
while (l < time) {
    int size = deq.size();          // 지금 큐에 있는 게 정확히 l 레벨
    for (int i = 0; i < size; i++) {
        int[] idx = deq.pollFirst();
        ...
    }
    l++;
}
```

`time` 이 커질 때 `new int[time+1]` 을 매 케이스 잡을 이유도 같이 사라진다.

**검증 안 함** — 위 교체본은 돌려보지 않았다. 다만 `breadthCnt[l]` 을 읽는 시점에 큐에 정확히 그 레벨만
들어 있다는 건 원본 코드의 동작 그대로다.

### 3. (사소) 안쪽 `for` + `break` 는 `contains` 한 줄 — <redundant-loop>

```java
for (int naxis: blocks[board[nx][ny]]) {
    if (naxis == dirMap[axis]) { ...; break; }
}
```

"이웃 칸이 반대 방향으로 뚫려 있는가" 를 묻는 건데, 루프와 `break` 때문에 본문(방문 처리·카운트)이
한 단계 더 들여쓰기됐다. 판정을 밖으로 빼면 본문이 평평해진다.

```java
static boolean opened(int type, int dir) {
    for (int a : blocks[type]) if (a == dir) return true;
    return false;
}
...
if (!opened(board[nx][ny], dirMap[axis])) continue;
deq.offerLast(new int[]{nx, ny});
visited[nx][ny] = true;
cnt++;
```

구조물 종류가 7개뿐이라 `boolean[8][4]` 표를 미리 만들어 두면 `if (!open[board[nx][ny]][dirMap[axis]])`
한 줄이 된다. `blocks` 를 표로 만든 원래 발상을 한 걸음 더 민 것이다.

### 4. (사소) 출력이 케이스마다 `System.out.println` — <io-performance>

`StringBuilder` 를 케이스마다 새로 만들어 한 줄 찍고 버린다. 루프 밖에 하나 두고 마지막에
`System.out.print(sb)` 한 번이면 된다. `SWEA1952.java` 도 같은 형태다.

## 복잡도

- 시간: `O(T·N·M)` — 각 칸을 최대 한 번 방문. `L` 은 레벨 수 상한일 뿐 재방문이 없어 곱해지지 않는다
- 공간: `O(N·M)` — `visited` 와 큐

## 요약

구조물 7종의 연결 방향을 표 두 개로 데이터화해서 탐색부에서 조건문을 완전히 없앤 게 이 풀이의 값어치다.
이 문제에서 제일 실수가 잦은 "양쪽 다 뚫려야 한다" 를 `dirMap` 한 줄로 처리했고, 무작위 1,500건 대조에서도
전부 일치했다. 남은 건 제출 형식 하나뿐이다 — `package` 와 클래스명 때문에 SWEA 에서는 컴파일이 안 된다.
