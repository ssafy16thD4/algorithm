---
platform: programmers
problemId: "67258"
author: seongil
source: 이성일/week4/보석쇼핑시간초과.java
week: 4
compiles: true
verdict: needs-fix
tags: [time-complexity, redundant-loop]
complexity:
  time: O(N^2) worst case
  space: O(K) (K = 보석 종류 수)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 보석 쇼핑 (programmers/67258) — 이성일 (`.failed` — 시간초과 버전)

## 접근

투 포인터/슬라이딩 윈도우로 모든 보석 종류를 포함하는 최소 구간을 찾으려는 시도다. 처음 등장하는 보석은 `isCollect` 맵으로 체크하며 앞으로 진행하고, 이미 수집한 종류를 다시 만나면 뒤로 걸어가며 현재 지점부터 윈도우 시작까지 서로 다른 종류 수를 다시 세는 방식이다.

**정답 자체는 맞다.** 공식 예제(`gems=["DIA","RUBY","RUBY","DIA","DIA","EMERALD","SAPPHIRE","DIA"]`)를 직접 컴파일·실행해 기대값 `[3, 7]`과 일치하는 것을 확인했다. 파일명이 예고하는 대로 문제는 정답이 아니라 시간이다.

## 개선점

### 1. (중요) 매번 뒤로 훑는 구조라 최악의 경우 `O(N^2)` — `time-complexity`

```java
int idx = i-1;
while (!gems[idx].equals(gems[i])) {
    if (!check.get(gems[idx])) { checkCnt++; check.replace(gems[idx], true); }
    ...
    idx--;
}
```

이미 등장한 보석을 다시 만날 때마다 `check`라는 새 `HashMap`을 만들고, 같은 보석이 다시 나올 때까지 **뒤로 한 칸씩** 훑는다. 일반적인 투 포인터는 `start`를 앞으로만 전진시켜 총 이동량을 `O(N)`으로 묶는데, 이 코드는 매 위치에서 되돌아보는 폭이 줄어든다는 보장이 없어 사실상 매번 새로 스캔하는 것과 비슷해진다.

**직접 재현했다.** 서로 다른 보석 종류 5000개를 4번 반복시킨 길이 20000짜리 입력(문제 제약 `gems.length <= 100000` 안에서의 축소판)으로 실행 시간을 쟀다:

```
k=5000 n=20000 time(ms)=1866
```

`n=100000`까지 늘어나면(제약 범위 안) 이 패턴은 실질적으로 `O(N^2)`에 가깝게 느려져 제한 시간을 넘길 가능성이 크다 — 파일명(`시간초과`)이 가리키는 문제가 실측으로도 재현된다. 표준 해법은 `start`를 뒤로 되돌리지 않고, 각 보석 종류가 "가장 최근에 등장한 위치"만 `Map<String,Integer>`로 유지하면서 `start`를 앞으로만 밀어 `O(N)`에 끝낸다. **이 수정안 자체를 코드로 재작성해 대조 검증하지는 않았다** — 접근을 통째로 바꿔야 해서 이번 리뷰 범위를 벗어난다고 판단했다.

### 2. (사소) 매 역방향 스캔마다 새 `HashMap` 생성 — `redundant-loop`

```java
Map<String, Boolean> check = new HashMap<>();
for(String type:gemType) { check.put(type, false); }
```

이미 등장한 보석을 만날 때마다 종류 수만큼 새 맵을 초기화한다. 1번 문제와 같은 뿌리(역방향 재스캔) 때문에 생기는 추가 비용이라, 1번을 고치면 이 맵 자체가 필요 없어질 가능성이 높다.

## 복잡도

- 시간: `O(N^2)` 최악 — 실측으로 재현(`n=20000, k=5000`에서 약 1.9초)
- 공간: `O(K)` — 보석 종류 수만큼의 맵

## 요약

정답은 맞지만(공식 예제 통과) 역방향 재스캔 구조 때문에 최악의 경우 `O(N^2)`로 느려진다. 실측으로도 제약 상한 근처에서 시간초과 가능성을 확인했다 — "가장 최근 등장 위치"만 관리하는 표준 투 포인터로 바꾸면 해결된다.
