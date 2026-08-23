---
platform: programmers
problemId: "42861"
author: chanung
source: 안찬웅/week4/섬 연결하기.java
week: 4
compiles: true
verdict: good
tags: [good-complexity, good-readability]
complexity:
  time: O(E log E)
  space: O(V)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 섬 연결하기 (programmers/42861) — chanung

## 접근

크루스칼입니다. 간선을 비용 오름차순으로 정렬하고 유니온-파인드로 사이클을 걸러내며 싼 것부터 집습니다. 맨 위 주석에 알고리즘·자료구조·3단계 절차가 적혀 있어서 코드를 읽기 전에 방향이 잡힙니다.

두 가지가 특히 잘 돼 있습니다.

- **`union` 이 `boolean` 을 반환합니다.** 합쳐졌으면 `true`, 이미 같은 덩어리면 `false`. 덕분에 호출부가 `if(union(a,b)) { answer += cost; ... }` 한 줄로 끝나고, `find(a) != find(b)` 를 밖에서 또 부르지 않습니다.
- **간선을 `n-1`개 채우면 즉시 `break`** 합니다. 정답에는 영향이 없지만 뒤쪽 비싼 간선을 헛돌지 않습니다.

경로 압축(`return parent[x] = find(parent[x])`)도 들어가 있습니다.

**검증**: 프림 알고리즘으로 독립 구현한 기준과 무작위 4,000건(n=2~7, 트리 간선 + 무작위 추가 간선, 중복 간선 포함)을 대조했습니다. **불일치 0건**입니다.

## 개선점

### 1. (사소) 유니온 바이 랭크가 없다

`parent[b] = a` 로 항상 `a` 쪽에 붙입니다. 경로 압축이 있어서 실사용에는 문제가 없지만, 랭크나 크기를 같이 들면 트리가 한쪽으로 기우는 걸 막을 수 있습니다.

```java
static int[] parent, size;
static boolean union(int a, int b) {
    a = find(a); b = find(b);
    if (a == b) return false;
    if (size[a] < size[b]) { int t = a; a = b; b = t; }
    parent[b] = a;
    size[a] += size[b];
    return true;
}
```

이 문제는 `n <= 100` 이라 체감 차이가 없습니다. 습관 차원의 지적입니다.

### 2. (사소) `Arrays.sort(costs, (a,b) -> a[2] - b[2])` 는 뺄셈 비교다

비용이 `1 이상 100 이하`라 여기서는 절대 오버플로가 나지 않습니다. 다만 값이 `int` 전 범위를 쓰는 문제에서 이 습관이 그대로 나오면 음수가 뒤집힙니다. `Integer.compare(a[2], b[2])` 가 항상 안전합니다.

### 3. (사소) 입력 배열을 제자리 정렬한다

`Arrays.sort(costs, ...)` 는 호출자가 넘긴 배열을 직접 뒤집습니다. 채점 환경에서는 문제가 없지만, 인자를 건드리지 않는 편이 안전한 습관입니다. 이 문제에서는 그대로 둬도 됩니다.

### 4. (사소) `parent` 가 `static` 이다

`solution()` 안에서 매번 새로 만들고 초기화하므로 **테스트케이스 간 오염은 없습니다.** 다만 `find`/`union` 을 `static` 으로 유지하려다 필드까지 `static` 이 된 형태라, 인자로 넘기면 `static` 이 전부 사라집니다. 같은 주차의 `이성일/week4/여행경로.java` 는 `static` 필드 하나를 초기화하지 않아 실제로 오답이 났습니다 — 그 대비로 보면 여기서 매번 초기화한 건 제대로 한 것입니다.

## 복잡도

- 시간: `O(E log E)` — 정렬이 지배적입니다. `n <= 100` 이므로 `E <= 4,950`.
- 공간: `O(V)` — `parent` 배열 하나. 그래프를 따로 만들지 않습니다.

## 요약

크루스칼의 교과서적 형태입니다. `union` 이 성공 여부를 반환하게 만든 것과 `n-1`개에서 끊는 것 둘 다 정확하고, 프림 기준 무작위 4,000건 대조에서도 오답이 없었습니다. 지적은 전부 습관 차원이라 지금 그대로 제출해도 됩니다.
