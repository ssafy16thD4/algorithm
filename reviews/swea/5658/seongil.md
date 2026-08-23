---
platform: swea
problemId: "5658"
author: seongil
source: 이성일/week4/SWEA5658.java
week: 4
compiles: true
verdict: good
tags: [good-decomposition, redundant-collection]
complexity:
  time: O(N^2 / 4) per TC
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 보물상자 비밀번호 (swea/5658) — seongil

## 접근

덱을 회전판으로 쓴 구성이 이 문제와 잘 맞습니다. 회전은 `pollLast()` → `offerFirst()` 한 쌍이고, 한 변을 읽을 때는 앞에서 `N/4`개를 꺼내 값을 만든 뒤 그대로 뒤에 되돌려 넣습니다. 네 변을 다 읽으면 덱이 원래 상태로 복귀하므로 **복사본을 만들 필요가 없습니다.**

```java
for (int j = 0; j < 4; j++) {
    char[] num = new char[deq.size()/4];
    for (int i = 0; i < num.length; i++) num[i] = deq.pollFirst();
    set.add(toDecimal(num));
    for (int i = 0; i < num.length; i++) deq.offerLast(num[i]);
}
```

회전 횟수를 `N/4`로 잡은 것도 맞습니다. 그 이상 돌리면 이미 본 배치가 반복됩니다.

역할 분리도 좋습니다 — `rotateAndSave`(회전과 수집), `toDecimal`(16진 변환), `setToArr`(변환) 세 개로 나뉘어 있어 `main` 이 입출력만 담당합니다.

**검증**: 회전·분할·16진 변환을 독립 구현한 기준과 무작위 500건(한 변 1~7자리, 문자 풀이 좁아 값이 겹치는 입력 포함, K는 항상 서로 다른 수의 개수 이하)을 대조했습니다. **불일치 0건**입니다.

성능도 실측했습니다. `N=28` 짜리 테스트케이스 **100,000건을 810 ms** 에 처리했습니다. 실제 문제의 테스트케이스 수보다 훨씬 큰 규모라 여유가 큽니다.

## 개선점

### 1. (사소) `Math.pow` 로 16진수를 만든다 — 부동소수점을 정수 계산에 쓴다

```java
cnt += (num[...] - '0') * (int) Math.pow(16, i);
```

`Math.pow` 는 `double` 을 반환합니다. 여기서는 `16^6 = 16,777,216` 까지라 `double` 이 정확히 표현할 수 있어 **결과가 틀리지 않습니다** — 무작위 500건 대조가 그걸 뒷받침합니다. 다만 자릿수가 더 커지는 문제에서 같은 패턴을 쓰면 반올림 오차가 납니다. 정수 누산이 더 안전하고 빠릅니다.

```java
static int toDecimal(char[] num) {
    int v = 0;
    for (char c : num) v = v * 16 + Character.digit(c, 16);
    return v;
}
```

`Character.digit(c, 16)` 이 `'0'~'9'` 와 `'A'~'F'` 를 모두 처리하므로 `map` 도 필요 없어집니다. 지금 코드의 `num.length - i - 1` 인덱스 계산도 같이 사라집니다.

### 2. (사소) `Map<Character,Integer> map` 은 위 1번을 적용하면 통째로 없어진다 — redundant-collection

`A~F` 를 10~15로 바꾸려고 `HashMap` 을 두었는데, `Character.digit` 한 번이면 끝납니다. `Character` 키라 조회마다 오토박싱도 붙습니다.

### 3. (사소) `Deque<Character>` 대신 `char[]` + 인덱스

값이 아니라 배치만 필요하므로, 문자열을 `char[]` 로 두고 시작 위치를 `(start + offset) % N` 으로 계산하면 덱 자체가 필요 없습니다. 회전이 산술 한 번으로 끝나고 오토박싱도 사라집니다.

```java
for (int r = 0; r < n / 4; r++)
    for (int f = 0; f < 4; f++) {
        int v = 0;
        for (int i = 0; i < side; i++) v = v * 16 + Character.digit(s.charAt((r + f * side + i) % n), 16);
        set.add(v);
    }
```

지금 코드로도 10만 TC에 810 ms 라 통과에는 아무 지장이 없습니다. "덱으로 표현한 회전을 모듈러 인덱스로 펴는 법" 을 익혀두는 차원의 지적입니다.

### 4. (사소) `setToArr` + `Arrays.sort` 대신 `TreeSet` 하나

```java
TreeSet<Integer> set = new TreeSet<>(Comparator.reverseOrder());
...
Iterator<Integer> it = set.iterator();
for (int i = 1; i < k; i++) it.next();
System.out.println("#" + tc + " " + it.next());
```

`arr[arr.length - k]` 라는 뒤집힌 인덱스 계산이 사라져서 "K번째 큰 수" 라는 문제 문장이 코드에 그대로 드러납니다. 지금 방식이 틀린 건 아닙니다.

### 5. 참고 — `package com.ssafy.swb;`

파일이 `이성일/week4/` 에 있는데 패키지 선언은 `com.ssafy.swb` 입니다. `javac` 는 이 상태로도 컴파일하고 저장소 도구도 정상 인식하지만, IDE에서 디렉터리 구조와 어긋난다는 경고가 뜰 수 있습니다. SWEA 제출에는 영향이 없습니다.

## 복잡도

테스트케이스 하나 기준, `N` 을 전체 자릿수라 하면:

- 시간: `O(N^2 / 4)` — 회전 `N/4`회 × 변 4개 × 한 변 `N/4`자리. `N <= 28` 이라 케이스당 200회 미만입니다.
- 공간: `O(N)` — 덱과 `set`. 서로 다른 수는 최대 `N` 개입니다.

## 요약

정확하고(무작위 500건 불일치 0건) 빠릅니다(10만 TC 810 ms). 덱을 회전판으로 쓰면서 네 변을 읽고 원상복귀시키는 구성이 이 문제에 잘 맞고, 함수 분리도 깔끔합니다. 지적은 전부 `Math.pow` 와 `HashMap` 을 정수 계산으로 대체하는 정리 수준이라 통과에는 영향이 없습니다.
