/*
전략
- 슬라이딩 윈도우
    - 윈도우에 넣고 빼는걸 어떻게 구현할지
        - 해시맵의 compute를 써서 있으면 +1, 없으면 1로
        - 빼는건 computeIfPresent를 쓰고, 결과를 꺼냈을 때 0이면 remove로 제거
    - 윈도우 변화가 있을 때마다 현재 윈도우에 모든 보석이 있는지 확인
        - 해시맵을 써서 Key의 개수 세기
    - 윈도우 시작인덱스, 윈도우 종료인덱스를 투포인터로 관리하면서 윈도우 조절
        - 윈도우에 모든 보석이 없으면 종료 인덱스를 1 늘려서 보석 추가
        - 윈도우에 모든 보석이 있으면 결과 갱신 후 시작 인덱스에 1 늘려서 보석 제거
            - 결과는 기존 결과보다 짧을때만 갱신 가능

시행착오
- 처음에 윈도우에 보석 전부 있는지 확인을 소수를 활용해서 하려고 함. 그러나 보석 개수가 26^10개가 가능한거 보고 포기
- 윈도우의 크기를 1부터 N까지 순차적으로 순회하는 방식으로 구현했으나 시간 초과 발생

*/
import java.util.*;

class Solution {
    static Map<String, Integer> window;
    
    public int[] solution(String[] gems) {
        // 초기 결과 : 맨앞부터 맨뒤까지
        int[] answer = new int[2];
        answer[0] = 1;
        answer[1] = gems.length;
        
        // 보석 종류 개수 세기
        Set<String> jewels = new HashSet<>();
        for(String gem : gems){
            jewels.add(gem);
        }
        int type = jewels.size();
        
        // 초기 윈도우 세팅
        int start = 0;
        int end = 0;
        window = new HashMap<>();
        window.put(gems[0], 1);
        
        // 투포인터로 윈도우 크기 조절
        while(start < gems.length){
            // 윈도우 내 모든 보석 있으면
            if(window.keySet().size() == type){
                // 결과 갱신 가능하면 갱신
                if(answer[1] - answer[0] > end - start){
                    answer[0] = start + 1;
                    answer[1] = end + 1;
                }
                
                // 보석 하나 빼기
                window.compute(gems[start], (k, v) -> v - 1);
                if(window.get(gems[start]) <= 0) window.remove(gems[start]);
                start++;
            }
            // 윈도우 내 모든 보석 없으면
            else{
                // 만약 end가 끝에 도달했으면 더 계산해도 못찾음
                if(end + 1 == gems.length){
                    break;
                }
                // 보석 하나 넣기
                window.compute(gems[end + 1], (k, v) -> (v == null) ? 1 : v + 1);
                end++;
            }
        }
        
        return answer;
    }
}