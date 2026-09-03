package coding;
import java.io.*;
import java.util.*;

public class SWEA2105 {
	
	
	static int[] dx = {1, 1, -1, -1};
	static int[] dy = {-1, 1, 1, -1};
	
	static boolean inSharps(int i, int j, int[][] sharps) {
		for (int[] sharp: sharps) {
			if (i == sharp[0] && j == sharp[1]) return true;
		}
		return false;
	}
	
	static boolean inRange(int x, int y, int n) {
		return (x >= 0 && x < n && y >= 0 && y < n);
	}
	
	static int startX;
	static int startY;
	static int answer;
	
	
	
	static void dfs(int x, int y, Set<Integer> set, int dir, int[][] board, int dessertCnt) {
		
		for (int i = dir; i <= dir + 1 && i < 4; i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if (!inRange(nx, ny, board.length)) continue;
			
			if (i == 3 && nx == startX && ny == startY) {
				answer = Math.max(answer,  dessertCnt);
				continue;
			}
			
			if (set.contains(board[nx][ny])) continue;
			
			set.add(board[nx][ny]);
			dfs(nx, ny, set, i, board, dessertCnt+1);
			set.remove(board[nx][ny]);
			
		}
	}
	
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for (int tc = 1; tc <= T; tc++) {
			int N = Integer.parseInt(br.readLine());
			int[][] board = new int[N][N];
			
			for (int i = 0; i < N; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine().trim());
				for (int j = 0; j < N; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
					
				}
			}
			
			int[][] sharps = {
					{0,0},
					{0, N-1},
					{N-1, 0},
					{N-1, N-1}
			};
			
			answer = -1;
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (inSharps(i,j,sharps)) continue;
					startX = i;
					startY = j;
					Set<Integer> set = new HashSet<>();
					set.add(board[i][j]);
					dfs(i, j, set, 0, board, 1);
				}
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(answer);
			
			System.out.println(sb);
		}
	}
}
