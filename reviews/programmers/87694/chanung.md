---
platform: programmers
problemId: "87694"
author: chanung
source: 안찬웅/week2/아이템줍기.java
week: 2
compiles: false
verdict: wrong
tags: [missing-return]
complexity:
  time: 미측정
  space: 미측정
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 아이템 줍기 (programmers/87694) — chanung

## 접근

구현 시도가 없다. 본문은 포기 메시지 두 줄뿐이고 알고리즘은 짜여 있지 않다.

## 개선점

### 1. (치명) 반환문이 없어 컴파일 자체가 안 된다 — `missing-return`

```java
public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
  System.out.println("모르겠습니다..");
  System.out.println("나중에 다시 도전하겠습니다.");
}
```

`int`를 반환해야 하는데 `return`이 없어 `missing return statement`로 컴파일 실패한다.

### 2. (치명) BFS 자체가 구현되지 않았다

주석의 "1. 직사각형의 모든 점들을 찍는다 → 2. 지나가는곳이 어딘지 어케알지" 까지는 방향을 제대로
잡았다. 막힌 지점은 "겉면만 걸을 수 있다"는 조건을 격자에 어떻게 표시하느냐인데, 이 문제는 좌표를
2배로 늘린 격자(`x*2, y*2`)에서 각 직사각형의 "테두리"만 이동 가능 칸으로 표시하고, 내부(꽉 찬
사각형 영역)는 막힌 칸으로 처리한 뒤 BFS를 돌리는 트릭이 필요하다. 다음에 다시 볼 때 "좌표 압축/
2배 확장 + 경계선만 마킹" 키워드로 찾아보면 된다.

## 복잡도

- 시간: 미측정 — 구현이 없어 측정 대상이 없다.
- 공간: 미측정

## 요약

방향은 잡았지만(직사각형 좌표 → BFS) 실제 구현으로 이어지지 못한 미완성 코드다. 겉면만 걷는 조건을
격자에 표시하는 방법(좌표 2배 확장 트릭)이 다음 시도의 핵심이다.
