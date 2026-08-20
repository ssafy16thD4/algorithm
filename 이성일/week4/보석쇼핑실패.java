import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

class Solution {
    
    static Set<String> gemType;
    static Map<String, Boolean> isCollect;
    static int gemSize;

    public int[] solution(String[] gems) {
        int[] answer = {0, Integer.MAX_VALUE};
        gemType = new HashSet<>();
        // 서로 다른 원소의 개수 구하기
        // 집합을 이용하기
        for (String gem: gems) {
            gemType.add(gem);
        }
        gemSize = gemType.size();
        isCollect = new HashMap<>();
        // 수집 맵 초기화 
        for(String type:gemType) {
            isCollect.put(type, false);
        }

        int start = 0, end = 0, cnt = 0;
                // 첫번째 원소부터 슬라이딩
                // 다음원소가 현재 윈도우에 들어갈 수 있는 지 확인하기
                    // 있다면 기존 요소에 추가하기
                    // 수집 개수를 채웠다면
                    // 최솟값 인덱스 갱신
                // 이미 슬라이드에 있다면
                    // 현재 원소 이전 원소부터 슬라이드 처음까지 현재원소와 같은 원소를 만나기 전까지 서로 다른 원소 카운트
                    // 원소갯수를 비교해서 더 적다면 기존 슬라이드와 병합, 같다면 잘라내기 
        for (int i = 0; i < gems.length; i++) {
            if (!isCollect.get(gems[i])) {
                end = i;
                isCollect.replace(gems[i], true);
                cnt++;
                if (cnt == gemSize) {
                    if (end - start < (answer[1] - answer[0])) {
                        answer[0] = start;
                        answer[1] = end;
                    } 
                }
            } else {
                Map<String, Boolean> check = new HashMap<>();
                for(String type:gemType) {
                    check.put(type, false);
                }
                int checkCnt = 1;
                int idx = i-1;
                while (!gems[idx].equals(gems[i])) {
                    if (!check.get(gems[idx])) {
                        checkCnt++;
                        check.replace(gems[idx], true);
                    }
                    if (checkCnt == cnt) {
                        start = idx;
                        
                        break;
                    } 
                    idx--;
                }
                if (cnt == 1 && checkCnt == 1) {
                    start = i;
                }
                end = i;
                if (checkCnt == gemSize) {
                    if (end - start < (answer[1] - answer[0])) {
                        answer[0] = start;
                        answer[1] = end;
                    }
                }
            }
        }
        answer[0] += 1;
        answer[1] += 1;
        return answer;
    }
}