---
platform: programmers
problemId: "64064"
author: junsoo
source: 김준수/week4/불량 사용자.java
week: 4
compiles: true
verdict: good
tags: [good-decomposition, redundant-collection]
complexity:
  time: O(U^B · B log B)
  space: O(U^B · B)
generatedBy: claude-code-local
generatedAt: 2026-08-24
---

# 불량 사용자 (programmers/64064) — junsoo

## 접근

이 문제의 함정은 "서로 다른 순서로 뽑힌 같은 집합" 을 하나로 세는 것인데, 그 처리를 정확히 했습니다.

1. 제재 아이디마다 매칭되는 응모자 **인덱스** 목록을 미리 만든다 (`banList`)
2. DFS로 각 제재 아이디에 응모자를 하나씩 배정한다
3. 끝에서 중복 배정을 걸러내고, **정렬한 뒤** `Set<List<Integer>>` 에 넣어 집합 단위로 중복 제거

파일 맨 위 시행착오 주석이 특히 좋습니다.

```
- Set<int[]>은 같은 요소가 똑같이 위치한 배열에 대한 중복 제거가 안됨(기본 배열은 equals가 없어서)
- Set<List<Integer>>를 쓰면 List는 equals가 있기 때문에 중복 제거가 가능함
```

**배열의 `equals` 가 참조 비교라는 걸 직접 부딪혀 알아내고 기록해둔 것**이라, 다음에 같은 함정을 만나도 안 걸립니다. 리뷰에서 지적할 게 아니라 다른 사람이 읽어야 할 메모입니다.

문자열을 그대로 다루지 않고 인덱스(`int`)로 바꿔 비교한 것도 맞는 판단입니다. `check` 가 `*` 를 건너뛰고 길이부터 거르는 것도 정확합니다.

**검증**: 완전탐색으로 독립 구현한 기준과 무작위 4,000건(응모자 1~5명, 아이디 길이 1~3, 문자 2종이라 매칭이 자주 겹치게, 제재 아이디는 실제 응모자에서 `*` 를 무작위로 씌워 생성)을 대조했습니다. **불일치 0건**입니다.

## 개선점

### 1. (사소) 중복 배정 검사를 잎에서만 한다 — redundant-collection

```java
if(depth == maxLen){
    Set<Integer> checkSet = new HashSet<>();
    for(int b : banned) checkSet.add(b);
    if(checkSet.size() != maxLen) return;
```

같은 응모자가 두 번 배정된 조합도 끝까지 내려간 뒤에 버립니다. 배정 중에 걸러내면 그 가지를 아예 안 타고, 잎에서 `HashSet` 을 새로 만들 필요도 없습니다.

```java
private void dfs(int depth, List<Integer> banned) {
    if (depth == maxLen) {
        List<Integer> sorted = new ArrayList<>(banned);
        Collections.sort(sorted);
        result.add(sorted);
        return;
    }
    for (int idIdx : banList[depth]) {
        if (banned.contains(idIdx)) continue;     // <- 여기서 거른다
        banned.add(idIdx);
        dfs(depth + 1, banned);
        banned.remove(banned.size() - 1);         // 백트래킹
    }
}
```

문제 제약이 `응모자 <= 8`, `제재 아이디 <= 응모자 수` 라 지금 방식으로도 충분히 빠릅니다. 정답에는 영향이 없습니다.

### 2. (사소) 재귀마다 리스트를 통째로 복사한다

```java
List<Integer> newBanned = new ArrayList<>(banned);
newBanned.add(idIdx);
dfs(depth + 1, newBanned);
```

주석에 `// 백트래킹 없이 DFS만` 이라고 적어둔 그 선택입니다. 복사본을 만들면 되돌릴 게 없어 실수 여지가 줄어드는 대신, 깊이 × 가지 수만큼 리스트가 생깁니다. 위 1번 코드처럼 `add` / `remove` 로 되돌리면 리스트 하나로 끝납니다.

**다만 지금 방식에는 장점이 하나 있습니다** — `result.add(banned)` 로 리스트를 그대로 집어넣어도 안전합니다. 백트래킹으로 바꾸면 같은 리스트를 계속 재사용하므로 `result` 에 넣기 전에 반드시 복사해야 하고(위 코드의 `sorted`), 그걸 빠뜨리면 `Set` 안의 모든 원소가 같은 객체를 가리키게 됩니다. **바꾼다면 이 지점을 꼭 같이 보세요.**

### 3. (사소) `List<Integer>[] banList` 는 raw type 이다

`new List[banned_id.length]` 는 제네릭 배열을 못 만들어 raw 배열을 담은 것이라 컴파일러가 unchecked 경고를 냅니다. `List<List<Integer>>` 로 두면 사라집니다.

### 4. (사소) `static` 필드 3개

`result`, `maxLen`, `banList` 가 `static` 인데 `solution()` 안에서 **매번 새로 대입하므로 테스트케이스 간 오염은 없습니다.** 그 부분은 제대로 했습니다. 다만 같은 주차의 `이성일/week4/여행경로.java` 는 `static boolean found` 하나를 초기화하지 않아 두 번째 케이스부터 전부 틀렸습니다. `static` 을 쓸 거면 여기처럼 진입 시 전부 다시 대입하는 습관이 맞습니다.

## 복잡도

`U` = 응모자 수(최대 8), `B` = 제재 아이디 수(최대 `U`).

- 시간: `O(U^B · B log B)` — 각 제재 아이디마다 최대 `U`개 후보를 시도하고, 잎마다 정렬이 `O(B log B)`. `U=8` 이라 최악에도 백만 단위입니다.
- 공간: `O(U^B · B)` — `result` 에 담기는 조합 수. 실제로는 중복 제거 후라 훨씬 작습니다.

## 요약

집합 중복 제거라는 이 문제의 핵심을 정확히 처리했고, 무작위 4,000건 대조에서 오답이 없었습니다. `Set<int[]>` 이 왜 안 되는지 직접 확인하고 주석으로 남긴 것이 이 파일에서 가장 값어치 있는 부분입니다. 지적은 전부 가지치기·복사 비용 수준이라 제약이 작은 이 문제에서는 지금 그대로 둬도 됩니다.
