---
platform: boj
problemId: "17472"
author: chanung
source: 안찬웅/week6/다리만들기2 실패.java
week: 6
compiles: true
lang: java
verdict: good
tags: [good-decomposition, naming]
complexity:
  time: O(N*M*(N+M) + E log E)
  space: O(N*M + K^2)
generatedBy: claude-code-local
generatedAt: 2026-09-06
---

# 다리 만들기 2 (boj/17472) — chanung (실패 버전)

## 접근

`실패` 접미사가 붙어 있고 주석에도 "구현 전혀 실패 Gpt 옮기기.." 라고 적혀 있지만,
**이 파일에 담긴 코드는 실제로 맞는 답을 낸다.** 그러니 이 리뷰는 "왜 틀렸나" 가 아니라
"이 코드에서 뭘 읽어가면 다음엔 직접 쓸 수 있나" 로 쓴다.

구조는 주석에 적어둔 3단계 그대로다. BFS 로 섬마다 2번부터 라벨을 칠하고(`bfs`),
모든 섬 칸에서 4방향으로 직진하며 바다를 2칸 이상 지나 다른 섬을 만나면 다리 후보로 등록하고
(`buildingEdges`), 길이 오름차순 크루스칼로 MST 를 만든다(`kruskal`).
후보를 `lens[from][to]` 2차원 배열에 **최솟값만** 눌러 담은 게 좋은 판단이다 —
같은 섬 쌍 사이 다리는 어차피 제일 짧은 것 하나만 쓰이므로 간선 개수가 K^2 로 묶인다.

## 개선점

### 1. (중요) 클래스 이름이 `Solution`, 패키지가 `algorithm` 이라 BOJ 제출이 안 된다 — <naming>

BOJ 는 public 클래스가 `Main` 이어야 하고 패키지 선언을 허용하지 않는다.
SWEA 습관(`package algorithm` + `public class Solution`)이 그대로 넘어왔다.
로컬 javac 는 통과하지만 채점 서버에서는 컴파일 단계에서 막힌다.

```java
// package algorithm;      ← 제거
public class Main {         ← Solution → Main
```

### 2. (사소) 섬 라벨을 2부터 시작해서 배열 크기가 전부 `islandCnt + 2` 가 됐다 — <magic-number>

`bfs(i, j, islandCnt + 1)` 로 첫 섬이 2번이 되는 바람에 `p`, `s`, `lens` 가 모두
`islandCnt + 2` 크기이고 반복문도 `for (int i = 2; i <= islandCnt + 1; i++)` 로 어긋난다.
바다(0)·미방문 육지(1)와 라벨을 구분하려는 의도는 맞지만, `+1` / `+2` / `<=` 가 섞여 있어
한 칸만 틀려도 조용히 섬 하나를 빼먹는다. **라벨을 그대로 두되 상수로 이름을 붙이면**
읽는 사람이 매번 셈을 다시 하지 않아도 된다.

```java
static final int FIRST_LABEL = 2;
static int lastLabel() { return islandCnt + FIRST_LABEL - 1; }
// for (int i = FIRST_LABEL; i <= lastLabel(); i++)
```

### 3. (참고) 섬이 1개일 때 `-1` 을 반환하지만 이 문제에서는 걸리지 않는다

`kruskal()` 은 `usedEdges == islandCnt - 1` 일 때만 `mstCost` 를 반환하므로
섬이 1개면 간선이 없어 `-1` 로 떨어진다(정답은 0). 다만 **BOJ 17472 는 섬이 2개 이상 6개 이하**로
보장되므로 실제 채점에서는 문제가 되지 않는다. 다른 문제에 이 골격을 재사용할 때만 조심하면 된다.

## 복잡도

- 시간: `O(N*M*(N+M) + E log E)` — 지배적인 건 후보 생성이다. 모든 섬 칸에서 4방향으로 최대 N 또는 M 칸을 직진한다. N, M ≤ 10 이라 사실상 상수. 간선은 섬 쌍당 1개라 E ≤ 15
- 공간: `O(N*M + K^2)` — 지도 2장(`graph`, `vis`)과 섬 쌍 거리표 `lens`. K ≤ 6 이라 거리표는 무시할 수준

## 요약

**"실패" 라는 파일명과 달리 이 코드는 정답이다.** 무작위 지도 496건을 독립 구현
(행·열을 한 줄씩 훑어 후보를 만들고 프림으로 MST)과 대조해 불일치 0건,
같은 문제를 푼 `이성일/week6/다리만들기2.java` 와도 435건 대조해 불일치 0건이었다.
실제로 막히는 건 알고리즘이 아니라 제출 형식(`Solution`/`package algorithm`) 하나뿐이다.

주석의 "어떻게 만들면 좋을지 까진 생각 ok / 구현 전혀 실패" 가 정확한 자기 진단이다.
설계는 이미 맞았으니 다음엔 **`buildingEdges` 한 함수만 직접 써보는 것**을 권한다.
BFS 라벨링과 크루스칼은 이미 다른 문제에서 여러 번 쓴 도구고, 이 문제의 진짜 새 부분은
"바다를 2칸 이상 직진해 다른 섬에 닿는 구간"을 빠짐없이 세는 그 스캔 하나다.
