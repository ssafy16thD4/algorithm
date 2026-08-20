---
platform: swea
problemId: "5643"
author: seongil
source: 이성일/week1/SWEA5643.java
week: 1
compiles: true
verdict: wrong
tags: [uninitialized-state, good-decomposition]
complexity:
  time: O(T * N^2) (테스트케이스별로 독립적으로 유지된다는 전제 하에)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 키 순서 (swea/5643) — 이성일

## 접근

`bigger`/`smaller` 두 집합을 노드마다 두고, 간선 `(small, big)`이 들어올 때마다 "small보다 작은 모든 노드 → big", "big보다 큰 모든 노드 → small과 small보다 작은 모든 노드"로 관계를 즉시 전파하는 **증분(incremental) 추이적 폐쇄(transitive closure)** 방식이다. Floyd-Warshall처럼 매번 전체를 다시 계산하지 않고 간선 하나가 들어올 때 필요한 부분만 갱신한다.

이 갱신 로직 자체는 **검증했다.** 별도 Python으로 무작위 DAG를 만들어 Floyd-Warshall로 정답을 구하고(밀도 높은 6~8노드 3세트, `n=6,m=13` / `n=7,m=18` / `n=8,m=20`), TC 사이에 `bigger`/`smaller`를 초기화하는 것만 고친 버전으로 같은 입력을 돌렸더니 세 케이스 모두 `[4, 5, 4]`로 정확히 일치했다. `process()`의 4단계 갱신(양방향 대칭) 자체는 옳다.

## 개선점

### 1. (치명) 테스트케이스 간 `bigger`/`smaller` 미초기화 — `uninitialized-state`

```java
for (int i = 0; i < n; i++) {
    bigger.add(new HashSet<>());
    smaller.add(new HashSet<>());
}
```

`bigger`, `smaller`는 `static List`인데 TC가 바뀌어도 `clear()`를 호출하지 않는다. TC2에서 `n`개를 새로 `add`하면 리스트 뒤쪽에 쌓이고, `process()`는 여전히 `bigger.get(small-1)`처럼 **0부터** 인덱싱하므로 TC1에서 쓰던(TC1의 관계가 남아있는) 앞쪽 엔트리를 다시 참조하게 된다. 즉 TC가 2개 이상이면 TC2부터는 사실상 이전 TC의 오염된 데이터 위에서 계산한다.

**실제로 재현했다.** 같은 밀집 랜덤 입력 3개(정답 `[4, 5, 4]`)를 원본 코드 그대로(초기화 없이) 3-TC 입력으로 돌리면:

```
#1 4
#2 1
#3 0
```

TC1만 우연히 맞고 TC2, TC3는 완전히 틀린다. `clear()` 두 줄만 추가하면(검증한 수정안):

```java
bigger.clear();
smaller.clear();
for (int i = 0; i < n; i++) {
    bigger.add(new HashSet<>());
    smaller.add(new HashSet<>());
}
```

같은 입력에서 `#1 4 / #2 5 / #3 4`로 정확히 나온다. SWEA 문제는 관례상 TC가 2개 이상인 경우가 대부분이라 이 버그는 실제 채점에서 거의 확실히 드러난다.

## 복잡도

- 시간: `O(T * N^2)` — TC별로 간선 처리와 최종 카운트가 노드 수의 제곱 수준
- 공간: `O(N^2)` — 최악의 경우 각 노드의 `bigger`/`smaller` 합이 N에 근접

## 요약

관계 전파 알고리즘 자체는 창의적이고 정확하다(무작위 대조로 확인). 하지만 TC 간 정적 리스트를 비우지 않아 두 번째 TC부터 답이 틀어진다 — 로직이 아니라 초기화 누락이 문제라 `clear()` 두 줄로 해결된다.
