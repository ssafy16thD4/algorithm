---
platform: programmers
problemId: "77486"
author: chanung
source: 안찬웅/week2/다단계 칫솔 판매.java
week: 2
compiles: true
verdict: good
tags: [logic-edge-case, uninitialized-state, missing-return, redundant-loop]
complexity:
  time: O(S · D)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 다단계 칫솔 판매 (programmers/77486) — chanung

## 접근

`parent<이름, 추천인>` 맵을 먼저 만들어 두고, 판매 건마다 `distribute(판매자, amount*100)` 을 불러
추천 체인을 한 칸씩 거슬러 올라가며 90/10 을 나눈다. **"사람이 돈을 벌면 90%를 갖고 10%를 위로
넘긴다"** 는 문제 한 문장이 함수 하나에 그대로 들어가 있어서, 규칙과 코드를 나란히 놓고 읽을 수 있다.

## 개선점

### 1. (수정 완료 / 치명) 컴파일이 안 됐다 — import 누락 — `missing-return`

`import java.util.*;` 가 없어 `Map`/`HashMap` 이 심볼 미해결이었다. 프로그래머스 채점기에서도
같은 자리에서 막힌다. 1행에 추가해 해결.

### 2. (수정 완료 / 치명) `result` 에 아무것도 담지 않아 항상 0 배열을 반환했다 — `uninitialized-state`

계산 결과는 전부 `totalMoney`(이름 → 금액)에 들어가는데 `result`(인덱스 → 금액)로 옮기는 코드가
없었다. `enroll` 순서로 옮기는 루프를 넣었다.

### 3. (수정 완료 / 치명) 본인 몫과 추천인 몫이 뒤바뀌어 있었다 — `logic-edge-case`

`money - money/10`(=90%)을 추천인에게 주고, 90%를 다시 위로 올리고 있었다. 게다가 최초 호출이
`recommend(referral[i], ...)` 라 **판매 당사자는 한 번도 적립되지 않았다.** 사람 기준 함수로 다시 썼다.

```java
static void distribute(String person, int money) {
    int up = money / 10;
    if(up < 1) { total.merge(person, money, Integer::sum); return; }  // 1원 미만이면 전액 본인
    total.merge(person, money - up, Integer::sum);                    // 90% 본인
    String p = parent.get(person);
    if(!p.equals("-")) distribute(p, up);                             // 추천인 없으면 up 은 본사 몫
}
```

> **`up < 1` 과 `추천인이 "-"` 를 한 조건으로 묶으면 안 된다.** 추천인이 없어도 10%는 본사가
> 가져가므로 본인은 90%만 갖는다. 두 경우의 본인 몫이 다르다. (이 리뷰 초안이 그렇게 묶었다가
> 무작위 2만 건 대조에서 19,842건이 어긋났다.)

### 4. (수정 완료 / 중요) "10%가 1원 미만이면 분배 중단" 규칙이 주석에만 있었다 — `logic-edge-case`

`if(up < 1)` 로 들어갔다. `money * 0.1` 은 double 이라 부동소수점이 섞인다 —
`money / 10` 정수 나눗셈이 맞다.

### 5. (수정 완료 / 중요) 같은 판매원이 여러 번 팔면 마지막 것만 남았다 — `logic-edge-case`

`sellMoney.put(seller[i], ...)` 이 덮어써서 앞 판매가 사라졌다. 지금은 `seller` 배열을 그대로 돌며
판매 건마다 `distribute` 를 부른다 — 10% 규칙이 건별로 적용돼야 하므로 합산해서 한 번 부르는 것보다
이쪽이 정확하다.

### 6. (수정 완료 / 중요) 추천인을 찾을 때마다 `enroll` 전체를 훑었다 — `redundant-loop`

체인 한 칸 이동이 O(N) 선형 탐색이라 전체가 O(S·D·N) 이었다. `parent` 맵으로 O(1) 이 됐고,
`enroll`/`referral` 을 재귀 인자로 넘길 필요도 없어졌다.

### 7. (수정 완료 / 사소) 디버그 출력 제거 — `dead-code`

재귀 호출마다 문자열을 만들어 찍고 있었다. 시간 초과의 직접적 원인이 될 수 있다.

## 검증

공식 예제 2건을 실제로 컴파일해서 돌렸다.

```
seller={young,john,tod,emily,mary}, amount={12,4,2,5,10}
  -> [360, 958, 108, 0, 450, 18, 180, 1080]  (기대값 일치)
seller={sam,emily,jaimie,edward}, amount={2,3,5,4}
  -> [0, 110, 378, 180, 270, 450, 0, 0]      (기대값 일치)
```

## 복잡도

- 시간: `O(S · D)` — 판매 건수 S × 추천 체인 깊이 D. 맵 조회가 O(1)이라 N 이 빠졌다.
- 공간: `O(N)` — 맵 두 개.

## 요약

추천 관계를 재귀로 거슬러 올라가는 뼈대는 처음부터 맞았다. 분배 방향이 뒤집혀 있고 반환값을
채우지 않던 것이 문제였는데, "사람 하나가 돈을 벌었을 때"를 기준으로 함수를 다시 쓰니 그 두 개와
1원 미만 규칙까지 한꺼번에 정리됐다. 공식 예제 2건 통과 확인.
