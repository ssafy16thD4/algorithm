/*
실패 코드 전략
- 슬라이딩 윈도우
    - 윈도우에 넣고 빼는걸 어떻게 구현할지
        - 해시맵의 compute를 써서 있으면 +1, 없으면 1로
        - 빼는건 computeIfPresent를 쓰고, 결과를 꺼냈을 때 0이면 remove로 제거
    - 윈도우 변화가 있을 때마다 현재 윈도우에 모든 보석이 있는지 확인
        - 해시맵을 써서 Key의 개수 세기
    - 윈도우 길이를 1부터 시작해서 100,000까지 늘어나게 하고, 모든 보석이 있는 경우 즉시 종료시키기
*/
import java.util.*;

class Solution {
    static Map<String, Integer> window;
    
    public int[] solution(String[] gems) {
        
        int[] answer = new int[2];
        
        // 보석 종류 개수 세기
        Set<String> jewels = new HashSet<>();
        for(String gem : gems){
            jewels.add(gem);
        }
        int type = jewels.size();
        
        int len = 0; // 윈도우 길이
        while(len++ < gems.length){
            // 초기 윈도우 세팅
            window = new HashMap<>();
            for(int i = 0; i < len; i++){
                window.compute(gems[i], (k, v) -> (v == null) ? 1 : v + 1);
            }
            
            // 초기 기준 종료 조건 검사
            if(window.keySet().size() == type){
                answer[0] = 0;
                answer[1] = len - 1;
                break;
            }
            
            // 윈도우를 맨 끝까지 반복하며 뒤로 한칸씩 밀기
            // i : 윈도우의 맨 뒤 인덱스
            for(int i = len; i < gems.length; i++){
                // System.out.println("start " + len + " " + (i - 1));
                // System.out.println(window.toString());
                
                // 종료 조건 검사
                if(window.keySet().size() == type){
                    answer[0] = i - len;
                    answer[1] = i - 1;
                    len = gems.length;
                    break;
                }
                
                // 맨 앞에 있는 요소 윈도우에서 제거
                // 1개 제거했을 때 0되면 맵에서 제거
                window.computeIfPresent(gems[i - len], (k, v) -> v - 1);
                if(window.getOrDefault(gems[i - len], 1) <= 0) window.remove(gems[i - len]);
                
                // 맨 뒤에 새 요소 윈도우에 추가
                // 이미 있으면 1 추가, 없으면 1
                window.compute(gems[i], (k, v) -> (v == null) ? 1 : v + 1);
                
                // System.out.println("end " + len + " " + (i - 1));
                // System.out.println(window.toString());
                // System.out.println();
            }
            
            
            
        }
        
        // gems의 인덱스를 0부터 시작한 걸 보정
        answer[0]++;
        answer[1]++;
        
        return answer;
    }
}