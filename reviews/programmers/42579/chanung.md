---
platform: programmers
problemId: "42579"
author: chanung
source: 안찬웅/week4/베스트앨범.java
week: 4
compiles: true
verdict: good
tags: [logic-edge-case, magic-branch, nonstatic-inner-class]
complexity:
  time: O(N log N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-09-08
---

# 베스트앨범 (programmers/42579) — chanung

## 접근

정렬 기준 3개(장르 총 재생수 ↓ / 곡 재생수 ↓ / 고유번호 ↑)를 `Node.compareTo` 하나에 몰아넣어서
문제 조건이 코드에 그대로 드러난다. 그 뒤 우선순위 큐에서 꺼내며 **장르별로 2곡까지** 담는다.

## 개선점

### 1. (수정 완료 / 치명) 장르 총합이 동점이면 3곡 이상 담겼다 — `logic-edge-case`

이전 코드는 **직전 장르 이름 하나**(`str`)와 카운터 하나(`cnt`)로 셌다. 장르 총합이 같으면
우선순위 큐에서 두 장르가 번갈아 나오는데, 그때마다 `str` 이 바뀌면서 카운터가 1로 리셋된다.

**실제로 돌려본 반례**: `genres={"a","a","a","b","b"}, plays={5,4,1,6,4}`
(a 총합 10, b 총합 10 — 동점)
- 기대: 장르마다 2곡씩 총 4곡
- 수정 전 실제: `[3, 0, 1, 4, 2]` — 5곡, a 가 3곡 들어갔다

장르별 카운터로 바꾸면 정렬 순서와 무관하게 규칙이 지켜진다.

```java
Map<String, Integer> picked = new HashMap<>();
while(!pq.isEmpty()) {
    Node n = pq.poll();
    int c = picked.getOrDefault(n.genres, 0);
    if(c >= 2) continue;
    picked.put(n.genres, c + 1);
    list.add(n.idx);
}
```

### 2. (수정 완료 / 사소) `cnt <= 1` / `cnt >= 2` / else 3분기가 사라졌다 — `magic-branch`

"처음일 때"를 따로 처리하던 분기까지 셋이 있었는데, 장르별 카운터 하나로 전부 없어졌다.
특수 케이스 분기가 많다는 건 대개 **상태를 잘못된 단위로 들고 있다**는 신호다.

### 3. (수정 완료 / 사소) `sing` 채우는 if/else 를 `merge` 한 줄로 — `redundant-collection`

```java
sing.merge(genres[i], plays[i], Integer::sum);
```

같은 이유로 `containsKey` 후 `get` 하던 두 번째 루프도 `sing.get(...)` 한 줄이 됐다
(모든 장르가 이미 맵에 있으므로 기본값 분기가 필요 없다).

### 4. (수정 완료 / 사소) `Node` 를 `static` 중첩 클래스로 — `nonstatic-inner-class`

비정적 내부 클래스는 바깥 인스턴스 참조를 하나씩 더 들고 다닌다. 노래 수만큼 만들어지는
객체라 습관적으로 `static` 을 붙이는 게 낫다.

## 검증

```
genres={classic,pop,classic,classic,pop}, plays={500,600,150,800,2500}
  -> [4, 1, 3, 0]      (공식 예제, 기대 일치)
genres={a,a,a,b,b}, plays={5,4,1,6,4}
  -> [3, 0, 1, 4]      (장르마다 정확히 2곡)
```

## 복잡도

- 시간: `O(N log N)` — 우선순위 큐 삽입·추출이 지배적.
- 공간: `O(N)` — 맵 두 개와 큐.

## 요약

정렬 기준을 `compareTo` 하나에 담은 판단이 좋고, 그 덕에 고칠 곳이 "몇 곡 담았는지 세는 방법"
한 군데로 좁혀졌다. **"직전 것과 같은가"로 그룹을 세면 정렬이 흔들리는 순간 깨진다** — 그룹별
카운터를 쓰면 입력 순서에 의존하지 않는다. 동점이 있을 수 있는 문제에서 반복해서 쓸 교훈이다.
