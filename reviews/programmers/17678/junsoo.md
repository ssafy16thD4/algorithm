---
platform: programmers
problemId: "17678"
author: junsoo
source: 김준수/week3/셔틀버스.java
week: 3
compiles: true
verdict: good
tags: [good-readability]
complexity:
  time: O(N · M_total)
  space: O(M_total)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# [1차] 셔틀버스 (programmers/17678) — junsoo

## 접근

우선순위 큐로 크루 도착 시각을 오름차순으로 뽑으면서, 버스마다 시간·좌석 조건을 만족하는 사람만
태우고 나머지는 `left` 큐에 모았다가 다음 버스를 위해 다시 pq에 돌려놓는 시뮬레이션이다.
마지막 버스에 자리가 남으면 버스 도착 시각에, 자리가 꽉 찼으면 마지막 탑승자보다 1분 일찍 줄을
서면 된다는 이 문제의 핵심 트릭을 정확히 구현했다. `people.get(num)`이 pq에서 뽑힌 순서(오름차순)
그대로 채워지기 때문에 마지막 원소가 항상 그 버스의 최종 탑승자라는 점도 맞게 활용했다.

## 개선점

특별히 지적할 부분을 찾지 못했다. `while(!pq.isEmpty())`가 매 버스마다 남은 pq 전체를 다시 훑어서
시간 조건에 못 미치는 사람까지도 계속 확인하지만(오름차순이라 첫 번째로 시간 초과인 사람을 만나면
이후는 다 확인할 필요가 없는데도 break 없이 끝까지 돈다), `n <= 10`, `timetable 길이 <= 2000`이라는
이 문제의 제약(직접 페이지에서 확인) 안에서는 전혀 체감되지 않는 수준이라 굳이 고칠 이유가 없다.

## 복잡도

- 시간: `O(N · M_total)` — 버스마다 남은 크루 전체를 훑는다. `N<=10`, `M_total<=2000`이라 최악에도
  `2만` 수준.
- 공간: `O(M_total)` — `pq`, `people`, `left`.

## 요약

문제의 핵심 트릭(마지막 버스 자리 유무에 따른 분기)을 정확히 구현했고, 시뮬레이션 로직도
군더더기 없이 깔끔하다. 지적할 결함을 찾지 못했다.
</content>
