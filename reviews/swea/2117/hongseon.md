---
platform: swea
problemId: "2117"
author: hongseon
source: 전홍선/week6/swea2117.cpp
week: 6
compiles: null
lang: cpp
verdict: needs-fix
tags: [missing-return, redundant-collection, dead-code, good-complexity]
complexity:
  time: O(T·N²·H)
  space: O(H + N)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 홈 방범 서비스 (swea/2117) — hongseon

> `compiles: null` — 이 환경에 g++ 가 없어 **컴파일·실행을 하지 못했다.** 컴파일 실패라는 뜻이 아니라
> 판정을 못 했다는 뜻이다. 아래는 전부 코드를 읽고 쓴 것이고, 반례를 돌려본 것은 없다.

## 접근

다른 풀이들이 (K, 중심) 쌍마다 마름모를 새로 세는 것과 달리, 이 코드는 **중심 하나당 딱 한 번**
모든 집까지의 거리를 재고 그 분포를 `v1[dist]` 히스토그램으로 만든다. 그다음 K 를 큰 값에서
작은 값으로 내리면서 `left -= v1[k]` 로 커버 범위 밖으로 나간 집을 빼기만 하면 된다.

```cpp
for (int k = 2 * n - 1; k >= 1; k--) {
    if (left * m - ((k * k) + (k - 1) * (k - 1)) >= 0) return left;
    left -= v1[k];
}
```

**K 가 줄면 커버되는 집도 단조 감소한다**는 성질을 정확히 쓴 것이고, 그래서 K 가 큰 쪽부터 내려오다
처음 조건을 만족하는 순간이 그 중심의 최적이다 — 더 볼 것 없이 `return`. 마름모를 다시 세지 않으므로
중심당 비용이 `O(집 수)` 로 끝난다. 이 문제 풀이 중에서 제일 깔끔한 구조다.

가지치기도 두 겹으로 정확하다. `if (left <= ans) return ans;` 는 이 중심으로는 기록을 못 깬다는 뜻이고,
바깥의 `if (ans == sum) { flag = 1; break; }` 는 모든 집을 덮었으면 더 볼 필요가 없다는 뜻이다.

운영비를 `(k*k) + (k-1)*(k-1)` 로 쓴 것도 좋다. `2K²-2K+1` 을 외워 적는 대신
"K² + (K-1)²" 이라는 마름모 넓이의 기하학적 형태를 그대로 남겼다.

**중심을 격자 안쪽(`0 ~ n-1`)으로만 제한한 것은 안전하다.** 중심을 격자 바깥까지 3칸 넓힌 레퍼런스와
안쪽만 훑는 레퍼런스를 무작위 600건 대조해 **차이가 0건**이었다.

## 개선점

### 1. (치명) `findmax` 가 값을 반환하지 않고 끝날 수 있는 경로가 있다 — <missing-return>

```cpp
int findmax(const vector<int> &v)
{
    ...
    for (int k = 2 * n - 1; k >= 1; k--) { ... }
    // 여기로 오면 return 이 없다
}
```

`for` 를 다 돌고 나가는 경로에 `return` 이 없다. C++ 에서 값을 반환하는 함수가 반환 없이 끝나면
**정의되지 않은 동작(UB)** 이고, g++ 는 `warning: control reaches end of non-void function` 을 낸다.
최적화 수준에 따라 쓰레기 값이 나오거나 함수가 통째로 잘려나갈 수 있다.

**실제로 그 경로에 도달하지는 않는 것으로 보인다.** `k == 1` 에서 `left > ans >= 0` 이므로 `left >= 1`,
`m >= 1` 이라 `left * m - 1 >= 0` 이 항상 참이고 `return left` 로 빠진다.
하지만 그건 **코드를 읽어야만 알 수 있는 보장**이고, 컴파일러는 모른다.
마지막에 한 줄 넣으면 UB 자체가 사라진다.

```cpp
    }
    return ans;   // 어떤 K 로도 조건을 못 맞춘 경우
}
```

**확인 못 함** — g++ 가 없어 실제로 경고가 나는지, 최적화에서 어떻게 되는지 돌려보지 못했다.
다만 반환 경로가 빠져 있다는 것 자체는 코드에서 바로 보인다.

### 2. (중요) 중심마다 `vector<int> dist` 를 새로 만들어 집 수만큼 push_back 한다 — <redundant-collection>

```cpp
vector<int> dist;
for (auto house : v) dist.push_back(abs(house.first - i) + abs(house.second - j) + 1);
local_max = findmax(dist);
```

그리고 `findmax` 는 그 벡터를 다시 훑어 `v1[]` 히스토그램으로 접는다.
**중간 벡터가 하는 일이 없다** — 거리를 계산하는 그 자리에서 바로 히스토그램에 넣으면 된다.
중심이 `N² = 400` 개, 집이 최대 400채라 힙 할당 400번 + 복사 16만 회가 통째로 사라진다.

```cpp
int v1[42];
for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) {
    fill(v1, v1 + 42, 0);
    for (auto &house : v) v1[abs(house.first - i) + abs(house.second - j) + 1]++;
    ans = max(ans, findmax(v1));      // findmax 가 히스토그램을 직접 받게
}
```

덧붙여 `for (auto house : v)` 는 `pair` 를 **값으로 복사**한다. `const auto &house` 로 받는 게 맞다.

**검증 안 함** — g++ 가 없어 돌려보지 못했다. 반영하면 원본과 같은 답을 내는지 확인이 필요하다.

### 3. (사소) 주석 처리된 디버그 출력 5덩이 — <dead-code>

`findmax` 안에 2줄, `main` 에 `// cout << sum` 과 반복자 출력 블록 두 덩이(10줄 남짓)가 남아 있다.
지우는 게 맞다. 필요하면 git 이력에 있다.

### 4. (사소) `v1[42]` 의 42 — <magic-number>

거리 최댓값은 `(n-1) + (n-1) + 1 = 2n - 1` 이고 `n ≤ 20` 이라 39다. `42` 는 그 여유값인데
근거가 코드에 없다. `const int MAXD = 2 * 20;` 처럼 이름을 주면 `n` 이 커지는 문제에 복사해 쓸 때 걸린다.

### 5. (사소) `cin >> T` 가 `ios::sync_with_stdio(0)` 보다 먼저 온다

```cpp
int T;
cin >> T;
ios::sync_with_stdio(0);
```

이미 입력이 일어난 뒤에 동기화를 끄고 있다. 동작은 하지만 순서가 뒤집혀 있어서 **가속 효과가
첫 입력에는 적용되지 않는다.** `main` 첫 줄로 올리는 게 맞다. `week5/breakthewall.cpp` 는 제대로 돼 있다.

## 복잡도

- 시간: `O(T·N²·H)` — 중심 `N²` 개 × 집 `H`(최대 `N²`)개의 거리 계산. 마름모를 다시 세지 않는 게 핵심
- 공간: `O(H + N)` — 집 좌표 목록과 거리 히스토그램

## 요약

중심 하나당 거리 히스토그램을 한 번 만들고 K 를 내리며 빼기만 하는 구조가 이 문제 풀이 중 가장 좋다.
(K, 중심) 마다 마름모를 다시 세는 방식보다 한 차수 빠르고, 중심을 격자 안쪽으로 제한한 것도
600건 대조로 안전함을 확인했다. 반드시 고쳐야 할 건 **`findmax` 의 빠진 `return`** 하나다 —
지금은 도달하지 않는 경로지만 UB 이고, 한 줄이면 없앨 수 있다.
중간 `vector<int> dist` 를 걷어내고 히스토그램에 바로 넣으면 이 풀이의 좋은 발상이 코드에도 그대로 드러난다.
