# references/ — 외부 정석 코드

우리 코드와 대조하려고 외부 공개 저장소에서 가져온 Java 풀이입니다. (CLAUDE.md T4)

## 구조

```
references/{platform}/{problemId}/{refId}.java        정석 코드 (원본 그대로)
references/{platform}/{problemId}/{provider}.notes.md 출처 저자가 쓴 풀이 설명
```

등록부는 `data/references.json` 한 곳입니다. 파일만 넣고 등록부에 안 적으면
`node scripts/check.mjs` 가 "고아 레퍼런스 파일" 로 잡아냅니다.

## 새 레퍼런스 추가하는 법

1. `data/references.json` 의 `entries` 에 한 줄 추가
2. `node scripts/sync-references.mjs` — 등록부를 보고 파일을 내려받습니다
3. `node scripts/build-site.mjs` — 사이트 번들 갱신
4. `node scripts/check.mjs && node scripts/smoke-site.cjs` — 검사

출처 저장소가 바뀌어 새 판본을 받고 싶으면 `providers[].pinnedCommit` 을 올린 뒤
`node scripts/sync-references.mjs --force` 를 돌립니다.

## 저작권 (T4-4)

- **코드 전문을 저장하는 대신 출처를 반드시 남깁니다.** `data/references.json` 의
  `repo` + `pinnedCommit` 으로 사이트가 커밋 고정 링크를 만들어 정석코드 옆에 띄웁니다.
- 파일에는 **주석 헤더를 넣지 않습니다.** 원본을 그대로 둬야 diff 가 알고리즘 차이만
  보여줍니다. 출처 표기는 이 README, `data/references.json`, 사이트 UI 가 담당합니다.
- **문제 지문은 저장하지 않습니다** (비목표 3항). 링크만 씁니다.
- `providers[].license` 가 `null` 이면 상대 저장소에 라이선스 표기가 없다는 뜻이고,
  사이트가 `라이선스 표기 없음 · 출처 링크 유지 필수` 라고 경고를 띄웁니다.
  **출처 링크를 지우거나 우리가 쓴 코드인 것처럼 옮기지 마세요.**
  공개 재배포가 필요해지면 원저자에게 먼저 허락을 받습니다.

## 현재 등록된 제공자

| id | 저장소 | 라이선스 | 비고 |
|---|---|---|---|
| `dwinging` | [DWinging/Algorithm](https://github.com/DWinging/Algorithm) | 없음 | 문제마다 `solve.md` 로 풀이 근거를 남기는 스타일 |

T4-2 는 제공자 2명을 목표로 합니다. 아직 1명입니다.

### 커버리지

우리 30문제 중 **5문제**에만 정석 코드가 있습니다 (`programmers` 12904 / 67259 / 81303 / 87694 / 92343).
DWinging 저장소는 BOJ·LeetCode 위주라 우리가 푼 SWEA 8문제와는 겹치는 게 없습니다.
나머지를 채우려면 제공자를 더 등록해야 합니다.
