---
platform: programmers
problemId: "43164"
author: junsoo
source: 김준수/week4/여행 경로.java
week: 4
compiles: true
verdict: needs-fix
tags: [time-complexity, collection-choice]
complexity:
  time: O(E!) 최악 (조기 종료 없음), 도시 조회까지 더하면 더 나빠짐
  space: O(E)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 여행경로 (programmers/43164) — junsoo

## 접근

도시 이름을 인덱스로 바꿔 문자열 비교를 없애고, 출발지별 인접 리스트를 도착지 알파벳 순으로
정렬한 뒤 백트래킹으로 전체 경로를 탐색한다. **알파벳 순으로 정렬된 인접 리스트를 순서대로
탐색하면서, 모든 티켓을 다 쓴 첫 번째 경로를 답으로 채택한다**는 게 이 문제의 "가능한 경로가
여럿이면 알파벳이 앞서는 경로"를 보장하는 핵심 성질인데, 이 성질 자체는 정확히 활용했다 —
`result`를 `depth == ticket.length`에서 처음 한 번만 채우고(`if(result == null)`) 이후로는 절대
덮어쓰지 않으므로, 가장 먼저 완성되는 경로(=사전식으로 가장 앞선 경로)가 그대로 답이 된다.
반례를 찾지는 못했다 — 로직 자체는 맞다.

## 개선점

### 1. (중요) 답을 찾은 뒤에도 나머지 가지를 계속 탐색한다 — `time-complexity`

```java
if(depth == ticket.length){
    if(result == null){
        result = new ArrayList<>();
        for(int c : course) result.add(c);
    }
    // 여기서 끝내지 않고 아래 for문으로 그대로 진입
}

for(int i = 0; i < graph.get(curr).size(); i++){ ... }
```

`result`가 채워진 뒤에도 `dfs`는 계속 호출되고 계속 백트래킹한다. `depth == ticket.length`인
지점에서는 `graph.get(curr)`의 모든 티켓이 이미 사용됐으니 그 자리에서의 루프 자체는 공회전으로
끝나지만, **그 위의 호출 스택들은 여전히 다른 가지를 계속 시도한다.** 답을 찾은 즉시 전체 탐색을
멈추는 신호(예: `boolean found` 필드를 두고 `if(found) return;`을 `dfs` 맨 앞에 추가하거나,
`RuntimeException`으로 탈출)가 없어서, 정답과 무관한 나머지 조합까지 전부 확인한다.

이 문제의 공식 제약(공항 수 최대 10,000개, 문제 페이지에서 확인)까지 감안하면, 알파벳 순으로
탐색해서 답이 일찍 나오는 입력에서는 체감이 안 되겠지만, 막다른 길이 많은 입력이면 불필요하게
많은 가지를 마저 확인하게 된다. **다만 실제로 이 코드가 시간초과가 나는지는 직접 돌려서 확인하지
못했다 — "확인 못 함"으로 남긴다.** 비슷한 유형의 다른 백트래킹 문제(`김준수/week3/표 편집
실패.java` 리뷰)에서는 실측으로 확인했지만, 이 문제는 조기 종료 유무가 실제로 얼마나 차이를
만드는지 별도로 재보지 않았다.

```java
// dfs 시작부에
if (result != null) return;
```

### 2. (중요) 도시 인덱스 조회를 `ArrayList.contains`/`indexOf`로 한다 — `collection-choice`

```java
if(!cities.contains(t[0])) cities.add(t[0]);   // O(cities.size())
...
ticket[i][0] = cities.indexOf(tickets[i][0]);  // O(cities.size())
```

두 곳 다 `cities`가 커질수록 매번 선형 탐색이라, 티켓 수를 `E`, 도시 수를 `V`라 하면 이 초기화
단계만 `O(E · V)`가 된다. `Map<String, Integer> cityIndex`를 만들어 이름→인덱스를 `O(1)`로
바꾸면 이 부분의 비용이 사라진다.

## 복잡도

- 시간: 조기 종료가 없어 최악은 `O(E!)`에 가깝고, 도시 인덱스 조회까지 더하면 초기화 단계가
  `O(E·V)`. `E, V`가 이 문제 제약(최대 만 단위)만큼 커지는 입력이면 부담이 크다.
- 공간: `O(E)` — 그래프와 경로 리스트.

## 요약

알파벳 순 탐색 + "첫 완성 경로 채택"이라는 핵심 아이디어는 정확하다. 답을 찾은 뒤 탐색을 멈추지
않는 것과 도시 조회를 선형 탐색으로 하는 두 가지가 실제 성능에 영향을 줄 수 있는데, 이 코드가
정말 시간초과가 나는지는 직접 돌려보지 못해 단정하지 않는다.
</content>
