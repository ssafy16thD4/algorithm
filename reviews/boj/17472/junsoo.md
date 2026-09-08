---
platform: boj
problemId: "17472"
author: junsoo
source: 김준수/week6/다리만들기2.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [logic-edge-case, naming, dead-code, good-decomposition]
complexity:
  time: O(I^2 * (N*M)^2)
  space: O(N*M + I^2)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 다리 만들기 2 (boj/17472) — junsoo

## 접근

파일 맨 위 주석에 적은 대로 섬을 노드, 다리를 간선으로 번역해서 MST 로 푼다. 코드도 그 순서 그대로다.

1. `findIslandBfs()` — 섬마다 번호를 매기고 좌표 목록을 `Island` 에 담는다. 번호를 `board` 에 덮어써서
   나중에 직선을 쏘다 만난 칸의 값이 곧 상대 섬 번호가 된다.
2. `getDist(p, dir, j)` — 섬 `i` 의 모든 칸에서 4방향 직선을 쏴 섬 `j` 까지 바다 칸 수를 잰다.
   다른 섬에 막히거나 격자 밖으로 나가면 `-1`.
3. 섬 쌍마다 최솟값 하나를 `Edge` 로 등록 → 정렬 → `kruskal()` (union-find).
4. 간선이 `섬수 - 1` 개 안 모이면 `-1`.

"시행착오" 에 적힌 십자가 섬 문제(섬 전체가 `visited` 를 공유하는 BFS 는 안 된다) 를 짚고
직선 하나씩 독립적으로 걷는 `getDist` 로 바꾼 판단이 맞다. 구조 분리도 좋다.

**실제로 돌려본 검증**: 라벨링 → 직선 간선 수집 → 크루스칼을 독립적으로 짠 레퍼런스와
무작위 800건(`N,M ≤ 9`, 밀도 0.2~0.6)을 대조했더니 **72건이 불일치**했다. 원인은 아래 1번 하나다.

## 개선점

### 1. (치명) 길이 1 짜리 직선이 하나라도 있으면 그 섬 쌍의 간선을 통째로 버린다 — <logic-edge-case>

`main` 에서 방향·시작칸을 전부 돌며 **길이 조건 없이** 최솟값을 잡는다.

```java
int dist = getDist(p, k, j);
if (dist != -1 && dist < minDist) minDist = dist;   // 길이 0·1 도 최솟값이 된다
```

그 뒤 `kruskal()` 이 `e.cost < 2` 인 간선을 건너뛴다. 즉 어떤 두 섬 사이에 길이 1 인 자리와 길이 2 이상인
자리가 **둘 다** 있으면, 최솟값은 1 이 되고 그 간선은 크루스칼에서 버려진다 — 실제로는 길이 2 다리가
가능한데도 연결 불가로 처리된다.

실제로 돌려본 반례:

```
3 4
1 0 1 1
1 0 0 1
1 0 0 1
```

0행에서는 두 섬 사이가 바다 1칸, 1·2행에서는 2칸이다. 기대 출력 `2`, 이 코드 출력 `-1`.

고치는 건 한 줄이다. 길이 필터를 크루스칼이 아니라 **최솟값을 잡는 자리**에서 건다.

```java
if (dist >= 2 && dist < minDist) minDist = dist;
```

**검증함** — 이 한 줄만 바꿔 같은 800건을 다시 돌리니 **불일치 0건**, 위 반례도 `2`.
(`kruskal()` 의 `e.cost < 2` 검사는 그대로 둬도 무해하다.)

### 2. (중요) `package algorithm;` + `public class 다리만들기2` 로는 BOJ 에 제출이 안 된다 — <naming>

BOJ Java 는 default package 의 `public class Main` 을 요구한다. 로컬 `javac` 는 클래스명에 맞춘 임시 파일로
돌려서 `compiles: true` 지만 채점 서버는 컴파일 단계에서 막는다. `게리맨더링.java` 와 같은 지적이다.

```java
// package algorithm;
public class Main {
```

### 3. (사소) `getDistBfs()` 는 어디서도 호출되지 않는다 — <dead-code>

주석에 "실패 코드" 라고 적힌 60줄이 살아 있는 메서드로 남아 있다. 컴파일·실행에는 영향이 없지만,
읽는 사람이 이 함수도 로직의 일부인지 한 번 추적하게 만든다. 기록으로 남기려면 주석 처리하거나
별도 파일로 빼는 편이 낫다.

## 복잡도

- 시간: `O(I^2 * (N*M)^2)` — 섬 쌍(≤ 15) × 시작칸(≤ 100) × 4방향마다 `getDist` 가 `new int[N][M]` 을
  잡고 목표 섬 칸을 표시한다. N, M ≤ 10 이라 전부 합쳐도 10^6 이하 — 문제없다
- 공간: `O(N*M + I^2)` — 격자 + 간선 목록

## 요약

번역(섬 → 노드, 다리 → 간선 → MST)과 구조는 맞고, 십자가 섬 함정도 스스로 잡았다. 틀린 건 딱 하나 —
**길이 1 인 직선이 그 섬 쌍의 유효한 다리까지 덮어쓴다.** `dist >= 2` 필터를 최솟값 갱신 자리에 넣으면
무작위 800건 전부 정답이다. 제출하려면 `Main` 클래스명도 같이 고쳐야 한다.
