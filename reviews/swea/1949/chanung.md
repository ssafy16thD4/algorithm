---
platform: swea
problemId: "1949"
author: chanung
source: 안찬웅/week5/등산로 조정.java
week: 5
compiles: true
verdict: good
tags: [space-complexity, good-readability]
complexity:
  time: O(봉우리수 × 경로수 × N^2)
  space: O(경로수 × N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 등산로 조성 (swea/1949) — chanung

## 접근

큐에 좌표만 넣는 대신 `Node` 에 `vis`·`graph`·`isUsed` 를 통째로 담아, **갈래마다 자기 상태를 들고 다니게** 만들었다. 등산로 조성은 최단거리가 아니라 최장경로 문제라서 전역 `visited` 를 공유하면 갈래끼리 간섭이 나는데, 그 함정을 정확히 피했다.

비용을 아끼는 판단도 들어가 있다. 깎지 않고 이동할 때는 `graph` 를 복사하지 않고 참조만 넘기고(`cur.graph`), **실제로 깎을 때만** `copyInt` 를 한다. `vis` 는 갈래마다 달라지므로 매번 복사하되 행 단위 `clone()` 을 써서 2중 루프보다 빠르게 했다.

주석도 좋다. `// static vis 아님, 반드시 cur.vis`, `// isUsed는 cur.isUsed로 물려받기. false 하드코딩하면 여러 번 깎임` 처럼 **한 번 틀렸던 자리**를 정확히 짚어놨다. 실패 버전(`chanung.failed`)과 나란히 놓고 보면 무엇을 고쳤는지 그대로 드러난다.

**검증**: 김준수·이성일의 통과 코드와 무작위 입력 60건(N=3~7, K=1~4)을 대조했고 **3개 구현이 전부 일치**했다.

## 개선점

### 1. (중요) 갈래마다 `boolean[N][N]` 을 복사해서 큐에 쌓는다 — `space-complexity`

이동 가능한 칸마다 `copyBool` 로 `N^2` 짜리 배열을 새로 만들고, 그걸 큐에 든 노드가 계속 붙들고 있다. 큐에 동시에 존재하는 노드 수만큼 `N^2` 메모리가 곱해진다. N ≤ 8이라 이 문제에서는 통과하지만, 같은 구조를 N이 조금만 커지는 문제에 그대로 가져가면 바로 메모리에서 터진다.

같은 탐색을 **DFS + 백트래킹**으로 바꾸면 복사가 아예 사라진다. 상태를 들고 다니는 대신 되돌리면 되기 때문이다:

```java
static void dfs(int x, int y, int cnt, boolean isUsed) {
    maxCnt = Math.max(maxCnt, cnt);
    for (int dir = 0; dir < 4; dir++) {
        int nx = x + dx[dir], ny = y + dy[dir];
        if (nx < 0 || nx >= n || ny < 0 || ny >= n || vis[nx][ny]) continue;

        if (graph[x][y] > graph[nx][ny]) {
            vis[nx][ny] = true;
            dfs(nx, ny, cnt + 1, isUsed);
            vis[nx][ny] = false;            // 되돌린다 = 복사 불필요
        } else if (!isUsed) {
            int len = graph[nx][ny] - graph[x][y] + 1;
            if (len <= k) {
                graph[nx][ny] -= len;  vis[nx][ny] = true;
                dfs(nx, ny, cnt + 1, true);
                vis[nx][ny] = false;   graph[nx][ny] += len;
            }
        }
    }
}
```

공간이 `O(경로수 × N^2)` 에서 `O(N^2)` 로 떨어진다. 김준수 코드가 이 형태다.

### 2. (사소) `Deque` 를 큐로 쓰지만 실제로는 순서가 결과에 영향을 주지 않는다 — `collection-choice`

`maxCnt` 를 `poll` 시점마다 갱신하므로 BFS든 DFS든 답이 같다. 즉 이 코드는 "큐를 쓴 완전탐색"이지 BFS의 성질(최단거리)을 쓰고 있지는 않다. 주석의 `알고리즘: bfs` 는 읽는 사람에게 오해를 줄 수 있다 — `완전탐색(큐 기반)` 정도가 실제에 가깝다.

## 복잡도

- 시간: `O(봉우리수 × 경로수 × N^2)` — 경로 하나 늘릴 때마다 `vis` 복사에 `N^2` 이 붙는 게 지배적이다.
- 공간: `O(경로수 × N^2)` — 큐에 살아 있는 노드가 각자 `vis` 를 들고 있다.

## 요약

최장경로 문제에서 상태를 갈래별로 분리해야 한다는 핵심을 정확히 잡았고, 깎을 때만 `graph` 를 복사하는 최적화까지 넣었다. 정답성에는 문제가 없다. 다만 상태를 **복사해서 들고 다니는 대신 백트래킹으로 되돌리면** 같은 탐색을 `O(N^2)` 공간으로 할 수 있다 — 그게 이 풀이에서 다음 단계다.
