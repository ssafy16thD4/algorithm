---
platform: programmers
problemId: "340213"
author: chanung
source: 안찬웅/pccp/동영상 재생기.java
week: null
compiles: true
verdict: wrong
tags: [logic-edge-case]
complexity:
  time: O(N)
  space: O(1)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [PCCP 기출문제] 1번 / 동영상 재생기 (programmers/340213) — chanung

## 접근

시간을 초로 바꿔 `curTime` 하나로 관리하고, 매 명령 처리 전에 오프닝 구간 체크를, `prev`/`next` 처리 후 각각 0 이하/10초 미만 여부를 확인해 클램프한다. 공식 예제 3건(`13:00`/`06:55`/`04:17`)은 모두 통과했다.

## 개선점

### 1. (치명) `next` 로 영상 길이를 넘어가는 경우를 처리하지 않음 — logic-edge-case

`next` 뒤 클램프 조건이 `if(curTime < 10) curTime = videlTime;` 인데, 이 조건은 "더할 수 있는 시간이 10초 미만이라 영상 끝으로 가야 하는" 상황이 아니라 "10초를 더한 결과가 10초 미만"인, 사실상 거의 발생하지 않는 조건이다. 정작 영상 길이를 넘겨버린 경우(`curTime > videoLen`)에 대한 클램프가 코드 어디에도 없다.

**실제로 돌려본 반례**: `video_len="00:20"(20초), pos="00:15"(15초), op_start="00:00", op_end="00:01", commands=["next"]`
- 기대 출력: `"00:20"` (남은 시간 5초 < 10초 → 영상 끝으로 이동)
- 실제 출력: `"00:25"` — 영상 길이(20초)를 넘는 값이 그대로 나온다.

```java
// 수정 방향: next 처리 후 videoLen을 넘겼는지 직접 클램프
} else if(commands[i].equals("next")) {
    curTime += 10;
    if (curTime > videlTime) curTime = videlTime;   // <- 이 클램프가 필요
}
```

## 복잡도

- 시간: `O(N)` — commands 길이만큼 한 번 순회.
- 공간: `O(1)`

## 요약

큰 구조는 맞고 공식 예제는 통과하지만, "next로 영상 끝을 넘는" 경계 케이스에 대한 클램프가 빠져 있어 실제로 반례가 난다. 오프닝 스킵·prev 클램프는 정상 동작한다.
