---
platform: programmers
problemId: "42895"
author: seongil
source: 이성일/week3/N으로표현FailVersion.java
week: 3
compiles: true
verdict: good
tags: [dead-code, good-decomposition]
complexity:
  time: O(8^2 * |dp|^2)
  space: O(32001 + sum |list[i]|)
generatedBy: claude-code-local
generatedAt: 2026-08-19
---

# N으로 표현 (programmers/42895) — seongil (실패 버전)

## 접근

정답 버전과 같은 개수별 집합 DP인데, `nums[32001]` 배열로 **각 값의 최소 사용 개수를 따로 기억**하고
이미 더 적은 개수로 만든 값은 `list[len]` 에 다시 넣지 않는 가지치기를 얹었다.
`amplify` 가 `boolean` 을 돌려주며 `number` 를 찾는 즉시 위로 빠져나가는 조기 종료도 들어 있다.

## 개선점

### 1. 파일 이름과 달리, 오답을 찾지 못했다

`FailVersion` 이라는 이름이 붙어 있어 어디가 틀렸는지 찾으려 했는데,
**정답 DP와 18,000케이스(N=1~9 × number=1~2000)를 전수 대조한 결과 불일치 0건이다.**

가지치기 논리도 검토했지만 구멍이 안 보인다.
`list[j]` 에는 "최소 개수가 정확히 j 인 값"만 남고, `(left, right)` 는 `left + right = len` 인
모든 분할을 훑으므로 조합이 빠지지 않는다.

그러니 이 파일은 **오답이라서가 아니라 다른 이유로 버려진 것**으로 보인다. 짚이는 후보는 둘이다.

- **`Arrays.fill(nums, Integer.MAX_VALUE)` 를 매 호출마다 돈다.** 32001칸을 채우는 비용이
  호출당 고정으로 붙는다. 케이스가 많으면 이게 쌓인다.
- **`static` 배열 두 개(`list`, `nums`)에 상태가 남는다.** 지금은 `solution` 첫머리에서 둘 다
  초기화하고 있어 괜찮지만, 제출 당시 그 초기화가 없었다면 두 번째 케이스부터 무너진다.
  같은 문제 `이승주` 풀이가 정확히 그 사고를 냈다.

**어디서 틀렸는지 기억나면 알려달라.** 여기 반례로 남겨두는 게 다음 사람에게 제일 도움이 된다.
기억나지 않는다면 이 파일은 지워도 되고, 정답 버전과의 비교 자료로 남겨둬도 좋다 —
가지치기를 넣은 쪽과 안 넣은 쪽을 나란히 볼 수 있는 건 드문 자료다.

### 2. (사소) `if (nums[opResult] < left + right) continue;` 의 경계가 헷갈린다 — `dead-code`

`left + right` 는 항상 `len` 과 같다. 루프에서 `left++, right--` 를 같이 하기 때문이다.
그런데 `amplify` 는 `len` 도 따로 받고 있어서, **같은 값을 두 이름으로 쓰고 있다.**

```java
static boolean amplify(int left, int right, int len, int number) {
    ...
    if (nums[opResult] < left + right) continue;   // len 과 같은 값
    nums[opResult] = left + right;
    list[len].add(opResult);
```

`left + right` 를 `len` 으로 통일하면 "이게 같은 값인가?" 를 되짚을 일이 없어진다.
`<` 와 `<=` 중 어느 쪽이 맞는지 판단할 때도 훨씬 빨리 보인다.

## 복잡도

- 시간: `O(8 * 8 * |list|^2)` + 호출당 `O(32001)` (`Arrays.fill`)
- 공간: `O(32001 + sum |list[i]|)` — `nums` 배열이 고정으로 붙는다

## 요약

이름은 실패 버전이지만 18,000케이스 대조에서 오답이 안 나왔다.
`nums` 로 최소 개수를 따로 들고 가는 가지치기와 조기 종료까지 들어간, 정답 버전보다 오히려 공격적인 구현이다.
실제로 어디서 틀렸는지 기억나면 그걸 여기 남겨두는 게 좋겠다.
