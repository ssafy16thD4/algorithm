/*
전략
- 조합 뽑기
- 각 불량 사용자 ID를 String으로 연산하지 않고 인덱스 int값으로 비교 연산
    - ID에 대한 String 비교 연산을 int 비교 연산으로 해결하여 시간, 메모리 아끼기 가능
- 각 불량 사용자 ID에 들어갈 수 있는 응모자 ID의 리스트를 초기화
- 이 리스트들로 조합 만들기
- 만들어진 조합을 정렬한 후 Set에 넣어서 중복을 제거해 개수 세기

시행착오
- Set<int[]>은 같은 요소가 똑같이 위치한 배열에 대한 중복 제거가 안됨(기본 배열은 equals가 없어서)
- Set<List<Integer>>를 쓰면 List는 equals가 있기 때문에 중복 제거가 가능함
*/

import java.util.*;

class Solution {
    static Set<List<Integer>> result;
    static int maxLen;
    static List<Integer>[] banList;
    
    public int solution(String[] user_id, String[] banned_id) {
        int answer = 0;
        result = new HashSet<>();
        maxLen = banned_id.length;
        
        // 각 제재 아이디가 가질 수 있는 유저 아이디의 인덱스 저장
        banList = new List[banned_id.length];
        for(int i = 0; i < banList.length; i++){
            banList[i] = new ArrayList<>();
        }
        
        for(int i = 0; i < banned_id.length; i++){
            for(int j = 0; j < user_id.length; j++){
                if(check(user_id[j], banned_id[i])) banList[i].add(j);
            }
        }
        
        dfs(0, new ArrayList<>());
        
        return result.size();
    }
    
    // 제재 아이디가 유저 아이디를 제재할 수 있는지 검사
    private boolean check(String user, String ban){
        if(user.length() != ban.length()) return false;
        
        for(int i = 0; i < user.length(); i++){
            if(ban.charAt(i) == '*') continue;
            if(user.charAt(i) != ban.charAt(i)) return false;
        }
        
        return true;
    }
    
    private void dfs(int depth, List<Integer> banned){
        // 종료 조건 : 제재 아이디 개수가 최대일때
        if(depth == maxLen){
            // banned에 중복 아이디가 있으면 저장 안함
            Set<Integer> checkSet = new HashSet<>();
            for(int b : banned) checkSet.add(b);
            if(checkSet.size() != maxLen) return;
                
            // 정렬해서 넣으면 Set을 통한 중복 제거 가능
            // Set<int[]>면 중복 제거 안됨
            // Set<List<Integer>> 사용하면 중복 제거 가능
            Collections.sort(banned);
            result.add(banned);
            
            return;
        }
        
        for(int idIdx : banList[depth]){
            // 백트래킹 없이 DFS만
            List<Integer> newBanned = new ArrayList<>(banned);
            newBanned.add(idIdx);
            dfs(depth + 1, newBanned);
        }
    }
}