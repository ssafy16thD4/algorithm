---
platform: swea
problemId: "5648"
author: junsoo
source: 김준수/week5/원자 소멸 시뮬레이션 실패.java
week: 5
compiles: false
verdict: needs-fix
tags: [time-complexity, space-complexity, redundant-collection]
complexity:
  time: O(T × 4000 × N)
  space: O(4001^2)
generatedBy: claude-code-local
generatedAt: 2026-08-31
---

# 원자 소멸 시뮬레이션 (swea/5648) — junsoo (실패 버전)

## 접근

좌표를 **2배로 늘려** 격자에 올린 뒤(`2000 + 값*2`) 원자를 한 칸씩 움직이며 충돌을 처리한다. 본인 표기대로 정확도는 통과했고 시간이 모자란 케이스다.

**2배 스케일링은 이 문제의 정석 처리다.** 원자는 0.5 단위 위치에서도 만날 수 있는데, 좌표를 2배로 두면 그 중간 지점이 정수 격자 칸이 되어 **정면 충돌이 자연스럽게 같은 칸에서 잡힌다.** 이걸 놓치면 마주 오는 두 원자가 서로를 통과해버린다.

주석의 시행착오도 정확하다 — "둘씩 소멸시키면 3개가 만날 때 1개가 남는다"를 발견하고 `deleteBoard` 로 **그 칸이 이번 턴에 이미 폭발했음**을 표시해, 뒤늦게 도착하는 세 번째 원자도 함께 소멸시키도록 고쳤다. 다중 충돌을 다루는 올바른 방향이다.

## 개선점

### 1. (치명) `usedLoc` 이 "지금까지 지나간 모든 칸"을 계속 쌓는다 — `redundant-collection`

```java
public void move() {
    ...
    board[nx][ny] = id;
    usedLoc.add(new int[] {nx, ny});   // 이동할 때마다 한 건씩
```

원자가 **한 칸 움직일 때마다** `int[2]` 객체가 하나씩 리스트에 들어간다. 마지막에 `board` 를 0으로 되돌리려고 모아두는 것인데, 최대 4000스텝 × 원자 수만큼 쌓인다. 원자가 1000개면 **400만 개의 `int[2]` 객체**가 테스트케이스 하나에서 생성된다. 할당 비용과 GC 압력이 그대로 실행 시간이 된다. 시간초과의 가장 큰 원인으로 보인다.

**되돌릴 필요를 없애는 게 낫다.** 칸마다 "몇 번째 테스트케이스에서 쓴 값인지"를 같이 적으면 초기화가 아예 사라진다:

```java
static int[][] board = new int[4001][4001];
static int[][] stamp = new int[4001][4001];   // 이 칸을 마지막으로 쓴 테스트케이스 번호
static int tc;                                 // 현재 테스트케이스 번호

// 읽을 때:  int v = (stamp[x][y] == tc) ? board[x][y] : 0;
// 쓸 때:    board[x][y] = id;  stamp[x][y] = tc;
```

`usedLoc` 도, 끝에서 도는 초기화 루프도 통째로 없어진다.

### 2. (중요) 소멸한 원자를 계속 순회한다 — `time-complexity`

```java
while(moveCnt++ <= 4000) {
    for(Atom a : atomList) {
        if(!a.isDelete) a.move();
    }
```

`atomList` 는 줄어들지 않으므로, 원자가 대부분 사라진 뒤에도 매 스텝 `N`번을 전부 돌며 `isDelete` 만 확인한다. 4000스텝 × N이 고정 비용이 된다.

살아 있는 원자만 다음 스텝으로 넘기면 뒤로 갈수록 빨라진다:

```java
List<Atom> alive = atomList;
for (int step = 0; step < 4000 && !alive.isEmpty(); step++) {
    for (Atom a : alive) a.move();
    List<Atom> next = new ArrayList<>();
    for (Atom a : alive) if (!a.isDelete) next.add(a);
    alive = next;
}
```

원자가 판을 벗어나면서 빠르게 줄어들기 때문에 실제 이득이 크다.

### 3. (중요) `int[4001][4001]` 두 장이 메모리를 크게 먹는다 — `space-complexity`

`board` 가 `int[4001][4001]` 로 약 **64MB**, `deleteBoard` 가 `boolean[4001][4001]` 로 약 **16MB**다. 합쳐 80MB에 행 배열 오버헤드까지 붙는다. SWEA 메모리 제한에 따라 이것만으로 걸릴 수 있다.

원자는 최대 1000개뿐이라 **격자가 거의 비어 있다.** 좌표를 키로 하는 `HashMap<Long, List<Atom>>` 을 스텝마다 새로 만들어 같은 칸에 모인 원자를 묶는 방식이면 메모리가 `O(살아있는 원자 수)` 로 떨어지고, 다중 충돌 처리도 "리스트 크기가 2 이상이면 전부 소멸"로 단순해진다 — `deleteBoard` 라는 우회 장치 자체가 필요 없어진다.

```java
Map<Long, List<Atom>> cells = new HashMap<>();
for (Atom a : alive) {
    a.x += dx[a.dir];  a.y += dy[a.dir];
    if (밖으로 나감) { a.isDelete = true; continue; }
    cells.computeIfAbsent(a.x * 100000L + a.y, k -> new ArrayList<>()).add(a);
}
for (List<Atom> group : cells.values()) {
    if (group.size() >= 2) {
        for (Atom a : group) { result += a.energy; a.isDelete = true; }
    }
}
```

### 4. (사소) 파일명이 `public class SWEA5648` 과 다르다 — `naming`

`compiles: false` 의 원인은 이것뿐이다.

## 복잡도

- 시간: `O(T × 4000 × N)` 에 더해 **이동 횟수만큼의 객체 할당**이 붙는다. 후자가 지배적이다.
- 공간: `O(4001^2)` ≈ 80MB — 원자 수(≤1000)에 비해 과도하다.

## 요약

2배 스케일링으로 정면 충돌을 격자 위에서 잡은 것과, 다중 충돌을 `deleteBoard` 로 해결한 것은 이 문제의 어려운 두 부분을 제대로 넘은 것이다. 정확도가 통과한 이유다. 남은 문제는 전부 **비용** 쪽이고, 그중 `usedLoc` 에 이동 경로를 전부 쌓는 것이 가장 크다 — 테스트케이스 번호 스탬프로 바꾸면 그 리스트와 초기화 루프가 함께 사라진다. 격자 대신 좌표 해시맵으로 가면 3번까지 한 번에 정리된다.
