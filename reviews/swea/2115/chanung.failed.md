---
platform: swea
problemId: "2115"
author: chanung
source: 안찬웅/week6/벌꿀채취 실패.java
week: 6
compiles: false
lang: java
verdict: wrong
tags: [missing-return, wrong-algorithm, logic-edge-case]
complexity:
  time: O(N^2 * 2^M)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-09-03
---

# 벌꿀채취 — 실패 버전 (swea/2115) — chanung

같은 문제의 대표 풀이(`chanung.md`)와 비교하면, 이 버전은 "겹치지 않는 두 덩어리를 골라 합산"하는 2단계가 아예 빠진 초기 시도로 보인다. 왜 틀렸는지 위주로 정리한다.

## 개선점

### 1. (치명) 클래스명이 파일명과 달라 컴파일이 안 된다 — `missing-return`

```java
public class algo {
```

파일명은 `벌꿀채취 실패.java`인데 `public class` 이름은 `algo`다. 자바는 `public` 클래스명과 파일명이 같아야 하므로 그 자리에서 컴파일이 실패한다:

```
안찬웅/week6/벌꿀채취 실패.java:11: error: class algo is public, should be declared in a file named algo.java
```

### 2. (치명) "두 덩어리를 겹치지 않게 골라 합산"하는 단계가 없다 — `wrong-algorithm`

```java
for(int i=0; i<n; i++) {
    for(int j=0; j<n; j++) {
        dfs(i, j, 0, 0, 0);
    }
}
sb.append("#").append(test_case).append(" ").append(maxSum).append("\n");
```

모든 시작 좌표에서 `dfs`를 호출하며 전역 `maxSum` 하나만 계속 갱신한다. 이 문제는 "겹치지 않는 두 덩어리를 골라 그 수익의 합을 최대화"하는 문제인데, 이 버전은 덩어리 하나의 최댓값만 구하고 그걸로 끝낸다 — 두 번째 덩어리를 고르는 로직 자체가 없다. 대표 풀이(`chanung.md`)가 `bestArr`에 저장 후 겹치지 않는 두 좌표 조합을 다시 순회하는 이유가 바로 이 부분이다.

### 3. (치명) 시작 열 j가 `n-m`을 넘어가도 그대로 진입해 배열 범위를 벗어난다 — `logic-edge-case`

```java
for(int j=0; j<n; j++) {
    dfs(i, j, 0, 0, 0);
}
```

덩어리 길이가 m이므로 시작 열은 `j <= n-m`이어야 `graph[x][y+idx]` (idx는 0..m-1)가 배열 범위 안에 든다. 그런데 이 버전은 `j<n`까지 전부 시도해서, `j`가 `n-m`보다 큰 경우 `dfs` 내부에서 `graph[x][y+idx]`가 `n`을 넘는 열 인덱스를 참조해 `ArrayIndexOutOfBoundsException`이 날 수 있다. 대표 풀이는 `j<=n-m`으로 이를 막아뒀다.

### 4. (치명) 제곱합을 계산할 때 엉뚱한 인덱스를 곱한다 — `wrong-algorithm`

```java
dfs(x, y, idx+1, sum + graph[x][y + idx],  score + graph[x][y + idx] * graph[x][c + idx]);
```

채취량의 제곱합이어야 하므로 `graph[x][y+idx] * graph[x][y+idx]` (자기 자신의 제곱)이어야 하는데, 두 번째 인자가 `graph[x][c + idx]`로 되어 있다. `c`는 최대 채취 허용량(입력 파라미터)이지 좌표가 아니라서, 값 자체가 문제 의도(제곱)와 무관할 뿐 아니라 `c+idx`가 `n` 이상이면 이 역시 배열 범위를 벗어난다.

## 복잡도

- 시간: `O(N^2 · 2^M)` — 시작 좌표마다 부분집합 DFS. (다만 두 번째 덩어리 결합이 없어 문제가 요구하는 답 자체를 구하지 못한다.)
- 공간: `O(N^2)` — `graph` 배열.

## 요약

클래스명 불일치로 컴파일부터 막히고, 설령 고치더라도 두 번째 덩어리를 고르는 단계가 없어 문제를 절반만 푸는 구조다. 열 경계 체크 누락과 제곱 계산에 쓰인 인덱스 오타까지 겹쳐 있어, 이 버전은 폐기하고 대표 풀이(`chanung.md`, 브레이스만 고치면 되는 버전) 쪽을 마저 고치는 게 낫다.
