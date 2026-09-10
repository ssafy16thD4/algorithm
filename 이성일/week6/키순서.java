package com.ssafy.swb;

import java.io.*;
import java.util.*;

public class 키높이 {
	
	static boolean[][] tallerGraph;
	static boolean[][] smallerGraph;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine().trim());
		
		for (int tc = 1; tc <= T; tc++) {
			int N = Integer.parseInt(br.readLine());
			int M = Integer.parseInt(br.readLine());
			
			tallerGraph = new boolean[N+1][N+1];
			smallerGraph = new boolean[N+1][N+1];
			/*
			 * 플로이드 워셜로 모든 간선 간, 경유로 이동 가능 조사, 모든 노드와 연결된 노드는 카운트 업
			 */
			for (int i = 0; i < M; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine().trim());
				int a = Integer.parseInt(st.nextToken());
				int b = Integer.parseInt(st.nextToken());
				
				tallerGraph[a][b] = true;
				smallerGraph[b][a] = true;
			}
			
			
			for (int k = 1; k <= N; k++) {
				for (int i = 1; i <= N; i++) {
					if (k == i) continue;
					for (int j = 1; j <= N; j++) {
						if (k == j || i == j) continue;
						if (tallerGraph[i][k] && tallerGraph[k][j]) {
							tallerGraph[i][j] = true;
						}
						if (smallerGraph[i][k] && smallerGraph[k][j]) {
							smallerGraph[i][j] = true;
						}
					}
				}
			}
			
			
			int answer = 0;
			
			for (int i = 1; i <= N; i++) {
				int tCnt = 0;
				int sCnt = 0;
				for (int j = 1; j <= N; j++) {
					if (i == j) continue;
					if (tallerGraph[i][j]) tCnt++;
					if (smallerGraph[i][j]) sCnt++;
				}
				if (tCnt + sCnt == (N-1)) answer++;
			}
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("#").append(tc).append(" ").append(answer);
			System.out.println(sb);
			
		}
	}
}
