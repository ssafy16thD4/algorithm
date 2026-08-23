---
platform: programmers
problemId: "81303"
author: junsoo
source: 김준수/week3/표 편집.java
week: 3
compiles: true
verdict: good
tags: [good-complexity, good-readability, magic-number]
complexity:
  time: O(n + m) — 명령당 O(1)
  space: O(n)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — junsoo

## 접근

배열 두 개(`frontPointers`, `backPointers`)로 **이중 연결 리스트를 직접 구현**하고,
삭제된 행은 스택에 쌓아 되돌린다. 이 문제의 정석이다.

주석의 시행착오 기록이 정확하다.

> java의 LinkedList, ArrayList는 각 아이템마다 Node를 생성하고 조작해야 해서 remove, insert 작업이 오래 걸림

같은 폴더의 실패 버전이 `ArrayList` 로 짠 것이고, **실측으로 그 판단이 맞다는 게 확인됐다.**

```
n=1,000,000 / 명령 200,000개
  junsoo(이 파일)   26ms
  실패 버전         15초 초과 (중단)
```

핵심은 `delete()` 에서 **삭제 노드의 포인터를 지우지 않고 이웃만 재연결한 것**이다.

```java
// current는 건드리지 않고, 앞 뒤 행끼리만 연결
if(frontPointers[current] != -1) backPointers[frontPointers[current]] = backPointers[current];
if(backPointers[current] != -1) frontPointers[backPointers[current]] = frontPointers[current];
```

삭제된 노드가 자기 이웃을 그대로 기억하고 있으니 `recover()` 가 그 정보만으로 복원된다.
"되돌리기"를 위해 별도 자료구조를 안 만들어도 되는 이유가 여기 있고, 주석에도 그 의도가 적혀 있다.

**정답 시뮬레이터와 무작위 600케이스를 대조해 불일치 0건이다.** `good-complexity`

## 개선점

### 1. (사소) 결과 문자열을 두 번 훑는다 — `magic-number`

```java
StringBuilder sb = new StringBuilder();
for(int i = 0; i < n; i++) sb.append("O");

while(!stack.isEmpty()){
    int del = stack.pop();
    sb.setCharAt(del, 'X');
}
```

`n` 만큼 `"O"` 를 채우고 스택을 비우며 `X` 로 덮는다. 정확하고, `n=100만` 에서도 26ms라 문제없다.

다만 `sb.append("O")` 는 문자열 리터럴이라 매번 `String` 을 다룬다. `sb.append('O')` (문자)가 더 싸고,
`"O".repeat(n)` 을 `char[]` 로 받으면 루프 자체가 없어진다.

```java
char[] out = new char[n];
Arrays.fill(out, 'O');
while (!stack.isEmpty()) out[stack.pop()] = 'X';
return new String(out);
```

### 2. (사소) `stack` 을 다 비워버려서 재사용이 안 된다

결과를 만들 때 `stack.pop()` 으로 스택을 소진한다. 지금은 마지막 단계라 상관없지만,
"삭제된 행 목록"이라는 정보가 사라진다. 위 1번처럼 `for (int d : stack)` 로 순회만 하면 남는다.

### 3. (사소) `frontPointers` / `backPointers` 이름

`front` 가 위쪽인지 앞쪽인지, `back` 이 아래쪽인지 헷갈린다. 실제로는 위/아래다.
`up` / `down` 이나 `prev` / `next` 가 이 문제의 어휘에 더 가깝다.

`-1` 이 "없음"을 뜻하는 것도 상수로 빼면 조건문이 읽기 쉬워진다.

### 4. (사소) `static` 필드에 상태를 둔다

```java
static int[] frontPointers;
static int[] backPointers;
static int current;
static Deque<Integer> stack;
```

`solution` 첫머리에서 전부 새로 만들기 때문에 지금은 안전하다.
다만 `delete()` / `recover()` 는 인스턴스 메서드인데 필드는 `static` 이라 섞여 있다.
`solution` 안의 지역 변수로 내리고 두 메서드에 넘기면 생명주기가 한눈에 보인다.

## 복잡도

- 시간: `O(n + m)` — 초기화 `O(n)`, 명령 하나당 포인터 조작 `O(1)`.
  `U x` / `D x` 는 `x` 만큼 도니 최악 `O(n)` 이지만 총합은 이동 거리로 묶인다
- 공간: `O(n)` — 포인터 배열 두 개와 스택

## 요약

`ArrayList` 로 시작해 시간초과를 만나고, 배열 이중 연결 리스트로 옮겨온 과정이 주석에 남아 있고
그 판단이 실측으로 확인된다(26ms vs 15초 초과). 600케이스 대조 오답 0건.
남은 건 전부 다듬기 수준이다.
