---
platform: swea
problemId: "1251"
author: junsoo
source: 김준수/week1/SWEA1251.java
week: 1
compiles: true
verdict: needs-fix
tags: [redundant-collection, redundant-loop]
complexity:
  time: O(N^2 log N)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 하나로 (swea/1251) — junsoo

## 접근

모든 섬 쌍을 간선으로 만들어 비용(거리^2 × tax) 순으로 정렬한 뒤, 유니온 파인드로 크루스칼 MST를 구성한다.
`find`/`union`을 별도 static 메서드로 분리하고 경로 압축까지 넣은 구조가 깔끔하다. 상단 주석에 전략과
"크루스칼/유니온 파인드 기초부터 학습했다"는 시행착오를 적어둔 것도 코드 의도를 파악하는 데 도움이 된다.
간선 개수가 `N-1`에 도달하면 조기 종료하는 것도 문제 조건(N개 정점 연결에 필요한 최소 간선 수)을 정확히 반영했다.

## 개선점

### 1. (중요) 이미 있는 배열을 다시 `List<List<Integer>>`로 감싼다 — `redundant-collection`

```java
List<List<Integer>> islands = new ArrayList<>();
...
islands.add(new ArrayList<>());
islands.get(i).add(xList[i]);
islands.get(i).add(yList[i]);
```

`xList`, `yList` 에 이미 좌표가 다 들어 있는데, 그걸 다시 `ArrayList<ArrayList<Integer>>` 로 복사해 담고
이후 `islands.get(i).get(0)`, `islands.get(i).get(1)` 로 꺼내 쓴다. 원본 배열을 직접 쓰면 이 구조 전체가
필요 없다.

```java
double length = Math.pow(xList[i] - xList[j], 2) + Math.pow(yList[i] - yList[j], 2);
```

`islands` 선언(36행)과 채우는 루프(55~59행)를 통째로 지우고 69~70행만 위처럼 바꾸면 된다. 부가 효과로
`Integer` 오토박싱/언박싱과 `get()` 호출 비용도 같이 없어진다.

### 2. (사소) 거리를 `sqrt`로 구했다가 다시 제곱한다 — `redundant-loop`

> 정확히는 "반복문 중복"이 아니라 "합칠 수 있는 중복 계산"이라 태그 목록에서 가장 가까운 `redundant-loop`
> 를 붙였다. 목록에 이 케이스에 딱 맞는 태그는 없다.

```java
double length = Math.pow(dx, 2);
length += Math.pow(dy, 2);
length = Math.sqrt(length);          // 여기서 실제 거리로 만들었다가
edges.add(new Edge(i, j, length * length * tax));  // 바로 다시 제곱해서 비용 계산에 씀
```

비용식은 `거리^2 × tax` 이므로 애초에 `dx^2 + dy^2` 값 자체가 필요하다. `sqrt` 를 거쳐 실수 거리를
만들었다가 다시 제곱하는 건 불필요한 `Math.sqrt` 호출일 뿐 아니라, 부동소수점 왕복 오차를
추가로 섞는다(무리수 거리를 sqrt로 근사한 뒤 제곱하면 원래 정수/정수 합 값과 완전히 같다는 보장이 없다).
출력이 `Math.round` 로 반올림되므로, 극단적으로 합계가 `.4999...`/`.5000...` 경계에 걸리면 이 오차가
결과를 바꿀 가능성이 이론적으로는 있다. 실제로 이 경계에 걸리는 입력을 만들어 확인해보지는 못했다 —
**검증 안 함.**

```java
double sq = Math.pow(xList[i] - xList[j], 2) + Math.pow(yList[i] - yList[j], 2);
edges.add(new Edge(i, j, sq * tax));
```

이렇게 하면 `sqrt` 호출이 아예 사라지고 오차 소스도 하나 준다.

## 복잡도

- 시간: `O(N^2 log N)` — 모든 섬 쌍으로 간선을 만들고(`O(N^2)`) 정렬하는 비용이 지배적. `data/problems.json`
  에 이 문제 URL이 없어 N의 정확한 상한은 확인 못 했지만, SWEA 하나로 문제는 N이 크지 않아(수백 이내)
  문제 되지 않는다.
- 공간: `O(N^2)` — 간선 리스트가 `N(N-1)/2` 개.

## 요약

크루스칼 + 유니온 파인드 뼈대는 정확하고 조기 종료 조건도 맞다. 정답을 못 낼 만한 로직 오류는 찾지 못했다.
다만 이미 있는 좌표 배열을 리스트로 다시 감싸는 불필요한 자료구조와, `sqrt` 후 재제곱하는 불필요한(그리고
정밀도상 살짝 위험한) 계산이 남아 있어 정리하면 좋다.
</content>
