/*
전략
- 완전탐색 시뮬레이션
- 두 명의 일꾼을 배치할 수 있는 모든 경우의 수에 대해 벌통 수익 계산
- 수익 계산 시, 가능한 모든 벌통 조합에 대해 백트래킹으로 수입 계산하고 가장 큰 값으로 갱신

시행착오
- 수익 계산 시, 각 일꾼 별로 가장 큰 벌통에서 먼저 다 채취하고, 그 다음 큰 벌통에서 다 채취하고 반복
  최대 C에 도달할때까지 반복해서 채취하니까 엣지 케이스 발생함(테케 2번)
*/

import java.io.*;
import java.util.*;

public class Solution {
	static int N, M, C;
	static int[][] board;
	static int[][] profits;
	static boolean[] visited;
	static int sum;
	static int[] nums;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine());
		for(int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			C = Integer.parseInt(st.nextToken());
			board = new int[N][N];
			profits = new int[N][N];
			
			int result = 0;
			
			for(int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j = 0; j < N; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					int end = (j + M - 1 >= N) ? N - 1 : j + M - 1;
					getProfit(i, j, end);
				}
            }
			
			// (i, j) : 일꾼A의 시작 좌표
			// (x, y) : 일꾼B의 시작 좌표
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					for(int x = i; x < N; x++) {
						for(int y = 0; y < N; y++) {
							// A와 B의 채취자리가 겹치면 스킵
							if(i == x && j <= y && j + M >= y) continue;
							if(i == x && y <= j && y + M >= j) continue;
							
							int curr = profits[i][j] + profits[x][y];
							if(result < curr) {
								result = curr;
							}
						}
					}
				}
			}
			
			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
	
	// 가장 큰 값부터 꺼내니까 엣지 케이스 발생(테케2번)
	// start ~ end까지의 최대 Profit을 찾아서 저장
	private static void getProfit(int row, int start, int end) {
		nums = new int[M];
		visited = new boolean[M];
		sum = 0;
		
		for(int i = start; i <= end; i++) {
			nums[i - start] = board[row][i];
		}
		
		dfs(C, 0);
		
		profits[row][start] = sum;
	}
	
	// 현재 채취 칸에서 가능한 모든 경우의 수 탐색
	private static void dfs(int currC, int currSum) {
		if(currSum > sum) sum = currSum;
		
		for(int i = 0; i < M; i++) {
			if(visited[i] || currC < nums[i]) continue;
			
			visited[i] = true;
			currC -= nums[i];
			currSum += nums[i] * nums[i];
			
			dfs(currC, currSum);
			
			visited[i] = false;
			currC += nums[i];
			currSum -= nums[i] * nums[i];
		}
	}
}