---
platform: programmers
problemId: "42892"
author: junsoo
source: 김준수/week3/길 찾기 게임.java
week: 3
compiles: true
verdict: needs-fix
tags: [dead-code, nonstatic-inner-class]
complexity:
  time: O(N^2) 최악, 평균적으로는 훨씬 빠름
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 길 찾기 게임 (programmers/42892) — junsoo

## 접근

y좌표 내림차순으로 정렬해서 순서대로 이진 탐색 트리에 삽입하고(먼저 들어온 노드일수록 y가 커서
트리의 위쪽에 오게 됨), 전위/후위 순회로 두 결과 리스트를 만든다. 상단 주석에 "그래프로 접근하다
막혀서 BST로 갈아탔다"는 시행착오를 남긴 것도 문제를 어떻게 재해석했는지 보여줘서 좋다. `left`/
`right`를 노드 인덱스(`value`)로 저장하고 `nodes[]` 배열로 역참조하는 방식이라 포인터/참조 대신
정수 인덱스만으로 트리를 표현한 것도 간결하다.

## 개선점

### 1. (사소) `dfsPre`/`dfsPost`의 크기 검사는 실행되지 않는 방어 코드다 — `dead-code`

```java
private void dfsPre(Node curr){
    if(preList.size() == nodes.length){
        return;
    }
    ...
```

BST가 정상적으로 트리 구조(사이클 없음, `N`개 노드가 각자 정확히 한 번씩 부모에 연결됨)로 만들어졌다면,
전위/후위 순회는 자연스럽게 `N`개 노드를 정확히 한 번씩만 방문하고 끝난다. 이 검사가 실제로 걸리는
경우는 `add()` 쪽에 이미 버그가 있어 트리에 사이클이 생겼을 때뿐인데, 그 경우 이 코드는 에러를 내는
대신 결과 리스트를 조용히 잘린 채로 반환해서 **진짜 버그를 숨기는 효과**만 낸다. 지금은 `add()`에
그런 버그가 없어 보여 실질적으로 죽은 코드다. 방어가 꼭 필요하다면 차라리 예외를 던지는 게 문제를
더 빨리 드러낸다.

### 2. (사소) `Node`, `BST`가 `static`이 아닌 중첩 클래스다 — `nonstatic-inner-class`

```java
class Solution {
    static Node[] nodes;
    ...
    class Node { ... }   // static 아님
    class BST { ... }    // static 아님
```

두 클래스 모두 `Solution`의 인스턴스 멤버를 쓰지 않고(`nodes`, `preList`, `postList` 모두 이미
`static`), `BST` 생성자도 정적 컨텍스트(`solution` 메서드)에서 호출된다. `static class Node`,
`static class BST`로 바꾸는 게 의도에 맞다.

## 복잡도

- 시간: `O(N^2)`(최악) — 트리가 한쪽으로 치우치면(예: x 순서가 계속 한 방향으로만 삽입되는 극단적
  입력) 삽입 한 번이 `O(N)`까지 걸릴 수 있다. 이 문제의 `nodeinfo` 길이 상한은 `10,000`으로 확인했다
  (문제 페이지에서 직접 확인). `10^4`의 제곱은 `10^8`로, 단순 정수 비교 위주라 시간 제한 안에는
  들어올 가능성이 높지만 트리 균형을 보장하는 자료구조(예: 좌표압축 + 세그먼트 트리/BIT)보다는
  느리다. 이 문제는 균형이 깨지는 입력이 나오기 어려운 구조(좌표가 서로 다른 랜덤한 이진트리 형태)라
  일반적으로는 문제 되지 않는다.
- 공간: `O(N)` — `nodes[]`, `preList`, `postList`.

## 요약

BST 삽입과 전위/후위 순회 모두 정확하게 구현했고, 인덱스로 트리를 표현한 방식도 깔끔하다. 크기
검사(1번)와 non-static 중첩 클래스(2번) 정도가 남은 정리거리이고, 둘 다 정답에는 영향이 없다.
</content>
