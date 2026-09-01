package com.ssafy.swb;

import java.io.*;
import java.util.*;

public class SWEA1953 {
	
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};
	
	static int[][] blocks = {
			{},
			{0,1,2,3},
			{0,2},
			{1,3},
			{0,1},
			{1,2},
			{2,3},
			{0,3}
	};
	
	static int[] dirMap = {2, 3, 0, 1}; 
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			int R = Integer.parseInt(st.nextToken());
			int C = Integer.parseInt(st.nextToken());
			int L = Integer.parseInt(st.nextToken());
			
			int[][] board = new int[N][M];
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			int answer = bfs(R, C, L, board);
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(answer);
			System.out.println(sb);
		}
	}
	
	static boolean inRange(int x, int y, int nr, int nc) {
		return (x >= 0 && x < nr && y >= 0 && y < nc);
	}
	
	static int bfs(int r, int c, int time, int[][] board) {
		Deque<int[]> deq = new ArrayDeque<>();
		deq.offerLast(new int[] {r,c});
		
		int l = 1;
		int cnt = 1;
		
		// 시간별 원소 수
		int[] breadthCnt = new int[time+1];
		breadthCnt[1] = 1;
		// 방문여부
		boolean[][] visited = new boolean[board.length][board[0].length];
		visited[r][c] = true;
		
		
		while (l < time) {
			
			
			for (int i = 0; i < breadthCnt[l]; i++) {
				int[] idx = deq.pollFirst();
				
				int x = idx[0];
				int y = idx[1];
				
				for (int axis: blocks[board[x][y]]) {
					
					int nx = x + dx[axis];
					int ny = y + dy[axis];
					
					if (!inRange(nx, ny, board.length, board[0].length) || visited[nx][ny] || board[nx][ny] == 0) continue;
					
					for (int naxis: blocks[board[nx][ny]]) {
						if (naxis == dirMap[axis]) {
							
							deq.offerLast(new int[] {nx,ny});
							
							visited[nx][ny] = true;
							cnt++;
							
							breadthCnt[l+1]++;
							
							break;
						}
					}
					
				}
			}
			l++;
			
		}
		
		return cnt;
		
	}
	
	
	
}
