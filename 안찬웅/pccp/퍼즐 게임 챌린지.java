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

    고친 점 두 가지
    - 숙련도를 1씩 올리면 최악의 경우 (난이도 최대 10만) × (퍼즐 30만) = 3e10 번 돈다.
      "level 이 커질수록 총 시간은 단조 감소" 이므로 이분탐색이 맞다. O(N log D)
    - (times[i] + prev) * x 는 int 곱이라 넘친다. times 1e4 × 난이도 1e5 만 돼도 int 범위를 벗어난다.
      long 으로 계산한다.
*/
class Solution {
    static int[] diffs;
    static int[] times;

    public int solution(int[] diffs, int[] times, long limit) {
        Solution.diffs = diffs;
        Solution.times = times;

        int low = 1;
        int high = 1;
        for(int d : diffs) high = Math.max(high, d); // 난이도 최대면 무조건 통과

        while(low < high) {
            int mid = low + (high - low) / 2;
            if(total(mid) <= limit) {
                high = mid;   // 이 숙련도로 되니까 더 낮춰본다
            } else {
                low = mid + 1;
            }
        }
        return low;
    }
    // 숙련도 level 로 전부 푸는 데 걸리는 시간
    static long total(int level) {
        long timeTotal = 0;
        for(int i=0; i<diffs.length; i++) {
            long prev = (i >= 1) ? times[i-1] : 0;

            int x = diffs[i] - level;
            if(x <= 0) {
                // 1.diff <= level: time_cur 시간 사용
                timeTotal += times[i];
            } else {
                // 2.diff > level: diff-level번 틀림
                timeTotal += (times[i] + prev) * (long) x + times[i];
            }
        }
        return timeTotal;
    }
}
