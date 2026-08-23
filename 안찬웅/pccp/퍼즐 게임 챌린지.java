import java.util.*;
/* 
    diff: 퍼즐의 난이도
    time_cur: 현재 퍼즐의 소요 시간
    time_prev: 퍼즐의 소요 시간
    level: 숙련도
    
    limit: 제한시간
    int[] times: 퍼즐의 소요시간
    int[] diffs: 퍼즐의 난이도
    
    0.숙련도를 계속 1씩 증가시킴
    1.diff <= level: time_cur 시간 사용
    2.diff > level: diff-level번 틀림
    2.1 틀릴때마다 time_cur만큼의 시간을 사용하고 time_prev 만큼의 시간을사용해 이전 퍼즐을 다시 풀어야함
    2.1.1 현재시간 += (times[i] + 현재시간) * (퍼즐의 난이도 - 숙련도) + times[i]
    3.숙련도가 해결하지 못하는 시간 최대값 출력
*/
class Solution {
    public int solution(int[] diffs, int[] times, long limit) {
        int level = 1; // 숙련도
        while(true) {
            long timeTotal = 0;
            for(int i=0; i<diffs.length; i++) {
                int prev = 0;
                if(i >= 1) { prev = times[i-1]; }
                
                int x = diffs[i] - level;
                if(x <= 0) {
                    // 1.diff <= level: time_cur 시간 사용
                    timeTotal += times[i];
                } else {
                    // 2.diff > level: diff-level번 틀림
                    // 2.1 틀릴때마다 time_cur만큼의 시간을 사용하고 time_prev 만큼의 시간을사용해 이전 퍼즐을 다시 풀
                    // 2.1.1 현재시간 += (times[i] + 현재시간) * (퍼즐의 난이도 - 숙련도) + times[i]
                    timeTotal += (times[i] + prev) * x + times[i];
                }
            }
            //System.out.println("level: " + level + " timeTotal: " + timeTotal);
            if(timeTotal <= limit) {
                break;
            }
            level++;
        }
        
        return level;
    }
}