---
platform: swea
problemId: "5658"
author: chanung
source: 안찬웅/week1/SWEA5658.java
week: 1
compiles: true
verdict: good
tags: [naming, collection-choice, string-concat-in-loop]
complexity:
  time: O(N^2)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 보물상자 비밀번호 (swea/5658) — chanung

## 접근

`section`(=n/4)번만 회전시키면서 매번 리스트를 `section` 길이로 균등 분할하는 방식이다. 직접 좌표로
따져보면, 회전 `r`(0..section-1)과 분할 인덱스 `k`(0..n/section-1)의 조합 `(k*section - r) mod n`가
0..n-1을 정확히 한 번씩 덮는다 — 즉 원래 표준 풀이처럼 n번 전부 회전하지 않고도 n개의 시작 위치를
모두 커버하는 영리한 방식이고, 실제로 맞는 계산이다.

## 개선점

### 1. (사소) 지역 변수 `k`가 필드 `k`(K번째 값)를 가린다 — `naming`

```java
for(int k=0; k<section; k++) { ... }
...
answer = list2.get(k-1); // 루프 밖이라 필드 k(입력받은 K)를 가리킴
```

루프 안에서는 지역 `k`(회전 횟수)가, 루프 밖 `list2.get(k-1)`에서는 필드 `k`(입력 K)가 쓰인다.
같은 이름이 스코프에 따라 다른 값을 가리켜서 지금은 우연히 동작하지만, 읽는 사람이 헷갈리기 쉽다.
`for(int r=0; r<section; r++)`처럼 다른 이름을 쓰는 게 안전하다.

### 2. (사소) 중복 체크에 `List.contains` 대신 `Set`을 쓰면 더 명확하다 — `collection-choice`

```java
if(!list2.contains(x)) list2.add(x);
```

매 삽입마다 O(원소 수) 선형 탐색이다. N이 작아 체감 성능 차이는 없지만, `TreeSet`(내림차순 comparator)을
쓰면 `contains`+`sort`를 한 번에 대체할 수 있어 의도("중복 없이, 정렬된 상태로 유지")가 코드에 더 드러난다.

### 3. (사소) 문자열 `+=`로 16진수 조각을 만든다 — `string-concat-in-loop`

```java
str += list1.get(i);
```

`section` 자리마다 새 `String` 객체가 만들어진다. 자리 수가 작아 실질적 영향은 없지만
`StringBuilder`가 관례적으로 더 맞다.

## 복잡도

- 시간: `O(N^2)` — section번 회전 × 매 회전마다 N 순회 + list2 내림차순 정렬(최대 N번).
- 공간: `O(N)` — list1, list2.

## 요약

회전 커버리지 계산은 좌표로 검산해봐도 정확하다. 지적한 것들은 전부 스타일/사소한 비효율이고
알고리즘 자체는 옳다.
