/*
매개변수 탐색으로 레벨을 탐색?
레벨의 최고는 diffs의 최고로, 레벨의 최소는 diffs의 최소로
isValid(mid) : 현재 레벨일 때 limit 내에 해결 가능한지
true면 end = mid - 1
false면 start = mid + 1
start <= end로 while 돌리기
*/

class Solution {
    static int[] diffs;
    static int[] times;
    static long limit;
    
    public int solution(int[] diffs, int[] times, long limit) {
        this.diffs = diffs;
        this.times = times;
        this.limit = limit;
        
        int start = Integer.MAX_VALUE;
        int end = Integer.MIN_VALUE;
        for(int d : diffs){
            start = Math.min(d, start);
            end = Math.max(d, end);
        }
        
        while(start <= end){
            int mid = (start + end) / 2;
            
            if(isValid(mid)){
                end = mid - 1;
            }
            else{
                start = mid + 1;
            }
        }
        
        return start;
    }
    
    private boolean isValid(int level){
        long time = times[0];
        long prevTime = times[0];
        
        for(int i = 1; i < diffs.length; i++){
            long d = diffs[i];
            long t = times[i];
            
            if(level >= d){
                time += t;
            }
            else{
                long error = d - level;
                time += error * (prevTime + t) + t;
            }
            
            prevTime = t;
        }
        
        return (time <= limit);
    }
}