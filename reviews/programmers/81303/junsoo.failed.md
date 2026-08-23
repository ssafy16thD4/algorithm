---
platform: programmers
problemId: "81303"
author: junsoo
source: 김준수/week3/표 편집 실패.java
week: 3
compiles: true
verdict: needs-fix
tags: [time-complexity, collection-choice, redundant-collection]
complexity:
  time: O(n*m) — 명령당 O(n), 마지막 집계가 O(n^2)
  space: O(n)
generatedBy: claude-code-local
generatedAt: 2026-08-20
---

# 표 편집 (programmers/81303) — junsoo (실패 버전)

## 접근

`Chart` 클래스에 `names`(살아있는 행 목록)와 `stack`(삭제 이력)을 담고,
"현재 위치"를 **리스트 인덱스**로 관리한다.

**논리는 맞다.** 정답 시뮬레이터와 무작위 600케이스를 대조해 불일치 0건이다.
특히 되돌리기에서 인덱스를 보정하는 부분이 정확하다.

```java
public void recover(){
    int[] item = stack.pop();
    names.add(item[0], item[1]);
    if(curr >= item[0]) curr++;      // 복구 행이 내 위에 끼어들면 내 인덱스가 하나 밀린다
}
```

이 보정은 인덱스 기반으로 풀 때 제일 틀리기 쉬운 자리인데 맞게 짚었다.
삭제 때 `if(curr == names.size()) curr--;` 로 마지막 행 처리한 것도 맞다.

그래서 이 파일이 실패한 이유는 **정확성이 아니라 전부 속도**다.

## 개선점

### 1. (치명) `ArrayList` 의 중간 삽입·삭제가 O(n) 이다 — `collection-choice`

```java
names.remove(curr);            // curr 뒤의 원소를 전부 한 칸씩 당긴다
names.add(item[0], item[1]);   // item[0] 뒤의 원소를 전부 한 칸씩 민다
```

`ArrayList` 는 내부가 배열이라 중간을 건드리면 뒤쪽을 통째로 옮긴다.
`n` 이 최대 100만, 명령이 최대 20만 개이므로 최악 `2 * 10^11` 번 이동이다.

실제로 재봤다.

```
n=50,000 / 명령 20,000개
  정답(배열 이중 연결 리스트)     5ms
  이 파일                      511ms

n=1,000,000 / 명령 200,000개
  정답                          18ms
  김준수 정답 버전               26ms
  이 파일                       15초 초과 (중단)
```

`n` 을 20배 늘렸더니 **측정조차 안 될 만큼** 벌어진다. 주석에 적어둔
"java.util의 클래스를 사용하니 시간초과 발생함" 이 정확히 이 현상이다.

고치는 방향은 정답 버전이 이미 보여준다 — **인덱스 대신 포인터로 관리한다.**
`names.get(curr)` 처럼 "몇 번째"를 물어보는 순간 O(n)이 되므로,
"내 위는 누구, 내 아래는 누구"만 배열에 들고 있으면 모든 연산이 O(1)이 된다.

```java
int[] up = new int[n], down = new int[n];
for (int i = 0; i < n; i++) { up[i] = i - 1; down[i] = i + 1; }
// 삭제: 이웃끼리 연결. 삭제된 노드의 up/down 은 그대로 둔다 → 복구 때 그 정보를 그대로 쓴다
```

이러면 `recover()` 의 인덱스 보정 로직 자체가 필요 없어진다.
**틀리기 쉬운 코드를 없애는 게 이 교체의 진짜 이득이다.**

### 2. (치명) 마지막 집계가 O(n^2) 이다 — `redundant-collection`

```java
for(int i = 0; i < n; i++){
    if(chart.names.contains(i)){        // List.contains 는 O(n)
        sb.append("O");
    }
```

`List.contains` 는 처음부터 훑으므로 O(n)이고, 그걸 `n` 번 돈다.
`n=100만` 이면 이 줄만으로 `10^12` 번이다. **1번을 고쳐도 여기서 다시 막힌다.**

삭제된 행은 이미 `stack` 에 다 들어 있으므로 그걸 쓰면 한 번만 훑으면 된다.

```java
char[] out = new char[n];
Arrays.fill(out, 'O');
for (int[] item : chart.stack) out[item[1]] = 'X';   // 스택에 남은 = 아직 안 되돌린 삭제
return new String(out);
```

정답 버전이 정확히 이 방식이다.

### 3. (사소) `U x` / `D x` 를 인덱스 산술로 처리한다

```java
case "U": chart.curr -= uNum; break;
case "D": chart.curr += dNum; break;
```

인덱스 기반이라 이게 성립한다 — 살아있는 행만 리스트에 있으니 `x` 칸 이동이 곧 인덱스 `±x` 다.
**이 부분만은 인덱스 방식이 포인터 방식보다 빠르다** (포인터는 `x` 번 따라가야 한다).

즉 이 풀이는 "이동은 O(1)인데 삭제·복구가 O(n)", 정답 버전은 "이동은 O(x)인데 삭제·복구가 O(1)"이다.
문제 제약에서는 삭제·복구 쪽이 훨씬 비싸서 후자가 이긴다.
**어느 연산이 비싼지를 제약과 함께 따져보는 게 이 문제의 핵심 판단이었다.**

### 4. (사소) `Chart` 가 static 이 아닌 내부 클래스다 — `nonstatic-inner-class`

`Chart` 는 바깥 인스턴스 상태를 안 쓴다. `static class Chart` 로 두면 숨은 바깥 참조가 사라진다.

## 복잡도

- 시간: `O(n*m)` — 명령당 `ArrayList` 이동 `O(n)`. 여기에 마지막 집계 `O(n^2)` 가 더해진다
- 공간: `O(n)` — 이건 정답 버전과 같다

## 요약

논리는 600케이스 대조에서 오답 0건으로 맞다. 되돌리기 인덱스 보정처럼 틀리기 쉬운 부분도 정확하다.
문제는 자료구조 선택 하나다 — `ArrayList` 중간 삽입·삭제와 `contains` 가 겹쳐
n=100만에서 15초를 넘긴다(정답 버전 26ms). 인덱스가 아니라 포인터로 관리하면
속도만 좋아지는 게 아니라 인덱스 보정 로직이 통째로 사라진다.
