import java.util.*;
import java.io.*;
/*
 * 모든 나무의 키가 첫날 가장 큰 나무와 같아지는 최소 날
 * 
 * 알고리즘: 이분탐색
 *
 * maxHeight 기준 부족한 개수
 * 1. d[i] = maxHeight - arr[i]
 * 2. 물 주는 순서 상관x
 *     +1 a장, +2 b장으로 d[] 변환
 * 3. b = day/2,  a = day-b
 * 4. check(day)
 *    - d[i] 홀수면 +1
 *    - +2는 짝수만
 */
public class SWEA14510 {
    static int[] arr;
    static int[] d;
    static int n;
    static int maxHeight; // 가장 큰 나무 높이
 
    public static void main(String[] args) throws Exception {
    	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    	//BufferedReader br = new BufferedReader(new StringReader(input));
    	StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());
        
        for(int tc=1; tc<=t; tc++) {
        	sb.append("#").append(tc).append(" ");
            n = Integer.parseInt(br.readLine());
            
            arr = new int[n];
            d = new int[n];
            
            maxHeight = Integer.MIN_VALUE;
            
            st = new StringTokenizer(br.readLine());
            for(int i=0; i<n; i++) {
                arr[i] = Integer.parseInt(st.nextToken());
                maxHeight = Math.max(maxHeight, arr[i]);
            }
            
            for(int i=0; i<n; i++) {
            	d[i] = maxHeight - arr[i]; 
            }
            
            int low = 0;
            // Integer.MAX_VALUE 로 두면 low+high 가 int 를 넘겨 mid 가 음수가 된다.
            // 답의 상한은 "모자란 양 전체를 +1 카드로만 채우는 날" 이면 충분하다.
            int high = 0;
            for(int i=0; i<n; i++) high += d[i];
            high = high * 2 + 2;
            int result = 0;
            
            while(low <= high) {
            	int mid = low + (high - low) / 2;
            	if(check(mid)) {
            		result = mid;
            		high = mid - 1;
            	} else {
            		low = mid + 1;
            	}
            }
            
            sb.append(result).append("\n");
        }
        System.out.println(sb.toString());
    }
    static boolean check(int day) {
    	int b = day / 2; // 짝수날 개수 = +2카드 장수
    	int a = day - b; // 홀수날 개수 = +1카드 장수
    	
    	int odd = 0;
    	int total = 0;
    	for(int i=0; i<n; i++) {
    		total += d[i];
    		if(d[i] % 2 == 1) {
    			odd++;
    		}
    	}
    	
    	if(odd > a) return false; // +1이 창고보다 많으면 false
    	
    	int need2 = (total - odd) / 2;
    	
    	if (need2 <= b) return true; // +2가 창고보다 많으면 true
    	
    	if((odd + (need2 - b) * 2) <= a) { // +2 -> +1 두 장으로 환산
    		return true;
    	}
    	
    	return false;
    }
}

