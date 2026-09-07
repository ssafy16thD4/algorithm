---
platform: programmers
problemId: "77486"
author: seongil
source: 이성일/week2/다단계칫솔판매.java
week: 2
compiles: true
verdict: good
tags: [dead-code, logic-edge-case, good-complexity, good-readability]
complexity:
  time: O(N + S · D)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-17
---

# 다단계 칫솔 판매 (programmers/77486) — seongil

## 접근

이름 → 인덱스 맵을 먼저 만들고, 매출 건마다 `while` 로 추천인 체인을 올라가며 정산한다.
**네 명 중 가장 짧고(51줄) 복잡도도 가장 좋다.** 답도 맞다.

핵심은 루프 안에서 두 변수를 굴리는 방식이다.

```java
while (!recommender.equals("-")) {
    money = pay;              // 아래에서 올라온 금액
    pay = money / 10;         // 그중 위로 보낼 몫
    if (pay < 1) {            // 10%가 1원 미만 -> 전액 본인
        account[recommenderN] += money;
        break;
    }
    account[recommenderN] += (money - pay);   // 90% 본인
    recommender = referral[recommenderN];     // 한 칸 위로
    recommenderN = empNo.get(recommender);
}
```

`money` 는 "이 사람이 받은 돈", `pay` 는 "위로 넘길 돈". 이 두 줄만 보면 문제 규칙이 그대로 읽힌다.
재귀 없이 반복문으로 끝낸 것도 스택 걱정이 없어 좋다.

## 개선점

### 1. (중요) 도달할 수 없는 분기인데, 도달하면 터진다 — `dead-code` · `logic-edge-case`

```java
if (pay < 1) {
    account[recommenderN] += money;
    continue;
}
```

`while` 루프 **바깥**, 첫 정산 직후의 이 블록이다. 두 가지 문제가 겹쳐 있다.

**(a) 지금은 절대 실행되지 않는다.** 문제 제약상 `amount` 는 1 이상 100 이하이므로
`money = amount * 100 ≥ 100`, `pay = money / 10 ≥ 10` 이다. `pay < 1` 은 성립할 수 없다.

**(b) 만약 실행되면 두 가지가 동시에 깨진다.**

```java
account[publisherN] += (money - pay);   // pay=0 이므로 판매자가 전액 받음 (여기까지 맞음)
...
account[recommenderN] += money;         // 추천인에게 '또' 전액을 준다 -> 이중 계산
```

게다가 판매자의 추천인이 `"-"` 이면 `recommenderN` 은 `enroll.length` 인데,
`account` 의 크기가 정확히 `enroll.length` 라서 `ArrayIndexOutOfBoundsException` 이 난다.

이 블록은 지우는 게 맞다. 루프 안의 `if (pay < 1)` 이 이미 그 역할을 정확히 하고 있다.

### 2. (중요) `"-"` 를 배열 밖 인덱스로 매핑해둔 게 위태롭다 — `logic-edge-case`

```java
empNo.put("-", enroll.length);      // account 의 유효 인덱스는 0 .. enroll.length-1
int[] account = new int[enroll.length];
```

`"-"` 를 "없는 사람"의 표식으로 쓰면서 하필 **배열 바로 바깥**을 가리키게 했다.
지금은 `while` 조건이 `"-"` 를 먼저 걸러내서 안전하지만, 1번 같은 분기가 하나만 더 생기면 바로 터진다.

두 가지 중 하나를 권한다.

- `-1` 로 두어 실수로 인덱싱하면 즉시 예외가 나게 한다 (조용히 넘어가지 않음)
- 또는 김준수처럼 `account` 를 한 칸 크게 잡고 그 칸을 본사 몫으로 실제 사용한다

### 3. (사소) `package codetest;` 가 남아 있다 — `dead-code`

`SWEA1249.java` 와 같은 문제다. IDE 패키지가 딸려왔고, 제출본에는 들어갈 이유가 없다.
저장소 경로(`이성일/week2/`)와도 맞지 않는다.

## 복잡도

- 시간: `O(N + S · D)` — 맵 생성 N, 매출마다 체인 깊이 D만큼. 10% 절삭 때문에 D는 매우 얕다.
  네 명 중 가장 좋다.
- 공간: `O(N)` — 맵 + 결과 배열. 적정하다.

## 요약

가장 짧고 가장 빠르다. `money`/`pay` 두 변수로 규칙을 그대로 표현한 게 좋다.
고칠 것은 실행되지 않는 분기 하나를 지우는 것뿐이고, 그게 사라지면 `"-"` 인덱스 문제도 같이 안전해진다.
