---
platform: swea
problemId: "2105"
author: seongil
source: 이성일/week6/SWEA2105.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [dead-code, boxing-cost, redundant-loop, good-complexity]
complexity:
  time: O(T·N⁴)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 디저트 카페 (swea/2105) — seongil

## 접근

사각 경로를 좌표 계산으로 만들지 않고 **"방향은 0→1→2→3 으로만 흐른다" 는 규칙 하나로 DFS 를 제약**했다.

```java
for (int i = dir; i <= dir + 1 && i < 4; i++)
```

같은 방향으로 더 가거나(`i == dir`) 다음 방향으로 꺾거나(`i == dir+1`) 둘뿐이고, 뒤로는 못 간다.
네 변의 길이를 이중 루프로 정하는 흔한 풀이보다 경계 조건이 훨씬 적고, "네 번 꺾으면 끝" 이라는 문제 조건이
코드에 그대로 드러난다. 종료 판정도 `i == 3 && nx == startX && ny == startY` 한 줄로 붙는다.

종료 판정을 `set.contains` **앞**에 둔 것도 정확하다. 시작점의 디저트는 이미 `set` 에 들어 있어서
순서가 바뀌면 모든 경로가 중복으로 잘려 답이 항상 -1 이 된다. 놓치기 쉬운 지점이다.

**정확성은 확인했다.** 위 꼭짓점과 두 변 길이로 마름모를 직접 만들어 검사하는 완전탐색 레퍼런스와
무작위 400건(`N ≤ 10`)을 대조해 **전부 일치**했다. 카페가 없을 때 `-1` 을 내는 것도 같았다.

## 개선점

### 1. (치명) `package coding;` + `public class SWEA2105` 로는 SWEA 에 제출이 안 된다 — <dead-code>

SWEA 는 default package 에 `public class Solution` 을 요구한다. 로컬 `javac` 는 클래스명에 맞춘 임시
파일로 돌리기 때문에 `compiles: true` 로 나오지만, **채점 서버에서는 컴파일 단계에서 막힌다.**

```java
// 지우고
package coding;
public class SWEA2105 {

// 이렇게
public class Solution {
```

`SWEA1953.java`, `다리만들기2.java` 도 같은 상태다. 이번 주차에서 반복되는 유일한 패턴이라, 제출 직전에
한 번 훑는 것만으로 세 문제가 같이 해결된다.

### 2. (중요) `HashSet<Integer>` 대신 `boolean[101]` — <boxing-cost>

디저트 번호는 1~100 의 작은 정수다. 그런데 `Set<Integer>` 를 쓰면 DFS 의 모든 간선마다
`add`/`remove`/`contains` 세 번이 오토박싱 + 해시 계산을 거친다. DFS 가 도는 횟수가 이 문제에서
제일 큰 값(N⁴ 급)이라, 상수배가 그대로 실행 시간에 실린다.

```java
static boolean[] used = new boolean[101];   // 디저트 번호 1..100

// dfs 안
if (used[board[nx][ny]]) continue;
used[board[nx][ny]] = true;
dfs(nx, ny, i, board, dessertCnt + 1);
used[board[nx][ny]] = false;
```

배열은 케이스마다 새로 만들지 말고 `Arrays.fill(used, false)` 로 초기화하면 된다.
`set` 인자도 사라져서 시그니처가 짧아진다.

**검증 안 함** — 위 교체본은 돌려보지 않았다. 다만 `set` 의 쓰임이 `add`/`remove`/`contains` 세 곳뿐이라
치환 범위는 좁다.

### 3. (사소) `inSharps` 는 매번 4칸을 훑는다 — <redundant-loop>

```java
if (inSharps(i, j, sharps)) continue;
```

시작점 후보 N² 개마다 배열 4개를 선형 탐색한다. 그런데 이 검사가 하는 일은 "네 모서리를 시작점에서 뺀다"
뿐이고, 모서리에서는 어차피 첫 대각선 이동이 범위 밖이라 `inRange` 에 걸려 곧바로 끝난다.
즉 **없어도 답이 같다** (400건 대조 레퍼런스에는 이 예외가 없는데 결과가 일치했다).
남겨 둘 거라면 조건식 한 줄로 충분하다.

```java
if ((i == 0 || i == N - 1) && (j == 0 || j == N - 1)) continue;
```

### 4. (사소) `sharps` 를 케이스마다 새로 만든다 — <redundant-collection>

`int[][] sharps = {...}` 가 테스트케이스 루프 안에 있어서 매번 새로 할당된다. 3번대로 조건식으로 바꾸면
같이 없어진다.

## 복잡도

- 시간: `O(T·N⁴)` — 시작점 N² × 그 지점에서 가능한 사각 경로 수. N ≤ 20 이라 여유롭다
- 공간: `O(N)` — DFS 재귀 깊이와 방문 집합 (경로 길이에 비례)

## 요약

방향 인덱스를 단조 증가로만 두는 제약 하나로 사각 경로 탐색의 경계 조건을 거의 다 없앤 게 좋다.
종료 판정을 중복 검사보다 앞에 둔 것까지 정확해서, 무작위 400건 대조에서 전부 일치했다.
남은 건 알고리즘 밖이다 — `package` + 클래스명 때문에 SWEA 에 제출이 안 되고,
가장 안쪽 루프에서 `Set<Integer>` 를 쓰는 탓에 박싱 비용이 N⁴ 에 곱해진다.
