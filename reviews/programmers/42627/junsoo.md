---
platform: programmers
problemId: "42627"
author: junsoo
source: 김준수/week2/디스크 컨트롤러.java
week: 2
compiles: true
verdict: needs-fix
tags: [magic-branch, redundant-collection, nonstatic-inner-class]
complexity:
  time: O(N² log N + T)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-17
---

# 디스크 컨트롤러 (programmers/42627) — junsoo

## 접근

우선순위 큐에 전부 넣고, 소요시간이 짧은 것부터 꺼내되 아직 도착 안 한 작업은
임시 리스트에 빼뒀다가 되돌려 넣는다. 답은 맞다.

`Task` 에 작업번호·요청시각·소요시간·반환시간을 담아 의미를 이름으로 드러낸 건 좋다.

## 개선점

### 1. (중요) `compareTo` 가 20줄이다 — `magic-branch`

```java
if(spend < t.spend) return -1;
else if(spend > t.spend) return 1;
else {
    if(call < t.call) return -1;
    else if(call > t.call) return 1;
    else {
        if(taskNum < t.taskNum) return -1;
        else if(taskNum > t.taskNum) return 1;
        else return 0;
    }
}
```

`Integer.compare` 를 쓰면 3줄이다.

```java
public int compareTo(Task t) {
    if (spend != t.spend) return Integer.compare(spend, t.spend);
    if (call != t.call) return Integer.compare(call, t.call);
    return Integer.compare(taskNum, t.taskNum);
}
```

같은 회차 안찬웅 코드가 정확히 이 형태다. 로직은 동일한데 길이가 1/6이고,
"세 기준을 순서대로 본다"가 한눈에 읽힌다. 중첩이 깊어질수록 부호를 잘못 넣을 확률도 올라간다.

### 2. (중요) 매번 큐를 헤집어 되넣는다 — `time-complexity`

```java
List<Task> notYet = new ArrayList<>();
while(curr.call > time){
    notYet.add(curr);
    curr = pq.poll();        // 도착한 게 나올 때까지 계속 뽑고
}
for(Task t : notYet){
    pq.offer(t);             // 뽑았던 걸 전부 되넣는다
}
```

한 작업을 고르려고 큐를 최악 O(N)번 뽑고 O(N)번 되넣는다. 전체 `O(N² log N)` 이다.
제약이 500이라 통과하지만, 되넣기를 없애는 표준형이 있다.

```java
// 요청시각순 정렬 + 포인터로 "도착한 것만" 큐에 투입
Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));
int idx = 0;
while (done < jobs.length) {
    while (idx < jobs.length && jobs[idx][0] <= time) {
        pq.offer(new Task(idx, jobs[idx][0], jobs[idx][1]));
        idx++;
    }
    ...
}
```

각 작업이 큐를 한 번만 통과해서 `O(N log N)` 이 된다. `notYet` 리스트도 통째로 사라진다.
이승주·안찬웅 코드가 이 형태다.

### 3. (사소) `resultList` 와 `ta` 필드는 없어도 된다 — `redundant-collection`

```java
curr.ta = time - curr.call;
resultList.add(curr);
...
int answer = 0;
for(Task t : resultList){ answer += t.ta; }
return answer / resultList.size();
```

반환 시간을 **필드에 저장해뒀다가 마지막에 다시 순회해서 더한다.**
합만 필요하므로 그 자리에서 누적하면 리스트도 필드도 필요 없다.

```java
total += time - curr.call;      // 처리하면서 바로 누적
...
return total / jobs.length;
```

### 4. (사소) `Task` 는 `static` 중첩 클래스여야 한다 — `nonstatic-inner-class`

```java
class Solution {
    class Task implements Comparable<Task>{   // static 누락
```

`Task` 는 `Solution` 의 상태를 쓰지 않으므로 `static class Task` 가 맞다.
안찬웅은 같은 문제에서 `static class Node` 로 선언했다.

## 복잡도

- 시간: `O(N² log N + T)` — 되넣기 때문. 2번을 적용하면 `O(N log N)`.
- 공간: `O(N)` — 큐 + `notYet` + `resultList`. 3번을 적용하면 큐만 남는다.

## 요약

동작은 맞고 `Task` 로 의미를 드러낸 것도 좋다.
다만 `compareTo` 20줄과 큐 되넣기가 둘 다 "표준형을 몰라서" 생긴 길이다.
`Integer.compare` 3줄, 그리고 요청시각순 정렬 + 포인터 투입 —
이 두 가지만 알면 코드가 절반으로 줄고 복잡도도 한 단계 내려간다.
