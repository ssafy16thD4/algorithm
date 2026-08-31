---
platform: swea
problemId: "1949"
author: junsoo
source: 김준수/week5/등산로 조성.java
week: 5
compiles: false
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(봉우리수 × 4^(경로길이))
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 등산로 조성 (swea/1949) — junsoo

## 접근

최고 높이 봉우리를 모두 모아 각각에서 DFS를 돌리고, `isCut` 파라미터로 "이 경로에서 이미 깎았는지"를 들고 다닌다. 깎기를 별도의 분기로 만들지 않고, **한 칸을 이동할 때마다 "안 깎고 갈 수 있는가"와 "깎으면 갈 수 있는가"를 각각 재귀호출**하는 구조가 깔끔하다.

특히 좋은 판단은 깎는 높이를 `board[nx][ny] = board[x][y] - 1` 로 **한 값만 시도**한 것이다. 깎는 양을 1..K 전부 시도하면 K배가 붙는데, 등산로는 아래로만 내려가므로 통과 가능한 선에서 **가장 높게 남기는 것이 항상 최적**이다. 뒤에 올 칸들이 더 낮아야 한다는 제약이 느슨해지기 때문이다. 이 한 줄이 탐색 공간을 K배 줄인다.

깎기 조건 `board[nx][ny] >= board[x][y] && board[nx][ny] - K < board[x][y]` 도 정확하다. 두 번째 식은 `next - K <= cur - 1`, 즉 "K 이내로 깎아서 `cur-1`까지 내릴 수 있는가"와 같은 말이다.

**검증**: 안찬웅·이성일의 통과 코드와 함께 무작위 입력 60건(N=3~7, K=1~4)을 대조했고 **3개 구현이 전부 일치**했다.

## 개선점

### 1. (사소) 파일명이 `public class SWEA1949` 와 달라 그대로는 컴파일되지 않는다 — `naming`

`compiles: false` 는 알고리즘 문제가 아니라 순전히 파일명 때문이다. 실제 오류는 이것 하나다:

```
error: class SWEA1949 is public, should be declared in a file named SWEA1949.java
```

`등산로 조성.java` 로 저장되어 있는데 안에 `public class SWEA1949` 가 있다. 파일명을 `SWEA1949.java` 로 바꾸거나 `public` 을 떼면 **경고 없이 그대로 컴파일된다** (확인함). 팀 규칙상 풀이 파일명은 건드리지 않기로 했으니, 제출 시점에만 맞춰주면 되는 문제다.

### 2. (사소) `if(depth >= result) result = depth;` 의 `>=` 는 `>` 로 충분 — `magic-branch`

같은 값일 때 다시 대입하는 것뿐이라 결과는 같다. 다만 읽는 사람이 "같을 때도 갱신해야 하는 이유가 있나?" 하고 한 번 멈추게 된다.

## 복잡도

- 시간: `O(봉우리수 × 4^(경로길이))` — 깎기를 1..K 전부 시도하지 않고 한 값으로 고정해서 K가 지수에서 빠졌다. N ≤ 8이라 충분하다.
- 공간: `O(N^2)` — `board`, `visited` 두 장. 백트래킹이라 경로마다 배열을 복사하지 않는다.

## 요약

세 구현 중 가장 효율적이다. 깎는 높이를 `cur-1` 하나로 고정한 판단이 핵심이고, 백트래킹으로 상태 복사를 아예 없앤 것도 좋다. 컴파일 실패는 파일명뿐이고 로직에는 지적할 것이 없다.
