---
platform: swea
problemId: "2117"
author: chanung
source: 안찬웅/week6/홈 방범 서비스 실패.java
week: 6
compiles: true
lang: java
verdict: wrong
tags: [logic-edge-case, wrong-algorithm]
complexity:
  time: O(N^2)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-09-03
---

# 홈 방범 서비스 — 실패 버전 (swea/2117) — chanung

## 접근

각 칸 (x, y)를 서비스 중심으로 놓고, 서비스 반경 k에 대해 커버되는 집 개수와 운영비용(`k*k + (k-1)*(k-1)`)·이익을 계산해 최댓값을 갱신하려는 시도다. 방향은 맞지만, 실제 구현에는 확신을 갖고 지적할 수 있는 버그가 여러 개 겹쳐 있다.

## 개선점

### 1. (치명) 디버그 출력이 그대로 남아있다 — `logic-edge-case`

```java
if(fee >= 0) {
    maxHouseCnt = Math.max(maxHouseCnt, houseCnt);
    System.out.println("houseCnt: " + houseCnt + " fee: " + fee + " profit: " + profit);
}
```

`simulation`은 모든 (i, j) 쌍마다 호출되고, `fee >= 0`은 사실상 항상 참(아래 3번 참고)이라 이 `println`이 사실상 매번 실행된다. 기대 출력(`#1 3` 형태의 한 줄)과 전혀 다른 대량의 로그가 표준출력에 섞여 나가 채점에서 100% 오답 처리된다.

### 2. (치명) 테스트케이스 개수를 입력값이 아니라 상수 10으로 고정했다 — `logic-edge-case`

```java
int t = Integer.parseInt(br.readLine());
...
for(int test_case=1; test_case<=10; test_case++) {
```

`t`를 읽어놓고 반복 횟수에는 쓰지 않고 `10`을 하드코딩했다. 실제 테스트케이스가 10개가 아니면(더 적으면 입력을 초과해서 읽어 `NumberFormatException`/`NullPointerException`이 나고, 더 많으면 뒤 테스트케이스가 통째로 누락된다) 정상 동작하지 않는다. `test_case<=t`로 고쳐야 한다.

### 3. (치명) 서비스 반경 k를 1과 2, 단 두 값만 시도하고 끝난다 — `wrong-algorithm`

```java
k = 0;
while(k <= 1) {
    k++;
    for(int i=0; i<n; i++)
        for(int j=0; j<n; j++)
            simulation(i, j);
}
```

이 루프를 직접 따라가 보면: `k=0`→조건참→`k=1`로 시뮬레이션 1회, 다시 조건 검사(`1<=1`참)→`k=2`로 시뮬레이션 1회, 다시 검사(`2<=1`거짓)→종료. 즉 **k=1과 k=2만 시도하고 끝난다.** 주석(16행)에는 "k가 하나씩 증가하며 테스트한다"고 되어 있어 이익이 음수가 될 때까지(또는 반경이 보드를 넘어설 때까지) k를 계속 늘려야 하는 의도가 보이는데, 정지 조건이 `k<=1`이라는 상수로 고정돼 있어 보드가 크고 k=3 이상에서 최적해가 나오는 경우를 전부 놓친다.

### 4. (치명) 손해를 보지 않는 조건이 실질적으로 걸러지지 않는다 — `wrong-algorithm`

```java
int fee = (k * k) + (k - 1) * (k - 1);
int profit = k * m - fee;
if(fee < 0) return; // 요금이 더 적으면 볼필요 없음
...
if(fee >= 0) {
    maxHouseCnt = Math.max(maxHouseCnt, houseCnt);
```

`fee = k² + (k-1)²`는 k≥1일 때 항상 0 이상이라 `fee < 0`은 절대 참이 안 되고, `fee >= 0`은 절대 거짓이 안 된다 — 두 분기 모두 사실상 무조건 통과하는 죽은 조건이다. 문제 요구사항은 "손해를 보지 않는 한"(주석 7행)이므로 걸러야 할 값은 `fee`가 아니라 `profit`(`k*m - fee`)이어야 하는데, `profit`은 계산만 되고 어디에도 조건으로 쓰이지 않는다. 결과적으로 이익이 음수인(손해 보는) 배치도 `maxHouseCnt` 갱신에 그대로 반영된다.

### 5. (중요) 커버 영역이 마름모가 아니라 십자(+) 모양으로 계산된다 — `wrong-algorithm`

```java
for(int i=y-k; i<=y+k; i++) { if(...) if(graph[x][i]==1) houseCnt++; }   // x행 전체
for(int i=x-k; i<=x+k; i++) { if(...) if(graph[i][y]==1) houseCnt++; }   // y열 전체
```

중심을 지나는 가로줄과 세로줄만 세고 있어 실제로는 "+" 십자 모양 영역을 계산한다. 그런데 비용 공식 `k*k + (k-1)*(k-1)`은 대각선 칸까지 포함하는 마름모(다이아몬드) 영역의 칸 수 공식과 일치한다 — 비용 산정 기준과 실제로 세는 영역의 모양이 서로 다르다. 마름모의 대각선 칸(`houseCnt` 계산에서 빠지는 칸들)이 반영되지 않아 커버 개수가 실제보다 작게 나온다.
※ 이 항목은 문제 페이지를 직접 재확인하지 못했고(`resolve.mjs`가 준 `url`이 비어 있음), 코드 자체의 비용 공식과 영역 계산 방식이 서로 불일치한다는 점에 근거한 지적이다.

## 복잡도

- 시간: `O(N^2)` — k를 1, 2 두 번만 시도하고 각 시도마다 전체 칸 O(N^2)을 순회하므로 상수 배만 붙는다(원래 의도대로 k를 계속 늘렸다면 `O(N^3)` 근처가 됐을 것).
- 공간: `O(N^2)` — `graph` 배열.

## 요약

방향(중심 좌표 + 반경 k 탐색)은 맞게 잡았지만, 디버그 출력·테스트케이스 개수 하드코딩·k 탐색 범위 고정·이익 조건 누락·커버 영역 모양까지 다섯 군데가 겹쳐 있어 지금 상태로는 정상적인 답을 기대하기 어렵다. 1~4번은 코드만 봐도 확정적인 버그이고, 5번은 비용 공식과 실제 계산이 서로 다른 도형을 가리킨다는 정황 근거다.
