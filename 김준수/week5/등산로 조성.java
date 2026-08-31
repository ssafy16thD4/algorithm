/*
전략
- dfs로 현재 경로에서 깎았는지 안깎았는지 여부를 같이 넘겨주기
- dfs로 현재 경로에서 4방향 가능한 곳 재귀호출하고, 깎았을 때 가능한 곳 4방향을 재귀호출 하면서 깎음여부를 true로 넘김
- 현재 깎음여부가 true면 dfs할 때 다음 깎기는 못하게
*/

import java.io.*;
import java.util.*;

public class SWEA1949 {
	static final int[] dx = new int[] {-1, 1, 0, 0};
	static final int[] dy = new int[] {0, 0, -1, 1};
	
	static int[][] board;
	static int K;
	static int result = 0;
	static boolean[][] visited;
	static List<int[]> tops; 
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine());
		for(int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());
			result = 0;
			int maxHeight = 0;
			
			board = new int[N][N];
			visited = new boolean[N][N];
			for(int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j = 0; j < N; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
					if(maxHeight < board[i][j]) maxHeight = board[i][j];
				}
			}
			
			tops = new ArrayList<>();
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					if(maxHeight == board[i][j]) tops.add(new int[] {i, j});
				}
			}
			
			for(int[] top : tops) {
				visited[top[0]][top[1]] = true;
			
				dfs(top[0], top[1], 1, false);
				
				visited[top[0]][top[1]] = false;
			}
			
			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
	
	private static void dfs(int x, int y, int depth, boolean isCut) {
		if(depth >= result) result = depth;
		
		for(int i = 0; i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(!inRange(nx, ny) || visited[nx][ny]) continue;
			
			// 아직 공사 안했으면 공사했을때 이동 가능한 곳도 완전 탐색
			if(!isCut && (board[nx][ny] >= board[x][y] && board[nx][ny] - K < board[x][y])) {
				int temp = board[nx][ny];
				board[nx][ny] = board[x][y] - 1;
				visited[nx][ny] = true;
				
				dfs(nx, ny, depth + 1, true);
				
				board[nx][ny] = temp;
				visited[nx][ny] = false;
			}
			
			if(board[nx][ny] < board[x][y]) {
				visited[nx][ny] = true;
				
				dfs(nx, ny, depth + 1, isCut);
				
				visited[nx][ny] = false;
			}
		}
	}
	
	private static boolean inRange(int x, int y) {
		return (0 <= x && x < board.length && 0 <= y && y < board.length);
	}
}