package com.ssafy.swb;

import java.io.*;
import java.util.*;

/*
 * 벌통 최대사이즈 슬라이딩 윈도우로 컨테이너 행별로 돌며 가능한 벌통 조합 구하기
 * 벌통 조합별로 DFS 
 * * 벌통 별로 선택, 자식 노드는 이전 선택 안한 벌통들
 * * depth가 M이 되면 종료 및 값 값 갱신
 * * depth가 M이 아니어도 현재까지 꿀양이 C보다 작으면 값 갱신
 * * 자식노드 선택 시 (꿀 양 더할 시 C를 초과하면 탐색 안함)
 * * 값은 static 변수로 매 dfs마다 0으로 초기화 후 dfs 이후 answer에 저장된 값을 list에 저장
 * * list정렬 후, 제일 큰 값 두개의 합을 출력
 */


public class SWEA2115 {
	
	static int[][] honeyContainer;
	static int[] honeyCrop;
	static int answer;
	static int maximum;
	static int M;
	static int C;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine().trim());
		
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine().trim());
			int N = Integer.parseInt(st.nextToken());
			M = Integer.parseInt(st.nextToken());
			C = Integer.parseInt(st.nextToken());
			
			honeyContainer = new int[N][N];
			maximum = 0;
			
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < N; j++) {
					honeyContainer[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N - M + 1; j++) {
					int sum = 0;
					answer = 0;
					honeyCrop = Arrays.copyOfRange(honeyContainer[i], j, j+M);
					boolean[] visited = new boolean[M];
					dfs(0, 0, 0, visited);
					sum += answer;
					
					int a = i;
					int b = j + M;
					while(a < N) {
						if (b >= N - M + 1) {
							a++;
							b = 0;
							continue;
						}
						answer = 0;
						honeyCrop = Arrays.copyOfRange(honeyContainer[a], b, b+M);
						visited = new boolean[M];
						dfs(0,0,0,visited);
						
						maximum = Math.max(maximum, answer + sum);
						
						b++;
					}
					
					
				}
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(maximum);
			System.out.println(sb);
			
			
			
			
		}
	}
	
	static void dfs(int depth, int cnt, int earning, boolean[] visited) {
		if (depth == M) {
			answer = Math.max(answer, earning);
			return;
		}
		if (cnt <= C) {
			// 수용량 안 쪽일 때 수익
			answer = Math.max(answer, earning);
		}
		
		for (int i = 0; i < honeyCrop.length; i++) {
			if (visited[i]) continue;
			if (honeyCrop[i] + cnt > C) continue;
			// 채집 가능 시
			visited[i] = true;
			dfs(depth + 1, cnt + honeyCrop[i], earning + (honeyCrop[i] * honeyCrop[i]), visited);
			visited[i] = false;
		}
	}
}
