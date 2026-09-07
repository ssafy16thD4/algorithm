---
platform: programmers
problemId: "77486"
author: chanung
source: 안찬웅/week2/다단계 칫솔 판매.java
week: 2
compiles: false
verdict: wrong
tags: [missing-return, uninitialized-state, logic-edge-case, dead-code]
complexity:
  time: O(S · D · N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-16
---

# 다단계 칫솔 판매 (programmers/77486) — chanung

## 접근

판매원별 판매액을 `sellMoney` 에 모으고, 판매가 있는 사람마다 추천인 체인을 따라 올라가며
`totalMoney` 에 금액을 누적하는 재귀(`recommend`)로 풀었다.

문제를 재귀로 본 것 자체는 맞다 — 추천인 관계는 트리고, 이익은 루트 방향으로만 흐른다.
주석에 규칙과 단계를 먼저 적어두고 시작한 것도 좋다.

다만 아래 이유로 현재는 **어떤 입력에도 0만 반환**한다.

## 개선점

### 1. (치명) 컴파일이 안 된다 — import 누락 — `missing-return`

```
안찬웅/week2/다단계 칫솔 판매.java:19: error: cannot find symbol
    static Map<String, Integer> totalMoney;
           ^  symbol: class Map
```

1행에 `import java.util.*;` 가 없다. 다른 파일(`베스트앨범.java`)에는 있으니 습관 문제는 아니고,
이 파일만 빠진 것으로 보인다. 프로그래머스 채점기에서도 동일하게 막힌다.

### 2. (치명) `result` 에 아무것도 넣지 않는다 — `uninitialized-state`

`result` 는 세 곳에만 등장한다.

```java
static int[] result;                        // 18행 선언
result = new int[enroll.length];            // 25행 0으로 초기화
return result;                              // 44행 그대로 반환
```

계산 결과는 전부 `totalMoney`(이름 → 금액)에 들어가는데, 그걸 `result`(인덱스 → 금액)로
옮기는 코드가 없다. 그래서 `import` 를 고쳐 컴파일이 되더라도 **반환값은 항상 전부 0**이다.

`enroll` 순서대로 옮겨주면 된다.

```java
for (int i = 0; i < enroll.length; i++) {
    result[i] = totalMoney.getOrDefault(enroll[i], 0);
}
return result;
```

### 3. (치명) 위로 올려보내는 금액과 본인 몫이 뒤바뀌었다 — `logic-edge-case`

문제 규칙은 **본인이 90%를 갖고, 추천인에게 10%를 올려보낸다**. 그런데 67·69행은 반대다.

```java
recommend(referral[i], money - money / 10, enroll, referral);   // 90%를 위로 올림
totalMoney.put(enroll[i], totalMoney.get(referralName) + money - money / 10);  // 추천인에게 90%를 줌
```

`money - money/10` 은 90%다. 이걸 추천인에게 주고, 90%를 다시 위로 올리고 있다.
게다가 **판매 당사자는 한 번도 적립되지 않는다** — 최초 호출이 `recommend(referral[i], ...)`,
즉 추천인 이름으로 시작하기 때문에 본인 몫을 넣을 자리가 없다.

사람 기준으로 다시 쓰면 규칙이 그대로 드러난다.

```java
// person 이 money 를 벌었을 때의 분배
static void distribute(String person, int money) {
    int up = money / 10;                                 // 추천인 몫 10%
    if (up < 1) {                                        // 1원 미만이면 분배 없음
        totalMoney.merge(person, money, Integer::sum);   // 전액 본인
        return;
    }
    totalMoney.merge(person, money - up, Integer::sum);  // 90% 본인
    // 추천인이 없으면 up 은 본사 몫이라 그냥 버린다 (본인이 갖는 게 아니다)
    if (!parent.get(person).equals("-")) distribute(parent.get(person), up);
}
```

> **`up < 1` 과 `추천인이 "-"` 를 한 조건으로 묶으면 안 된다.**
> 추천인이 없어도 10%는 본사가 가져가므로 본인은 90%만 갖는다. 두 경우의 본인 몫이 다르다.
> (이 리뷰의 초안이 그렇게 묶었다가, 무작위 2만 건 대조에서 19,842건이 어긋나 바로잡았다.)

### 4. (중요) "10%가 1원 미만이면 분배 중단" 규칙이 빠졌다 — `logic-edge-case`

54~57행에 주석으로 남아는 있는데 실제 코드에는 없다.

```java
// if(money * 0.1 >= 1) {
//     center += money * 0.1;
// }
```

이 조건이 없으면 금액이 작아져도 계속 위로 전파돼서 상위 판매원 금액이 과대 계상된다.
위 3번 코드의 `if (up < 1)` 이 그 역할을 한다. 참고로 `money * 0.1` 은 `double` 이라
부동소수점이 섞인다 — `money / 10` 정수 나눗셈으로 두는 편이 안전하다.

### 5. (중요) 같은 판매원이 여러 번 팔면 마지막 것만 남는다 — `logic-edge-case`

```java
sellMoney.put(seller[i], amount[i] * 100);   // 29행
```

`seller` 배열에는 같은 이름이 여러 번 나올 수 있다. `put` 은 덮어쓰므로 앞의 판매가 사라진다.

```java
sellMoney.merge(seller[i], amount[i] * 100, Integer::sum);
```

### 6. (중요) 추천인을 찾을 때마다 `enroll` 전체를 훑는다

```java
for(int i=0; i<enroll.length; i++) {
    if(enroll[i].equals(referralName)) { ... }
}
```

체인을 한 칸 올라갈 때마다 O(N) 선형 탐색이라 전체가 O(S · D · N)이 된다.
이름 → 추천인 맵을 미리 만들어두면 한 칸 이동이 O(1)이다.

```java
Map<String, String> parent = new HashMap<>();
for (int i = 0; i < enroll.length; i++) parent.put(enroll[i], referral[i]);
```

이러면 `enroll`/`referral` 배열을 재귀 인자로 넘길 필요도 없어진다.

### 7. (사소) 디버그 출력이 남아 있다 — `dead-code`

43행 `System.out.println("최종답: " + totalMoney)`, 52행의 호출별 출력이 그대로 있다.
재귀 호출마다 문자열을 만들어 찍기 때문에 시간 초과의 직접적인 원인이 될 수 있다.

## 복잡도

- 시간: `O(S · D · N)` — 판매 건수 S × 추천 체인 깊이 D × 이름 선형 탐색 N.
  6번을 적용하면 `O(S · D)` 로 줄어든다.
- 공간: `O(N)` — 맵 두 개. 적정하다.

## 요약

추천 관계를 재귀로 올라가는 뼈대는 맞다. 다만 `result` 를 채우지 않아 항상 0을 반환하고,
본인 몫과 추천인 몫이 뒤바뀌어 있어 현재는 동작하지 않는다.
"사람이 돈을 벌면 90%를 갖고 10%를 위로 넘긴다"는 한 문장을 그대로 함수 하나로 옮기면
3·4번이 같이 해결되고, 추천인 맵(6번)까지 만들면 인자도 깔끔해진다.
