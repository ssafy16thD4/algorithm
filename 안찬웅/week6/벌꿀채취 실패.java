import java.util.*;
import java.io.*;
/*
 * 벌통의 크기 n과 벌통의 개수 m이 주어질때
 * 연속하는 m개의 숫자를 2개 고를 때, 각 값들은 최대양 c보단 작은 것만 고른다
 * 각 수들의 제곱의 합이 가장 클 때 값을 구하시오
 * 
 * 알고리즘: DFS(조합)
 * 
*/
public class algo {
	static int[][] graph;
	static int n, m, c;
	static int maxSum;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());
			
			n = Integer.parseInt(st.nextToken());
			m = Integer.parseInt(st.nextToken());
			c = Integer.parseInt(st.nextToken());
			
			graph = new int[n][n];
			
			for(int i=0; i<n; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<n; j++) {
					graph[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			maxSum = 0;
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					dfs(i, j, 0, 0, 0);
				}
			}
			
			sb.append("#").append(test_case).append(" ").append(maxSum).append("\n");
		}
		System.out.print(sb);
	}
	
	static void dfs(int x, int y, int idx, int sum, int score) {
		
		if(sum > c) return;
		
		if(idx == m) {
			maxSum = Math.max(maxSum, score);
			return;
		}
		
		dfs(x, y, idx+1, sum + graph[x][y + idx],  score + graph[x][y + idx] * graph[x][c + idx]);
		dfs(x, y, idx+1, sum,  score);
	}
}
