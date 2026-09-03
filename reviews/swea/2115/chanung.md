---
platform: swea
problemId: "2115"
author: chanung
source: 안찬웅/week6/벌꿀채취.java
week: 6
compiles: false
lang: java
verdict: wrong
tags: [missing-return]
complexity:
  time: O(N^2 * 2^M + N^4)
  space: O(N^2)
generatedBy: claude-code-local
generatedAt: 2026-09-03
---

# 벌꿀채취 (swea/2115) — chanung

## 접근

두 단계로 나눈 설계가 깔끔하다: 1단계는 모든 시작 좌표에서 길이 m짜리 가로 덩어리 하나의 최대 수익(부분집합 DFS, 합이 c 이하인 것만)을 `bestArr`에 미리 저장하고, 2단계는 `bestArr` 위에서 겹치지 않는 두 덩어리 조합의 최댓값을 찾는다. 같은 행일 때만 `j1+m` 이후로 시작 열을 제한해 겹침을 막고, 다른 행이면 아무 열이나 허용한 것도 "가로로 연속한 덩어리는 같은 행 안에서만 겹칠 수 있다"는 조건을 정확히 반영한다. `dfs`의 `sum > c` 가지치기와 "채취한다/안 한다" 두 갈래 재귀도 부분집합 탐색으로 정확하다.

## 개선점

### 1. (치명) 중괄호 개수가 안 맞아 컴파일 자체가 안 된다 — `missing-return`

```java
for(int i1=0; i1<n; i1++) {
    for(int j1=0; j1<=n-m; j1++) {
        for(int i2=i1; i2<n; i2++) {
            int start = (i1 == i2) ? j1 + m : 0;
            for(int j2=start; j2<=n-m; j2++) {
                answer = Math.max(answer, bestArr[i1][j1] + bestArr[i2][j2]);
            }
        }

    sb.append("#").append(test_case).append(" ").append(answer).append("\n");
}
System.out.print(sb);
```

`i1`, `j1`, `i2`, `j2` 4중 for문을 열어놓고 닫는 `}`는 2개(`j2`, `i2`)뿐이다. `j1`, `i1` 루프와 바깥의 `test_case` 루프를 닫는 `}`가 없어서, `sb.append(...)`가 실제로는 `j1`·`i1` 루프 안에 갇히고 `System.out.print(sb)`도 `i1` 루프 안에 갇힌다. 이 상태로 파일 끝까지 브레이스가 하나씩 밀리면서 67행의 `static void dfs(...)` 선언이 `main` 메서드(정확히는 아직 닫히지 않은 `test_case` for문) 내부의 한 "문장"처럼 취급돼 `illegal start of expression`이 난다. 실제 javac 결과:

```
안찬웅/week6/벌꿀채취.java:67: error: illegal start of expression
	static void dfs(int x, int y, int idx, int sum, int score) {
	^
```

고치려면 `j1`, `i1` 루프를 닫는 `}` 두 개를 `sb.append(...)` 앞에 추가하고, `test_case` 루프를 닫는 `}`를 `System.out.print(sb);` 뒤(현재 다른 위치에 있을 닫는 괄호)에 맞춰줘야 한다:

```java
            for(int j2=start; j2<=n-m; j2++) {
                answer = Math.max(answer, bestArr[i1][j1] + bestArr[i2][j2]);
            }
        }   // i2 닫기 (기존)
    }       // j1 닫기 (추가)
}           // i1 닫기 (추가)

sb.append("#").append(test_case).append(" ").append(answer).append("\n");
```

브레이스만 맞추면 나머지 로직(부분집합 DFS, 겹침 방지 조건)은 검증한 바로는 그대로 맞는 접근이라 별도 수정 없이 통과할 가능성이 높다 — 다만 실제 채점 데이터로 돌려본 것은 아니라서 "검증 안 함"으로 남긴다.

## 복잡도

- 시간: 1단계는 시작 좌표 O(N^2)마다 부분집합 DFS O(2^M) → `O(N^2 · 2^M)`. 2단계는 겹치지 않는 두 덩어리 조합을 4중 루프로 순회 → `O(N^4)`. 둘을 합쳐 `O(N^2 · 2^M + N^4)`.
- 공간: `O(N^2)` — `graph`, `bestArr` 두 배열.

## 요약

알고리즘 설계(부분집합으로 덩어리 하나의 최댓값 계산 → 겹치지 않는 두 덩어리 조합)는 문제 조건과 잘 맞는다. 문제는 순수하게 중괄호 개수가 어긋난 구문 오류 하나이고, 이 때문에 컴파일 자체가 안 돼 채점 불가 상태다. 위 브레이스만 맞추면 로직상 큰 결함은 보이지 않는다.
