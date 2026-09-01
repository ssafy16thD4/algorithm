/*
전략
- 완전탐색
- K = 1일때, K = 2일때 .. K = 2N일때까지 DP로 비용 계산
    - K = N일때 비용 dp[N] = dp[N-1] + 4 * (N - 1)
- 지도의 범위가 (1, 1)~(N, N)일때 배치 가능한 서비스의 중심은 (0, 0) ~ (N + 1, N + 1)까지 배치 가능하게
- 즉, 각 K에 대해서(1 ~ 2N), 각 행에 대해서(0 ~ N + 1) 집과 비용을 계산하여 result를 갱신하는 find 함수 호출
- find 함수는 점점 열의 숫자를 늘리면서 재귀호출시키기
*/

package algorithm;

import java.io.*;
import java.util.*;

public class SWEA2117 {
	static int N, M;
	static boolean[][] board;
	static int[] dp;
	static int result;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine().trim());
		for(int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			result = 0;
			
			board = new boolean[N + 2][N + 2];
			for(int i = 1; i <= N; i++){
				st = new StringTokenizer(br.readLine());
				for(int j = 1; j <= N; j++) {
					if(Integer.parseInt(st.nextToken()) == 1) board[i][j] = true;
				}
			}
			
			// K별 비용 갱신
			dp = new int[2 * N + 1];
			dp[1] = 1;
			for(int i = 2; i <= 2 * N; i++) {
				dp[i] = dp[i - 1] + 4 * (i - 1);
			}
			
			// 각 K에 대해서 (1 ~ 2N)
			for(int k = 1; k <= 2 * N; k++) {
				// 각 행에 대해서
				for(int i = 0; i <= N + 1; i++) {
					find(k, i, 0, getHouseCnt(k, i, 0));
				}
			}
			
			sb.append("#").append(tc).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
	
	private static boolean inRange(int x, int y) {
		return (x >= 0 && x <= N + 1 && y >= 0 && y <= N + 1);
	}
	
	private static int getHouseCnt(int k, int r, int c) {
		k--; // 밑 for문을 k + 1기준으로 만들어버려서 사후처리
	    int cnt = 0;
	    
	    // dr과 dc가 k를 나눠먹는 느낌. dr이 먹고 남은걸 dc가 remain으로 가져감
	    for (int dr = -k; dr <= k; dr++) {
	        int remain = k - Math.abs(dr);  // 이 행에서 좌우로 갈 수 있는 여유
	        for (int dc = -remain; dc <= remain; dc++) {
	            int nr = r + dr;
	            int nc = c + dc;
	            if (inRange(nr, nc) && board[nr][nc]) {
	                cnt++;
	            }
	        }
	    }
	    
	    return cnt;
	}
	
	private static void find(int k, int r, int c, int houseNum) {
		if(houseNum * M >= dp[k]) {
			if(result < houseNum) result = houseNum;
		}
		
		if(c == N + 1) return;
		houseNum = getHouseCnt(k, r, c + 1);
		find(k, r, c + 1, houseNum);
	}
}
