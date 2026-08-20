---
platform: swea
problemId: "5658"
author: junsoo
source: 김준수/week1/SWEA5658.java
week: 1
compiles: true
verdict: needs-fix
tags: [collection-choice]
complexity:
  time: O(N^2)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 보물상자 비밀번호 (swea/5658) — junsoo

## 접근

4변을 `String[4]`로 들고, 매 회전마다 "이전 변의 마지막 글자를 내 앞에 붙이고 뒤를 잘라낸다"로
회전을 시뮬레이션한다. 회전과 트림을 한 루프에서 같이 하면 안 되고 두 단계로 나눠야 한다는 걸
주석에 미리 적어둔 이유(마지막 문자를 다음 변에 줄 수 없어서 꼬임)를 직접 손으로 따라가봤다.

한 가지 눈여겨볼 점은 **바깥 루프가 `N/4`번만 돌고 "회전 전 원본" 상태를 따로 추가하지 않는데도
전체 N가지 회전 상태가 정확히 다 커버된다**는 것이다. 앞 글자만 계속 덧붙이기 때문에 `.charAt(length-1)`
(마지막 글자)은 트림 전이든 후든 항상 같은 값을 가리키고, `i == N/4 - 1`(마지막 반복)에서 각 변이
얻는 값은 정확히 그 다음 변의 "회전 전" 원본과 같다 — 그래서 원본 4개 변도 마지막 반복에서 자연스럽게
전부 등장한다. `N=8`(변 길이 2) 예시로 직접 손으로 두 바퀴를 추적해서 이 부분을 확인했고, 4개 원본
`AB/CD/EF/GH`가 모두 `nums`에 들어가는 걸 확인했다. 놓친 회전 상태 없이 정확한 로직이다.

## 개선점

### 1. (사소) 중복 검사에 `List.contains`를 써서 매 추가마다 선형 탐색한다 — `collection-choice`

```java
List<Long> nums = new ArrayList<>();
...
if(!nums.contains(lockNum)) {
    nums.add(lockNum);
}
```

`nums`에 값이 쌓일수록 `contains` 한 번이 `O(현재 크기)`가 되고, 총 `N`번 추가하므로 전체 중복 검사가
`O(N^2)`이 된다. `data/problems.json`에 URL이 없어 `N`의 정확한 상한은 확인 못 했지만, `HashSet`이나
`TreeSet`을 쓰면 애초에 이 문제가 생기지 않는다. 특히 내림차순 정렬까지 필요하므로 비교자를 반대로
준 `TreeSet`을 쓰면 중복 제거와 정렬을 한 번에 끝낼 수 있다.

```java
TreeSet<Long> nums = new TreeSet<>(Collections.reverseOrder());
...
nums.add(lockNum);   // 중복이면 자동으로 무시됨
...
Iterator<Long> it = nums.iterator();
for (int k = 1; k < K; k++) it.next();
sb.append("#").append(t).append(" ").append(it.next()).append("\n");
```

`Collections.sort` 호출도 같이 없앨 수 있다. (K번째 값을 꺼내는 부분은 `TreeSet`이 `List`가 아니라서
`get(K-1)`이 안 되므로 위처럼 순회하거나, `new ArrayList<>(nums).get(K-1)`로 바꿔도 된다.)

## 복잡도

- 시간: `O(N^2)` — 회전 시뮬레이션 자체는 `O(N)`이지만 `List.contains` 선형 탐색 때문에 전체가
  `O(N^2)`이 된다. `HashSet`/`TreeSet`으로 바꾸면 `O(N log N)`(정렬 비용)까지 줄어든다.
- 공간: `O(N)` — `nums`, `locks` 배열들.

## 요약

회전 시뮬레이션 로직은 자잘한 인덱스 계산까지 정확했고, "원본 상태를 따로 안 넣어도 되는 이유"까지
손으로 확인해봐도 빠지는 회전이 없었다. `List.contains` 기반 중복 제거만 `Set` 계열로 바꾸면 더 이상
지적할 게 없는 코드다.
</content>
