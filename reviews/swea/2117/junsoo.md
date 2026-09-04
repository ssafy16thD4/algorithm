---
platform: swea
problemId: "2117"
author: junsoo
source: 김준수/week6/홈 방범 서비스.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [redundant-loop, dead-code, good-complexity]
complexity:
  time: O(T·N⁵)
  space: O(N²)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 홈 방범 서비스 (swea/2117) — junsoo

## 접근

K = 1..2N 을 전부 훑는 완전탐색이고, 세 가지 판단이 정확하다.

1. **운영비를 점화식으로 뽑았다.** `dp[k] = dp[k-1] + 4(k-1)` 은 마름모의 둘레가 매 단계 4칸씩 늘어난다는
   사실을 그대로 옮긴 것이라, `2K²-2K+1` 공식을 외우지 않고도 맞는 값이 나온다. 주석에 근거도 적혀 있다.
2. **중심을 지도 밖(`0 ~ N+1`)까지 허용했다.** 최적 중심이 지도 경계 밖일 수 있다는 걸 놓치기 쉬운데,
   `board` 를 `(N+2)×(N+2)` 로 잡고 `inRange` 로 막아서 인덱스 예외 없이 처리했다.
3. **커버 범위를 `remain = k - |dr|` 로 접었다.** 마름모를 4방향 BFS 나 두 겹 조건문으로 처리하지 않고
   행마다 좌우 여유를 계산해서, 이 문제에서 제일 자주 틀리는 부분이 짧게 끝났다.

**정확성은 확인했다.** 모든 (K, 중심) 을 정직하게 훑는 레퍼런스와 무작위 200건(`N ≤ 8`, `M ≤ 10`)을
대조해 **전부 일치**했다.

## 개선점

### 1. (치명) `package algorithm;` + `public class SWEA2117` 로는 SWEA 에 제출이 안 된다 — <dead-code>

SWEA 는 default package 에 `public class Solution` 을 요구한다. 로직과 무관하게 채점 서버의 컴파일 단계에서
막힌다. 같은 주차의 `벌꿀채취.java` 는 `public class Solution` + package 없음으로 제대로 돼 있으니,
이 파일만 IDE 설정이 남은 것으로 보인다. `미생물 격리.java` 도 같은 상태다.

```java
// 지우고
package algorithm;
public class SWEA2117 {

// 이렇게
public class Solution {
```

> 로컬 `javac` 는 클래스명에 맞춘 임시 파일로 복사해 돌리므로 `compiles: true` 로 나온다.
> 우리 도구의 판정이지 SWEA 채점 서버의 판정이 아니다.

### 2. (중요) 중심을 한 칸 옮길 때마다 마름모를 처음부터 다시 센다 — <redundant-loop>

`find` 는 열을 하나 옮길 때마다 `getHouseCnt(k, r, c+1)` 로 **마름모 전체를 다시 훑는다.**
칸 수가 `2k²-2k+1` 이므로, 한 테스트케이스에서 세는 칸의 총합은

```
Σ(k=1..2N) (N+2)² · (2k²-2k+1)  ≈  484 × 42,000  ≈  2.0×10⁷   (N=20)
```

이고 T 가 10이면 2×10⁸ 칸이다. Java 에서 3초 안팎이 걸릴 수 있는 양이라 여유가 없다.

마름모를 오른쪽으로 한 칸 밀면 **왼쪽 대각선 한 줄이 빠지고 오른쪽 대각선 한 줄이 들어올 뿐**이므로,
차분만 계산하면 갱신이 O(k) 로 준다.

```java
// c -> c+1 로 옮길 때: 빠지는 칸은 (r+dr, c-(k-1-|dr|)), 들어오는 칸은 (r+dr, c+1+(k-1-|dr|))
private static int shift(int k, int r, int c, int cnt) {
    int kk = k - 1;
    for (int dr = -kk; dr <= kk; dr++) {
        int remain = kk - Math.abs(dr);
        int nr = r + dr;
        int out = c - remain, in = c + 1 + remain;
        if (inRange(nr, out) && board[nr][out]) cnt--;
        if (inRange(nr, in) && board[nr][in]) cnt++;
    }
    return cnt;
}
```

**검증 안 함** — 위 차분 갱신은 돌려보지 않았다. 반영한다면 원본과 무작위 대조를 한 번 돌리는 게 안전하다.
N ≤ 20 이라 지금도 통과할 가능성이 높으니, 시간 초과가 실제로 났을 때만 손대도 된다.

### 3. (사소) `find` 는 재귀일 이유가 없다 — <long-method>

`find` 가 하는 일은 "c 를 0부터 N+1 까지 늘리면서 검사" 뿐이다. 분기도 없고 되돌아오지도 않는다.
그런데 재귀라서 `houseNum` 을 인자로 받았다가 안에서 다시 대입하는 등 흐름이 한 번 꼬여 있다.

```java
for (int k = 1; k <= 2 * N; k++) {
    for (int r = 0; r <= N + 1; r++) {
        for (int c = 0; c <= N + 1; c++) {
            int cnt = getHouseCnt(k, r, c);
            if (cnt * M >= dp[k] && cnt > result) result = cnt;
        }
    }
}
```

이렇게 펴면 `find` 가 통째로 없어지고, 2번의 차분 갱신도 이 루프에 바로 얹을 수 있다.

### 4. (사소) `getHouseCnt` 의 `k--` — <naming>

```java
k--; // 밑 for문을 k + 1기준으로 만들어버려서 사후처리
```

주석이 솔직한 건 좋은데, 인자를 깎아서 맞추는 대신 `int reach = k - 1;` 로 이름을 붙이면
"커버 반경 = K-1" 이라는 사실이 코드에 그대로 드러난다.

## 복잡도

- 시간: `O(T·N⁵)` — K(2N) × 중심(N²) × 마름모 칸수(O(N²)). 차분 갱신을 넣으면 `O(T·N⁴)`
- 공간: `O(N²)` — `(N+2)²` 지도 + `dp[2N+1]`

## 요약

운영비 점화식, 지도 밖 중심 허용, 마름모를 `remain` 으로 접은 것 — 이 문제에서 틀리기 쉬운 세 곳을
전부 맞게 짚었고 무작위 200건 대조에서도 전부 일치했다. 남은 건 제출 형식과 성능이다.
`package` + 클래스명 때문에 SWEA 에서는 컴파일조차 안 되고, 중심을 옮길 때마다 마름모를 다시 세는 탓에
최대 입력에서 여유가 없다. 전자는 반드시, 후자는 시간 초과가 나면 고치면 된다.
