---
platform: swea
problemId: "2382"
author: junsoo
source: 김준수/week6/미생물 격리.java
week: 6
compiles: true
lang: java
verdict: needs-fix
tags: [time-complexity, dead-code, good-decomposition]
complexity:
  time: O(M·K²)
  space: O(K + N²)
generatedBy: claude-code-local
generatedAt: 2026-09-05
---

# 미생물 격리 (swea/2382) — junsoo

## 접근

군집을 `Cluster` 클래스로 만들고, 생성자에서 입력 방향(1~4)을 `dx/dy` 인덱스로 즉시 변환해 저장한다.
이동·약품처리·합체를 `move()` / `med()` / `combine()` 으로 나눠 놓아서, `main` 의 시뮬레이션 루프가
"합치고, 옮기고, 정렬한다" 세 줄로 읽힌다. 상태와 상태 전이를 한 곳에 모은 판단이 좋다.

특히 **`Comparable` 로 num 내림차순 정렬을 깔아두고 합체 시 앞쪽 군집이 흡수하게 만든 것**이 이 풀이의 핵심이다.
"방향은 가장 큰 군집을 따른다" 는 조건을 `if` 로 비교하지 않고 정렬 불변식으로 바꿔서, 3개 이상이 한 칸에
모이는 경우가 특별 케이스가 아니게 됐다. 파일 맨 위 주석에 그 근거가 적혀 있는 것도 좋다.

**정확성은 확인했다.** 정석 순서(이동 → 가장자리 약품 → 합체)로 짠 레퍼런스와 무작위 4,000건을 대조해
**타이가 없는 3,990건 전부 일치**했다. 아래 3번은 그 타이 케이스에 대한 기록이다.

## 개선점

### 1. (치명) `package algorithm;` + `public class SWEA2382` 로는 SWEA 에 제출이 안 된다 — <dead-code>

SWEA 는 default package 에 `public class Solution` 을 요구한다. 지금 파일은 둘 다 어긋나 있어서
로직과 무관하게 채점 서버에서 컴파일 단계에서 막힌다. 같은 주차의 `벌꿀채취.java` 는
`public class Solution` 에 package 없이 제출 가능한 형태라, 이 파일만 실수로 IDE 설정이 남은 것으로 보인다.

```java
// 지우고
package algorithm;
public class SWEA2382 {

// 이렇게
public class Solution {
```

`홈 방범 서비스.java` 도 같은 상태다.

> 참고: 로컬 `javac` 는 클래스명에 맞춘 임시 파일로 복사해서 돌리기 때문에 `compiles: true` 로 나온다.
> 이건 **우리 도구의 판정이지 SWEA 채점 서버의 판정이 아니다.**

### 2. (중요) `doCombine()` 이 매 초 O(K²) — K, M 이 최대일 때 10⁹ — <time-complexity>

제약은 `5 ≤ N ≤ 100`, `1 ≤ M ≤ 1000`, `5 ≤ K ≤ 1000` 이다.
`doCombine()` 은 군집 쌍을 전부 비교하므로 초당 K²/2 ≈ 5×10⁵, M 초면 **5×10⁸ 회 비교**다.
게다가 합쳐진 군집도 `clusterList` 에서 빠지지 않고 `!a.active` 로 걸러지기만 해서, 시간이 지나도
루프 크기가 줄지 않는다. 매 초 도는 `Collections.sort` 도 같은 이유로 계속 K 개를 정렬한다.

같은 칸에 모인 것만 찾으면 되므로 **좌표를 키로 한 버킷 한 번이면 O(K)** 다.
마침 `board` 를 `new int[N][N]` 으로 이미 잡아 놓고 안 쓰고 있는데, 그게 딱 이 용도다.

```java
// doCombine() 을 이렇게. board[i][j] = 그 칸을 먼저 차지한 군집의 인덱스 + 1
private static void doCombine() {
    for (int[] row : board) Arrays.fill(row, 0);
    for (int i = 0; i < clusterList.size(); i++) {
        Cluster c = clusterList.get(i);
        if (!c.active) continue;
        int prev = board[c.next[0]][c.next[1]];
        if (prev == 0) {
            board[c.next[0]][c.next[1]] = i + 1;
        } else {
            // 리스트가 num 내림차순이라 먼저 자리를 잡은 prev 쪽이 항상 더 크다 (기존 불변식 그대로)
            clusterList.get(prev - 1).num += c.num;
            c.active = false;
        }
    }
}
```

정렬 불변식(앞쪽이 더 크다)을 그대로 쓰기 때문에 방향 결정 규칙은 안 바뀐다.
여기에 `clusterList.removeIf(c -> !c.active)` 를 매 초 한 줄 더하면 정렬 비용도 같이 줄어든다.

**검증 안 함** — 위 교체본은 돌려보지 않았다. 반영하면 무작위 대조를 한 번 돌려보길 권한다.

### 3. (사소) 같은 크기 군집이 만나면 방향이 갈린다 — 문제 정의가 모호한 지점

`N=8, M=3, 군집 (4,3,80,좌) / (4,1,160,좌)` 에서 이 코드는 `160`, 방향을 배열 순서로 정하지 않는
레퍼런스는 `80` 이 나온다. 2초째에 두 군집이 **똑같이 80** 인 채로 한 칸에 모이는데, 문제의
"가장 큰 군집의 방향을 따른다" 가 동점일 때 뭘 고르라는 말인지 정의하지 않기 때문이다.

**이 코드가 틀렸다는 뜻이 아니다.** 무작위 4,000건 중 타이가 난 10건에서만 갈렸고 나머지는 전부 같았다.
채점에서 틀린다면 여기를 의심해볼 값어치가 있다는 기록으로만 남긴다.

### 4. (사소) 안 쓰는 `combine()` 메서드 — <dead-code>

`Cluster.combine(Cluster c)` 는 아무 데서도 호출되지 않는다. `doCombine()` 이 같은 일을 인라인으로 한다.
둘 중 하나로 합치는 게 좋다 — 2번 수정안을 적용한다면 `combine()` 쪽을 살리고 인라인을 지우는 편이 읽기 좋다.

## 복잡도

- 시간: `O(M·K²)` — `doCombine()` 의 쌍 비교가 지배적. 버킷으로 바꾸면 `O(M·(K + N²))`
- 공간: `O(K + N²)` — 군집 리스트 + (지금은 안 쓰이는) `board`

## 요약

뼈대는 좋다. 방향 결정이라는 까다로운 조건을 정렬 불변식으로 바꿔서 다중 충돌을 특별 케이스 없이 처리했고,
정석 순서와 무작위 4,000건 대조에서 타이 케이스를 뺀 전부가 일치했다. 실제 문제는 로직이 아니라 두 가지다 —
`package` + 클래스명 때문에 SWEA 제출 자체가 막히고, 쌍 비교 합체가 최대 입력에서 10⁹ 에 닿는다.
이미 선언해 둔 `board` 로 버킷을 만들면 둘째는 그대로 해결된다.
