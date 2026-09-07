---
platform: programmers
problemId: "81303"
author: chanung
source: 안찬웅/week3/표 편집.java
week: 3
compiles: true
verdict: good
tags: [wrong-algorithm, collection-choice, dead-code]
complexity:
  time: O(N + 명령의 이동량 합)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 표 편집 (programmers/81303) — chanung

## 접근

`prev`/`next` 배열로 이중 연결 리스트를 만들어 삭제·복구를 연결만 끊었다 붙이는 O(1) 연산으로
처리한다. 삭제된 행 번호를 스택에 쌓고 `Z` 에서 꺼내 **원래 자리**에 다시 끼운다 — 삭제 시점의
`prev[r]`/`next[r]` 이 그대로 남아 있으니 복구에 따로 좌표를 기억할 필요가 없다.

## 개선점

### 1. (수정 완료 / 치명) `Z` 가 삭제된 행 번호가 아니라 조정된 커서를 되돌렸다 — `wrong-algorithm`

```java
table.remove(cursor);
if(table.size() <= cursor) cursor = table.size()-1;
removeTable.offer(cursor);        // 삭제된 "행 번호"가 아니라 조정된 "커서"
...
int cur = removeTable.pollLast();
table.add(cur, cur);              // 커서를 행 번호이자 삽입 위치로 동시에 오용
```

**실제로 돌려본 반례**: `n=5, k=0, cmd=["D 4","C","Z"]`
- `D 4` → 4번 행 선택, `C` → 삭제, 커서가 3으로 조정되며 `removeTable` 에 `3` 이 쌓임(진짜 지운 값은 4)
- `Z` → `table.add(3, 3)` → `[0,1,2,3,3]` — 4번 행은 영영 사라지고 3이 중복으로 끼워짐
- 기대 `"OOOOO"` / 수정 전 실제 `"0000X"`

지금은 삭제 **직전의 행 번호 자체**를 스택에 쌓고, 복구 때 그 번호의 앞뒤 링크를 되살린다.

### 2. (수정 완료 / 치명) 출력 문자가 `O` 가 아니라 숫자 `0` 이었다

`sb.append("0")` → `sb.append('O')`. 파일 맨 위 주석은 처음부터 `O` 라고 적혀 있었다.

### 3. (수정 완료 / 중요) `ArrayList` 라 삭제·복구가 매번 O(N) 이었다 — `collection-choice`

`table.remove(cursor)` / `table.add(cur, cur)` 는 뒤 원소를 전부 밀어서 O(N)이다. 명령이 20만 개인
문제라 정확성을 고쳐도 큰 입력에서 시간 초과가 남는다. `prev`/`next` 배열로 바꿔 O(1)이 됐다.
마지막 판정도 `table.contains(j)`(O(N) 탐색)에서 `deleted[]` 배열 조회로 바뀌었다.

### 4. (수정 완료 / 사소) 명령마다 찍던 디버그 출력 제거 — `dead-code`

### 5. (사소) 남아 있는 위험 요소

`U`/`D` 는 여전히 `move` 만큼 링크를 한 칸씩 따라간다. 이 문제의 제약에서는 통과하는 표준
방식이지만, "이동량 합"이 커지는 변형 문제에서는 이 부분이 병목이 된다.

## 검증

공식 예제 2건 + 위 반례를 컴파일해서 돌렸다.

```
n=8,k=2, [D 2,C,U 3,C,D 4,C,U 2,Z,Z]        -> OOOOXOOO (기대 일치)
n=8,k=2, [... ,Z,Z,U 1,C]                    -> OOXOXOOO (기대 일치)
n=5,k=0, [D 4,C,Z]                           -> OOOOO    (기대 일치)
```

## 복잡도

- 시간: `O(N + 명령의 이동량 합)` — 삭제·복구는 각각 O(1).
- 공간: `O(N)` — prev, next, deleted, 삭제 스택.

## 요약

"무엇을 되돌릴지"를 커서가 아니라 **행 번호**로 잡는 것이 이 문제의 전부였다. 자료구조를 연결
리스트로 바꾸면 그 구분이 코드에 강제로 드러난다 — 인덱스와 값이 같아 보이는 `ArrayList` 에서는
둘이 섞여도 눈에 안 띈다. 참고로 이 파일은 확장자가 없어 자동 컴파일 검사에서 빠져 있었는데,
`.java` 를 붙여 다른 풀이와 같은 검사를 받도록 했다.
