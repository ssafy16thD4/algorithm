/*
실패 코드
- M개의 1과 N-M개의 0으로 이루어진 조합 탐색
- 조합이 완성됐으면 규칙대로 점수 계산 후 최고 점수 갱신
    - 시간복잡도 O(N!)으로 N ≤ 500일때 무조건 시간초과
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 면접실패 {
	static int N, M, K;
	static List<Boolean> interview;
	static List<Boolean> curr;
	static boolean[] visited;
	static int result;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine().trim());
		for(int t = 1; t <= T; t++) {
			String[] line = br.readLine().trim().split("");
			N = Integer.parseInt(line[0]);
			M = Integer.parseInt(line[1]);
			K = Integer.parseInt(line[2]);
			result = Integer.MAX_VALUE;
			
			curr = new ArrayList<>();
			interview = new ArrayList<>();
			for(int i = 0; i < M; i++) {
				interview.add(true);
			}
			for(int i = 0; i < N - M; i++) {
				interview.add(false);
			}
			
			visited = new boolean[N];
			dfs(0);
			
			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
	
	private static void dfs(int depth) {
		if(depth == N) {
			calc();
			return;
		}
		
		for(int i = 0; i < N; i++) {
			if(visited[i]) continue;
			
			visited[i] = true;
			curr.add(interview.get(i));
			
			dfs(depth + 1);
			
			visited[i] = false;
			curr.remove(curr.size() - 1);
		}
	}
	
	private static void calc() {
		int score = 0;
		int counter = 0;
		
		for(int i = 0; i < N; i++) {
			boolean b = curr.get(i);
			
			if(b) {
				score++;
				counter++;
			}
			else {
				counter = 0;
			}
			
			if(counter == K) {
				counter = 0;
				score *= 2;
			}
		}
		
		if(result > score) result = score;
	}
}