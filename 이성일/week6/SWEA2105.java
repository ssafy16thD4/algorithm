package com.ssafy.swb;

import java.io.*;
import java.util.*;

public class SWEA2105 {
	
	/*
	 * 배열 꼭짓점 인덱스 제외 모든 인덱스가 우상단 사각형 인덱스라 가정하고 DFS
	 * 시작 방향은 좌하단 고정
	 * 경계조건 , 중복 조건 발생 시 디렉션 변경
	 * 진행을 한칸도 못했을 시, -1
	 * 복귀 하면
	 */
	
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
	
	static int dfs(int[][] board, int dir, int x, int y, Set<Integer> set, int cnt) {
		int nx = x + dx[dir];
		int ny = x + dy[dir];
		
		while (inRange(nx, ny, board.length) && !set.contains(board[nx][ny])) {
			
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
			
			int answer = -1;
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (inSharps(i,j,sharps)) continue;
					answer = Math.max(answer, dfs(board, 0, i, j, new HashSet<>(), 0));
				}
			}
		}
	}
}
