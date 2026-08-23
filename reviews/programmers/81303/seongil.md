---
platform: programmers
problemId: "81303"
author: seongil
source: 이성일/week3/표편집.java
week: 3
compiles: true
verdict: needs-fix
tags: [collection-choice, duplicate-code, magic-branch, good-complexity]
complexity:
  time: O(n + m)
  space: O(n)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — seongil

## 접근

`Node` 객체에 `prev` / `val` / `next` 를 담아 **이중 연결 리스트를 객체로 구현**했다.
`nodes[]` 배열로 인덱스 접근도 열어두고, 삭제된 노드는 `spares` 리스트에 쌓아 되돌린다.

삭제할 때 **노드 자신의 `prev`/`next` 는 건드리지 않고 이웃만 재연결한 것**이 정확하다.
그 덕에 `Z` 에서 별도 정보 없이 `restore.prev.next = restore` 만으로 복원된다.
이 문제의 핵심을 짚었다.

**정답 시뮬레이터와 무작위 600케이스를 대조해 불일치 0건**이고, 속도도 충분하다. `good-complexity`

```
n=1,000,000 / 명령 200,000개
  정답(배열 방식)   18ms
  이 파일          20ms
  김준수 실패 버전   15초 초과
```

## 개선점

### 1. (중요) `spares.removeLast()` 는 Java 21 이상에서만 있다 — `collection-choice`

```java
Node restore = spares.removeLast();
```

`List.removeLast()` 는 **Java 21에서 추가된 `SequencedCollection` 메서드**다.
로컬은 JDK 26이라 컴파일되지만, **채점 서버의 Java 버전이 21 미만이면 컴파일 에러가 난다.**

제출해서 통과했다면 문제없다. 아직 안 해봤다면 이 줄이 첫 번째 의심 대상이다.
버전을 안 타는 형태는 이렇다.

```java
Node restore = spares.remove(spares.size() - 1);
```

애초에 되돌리기는 스택이라 `Deque` 가 의미에 맞다. `ArrayDeque` 로 두면 `pop()` 한 줄이고
버전 문제도 없다.

```java
Deque<Node> spares = new ArrayDeque<>();
... spares.push(currNode);  ...  Node restore = spares.pop();
```

### 2. (중요) 마지막 집계가 현재 위치에서 거슬러 올라간다 — `magic-branch`

```java
Node node = currNode;
while (node.prev != null) node = node.prev;      // 머리까지 거슬러 올라가고
...
while (node.next != null) { oxArray[node.val] = 'O'; node = node.next; }
oxArray[node.val] = 'O';                          // 마지막 노드는 루프 밖에서 따로
```

동작은 맞다. 다만 **마지막 노드 처리가 루프 밖으로 새어 나와 있어서**
"왜 한 번 더 쓰지?" 를 되짚게 만든다. 조건을 노드 자체로 바꾸면 한 덩어리가 된다.

```java
for (Node x = head; x != null; x = x.next) oxArray[x.val] = 'O';
```

그리고 애초에 **머리를 찾아 거슬러 올라갈 필요가 없다.** 삭제된 노드는 `spares` 에 다 있으므로
전부 `'O'` 로 채우고 `spares` 에 남은 것만 `'X'` 로 덮으면 끝이다.

```java
char[] out = new char[n];
Arrays.fill(out, 'O');
for (Node d : spares) out[d.val] = 'X';
```

`이승주` 는 아예 삭제 스택만으로 집계했고, `김준수` 정답 버전도 같은 방식이다.

### 3. (사소) 초기 연결 루프의 분기가 과하다 — `magic-branch`

```java
for(int i = 0; i < n; i++) {
    if (i == 0)   { nodes[i].next = nodes[i+1]; nodes[i].prev = null; continue; }
    if (i == n-1) { nodes[i].prev = nodes[i-1]; nodes[i].next = null; break; }
    nodes[i].prev = nodes[i-1];
    nodes[i].next = nodes[i+1];
}
```

`Node` 필드는 기본값이 `null` 이므로 **`prev = null`, `next = null` 대입은 필요 없다.**
경계만 조건으로 걸면 세 갈래가 한 줄로 줄어든다.

```java
for (int i = 0; i < n; i++) {
    if (i > 0)     nodes[i].prev = nodes[i - 1];
    if (i < n - 1) nodes[i].next = nodes[i + 1];
}
```

`n == 1` 일 때 첫 분기의 `nodes[i+1]` 이 범위를 벗어나는 위험도 같이 사라진다.
(지금 코드는 `n=1` 이면 `i==0` 분기에서 `nodes[1]` 을 읽어 예외가 난다. 문제 조건상 `n >= 3` 이라
실제로는 안 걸리지만, 코드만 봐서는 안전한지 알 수 없다.)

### 4. (사소) `C` 와 `Z` 의 분기가 서로 베껴져 있다 — `duplicate-code`

```java
if (currNode.next == null && currNode.prev == null) break;
if (currNode.next == null)      { ... }
else if (currNode.prev == null) { ... }
else                            { ... }
```

`Z` 쪽도 같은 모양의 3분기다. 이웃이 `null` 인지만 각각 확인하면 분기가 필요 없다.

```java
// C
if (currNode.prev != null) currNode.prev.next = currNode.next;
if (currNode.next != null) currNode.next.prev = currNode.prev;
currNode = currNode.next != null ? currNode.next : currNode.prev;

// Z
if (restore.prev != null) restore.prev.next = restore;
if (restore.next != null) restore.next.prev = restore;
```

여섯 갈래가 네 줄이 된다. `김준수` 정답 버전이 이 형태다.

### 5. (사소) `s.split(" ")[1]` 이 명령마다 배열을 만든다

```java
int x = Integer.parseInt(s.split(" ")[1]);
```

`split` 은 정규식을 돌리고 배열을 새로 만든다. 명령이 20만 개면 그만큼 쌓인다.
실측 20ms라 통과에는 지장이 없지만, `s.substring(2)` 면 할당이 없다.

## 복잡도

- 시간: `O(n + m)` — 노드 생성 `O(n)`, 명령당 포인터 조작 `O(1)`.
  마지막 집계에서 머리까지 거슬러 올라가는 `O(n)` 이 한 번 더 붙는다
- 공간: `O(n)` — 노드 객체 `n` 개. 배열 방식(`김준수`)보다 상수가 크다

## 요약

이중 연결 리스트를 객체로 정확히 구현했고, 삭제 노드의 포인터를 남겨 복구에 재사용하는
핵심 아이디어를 짚었다. 600케이스 오답 0건, n=100만에서 20ms.
가장 급한 건 `spares.removeLast()` 의 Java 21 의존성이다 — `ArrayDeque` 로 바꾸면
버전 문제와 함께 `Z` 분기도 정리된다.
