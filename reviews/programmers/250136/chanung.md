---
platform: programmers
problemId: "250136"
author: chanung
source: 안찬웅/pccp/석유 시추.java
week: null
compiles: true
verdict: good
tags: [logic-edge-case]
complexity:
  time: O(n*m)
  space: O(n*m)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# [PCCP 기출문제] 2번 / 석유 시추 (programmers/250136) — chanung

## 접근

BFS 로 석유 덩어리마다 **고유 ID** 를 칠하고 ID별 크기를 `compSize` 에 담은 뒤, 열마다 위에서
아래로 훑으며 `Set` 에 처음 들어가는 ID 만 크기를 더한다. 덩어리 단위로 "이미 셌는지"를 기억하는
구조라 모양이 아무리 오목해도 중복이 생기지 않는다.

## 개선점

### 1. (수정 완료 / 치명) 같은 덩어리가 한 열에 두 번 걸치면 크기가 중복 합산됐다 — `logic-edge-case`

이전 코드는 `dist[j][i] >= 1 && dist[j-1][i] == 0`(직전 행이 0 → 이 행이 석유)일 때만 더하는
"열별 새 구간" 판정이었다. 이 판정은 **그 구간이 이미 더한 덩어리와 같은 덩어리인지**를 구분하지
못한다. 실제로 돌려본 반례:

```java
land = { {1,1,0,0},
         {0,1,0,0},
         {0,1,0,0},
         {1,1,0,0},
         {0,1,0,0} };
// 전체가 열1로 이어진 덩어리 하나(칸 7개)
// 수정 전 출력: 14   기대: 7
```

열0에서 (0,0)과 (3,0) 두 곳이 각각 "직전 행이 0" 조건에 걸려 같은 덩어리(크기 7)를 두 번 더했다.

수정 후 구조:

```java
for(int col=0; col<m; col++) {
    Set<Integer> seen = new HashSet<>();
    int sum = 0;
    for(int row=0; row<n; row++) {
        int id = compId[row][col];
        if(id != 0 && seen.add(id)) sum += compSize.get(id);
    }
    maxSum = Math.max(maxSum, sum);
}
```

같이 정리한 것: `bfs` 가 `cnt` 를 static 으로 들고 다니며 두 번째 큐(`qq`)로 되칠하던 구조를,
ID 를 칠하면서 세는 한 번의 순회로 합쳤다.

## 검증

실제로 컴파일해서 돌렸다.

```
공식 예제 1 -> 9  (기대 9)
공식 예제 2 -> 16 (기대 16)
위 반례      -> 7  (기대 7)
```

## 복잡도

- 시간: `O(n*m)` — 라벨링 BFS 한 번 + 열 순회 한 번. 500×500 제약에서 여유 있다.
- 공간: `O(n*m)` — compId, vis.

## 요약

BFS 로 덩어리 크기를 구하는 뼈대는 처음부터 맞았다. "열마다 새 구간이냐"가 아니라 "이 덩어리를
이 열에서 이미 셌냐"로 기준을 바꾸는 것이 핵심이었고, 그건 덩어리에 ID 를 붙이는 순간 자연스럽게 따라온다.
