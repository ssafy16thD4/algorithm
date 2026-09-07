---
platform: programmers
problemId: "64064"
author: chanung
source: 안찬웅/week4/불량 사용자.java
week: 4
compiles: true
verdict: good
tags: [wrong-algorithm]
complexity:
  time: O(B! × U × L)
  space: O(U + 조합 수)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 불량 사용자 (programmers/64064) — chanung

## 접근

`banned_id` 를 깊이로 삼는 백트래킹으로 "각 `banned_id` 에 서로 다른 `user_id` 인덱스를 하나씩
배정" 하는 모든 경우를 만든다. 깊이 끝에서 고른 인덱스를 **오름차순 문자열 키**로 만들어 `Set` 에
넣기 때문에, 배정 순서만 다른 같은 조합이 자동으로 하나로 합쳐진다.

## 개선점

### 1. (수정 완료 / 치명) 문제를 잘못 풀고 있었다 — 조합 수가 아니라 최대 매칭 수를 반환 — `wrong-algorithm`

이전 코드는 `banned_id` 각각에 매칭되는 `user_id` 개수를 세고 그 최댓값을 반환했다. 이 문제가 묻는
것은 "**서로 다른 banned_id 에 서로 다른 user_id 를 하나씩 배정하는 방법이 몇 가지냐**"다.
여러 `banned_id` 가 후보를 공유할 때의 조합을 전혀 계산하지 못한다.

**실제로 돌려본 반례** — 공식 예시 3번:

```java
user_id   = {"frodo","fradi","crodo","abc123","frodoc"}
banned_id = {"fr*d*","*rodo","******","******"}
// 수정 전 출력: 2   기대: 3
```

`"fr*d*"` 가 2개, `"*rodo"` 가 2개에 매칭돼서 `maxCnt=2` 로 끝났다.

수정 후 구조:

```java
static void dfs(int depth) {
    if(depth == banned.length) {          // 인덱스 오름차순 키 -> 순서 차이가 사라진다
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<used.length; i++) if(used[i]) sb.append(i).append(',');
        answers.add(sb.toString());
        return;
    }
    for(int i=0; i<user.length; i++) {
        if(used[i] || !verse(user[i], banned[depth])) continue;
        used[i] = true;  dfs(depth + 1);  used[i] = false;
    }
}
```

### 2. (수정 완료 / 사소) 길이 비교를 `verse` 안으로 옮겼다

호출부에서 `lenUserId == lenBananaId` 를 검사하던 것을 `verse` 첫 줄로 옮겼다. 매칭 규칙이 한
함수 안에 다 있어야 호출부가 늘어나도 빠뜨리지 않는다.

## 검증

공식 예제 3건 전부 컴파일해서 돌렸다 — `2 / 2 / 3`, 기대값과 일치.

## 복잡도

- 시간: `O(B! × U × L)` 상한 — B(banned_id) ≤ 8, U(user_id) ≤ 8 이라 실제로는 아주 작다.
- 공간: `O(U + 조합 수)` — `used` 배열과 중복 제거용 `Set`.

## 요약

와일드카드 매칭(`verse`) 자체는 처음부터 맞았다. 빠져 있던 건 "배정 조합" 이라는 문제의 본질이었다.
이런 유형은 **답이 개수인지 조합인지**를 먼저 못 박고 시작하면 접근이 갈리지 않는다.
