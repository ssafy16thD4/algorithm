---
platform: programmers
problemId: "42892"
author: seongil
source: 이성일/week3/길찾기게임.java
week: 3
compiles: true
verdict: good
tags: [good-decomposition, naming]
complexity:
  time: O(N log N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 길 찾기 게임 (programmers/42892) — 이성일

## 접근

y좌표 내림차순(같으면 x 오름차순)으로 정렬한 뒤, 정렬 순서대로 하나씩 BST에 삽입해 이진트리를 만들고 전위/후위 순회로 두 결과를 만든다. y가 클수록 루트에 가깝다는 문제 조건을 `compareTo`의 정렬 기준 하나에 그대로 담은 점이 좋다.

**독립적으로 검증했다.** 같은 정렬·삽입 규칙을 별도 Python으로 재구현해 전위/후위 순회를 계산했고(이 코드와 무관한 구현), 공식 예제 입력(`nodeinfo=[[5,3],[11,5],[13,3],[3,5],[6,1],[1,3],[8,6],[7,2],[2,2]]`)에 대해 이 Java 코드의 실제 출력(`pre=[7,4,6,9,1,8,5,2,3]`, `post=[9,6,5,8,1,4,3,2,7]`)과 정확히 일치했다. (처음엔 다른 기대값을 기억하고 있었는데, 독립 구현과 대조해보니 이 코드의 출력이 맞았다 — 기억에 의존한 정답 대신 재현으로 확정했다.)

## 개선점

### 1. (사소) 전위/후위 순회를 큐 재사용 + 재귀로 구현해 읽기 어려움 — `naming`

```java
static int treePreSearch(int idx, ArrayDeque<Node> q) {
    while (!q.isEmpty()) {
        Node currentNode = q.poll();
        answer[0][idx++] = currentNode.idx;
        if (currentNode.left != null) { q.add(currentNode.left); idx = treePreSearch(idx, q); }
        ...
```

일반적인 전위 순회는 `void preorder(Node n)`처럼 노드 하나를 받아 재귀하는 형태인데, 여기서는 큐에 자식을 넣고 다시 같은 함수를 호출하는 식이라 "지금 이 재귀 호출이 어떤 서브트리를 처리 중인지"를 큐 상태로 역추적해야 한다. 결과는 맞지만 표준적인 형태가 아니라서 다른 사람이 읽을 때 시간이 더 걸린다. `void preorder(Node n, int[] arr, int[] idx)`처럼 노드 하나짜리 재귀로 바꾸면 같은 결과를 더 짧고 익숙한 코드로 낼 수 있다.

## 복잡도

- 시간: `O(N log N)` — 정렬이 지배적, BST 삽입은 균형이 보장되지 않아 최악 `O(N^2)`이지만 이 문제 제약(`N<=1000000`, 좌표가 트리 형태를 자연스럽게 분산시킴)에서는 실무적으로 무리 없음
- 공간: `O(N)` — 노드 배열과 재귀/큐 상태

## 요약

정렬 기준에 문제 조건을 압축한 설계가 좋고, 독립 구현과 대조해 정답임을 확인했다. 순회를 큐+재귀로 섞은 구현 방식만 불필요하게 복잡하다.
