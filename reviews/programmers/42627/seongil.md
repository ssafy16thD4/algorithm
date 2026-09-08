---
platform: programmers
problemId: "42627"
author: seongil
source: 이성일/week2/디스크컨트롤러.java
week: 2
compiles: true
verdict: needs-fix
tags: [time-complexity, uninitialized-state]
complexity:
  time: O(N² log N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-17
---

# 디스크 컨트롤러 (programmers/42627) — seongil

## 접근

**네 명 중 유일하게 다른 방식이다.** 정렬용 배열과 대기 큐를 나누지 않고,
우선순위 큐 하나에 전부 넣은 뒤 요청시각을 현재 시각으로 "갱신"해가며 되넣는다.

```java
} else if (v[0] < t) {                  // 이미 도착해 기다리던 작업
    pq.offer(new int[]{t, v[1]});       // 요청시각을 t 로 갱신해서 다시 넣고
    turnAround += (t - v[0]);           // 그동안 기다린 시간을 먼저 적립
}
```

대기 시간을 **미리 쪼개서 적립**하고, 요청시각을 현재로 당겨 재삽입한다.
그러면 큐의 1순위(요청시각)가 같아진 것들끼리 2순위(소요시간)로 겨루게 되어
결과적으로 "도착한 것 중 가장 짧은 작업"이 뽑힌다.

발상이 좋다. 실제로 `[[0,3],[1,9],[2,6]]` 을 넣어보면 정확히 9가 나온다.
직접 손으로 굴려봤을 때 답이 맞는 것도 확인했다.

## 개선점

### 1. (중요) 되넣기 때문에 작업이 큐를 여러 번 통과한다 — `time-complexity`

각 작업은 시각 `t` 가 바뀔 때마다 한 번씩 "갱신 후 재삽입"될 수 있다.
`t` 가 바뀌는 횟수가 O(N)이므로 큐 연산이 최악 **O(N²)**, 전체 `O(N² log N)` 이다.

제약이 `jobs` 길이 500이라 500² × log ≈ 450만으로 **통과한다.**
다만 같은 문제를 이승주·안찬웅은 `O(N log N)` 으로 푼다. 차이는 구조 하나다.

```java
// 요청시각순으로 정렬해두고, 포인터로 "도착한 것만" 큐에 넣는다
Arrays.sort(jobs, (a, b) -> Integer.compare(a[0], b[0]));
int idx = 0;
while (done < jobs.length) {
    while (idx < jobs.length && jobs[idx][0] <= t) pq.offer(jobs[idx++]);
    ...
}
```

이러면 각 작업이 큐를 딱 한 번 통과한다. 되넣기가 아예 사라지고,
`v[0] > t` / `v[0] < t` / `v[0] == t` 세 갈래도 없어진다.

지금 방식은 대기 시간을 조각내서 적립하는 만큼 **머릿속으로 검증하기 어렵다.**
답이 맞는지 확인하려면 예제를 손으로 끝까지 굴려야 하는데, 그건 나중에 이 코드를 볼 사람에게도 마찬가지다.

### 2. (중요) `answer` 가 `static` 이다 — `uninitialized-state`

```java
public static int answer = 0;
...
answer = turnAround / jobs.length;
return answer;
```

`answer` 는 이 메서드 안에서만 쓰이는 값인데 클래스 전체가 공유하는 필드로 잡혀 있다.
채점기가 같은 JVM에서 여러 번 호출하면 이전 값이 남는다.
지금은 항상 대입 후 반환해서 안전하지만, 지역 변수로 두면 그 위험 자체가 없다.

```java
return turnAround / jobs.length;   // 필드 불필요
```

`입국심사` 에서도 `static answer` 로 결과를 넘겼다. 반복되는 패턴이니 한 번에 고쳐두면 좋다.

## 복잡도

- 시간: `O(N² log N)` — 되넣기 때문. 1번을 적용하면 `O(N log N)`.
- 공간: `O(N)` — 큐. 적정하다.

## 요약

접근이 독창적이고 실제로 맞다. 대기 시간을 조각내 적립한다는 발상은 설명할 가치가 있다.
다만 되넣기가 복잡도와 가독성을 동시에 깎는다 —
"요청시각순 정렬 + 도착한 것만 큐에 투입" 으로 바꾸면 세 갈래 분기가 통째로 사라진다.
