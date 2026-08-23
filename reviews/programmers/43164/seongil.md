---
platform: programmers
problemId: "43164"
author: seongil
source: 이성일/week4/여행경로.java
week: 4
compiles: true
verdict: wrong
tags: [uninitialized-state, good-complexity]
complexity:
  time: O(N·N!) 최악 (실제로는 정렬 덕에 첫 경로에서 끝남)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 여행경로 (programmers/43164) — seongil

## 접근

알고리즘 자체는 맞습니다. 티켓을 `(출발지, 도착지)` 사전순으로 정렬해두고 DFS로 `ICN` 에서 출발해 **모든 티켓을 다 쓴 첫 번째 경로**를 답으로 채택합니다. 정렬된 순서로 탐색하므로 처음 완성되는 경로가 곧 사전순으로 가장 앞선 경로입니다 — 이 관찰이 이 문제의 핵심이고, 정확히 잡았습니다.

`found` 플래그로 정답을 찾자마자 재귀를 전부 걷어 올리는 것도 맞는 처리입니다.

**하지만 `found` 를 `solution()` 에서 초기화하지 않습니다. 그래서 두 번째 테스트케이스부터 전부 틀립니다.**

## 개선점

### 1. (치명) `static boolean found` 가 호출 간에 살아남는다 — uninitialized-state

```java
static boolean found;      // solution() 에서 되돌리는 코드가 없다
```

`answer` 와 `visited` 는 `solution()` 안에서 새로 만듭니다. `found` 만 빠졌습니다. 첫 호출에서 `true` 가 되고 나면 그 값이 그대로 남아, 다음 호출은 `dfs` 의 첫 줄 `if (found) return;` 에서 즉시 되돌아옵니다. `answer` 는 `answer[0] = "ICN"` 만 채워진 채 나갑니다.

**채점 서버는 테스트케이스마다 `Solution` 인스턴스를 새로 만들지만 `static` 필드는 JVM 안에서 계속 살아 있습니다.** 그래서 첫 케이스만 맞고 나머지가 다 틀립니다.

실제로 돌려본 결과입니다. 같은 JVM 에서 `solution()` 을 세 번 불렀습니다.

```
1회차  tickets = [[ICN,AAA],[AAA,ICN]]   ->  [ICN, AAA, ICN]     (정답)
2회차  tickets = [[ICN,CCC],[CCC,BBB]]   ->  [ICN, null, null]   (기대: [ICN, CCC, BBB])
3회차  tickets = [[ICN,CCC],[CCC,BBB]]   ->  [ICN, null, null]
```

무작위 대조로도 확인했습니다. 사전순 최소 경로를 구하는 독립 구현을 기준으로 4,000건(공항 4개, 티켓 2~5장, 오일러 경로가 반드시 존재하도록 생성)을 돌린 결과 **3,999 / 4,000 불일치** 입니다. 맞은 1건은 첫 호출입니다.

고치는 방법은 한 줄입니다.

```java
public String[] solution(String[][] tickets) {
    Solution.tickets = tickets;
    answer = new String[tickets.length + 1];
    visited = new boolean[tickets.length];
    found = false;                    // <- 이 줄
    answer[0] = "ICN";
    ...
}
```

더 확실한 방법은 `static` 을 떼고 전부 인스턴스 필드로 두는 것입니다. 그러면 "초기화를 빠뜨리면 틀린다" 는 위험 자체가 사라집니다.

### 2. (사소) `cnt++` / `cnt--` 로 인자를 되돌리는 방식

```java
answer[cnt++] = temp;
dfs(temp, cnt);
if (found) return;
cnt--;
```

`cnt` 는 값 인자라 재귀에서 돌아오면 어차피 원래 값입니다. `cnt++` 로 올렸다가 `cnt--` 로 내리는 건 같은 줄 안에서 두 가지 일(대입과 증가)을 하기 때문에 읽는 사람이 한 번 멈추게 됩니다. 아래처럼 쓰면 되돌릴 게 없습니다.

```java
answer[cnt] = tickets[i][1];
dfs(tickets[i][1], cnt + 1);
if (found) return;
visited[i] = false;
```

동작은 같습니다. 위 1번을 고치면 이 부분도 같이 정리하는 게 좋습니다.

### 3. (사소) 정렬이 인자로 받은 배열을 제자리에서 뒤집는다

`Arrays.sort(tickets, ...)` 가 호출자의 배열을 직접 바꿉니다. 채점에는 지장이 없지만, `Solution.tickets = tickets` 로 같은 배열을 static 에도 물려두었기 때문에 "정렬 전 원본" 이 어디에도 남지 않습니다.

### 4. 참고 — 최악 복잡도

모든 티켓을 쓰는 경로가 존재하지 않는 갈래로 깊이 들어갔다가 되돌아 나오는 경우 지수적으로 커질 수 있습니다. 문제 제약이 `티켓 <= 10,000` 이지만 실제 테스트케이스는 백트래킹이 얕게 끝나도록 되어 있어 통과합니다. 완전히 안전하게 가려면 Hierholzer 알고리즘으로 `O(E log E)` 에 오일러 경로를 구하면 됩니다. **이건 지금 고칠 필요는 없습니다** — 1번이 훨씬 급합니다.

## 복잡도

- 시간: 정렬 `O(N log N)` + DFS. 정렬된 순서 덕에 대부분 첫 경로에서 끝나 사실상 `O(N^2)` 이지만, 최악은 백트래킹이 터져 `O(N·N!)` 입니다.
- 공간: `O(N)` — `answer`, `visited`, 재귀 스택.

## 요약

알고리즘 선택과 "정렬 후 첫 완성 경로가 답" 이라는 관찰은 정확합니다. 문제는 `found` 를 `solution()` 에서 되돌리지 않아 **두 번째 테스트케이스부터 전부 `[ICN, null, ...]` 을 내보낸다**는 것이고, 무작위 4,000건 중 3,999건이 틀렸습니다. `found = false;` 한 줄이면 해결됩니다. 같은 실수를 `이성일/week2/아이템줍기.java` 의 `static board` 에서도 했으니, **`static` 필드는 `solution()` 진입 시 전부 초기화한다**를 규칙으로 두면 좋겠습니다.
