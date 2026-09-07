---
platform: programmers
problemId: "340213"
author: chanung
source: 안찬웅/pccp/동영상 재생기.java
week: null
compiles: true
verdict: good
tags: [logic-edge-case, off-by-one]
complexity:
  time: O(N)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# [PCCP 기출문제] 1번 / 동영상 재생기 (programmers/340213) — chanung

## 접근

시간을 전부 초로 바꿔 `curTime` 하나로 관리하고, 명령마다 오프닝 구간 체크 → prev/next 처리 →
클램프 순서로 돈다. 명령 루프가 끝난 뒤 한 번 더 오프닝 체크를 하는 것도 맞다(마지막 명령이
오프닝 안으로 데려다 놓는 경우).

## 개선점

### 1. (수정 완료 / 치명) `next` 로 영상 길이를 넘어가는 경우를 막지 않았다 — `logic-edge-case`

클램프 조건이 `if(curTime < 10) curTime = videlTime;` 이었다. 이건 "10초를 더한 결과가 10초 미만"
이라는, 사실상 성립하지 않는 조건이다. 정작 필요한 `curTime > videoLen` 검사가 없었다.

**실제로 돌려본 반례**: `video_len="00:20", pos="00:15", op="00:00"~"00:01", commands=["next"]`
- 기대 `"00:20"` / 수정 전 실제 `"00:25"` — 영상 길이를 넘는 값이 그대로 나왔다.

### 2. (수정 완료 / 치명) `prev` 클램프 기준도 틀렸다 — `off-by-one`

`curTime -= 10` 뒤 `if(curTime < 10) curTime = 0;` 이었다. "남은 시간이 10초 미만"은 **빼기 전**
기준인데 **뺀 뒤** 값을 10과 비교하고 있었다. 그래서 15초에서 prev 를 누르면 5초가 되어야 하는데 0초가 됐다.

**실제로 돌려본 반례**: `pos="00:15", commands=["prev"]` → 기대 `"00:05"` / 수정 전 `"00:00"`.

두 클램프를 "연산 후 범위를 벗어났는지"로 통일했다.

```java
if(commands[i].equals("prev")) {
    curTime -= 10;
    if(curTime < 0) curTime = 0;
} else if(commands[i].equals("next")) {
    curTime += 10;
    if(curTime > videlTime) curTime = videlTime;
}
```

## 검증

공식 예제 3건 + 위 반례 2건을 컴파일해서 돌렸고 전부 기대값과 일치한다.

```
34:33 / 13:00 / next,prev      -> 13:00
10:55 / 00:05 / prev,next,next -> 06:55
07:22 / 04:05 / next           -> 04:17
00:20 / 00:15 / next           -> 00:20
00:20 / 00:15 / prev           -> 00:05
```

## 복잡도

- 시간: `O(N)` — commands 길이만큼 한 번.
- 공간: `O(1)`

## 요약

초 단위 정수 하나로 상태를 관리한 판단이 좋았고 오프닝 스킵도 처음부터 맞았다. 문제는 클램프
두 개가 모두 "연산 전 남은 시간"과 "연산 후 값"을 섞어 쓴 것이었다. 경계 조건은 **어느 시점 기준인지**를
문장으로 먼저 못 박고 코드로 옮기면 이런 실수가 줄어든다.
