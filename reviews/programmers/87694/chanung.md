---
platform: programmers
problemId: "87694"
author: chanung
source: 안찬웅/week2/아이템줍기.java
week: 2
compiles: false
verdict: unattempted
tags: []
complexity:
  time: —
  space: —
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 아이템 줍기 (programmers/87694) — chanung

## 접근

풀이 대신 포기 선언이 들어 있습니다.

```java
public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
  System.out.println("모르겠습니다..");
  System.out.println("나중에 다시 도전하겠습니다.");
}
```

`int` 를 반환해야 하는데 `return` 이 없어서 컴파일도 되지 않습니다 — `javac` 실측 결과입니다.

```
안찬웅/week2/아이템줍기.java:16: error: missing return statement
```

알고리즘이 없으므로 지적할 코드가 없습니다. 대신 주석에 적어둔 계획을 보면 어디서 막혔는지가 드러납니다.

```
1. 직사각형의 모든 점들을 찍는다.
2. 지나가는곳이 어딘지 어케알지     <- 여기서 멈춤
3. BFS로 돌리면서 최단거리 구하기
```

1번과 3번은 맞습니다. 막힌 건 2번, **"테두리가 어디인가"** 입니다.

## 다음에 볼 것

두 가지만 알면 풀립니다.

1. **테두리 판정** — 직사각형들을 전부 채운 뒤, *채워진 칸 중에서 8방향 이웃에 빈 칸이 하나라도 있는 칸* 이 테두리입니다. "안쪽 칸의 이웃" 이 아니라 "빈 칸과 맞닿은 칸" 이라는 점이 중요합니다. 두 직사각형이 맞붙은 이음매는 채워져 있지만 빈 칸과 안 닿으므로 테두리가 아니고, 그래서 지나갈 수 없습니다.

2. **좌표 2배** — 좌표를 그대로 쓰면 테두리가 자기 자신과 맞닿는 지점에서 BFS가 가로질러 버려 실제보다 짧은 답이 나옵니다. 모든 좌표에 2를 곱해 격자를 늘리고, 마지막에 이동 횟수를 2로 나누면 사라집니다. (같은 문제를 푼 이성일 코드가 이 두 가지를 빠뜨려서 무작위 대조 3,000건 중 883건이 틀렸습니다 — `reviews/programmers/87694/seongil.md` 참고.)

같은 문제의 정석 코드가 `references/programmers/87694/dwinging.java` 에 있습니다. 사이트 문제 상세에서 `정석코드 비교` 버튼으로 볼 수 있고, 저자가 쓴 풀이 설명도 같이 붙어 있습니다.

## 요약

미제출입니다. 접근 1·3번은 맞게 잡았고 2번(테두리 판정)에서 멈췄습니다. 위 두 가지를 보고 다시 도전하면 됩니다.
