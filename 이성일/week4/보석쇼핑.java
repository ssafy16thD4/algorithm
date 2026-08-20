import java.util.*;

class Solution {

    static Map<String, Integer> gemCountMap;
    static int gemTypesSize;

    static void extendRight(String[] gems, Map<String, Integer> gemCountMap, int[] answer) {
        int rightIdx = 0, leftIdx = 0;

        for (int i = 0; i < gems.length; i++) {
            rightIdx = i;
            gemCountMap.put(gems[i], gemCountMap.getOrDefault(gems[i], 0) + 1);

            if (gemCountMap.size() == gemTypesSize) {
                leftIdx = shrinkLeft(gems, gemCountMap, rightIdx, leftIdx, gemTypesSize, answer);
            }

        }
    }

    static int shrinkLeft(String[] gems, Map<String, Integer> gemCountMap, int rightIdx, int leftIdx, int gemTypesSize,
            int[] answer) {
        while (gemCountMap.size() == gemTypesSize) {
            gemCountMap.put(gems[leftIdx], gemCountMap.get(gems[leftIdx]) - 1);
            if (gemCountMap.get(gems[leftIdx]) == 0) {
                gemCountMap.remove(gems[leftIdx]);
                if (rightIdx - leftIdx < (answer[1] - answer[0])) {
                    answer[0] = leftIdx;
                    answer[1] = rightIdx;
                }
            }
            leftIdx++;
        }
        return leftIdx;
    }

    public int[] solution(String[] gems) {
        int[] answer = new int[] { 0, gems.length };
        gemCountMap = new HashMap<>();
        gemTypesSize = new HashSet<>(Arrays.asList(gems)).size();
        extendRight(gems, gemCountMap, answer);
        // - $O(N^2)$ 을 만드는 게 아니라 $O(2*N)$으로 루프를 돌며 포인터를 수정한다
        // - 잼스톤 종류를 관리한다
        // - 구간에 따라 gem의 개수를 카운트한다.
        // - Map<String, Integer> gemCountMap
        // - 잼스톤 마지막 인덱스를 확장한다.
        // - 끝 인덱스 탐색 함수 (gems, gemCountMap, answer) → void
        // - 확장 시, 카운트를 센다
        // - int cnt;
        // - 카운트 임계 시 값을 저장한다, 시작 인덱스 조정 가능 여부를 확인한다.
        // - 시작인덱스 조절 함수 (gems, gemCountMap, idx) → idx
        // - feasible한 구간을 만들고 → 축소해서 최소화하고 → 깨지면 다시 확장한다
        answer[0] += 1;
        answer[1] += 1;
        return answer;
    }
}
