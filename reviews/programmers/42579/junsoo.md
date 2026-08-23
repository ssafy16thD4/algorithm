---
platform: programmers
problemId: "42579"
author: junsoo
source: 김준수/week4/베스트앨범.java
week: 4
compiles: true
verdict: needs-fix
tags: [dead-code, collection-choice, nonstatic-inner-class]
complexity:
  time: O(N·G + N log N)
  space: O(N)
generatedBy: claude-code-local
generatedAt: 2026-08-24
---

# 베스트앨범 (programmers/42579) — junsoo

## 접근

세 단계 정렬 조건(장르 총재생수 → 장르 내 재생수 → 고유번호)을 자료구조로 나눠 담았습니다. 장르별 노래는 `PriorityQueue<Song>` 에 넣고 `Song.compareTo` 에 "재생수 내림차순, 같으면 인덱스 오름차순" 을 담았습니다. 장르 순서는 `Map<String,Integer>` 의 총합을 정렬해 만듭니다.

`compareTo` 를 두 줄로 끝낸 게 깔끔합니다.

```java
int comp = Integer.compare(s.play, this.play);   // 재생수 많은 순
if(comp != 0) return comp;
return Integer.compare(this.idx, s.idx);         // 같으면 번호 낮은 순
```

`Integer.compare` 를 쓴 것도 맞습니다. `s.play - this.play` 로 썼다면 재생수가 `int` 범위 끝까지 갈 때 뒤집힙니다.

**검증**: 문제 명세를 그대로 구현한 기준과 무작위 4,000건(장르 3종, 곡 1~8개, 재생수 0~5로 동점이 자주 생기게)을 대조했습니다. 결과는 아래 2번에서 나눠 적습니다.

## 개선점

### 1. (중요) 디버그 출력이 남아 있다 — dead-code

```java
for(Map.Entry e : rankList){
    int idx = genreList.indexOf(e.getKey());
    System.out.println(genreList.get(idx));      // <- 이 줄
```

장르마다 이름을 찍습니다. 채점에서 오답 처리되지는 않지만 장르 수만큼 출력이 나가고, 실측에서 무작위 대조를 돌릴 때 이 출력이 결과와 뒤섞여 나왔습니다. 제출 전에 지워야 합니다.

### 2. (중요) 장르 총합이 같을 때 순서가 `HashMap` 순회 순서에 좌우된다 — collection-choice

```java
Map<String, Integer> genreMap = new HashMap<>();
...
List<Map.Entry<String, Integer>> rankList = new ArrayList<>(genreMap.entrySet());
rankList.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
```

`sort` 는 안정 정렬이라 **동점인 장르들은 `entrySet()` 이 내놓은 순서를 그대로 유지합니다.** 그런데 `HashMap` 의 순회 순서는 키의 해시에 좌우되고 삽입 순서와 무관합니다. 즉 장르 총합이 같을 때 어느 장르가 먼저 나올지가 사실상 임의로 정해집니다.

실측입니다. 무작위 4,000건 중:

| 구분 | 건수 | 기준 구현과 불일치 |
|---|---|---|
| 장르 총합에 동점이 **없는** 입력 | 3,459 | **0건** |
| 장르 총합에 동점이 **있는** 입력 | 541 | 266건 |

**동점이 없는 입력에서는 한 건도 틀리지 않았습니다.** 불일치 266건은 전부 동점 입력이었습니다.

예를 들면 이런 입력입니다.

```
genres = ["b","c","a","a","a","c","c"]
plays  = [ 2,  4,  2,  1,  5,  1,  3 ]
장르 총합: b=2, c=8, a=8      <- c 와 a 가 동점

기준 구현(먼저 등장한 장르 우선): [1, 6, 4, 2, 0]
이 코드                        : [4, 2, 1, 6, 0]
```

**여기서 어느 쪽이 정답인지는 문제가 정하지 않았습니다.** 프로그래머스 문제 지문에 장르 총합이 같을 때의 규칙이 없고, 실제 테스트케이스에 그런 입력이 들어 있는지도 확인하지 못했습니다. 그래서 이걸 "오답" 이라고는 적지 않습니다.

문제는 정답 여부가 아니라 **순서가 재현되지 않는다**는 점입니다. `HashMap` 순회 순서는 JDK 버전이나 키 집합이 바뀌면 달라질 수 있어서, 같은 코드가 환경에 따라 다른 배열을 낼 수 있습니다. `LinkedHashMap` 으로 바꾸면 최소한 "먼저 등장한 장르 우선" 으로 고정됩니다.

```java
Map<String, Integer> genreMap = new LinkedHashMap<>();
```

한 글자 차이이고, 동점이 없는 입력에서는 결과가 완전히 같습니다.

### 3. (사소) `genreList.contains` / `indexOf` 를 반복문 안에서 부른다

```java
for(String g : genres){
    if(!genreList.contains(g)) genreList.add(g);      // O(G)
}
...
for(int i = 0; i < genres.length; i++){
    int idx = genreList.indexOf(genres[i]);           // O(G)
```

`G`(장르 종류 수)가 작아서 실제로는 문제가 안 됩니다. 다만 `Map<String,Integer> genreIndex` 하나를 같이 들면 둘 다 `O(1)` 이 되고, 위 2번의 `LinkedHashMap` 과도 자연스럽게 합쳐집니다.

### 4. (사소) `Song` 이 non-static 내부 클래스다 — nonstatic-inner-class

`class Song` 은 바깥 `Solution` 인스턴스에 대한 숨은 참조를 들고 다닙니다. `Song` 은 `idx` 와 `play` 만 쓰므로 `static` 을 붙이면 그 참조가 사라집니다. 곡이 최대 10,000개라 체감 차이는 없지만, 습관으로 굳혀두면 좋습니다.

### 5. (사소) `PriorityQueue<Song>[]` 는 raw type 이다

`new PriorityQueue[genreList.size()]` 는 제네릭 배열을 못 만들어 raw 배열을 담은 것입니다. `List<PriorityQueue<Song>>` 이나 `Map<String, PriorityQueue<Song>>` 을 쓰면 경고가 사라지고, 3번의 인덱스 조회도 같이 없어집니다.

## 복잡도

`N` = 곡 수, `G` = 장르 종류 수.

- 시간: `O(N·G + N log N)` — 장르 인덱스 조회가 `O(N·G)`, 우선순위 큐 삽입이 `O(N log N)`. `G` 는 작아서 사실상 `O(N log N)` 입니다.
- 공간: `O(N)` — 모든 곡이 우선순위 큐에 한 번씩 들어갑니다.

## 요약

세 단계 정렬을 `compareTo` 와 우선순위 큐로 나눠 담은 구조가 정확하고, **장르 총합이 겹치지 않는 입력 3,459건에서 오답이 0건**이었습니다. 제출 전에 반드시 고칠 건 디버그 `System.out.println` 하나이고, `HashMap` → `LinkedHashMap` 은 동점일 때 순서가 환경에 따라 흔들리는 걸 막아줍니다. 나머지는 정리 수준입니다.
