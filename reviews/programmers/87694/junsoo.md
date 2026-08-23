---
platform: programmers
problemId: "87694"
author: junsoo
source: 김준수/week2/아이템 줍기.java
week: 2
compiles: true
verdict: unattempted
tags: []
complexity:
  time: —
  space: —
generatedBy: claude-code-local
generatedAt: 2026-08-23
---

# 아이템 줍기 (programmers/87694) — junsoo

## 접근

**이 파일에는 아이템 줍기 풀이가 없습니다.** 안에 들어 있는 건 자물쇠와 열쇠(programmers/60059) 코드입니다.

같은 폴더의 `김준수/week2/자물쇠와 열쇠.java` 와 대조해 보면 확인됩니다.

```
$ diff "김준수/week2/아이템 줍기.java" "김준수/week2/자물쇠와 열쇠.java"
2c2
< 전략
---
> 전략.
90c90
< }
---
> }
\ No newline at end of file
```

첫 줄 주석의 마침표 하나와 파일 끝 개행 말고는 **두 파일이 같습니다.** 시그니처도 둘 다 `public boolean solution(int[][] key, int[][] l)` 로, 아이템 줍기가 요구하는 `public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY)` 와 다릅니다. 파일을 만들면서 옆 파일 내용을 그대로 붙여넣은 것으로 보입니다.

내용이 다른 문제 코드라서 아이템 줍기 리뷰를 쓸 수 없습니다. 자물쇠와 열쇠 코드에 대한 리뷰는 `reviews/programmers/60059/junsoo.md` 쪽에서 다룹니다.

## 개선점

### 1. (치명) 파일 내용이 파일명과 다르다

봇은 파일명으로 문제를 식별하므로(`data/problems.json` 의 alias), 이 파일은 **사이트에서 아이템 줍기 열에 자물쇠와 열쇠 코드로 표시됩니다.** 비교 뷰가 어긋납니다.

**작성자 본인만 고칠 수 있습니다.** 아이템 줍기를 실제로 풀어서 이 파일을 덮어쓰거나, 안 풀었다면 파일을 지우면 됩니다. 봇은 팀원 풀이 파일을 수정하지 않습니다(CLAUDE.md T0-1).

## 복잡도

측정 대상 없음.

## 요약

아이템 줍기 풀이가 아니라 자물쇠와 열쇠 코드가 잘못 올라간 파일입니다. 실제로 푼 뒤 다시 올리면 그때 리뷰가 붙습니다.
