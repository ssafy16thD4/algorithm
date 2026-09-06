package com.ssafy.swb;

import java.io.*;
import java.util.*;

public class 사과먹기게임 {
	/*
	 * 현재 위치에서 다음 사과 위치로 진행
	 * 사과 좌표 값과의 비교로 다음 사과로 가기 위한 필요 디렉션 구하기
	 * * 8개의 다음 방향 진행을 위해 필요한 디렉션 모음
	 * 현재 디렉션에서 필요 디렉션들을 모두 달성하기 위한 회전 횟수 구하기
	 * * 필요 디렉션별로 현재 디렉션에서 해당 필요 디렉션으로 가기 위한 회전 횟수 구하기
	 * * 우 하 좌 상
	 * 마지막 사과먹을 때까지 회전회수 합산
	 */
	
	static int[][] points;
	static int[][] board;
	
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for (int tc = 1; tc <= T; tc++) {
			int N = Integer.parseInt(br.readLine());
			board = new int[N][N];
			int appleNum = 0;
			for (int i = 0; i < N; i++) {
				String row = br.readLine().trim();
				for (int j = 0; j < N; j++) {
					board[i][j] = row.charAt(j) - '0';
					if (board[i][j] > 0) appleNum++;
				}
			}
			
			points = new int[appleNum+1][2];
			points[0][0] = 0;
			points[0][1] = 0;
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (board[i][j] > 0) {
						points[board[i][j]][0] = i;
						points[board[i][j]][1] = j;
					}
				}
			}
			
			int i = 0;
			int rotateCnt = 0;
			int currDir = 0;
			while (i < points.length - 1) {
				int currX = points[i][0];
				int currY = points[i][1];
				int nextX = points[i+1][0];
				int nextY = points[i+1][1];
				
				if (nextX < currX && nextY > currY) {
					// 상, 우 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {3, 0});
					currDir = 0;
				} else if (nextX == currX && nextY > currY) {
					// 우 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {0});
					currDir = 0;
				} else if (nextX > currX && nextY > currY) {
					// 우, 하 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {0, 1});
					currDir = 1;
				} else if (nextX > currX && nextY == currY) {
					// 하 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {1});
					currDir = 1;
				} else if (nextX > currX && nextY < currY) {
					// 하, 좌 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {1, 2});
					currDir = 2;
				} else if (nextX == currX && nextY < currY) {
					// 좌 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {2});
					currDir = 2;
				} else if (nextX < currX && nextY < currY) {
					// 좌 상 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {2,3});
					currDir = 3;
				} else { // nextX < currX && nextY == currY
					// 상 방향 이동 필요
					rotateCnt += rotateCounter(currDir, new int[] {3});
					currDir = 3;
				}
				
				i++;
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(rotateCnt);
			System.out.println(sb);
			
		}
		
	}
	
	static int rotateCounter(int currDir, int[] needDirs) {
		
		int rCnt = 0;
		
		int tempCnt= 0;
		int tempDir = currDir;
		boolean flag = false;
		for (int needDir: needDirs) {
			if (needDir == tempDir) {
				flag = true;
				continue;
			} else {
				while (needDir != tempDir) {
					tempCnt++;
					tempDir = (tempDir+1) % 4;
				}
			}
		}
		
		if (flag) return tempCnt;
		
		for (int needDir : needDirs) {
			while (needDir != currDir) {
				currDir = (currDir + 1) % 4;
				rCnt += 1;
			}
		}
		
		return rCnt;
	}
}
