---
platform: programmers
problemId: "81303"
author: chanung
source: 안찬웅/week3/표 편집
week: 3
compiles: true
verdict: wrong
tags: [wrong-algorithm, collection-choice, dead-code]
complexity:
  time: O(N*K) 최악
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — chanung

## 접근

`resolve.mjs`는 이 파일이 `.java` 확장자가 없어 `compiles: null`(javac 확인 불가)로 표시하지만,
임시 파일로 확장자를 붙여 직접 컴파일해보니 정상적으로 컴파일된다 — 위 프론트매터의 `compiles: true`는
그 수동 확인 결과다. 파일명에 `.java`가 없어 저장소의 자동 리뷰 워크플로가 이 파일을 못 찾는 문제는
CLAUDE.md에 이미 기록돼 있고, 파일명을 바꾸는 것은 본인 몫이라 이 리뷰에서는 손대지 않았다.

`table`을 원래 행 번호가 들어있는 정렬된 리스트로 보고, `U`/`D`를 인덱스 이동으로, `C`를
`table.remove(cursor)`(인덱스 삭제)로 처리하는 구조 자체는 방향이 맞다.

## 개선점

### 1. (치명) `Z` 복구가 삭제된 행 번호가 아니라 커서(인덱스)를 다시 넣는다 — `wrong-algorithm`

```java
} else if(command.equals("C")) {
    table.remove(cursor);
    if(table.size() <= cursor) {
        cursor = table.size()-1;
    }
    removeTable.offer(cursor);   // <- 삭제된 "행 번호"가 아니라 조정된 "커서(인덱스)"를 저장
} else if(command.equals("Z")) {
    int cur = removeTable.pollLast();
    table.add(cur, cur);         // <- 커서 값을 행 번호이자 삽입 위치로 동시에 오용
}
```

`removeTable`에는 방금 지운 **행 번호**를 저장해야 하는데, 실제로는 삭제 뒤 조정된 **커서(인덱스)**를
저장한다. 이 둘은 일반적으로 다른 값이다. `Z`에서는 이 값을 행 번호이자 삽입 인덱스로 동시에 쓰기
때문에, 지워진 진짜 행이 영영 사라지고 엉뚱한 값이 중복으로 끼워 넣어진다.

실제로 컴파일해서 돌려본 반례: `n=5, k=0, cmd=["D 4","C","Z"]`.
- `D 4`로 4번 행(마지막 행) 선택 → `C`로 4번 행 삭제, `table=[0,1,2,3]`, 커서는 마지막 행(3)으로 이동
  → `removeTable`에 `3`이 쌓인다 (진짜 지워진 값 `4`가 아니라 커서 `3`).
- `Z` → `table.add(3, 3)` → `table=[0,1,2,3,3]` (4번 행은 복구되지 않고, 3이 중복으로 끼워짐)

기대 출력은 전부 복구되었으니 `OOOOO`여야 하는데, 실제 실행 결과는 `0000X`였다
(마지막 자리 X — 4번 행이 영구히 사라진 것이 그대로 드러남. 항목 2의 `O`/`0` 문제 때문에 문자는
`0`으로 나오지만, X가 찍힌다는 것 자체가 값 유실을 보여준다).
더 복잡한 명령 시퀀스(`n=8,k=2`, U/D/C/Z 아홉 개 조합)로도 같은 증상을 재현했다 — 마지막 자리만
X이고 중간에 있어야 할 삭제가 유실됐다.

수정 방향: `C`에서 삭제 직전에 `int removedValue = table.get(cursor);`로 값을 따로 저장하고,
`removeTable`에는 `(삭제된 인덱스, removedValue)` 쌍을 저장해서 `Z`에서 `table.add(그때인덱스, removedValue)`로
복구해야 한다. 이 수정안은 별도로 구현해 검증하지는 않았다(**검증 안 함** — 팀원의 정답 코드가 있으면
무작위 대조로 확인하는 것을 권한다).

### 2. (치명) 출력 문자가 `O`(문자)가 아니라 `0`(숫자)이다

```java
sb.append("0");
```

파일 맨 위 주석 자체가 ":처음 표와 비교해서 삭제되었으면 X 아니면 O"라고 명시하는데, 실제 구현은
문자 `O` 대신 숫자 `0`을 채운다. 위 반례 실행 결과에 `0000X`로 찍힌 것도 이 때문이다. `sb.append("O")`로
바꿔야 한다.

### 3. (사소) 디버그용 `System.out.println`이 그대로 남아있다 — `dead-code`

```java
System.out.println(table);
System.out.println("cursor: " + cursor + " cmd: " + cmd[i] + " table: " + table.size());
```

채점에는 영향 없지만(반환값만 채점됨) 제출 전에 지우는 게 맞다.

### 4. (중요) `ArrayList.remove`/`add`가 매 명령마다 O(N)이라 전체가 O(N*K)다 — `collection-choice`

`table.remove(cursor)`와 `table.add(cur, cur)`는 인덱스 뒤쪽 원소를 전부 한 칸씩 밀어야 해서 O(N)이다.
이 문제는 명령 수와 행 수가 커서(정확한 제약 수치는 재확인 필요 — 검증 안 함) `O(N*K)`가 위험한
오더가 되기 쉽고, 표 편집류 문제는 보통 이중 연결 리스트로 삭제/복구를 O(1)에 처리하도록 요구된다.
지금 구현은 정확성 버그(1, 2번)를 고치더라도 큰 입력에서 시간 초과 위험이 남는다.

## 복잡도

- 시간: `O(N*K)` 최악 — `ArrayList` 인덱스 삭제/삽입이 매 명령마다 O(N).
- 공간: `O(N)` — `table`, `removeTable`.

## 요약

인덱스를 커서로 쓰는 전체 뼈대는 방향이 맞지만, `Z` 복구가 삭제된 행 번호 대신 커서 값을 잘못
재사용해서 실제로 값이 유실되고 중복이 생긴다(직접 실행해서 확인). 출력 문자도 `O` 대신 `0`이 찍히는
별개의 버그가 있다. 두 버그 모두 기초적인 테스트만으로 드러나는 수준이라, 정확성부터 먼저 고쳐야 한다.
