---
platform: programmers
problemId: "87694"
author: seongil
source: 이성일/week2/아이템줍기.java
week: 2
compiles: true
verdict: wrong
tags: [wrong-algorithm, uninitialized-state, logic-edge-case]
complexity:
  time: O(S^2)
  space: O(S^2)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 아이템 줍기 (programmers/87694) — seongil

## 접근

세 단계로 나눈 구성이 명확합니다. ①직사각형 내부를 `-1` 로 칠하고 ②전체를 훑어 테두리가 아닌 칸을 `-2` 로 막고 ③남은 칸만 BFS로 걷습니다. 단계마다 주석으로 의도를 적어둬서 읽는 사람이 따라가기 쉽습니다.

문제는 **②번의 판정식**입니다. 지금은 "8방향 이웃에 `-1`(내부)이 하나도 없으면 막는다" 인데, 실제 테두리는 "채워진 칸인데 8방향 이웃에 **빈 칸**이 있다" 입니다. 둘은 다른 조건이고, 그 차이가 아래 오답으로 이어집니다.

**검증**: 좌표 2배 격자에서 테두리 BFS를 도는 독립 구현을 기준으로 무작위 3,000건(직사각형 1~3개, 좌표 1~10)을 대조했습니다. 같은 입력을 `references/programmers/87694/dwinging.java` 에도 넣어 기준 구현이 맞는지 교차 확인했습니다.

| 대상 | 불일치 |
|---|---|
| 이성일 | **883 / 3,000** |
| dwinging 정석코드 | 0 / 3,000 |

## 개선점

### 1. (치명) `board` 가 `static` 이라 다음 테스트케이스가 앞 케이스의 판을 물려받는다 — uninitialized-state

```java
public static int[][] board = new int[51][51];
```

`solution()` 안에서 `board` 를 비우는 코드가 없습니다. 채점 서버는 테스트케이스마다 `Solution` 인스턴스를 새로 만들지만 **static 필드는 JVM 안에서 계속 살아 있습니다.** 앞 케이스가 칠해둔 `-1`·`-2`·`1` 이 그대로 남아서 다음 케이스가 오염된 판 위에서 돕니다.

실측입니다. 같은 입력으로 `solution()` 을 연속 두 번 호출했더니 **3,000건 중 1,518건에서 1회차와 2회차의 반환값이 달랐습니다.**

고치는 법은 `static` 을 떼고 인스턴스 필드로 두는 것입니다.

```java
private int[][] board = new int[102][102];   // static 제거
```

### 2. (치명) 테두리 판정식이 이음매를 막지 못한다 — wrong-algorithm

두 직사각형이 맞붙으면 그 이음매 칸은 양쪽 내부와 이웃하므로 `flag` 가 `false` 가 되지 않아 **걸을 수 있는 칸으로 남습니다.** 하지만 이음매는 도형 안쪽이라 실제로는 지나갈 수 없습니다.

실제로 돌려본 반례:

```
rectangle = [[3,2,6,3], [6,2,7,5]]
characterX,Y = (6,2)   itemX,Y = (6,3)
기대 출력: 7      실제 출력: 1
```

두 직사각형은 `x=6` 선에서 맞닿습니다. 이 코드는 그 선을 타고 `(6,2) -> (6,3)` 으로 한 칸에 내려가 `1` 을 냅니다. 실제로는 도형 바깥 테두리를 빙 돌아야 하므로 `7` 입니다.

### 3. (치명) 좌표를 2배로 늘리지 않아 테두리를 가로지른다 — wrong-algorithm

테두리가 자기 자신과 맞닿는 지점에서 BFS가 대각선으로 새는 문제입니다. 실제로 돌려본 반례:

```
rectangle = [[6,6,10,7], [4,4,5,6], [2,6,6,8]]
characterX,Y = (4,8)   itemX,Y = (5,6)
기대 출력: 11     실제 출력: 5
```

### 4. (치명) 폭이나 높이가 1인 도형에서 테두리가 통째로 사라진다 — logic-edge-case

```java
for (int i = (rec[0] + 1); i < rec[2]; i++)
```

`rec[0]+1 == rec[2]` 이면 이 루프가 한 번도 안 돕니다. 즉 `[[4,5,5,9]]` 처럼 폭이 1인 직사각형은 내부가 비어 `-1` 이 하나도 안 생기고, ②단계에서 **모든 칸이 `-2` 로 막힙니다.** BFS가 출발점에서 못 움직이고 `answer` 가 초기값 그대로 반환됩니다.

```
rectangle = [[4,5,5,9]]
characterX,Y = (5,5)   itemX,Y = (4,9)
기대 출력: 5      실제 출력: 2147483647   (= Integer.MAX_VALUE)
```

도달 못 했을 때 `Integer.MAX_VALUE` 가 그대로 나가는 것도 같이 짚어둡니다. 3번을 적용해 좌표를 2배로 만들면 폭 1짜리도 2배 격자에서는 내부가 생겨서 이 문제는 자동으로 사라집니다.

### 5. 수정안 — 검증했습니다

원본의 3단계 구성을 그대로 두고 세 군데만 고쳤습니다.

```java
public class Solution {
    private static final int SIZE = 102;
    private int[][] board = new int[SIZE][SIZE];   // (1) static 제거
    static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    static int[] checkDx = {-1,-1,0,1,1,1,0,-1}, checkDy = {0,1,1,1,0,-1,-1,-1};

    public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
        // 1. 테두리까지 포함해서 전부 채운다 (좌표는 2배)   (3)
        for (int[] rec : rectangle)
            for (int i = rec[0] * 2; i <= rec[2] * 2; i++)
                for (int j = rec[1] * 2; j <= rec[3] * 2; j++)
                    board[i][j] = 1;

        // 2. 채워진 칸 중 8방향에 빈 칸이 있으면 테두리   (2)
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] != 1) continue;
                for (int d = 0; d < 8; d++) {
                    int x = i + checkDx[d], y = j + checkDy[d];
                    if (!inRange(x, y) || board[x][y] == 0) { board[i][j] = 2; break; }
                }
            }

        // 3. 테두리(2)만 밟는 BFS
        ArrayDeque<Point> deq = new ArrayDeque<>();
        deq.add(new Point(characterX * 2, characterY * 2, 0));
        board[characterX * 2][characterY * 2] = 3;
        while (!deq.isEmpty()) {
            Point p = deq.poll();
            if (p.r == itemX * 2 && p.c == itemY * 2) return p.moving / 2;   // 2배로 걸었으니 절반
            for (int i = 0; i < 4; i++) {
                int x = p.r + dx[i], y = p.c + dy[i];
                if (!inRange(x, y) || board[x][y] != 2) continue;
                board[x][y] = 3;
                deq.add(new Point(x, y, p.moving + 1));
            }
        }
        return -1;
    }
}
```

바뀐 곳은 세 군데입니다.

1. `static int[][] board` → `private int[][] board` (인스턴스 필드)
2. 채우는 범위가 내부(`rec[0]+1 .. rec[2]-1`)가 아니라 **전체**(`rec[0]*2 .. rec[2]*2`), 테두리 판정이 "내부와 이웃" → **"빈 칸과 이웃"**
3. 모든 좌표 `*2`, 반환할 때 `/2`

이 판본을 무작위 입력으로 다시 돌렸습니다. 서로 다른 seed 3개, 직사각형 1~4개짜리 입력입니다.

```
checked=3753 | 불일치=0 | 상태 오염=0
checked=3727 | 불일치=0 | 상태 오염=0
checked=3769 | 불일치=0 | 상태 오염=0
```

합계 **11,249건 전부 일치**했습니다.

> 참고로 처음에 (1)과 (3)만 고치고 (2)를 그대로 뒀더니 2,278건 중 107건이 여전히 틀렸습니다. 이음매 문제는 좌표 2배로는 안 없어지고, 판정식을 바꿔야 사라집니다.

## 복잡도

`S` 를 격자 한 변(좌표 2배 후 102)이라 할 때:

- 시간: `O(S^2)` — 테두리 판정이 전체 격자를 한 번 훑고(8방향 상수배), BFS도 각 칸을 한 번씩 봅니다. 직사각형 채우기는 `O(직사각형 수 * 넓이)` 로 최대 4 * 101 * 101 수준입니다. 전부 합쳐 10만 연산대라 여유가 큽니다.
- 공간: `O(S^2)` — 격자 하나. 원본의 `51*51` 은 좌표 2배를 하면 `102*102` 로 늘려야 합니다.

## 요약

3단계로 나눈 뼈대와 주석은 좋습니다. 다만 테두리를 "내부와 이웃한 칸" 으로 정의한 것이 이 풀이의 핵심 결함이고, 여기에 `static board` 미초기화와 좌표 2배 누락이 겹쳐 무작위 3,000건 중 883건이 틀렸습니다. 위 수정안 세 군데를 적용하면 11,249건 전부 통과합니다 — 구조를 갈아엎을 필요는 없습니다.
