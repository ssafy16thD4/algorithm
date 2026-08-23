---
platform: programmers
problemId: "250136"
author: chanung
source: 안찬웅/pccp/석유 시추.java
week: null
compiles: true
verdict: wrong
tags: [logic-edge-case]
complexity:
  time: O(n*m)
  space: O(n*m)
generatedBy: claude-code-local
generatedAt: 2026-08-21
---

# [PCCP 기출문제] 2번 / 석유 시추 (programmers/250136) — chanung

## 접근

BFS로 각 연결된 석유 덩어리를 찾아 크기를 `dist[r][c]`(덩어리에 속한 모든 칸에 같은 값)에 채운 뒤,
열마다 `dist[0][i]` 를 시작값으로 삼고 `dist[j][i]>=1 && dist[j-1][i]==0` (직전 행이 0에서 이 행이
석유로 바뀌는 지점)일 때만 `dist[j][i]` 를 더해 "그 열에서 새로 시작되는 구간"만 합산하려 한 시도다.
공식 예제 2개는 통과한다.

## 개선점

### 1. (치명) 같은 덩어리가 한 열에 두 번 이상 걸치면 크기가 중복 합산된다 — `logic-edge-case`

"직전 행이 0이면 새 구간"이라는 판정은 **그 구간이 이미 합산에 들어간 덩어리와 같은 덩어리인지**를
구분하지 못한다. 하나의 연결된 덩어리가 어떤 열을 세로로 두 번(중간에 다른 칸이 끼어) 지나가면,
그 열에서 `dist[j][i]` 값(=덩어리 전체 크기)이 두 번 더해진다.

실제로 컴파일해서 돌려본 반례:

```java
land = {
  {1,1,0,0},
  {0,1,0,0},
  {0,1,0,0},
  {1,1,0,0},
  {0,1,0,0}
};
```

전체가 열1로 연결된 하나의 덩어리(칸 7개)다. 시추관이 열0을 지나갈 때, 열0의 (0,0)과 (3,0) 두 곳에서
"직전 행이 0" 조건이 각각 성립해 같은 덩어리(크기7)가 두 번(0행 시작값 + 3행에서 재합산) 더해진다.

```
실제 출력: 14
기대 출력: 7   (열1을 뚫어도, 열0을 뚫어도 같은 덩어리 하나만 얻는다)
```

같은 입력을 김준수·이승주 풀이에 넣으면 둘 다 7을 반환한다 (교차 검증 완료).

수정하려면 "열별로 새 구간인지"가 아니라 "이 열에서 이 덩어리를 이미 셌는지"를 덩어리 단위로
추적해야 한다. 가장 간단한 방법은 열마다 별도로 BFS/DFS를 도는 것 — 김준수·이승주 풀이가 쓴 방식이다.
아래처럼 "덩어리 ID"를 두고 열별로 이미 합산한 ID를 Set에 기록하는 방법도 가능하다.

```java
// dist[r][c] 대신 compId[r][c] (덩어리 고유 ID)와 compSize[] (ID -> 크기)를 따로 둔 뒤
for (int col = 0; col < m; col++) {
    Set<Integer> seen = new HashSet<>();
    int sum = 0;
    for (int row = 0; row < n; row++) {
        if (land[row][col] == 1 && seen.add(compId[row][col])) {
            sum += compSize[compId[row][col]];
        }
    }
    maxSum = Math.max(maxSum, sum);
}
```

## 복잡도

- 시간: `O(n*m)` — BFS 한 번 + 열 순회 한 번. 접근 자체는 제약(500×500)에서 문제없다.
- 공간: `O(n*m)` — dist, vis 배열.

## 요약

BFS로 덩어리 크기를 구하는 뼈대는 맞다. 다만 "열별 합산"을 행 전이(0→1)로만 판단해서, 같은 덩어리가
한 열을 여러 구간으로 지나가는 모양(오목한 덩어리)에서 중복 합산되어 오답이 난다.
