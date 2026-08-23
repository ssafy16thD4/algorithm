---
platform: programmers
problemId: "64062"
author: junsoo
source: 김준수/week4/징검다리 건너기 실패.java
week: 4
compiles: true
verdict: wrong
tags: [off-by-one, time-complexity]
complexity:
  time: O(N·k·log(max))
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 징검다리 건너기 (programmers/64062) — junsoo (실패 버전)

## 접근

"건널 수 있는 인원 수"를 매개변수 탐색으로 잡은 것은 맞습니다. `isValid(people)` 이 단조(어떤 인원으로 건널 수 있으면 그보다 적은 인원으로도 건널 수 있다)이므로 이분탐색이 성립합니다.

파일 맨 위 주석에는 이렇게 적혀 있습니다.

```
- 정확성 테스트는 다 맞는데 효율성 테스트 3/14
- 3개는 통과했으니까 매개변수 탐색이라는 접근이 틀린 건 아닌듯
- isValid의 시간 복잡도를 더 줄여야 할 것 같음
```

**접근이 틀리지 않았다는 판단은 맞습니다. 하지만 "정확성은 다 맞다"는 전제가 틀렸습니다.** 이 코드에는 시간 문제와 별개로 오답 버그가 하나 있습니다. 아래 1번입니다.

**검증**: 크기 k 창의 최댓값 중 최솟값을 구하는 브루트포스를 기준으로 무작위 30,000건(n=1~12, k=1~n)을 대조했습니다. 같은 문제를 푼 안찬웅·이성일 코드 3종도 같이 돌렸습니다.

| 대상 | 불일치 |
|---|---|
| **김준수 (이 파일)** | **8,658 / 30,000** |
| 안찬웅 | 0 / 30,000 |
| 이성일 (덱) | 0 / 30,000 |
| 이성일 (이분탐색) | 0 / 30,000 |

## 개선점

### 1. (치명) 0번 돌을 한 번도 검사하지 않는다 — off-by-one

```java
for(int i = 0; i < nextStones.length - 1; i++){
    if(nextStones[i + 1] <= 0){        // <- i+1 부터 본다
```

바깥 루프의 `i` 는 `0 .. n-2` 이고 실제로 값을 보는 건 `nextStones[i+1]` 이라 **검사 대상 인덱스는 `1 .. n-1` 입니다.** 0번 돌이 죽었는지는 아무도 안 봅니다.

실제로 돌려본 반례입니다.

```
stones = [1, 2, 4]   k = 1
기대 출력: 1      실제 출력: 2
```

`k=1` 이면 돌 하나만 죽어도 못 건넙니다. 인원이 2명이면 `stones[0] = 1` 이 먼저 바닥나므로 답은 1이어야 하는데, 0번 돌을 안 보기 때문에 `isValid(2)` 가 `true` 를 반환합니다.

`k=1` 이 아니어도 같은 문제가 납니다. 0번부터 k개가 연속으로 죽는 경우를 놓칩니다.

고치려면 검사 시작점을 0으로 내리면 됩니다.

```java
for (int i = 0; i < nextStones.length; i++) {
    if (nextStones[i] > 0) continue;
    int dead = 0;
    while (i + dead < nextStones.length && nextStones[i + dead] <= 0) dead++;
    if (dead >= k) return false;
    i += dead;           // 이미 본 구간은 건너뛴다
}
return true;
```

`i` 자체를 보게 바꾸고, 죽은 구간을 한 번에 세어 넘깁니다.

### 2. (중요) `isValid` 가 `O(N·k)` 라 이분탐색까지 곱하면 못 맞춘다 — time-complexity

본인이 이미 짚은 부분입니다. 매 호출마다 배열을 통째로 복사(`Arrays.copyOf`)하고, 죽은 돌마다 최대 k칸을 다시 훑습니다. `N`, `k` 가 각각 최대 200,000 이므로 한 번의 `isValid` 만으로도 최악 `4 × 10^10` 입니다.

실측했습니다. `n = 200,000`, `k = 100,000` 입력입니다.

| 입력 | 이 코드 | 안찬웅(이분탐색) | 이성일(덱) |
|---|---|---|---|
| 내림차순 | **33,317 ms** | 12 ms | 15 ms |
| 무작위 | **12,573 ms** | 5 ms | 9 ms |

위 1번 수정안을 쓰면 `isValid` 가 `O(N)` 이 되어 전체가 `O(N log(max))` 로 떨어집니다. 배열 복사도 필요 없어집니다 — `stones[i] - people <= 0` 은 `stones[i] <= people` 과 같으므로 원본을 그대로 읽으면 됩니다.

```java
private boolean isValid(int people) {
    int dead = 0;
    for (int s : stones) {
        dead = (s <= people) ? dead + 1 : 0;
        if (dead >= k) return false;
    }
    return true;
}
```

같은 문제를 푼 안찬웅 코드가 정확히 이 형태이고, 위 표에서 12 ms 로 끝났습니다.

### 3. (사소) `static` 필드에 `this` 로 대입한다

```java
static int[] stones;
static int k;
...
this.stones = stones;
this.k = k;
```

`static` 필드를 `this.` 로 접근하는 건 컴파일은 되지만 인스턴스 필드처럼 보이게 만듭니다. 실제로는 모든 인스턴스가 공유하는 값이라, 채점 서버가 테스트케이스마다 새 인스턴스를 만들어도 이 값은 이어집니다. 여기서는 매번 대입하므로 문제가 되지 않지만, `static` 을 떼거나 `Solution.stones` 로 명시하는 편이 오해가 없습니다.

## 복잡도

- 시간: `O(N·k·log(max))` — 이분탐색 `log(2×10^8) ≈ 28` 회 × `isValid` 의 `O(N·k)`. 위 수정안 적용 시 `O(N log(max))`.
- 공간: `O(N)` — `isValid` 호출마다 배열을 복사합니다. 수정안에서는 `O(1)`.

## 요약

매개변수 탐색이라는 큰 방향은 맞고, 본인이 "isValid를 더 줄여야 한다"고 짚은 것도 정확합니다. 다만 **효율성만 문제인 게 아니라 0번 돌을 안 보는 오답 버그가 있습니다** — 무작위 30,000건 중 8,658건이 틀렸습니다. 검사 시작점을 0으로 내리고 죽은 돌을 연속 카운트로 세면 오답과 시간초과가 같이 해결됩니다.
