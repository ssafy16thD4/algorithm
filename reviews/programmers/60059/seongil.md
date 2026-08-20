---
platform: programmers
problemId: "60059"
author: seongil
source: 이성일/week2/자물쇠와열쇠.java
week: 2
compiles: true
verdict: wrong
tags: [wrong-algorithm, uninitialized-state]
complexity:
  time: O(8 * M^4) (M = key 한 변의 길이, 방문 비교가 M^2)
  space: O(방문 상태 수 * M^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 자물쇠와 열쇠 (programmers/60059) — 이성일

## 접근

열쇠를 회전 3가지 + 이동 4가지, 총 7가지 변형을 DFS로 시도하며 `verify()`로 자물쇠와 맞는지 검사하는 백트래킹 구조다. 같은 모양을 다시 탐색하지 않으려고 `visited` 리스트를 두는 아이디어, 그리고 회전을 3번 더 하면 원위치로 돌아온다는 관찰(`rotate(key,3)`으로 undo)은 맞는 접근이다.

## 개선점

### 1. (치명) 자물쇠 바깥으로 열쇠가 삐져나가는 배치를 아예 시도하지 않음 — `wrong-algorithm`

```java
for (int i = 0; i <= (lock.length - key.length); i++) {
    for (int j = 0; j <= (lock.length - key.length); j++) {
```

`verify()`가 열쇠를 자물쇠 크기(`lock.length`) 안에 완전히 들어가는 위치에만 대 본다. 이 문제의 표준 풀이는 자물쇠 배열을 `key.length - 1`만큼 상하좌우로 패딩(0으로 확장)해서, 열쇠가 자물쇠 경계 밖으로 걸쳐 있는 배치까지 검사해야 한다. 이 코드는 그 패딩이 없어서 애초에 정답이 되는 배치를 후보에서 제외한다.

**공식 예제 1**로 직접 컴파일·실행해서 확인했다:

```
key  = {{0,0,0},{1,0,0},{0,1,1}}
lock = {{1,1,1},{1,1,0},{1,0,1}}
```

기대값 `true`, 실제 출력 `false`.

### 2. (치명) `visited`에 배열 참조를 그대로 저장 — `uninitialized-state`

```java
visited.add(key);
```

`key`는 `rotate()`/`move()`가 **제자리에서** 원소를 바꾸는 같은 배열 객체다(새 배열을 만들어 반환하지 않는다). 그런데 `visited`에는 이 배열의 복사본이 아니라 참조 자체를 넣기 때문에, 이후 `key`가 계속 변형되면 `visited`에 들어있는 "과거 상태"들도 전부 같이 바뀐다. `isVisited()`가 비교하는 대상이 사실상 전부 "현재 key 자기 자신"이 되어버려 방문 판정이 의미를 잃는다. `move()` 분기의 `int[][] temp = key;`도 마찬가지로 얕은 참조 복사라 되돌리기(undo) 역할을 하지 못한다 — `temp`와 `key`가 같은 배열이므로 `move()`가 이미 그 배열을 변형한 뒤라 `key = temp;`는 아무것도 복원하지 못한다.

이 두 번째 문제는 1번(패딩 누락)이 고쳐지지 않는 한 답에 영향을 주는지 별도로 확인하기 어려워 **수정안은 제시하지 않는다.** 다만 `visited.add(new int[][]{...} 깊은 복사)`와 `int[][] temp = deepCopy(key)`가 필요하다는 점은 코드 구조상 명확하다.

## 복잡도

- 시간: `O(8 * M^4)` — 7가지 변형 × 매번 `M x M` 전체 위치에서 `M x M` 비교
- 공간: `O(상태 수 * M^2)` — `visited`에 매번 배열(참조)을 쌓음

## 요약

회전/이동을 DFS로 완전탐색한다는 뼈대는 맞지만, 자물쇠 패딩을 빼먹어 공식 예제 1부터 오답이 난다. 여기에 `key` 배열을 복사하지 않고 참조만 저장하는 문제까지 있어 `visited`도 제 역할을 못 한다 — 좌표계(패딩)와 상태 저장(깊은 복사) 둘 다 다시 짜야 한다.
