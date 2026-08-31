import java.util.*;
/*
    진열된 모든 종류의 보석을 적어도 1개이상 포함하는 가장 짧은 구간을 찾아서 구매
    : 가장 짧은 구간의 [시작 진열대 번호, 끝 진열대 번호] 담기
    0
    알고리즘: 슬라이딩윈도우
    시간복잡도: NlogN
    
    1. gems 배열을 돌면서 보석의 종류수를 구합니다.
    2. gems 돌기 left, right=0 시작
     2.1 해시에 gems[i]가 없으면 종류수++
     2.2 종류수가 보석 전체종류수와 같으면 break;
     2.3 종류수가 보석 전체종류수와 다르면 left++;
    3. 시작, 끝 인덱스 출력
*/
class Solution {
    static int[] res;
    static HashMap<String, Integer> mapCnt;
    static HashMap<String, Integer> map;
    public int[] solution(String[] gems) {
        res = new int[2];
        mapCnt = new HashMap<>();
        map = new HashMap<>();
        for(int i=0; i<gems.length; i++) {
            map.put(gems[i], map.getOrDefault(gems[i], 0) + 1);
        }
        
        int diaType = map.size(); // 전체 보석 종류수
        int curType = 0; // 현재 보석 종류수
        int left = 0; 
        int minLen = Integer.MAX_VALUE;
        for(int right=0; right<gems.length; right++) {
            if(!mapCnt.containsKey(gems[right])) {
                curType++; // 현재 보석 종류 개수 추가
                mapCnt.put(gems[right], 1); // 보석 추가
            } else {
                mapCnt.put(gems[right], mapCnt.get(gems[right]) + 1);
            }

            while(curType == diaType) {
                int len = right - left;
                mapCnt.put(gems[left], mapCnt.get(gems[left]) - 1); //left에 있던거 개수빼기
                if(mapCnt.get(gems[left]) == 0) {
                    mapCnt.remove(gems[left]);
                    curType--;
                }
                if(len < minLen) {
                    minLen = len;
                    res[0] = left+1;
                    res[1] = right+1;
                }
                left++;
            }
        }
        return res;
    }
}
