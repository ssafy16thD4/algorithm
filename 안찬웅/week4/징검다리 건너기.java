import java.util.*;

class Solution {
    public int solution(int[] stones, int k) {
        int person = 0;
        
        int left = 0;
        int right = 0;
        
        for(int i=0; i<stones.length; i++) {
            right = Math.max(stones[i], right);
        }
        
        while(left <= right) {
            int mid = (left + right) / 2;
            
            int cnt = 0;
            boolean flag = false;
            for(int i=0; i<stones.length; i++) {
                if(stones[i] < mid) {
                    cnt++;
                    if(cnt >= k) {
                        flag = true;
                        break;
                    }
                } else {
                    cnt=0;
                }
            }
            
            if (!flag) {
                person = mid;
                left = mid + 1; 
            } else {
                right = mid - 1;
            }
        }
        
        return person;
    }
}
