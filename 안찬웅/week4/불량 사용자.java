import java.util.*;
/*
    불량 사용자의 글자
    
    user_id: 이벤트 응모자 아이디 목록
    banned_id: 불량 사용자 아이디 목록 
*/
class Solution {
    public int solution(String[] user_id, String[] banned_id) {
        int maxCnt = 0;
        for(int i=0; i<banned_id.length; i++) {
            int cnt = 0;
            for(int j=0; j<user_id.length; j++) {
                // user_id에 banned_id가 포함되어있으면 cnt++
                int lenUserId = user_id[j].length();
                int lenBananaId = banned_id[i].length();
                if(lenUserId == lenBananaId) {
                   if(verse(user_id[j], banned_id[i])) {
                       cnt++;
                   }
                }
            }
            // cnt가 maxCnt보다 크면 갱신
            maxCnt = Math.max(cnt, maxCnt);
        }
        
        return maxCnt;
    }
    static boolean verse(String str1, String str2) {
        for(int i=0; i<str1.length(); i++) {
            char ch1 = str1.charAt(i);
            char ch2 = str2.charAt(i);
            if(ch2 != '*') {
                if(ch1 != ch2) {
                    return false;
                }
            } 
        }
        return true;
    }
}
