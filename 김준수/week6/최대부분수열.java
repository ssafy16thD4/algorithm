/*
전략
- memo[i] : i번째 원소부터 i + K번째 원소까지의 합
- 모든 memo에서 2개를 고르는 조합. 이때 memo 간에 구간이 겹치면 스킵
- AI 평가 : O(N^2)이라 시간 복잡도 아슬아슬함
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 최대부분수열 {
	static int N, K;
	static int[] arr;
	static int[] memo;
	static int result;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine().trim());
		for(int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine().trim());
			N = Integer.parseInt(st.nextToken());
			K = Integer.parseInt(st.nextToken());
			result = Integer.MIN_VALUE;
			
			arr = new int[N];
			st = new StringTokenizer(br.readLine().trim());
			for(int i = 0; i < N; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}
			
			// memo[i] : i ~ i + K번째 요소의 합
			memo = new int[N - K + 1];
			for(int i = 0; i < N - K + 1; i++) {
				int sum = 0;
				for(int j = i; j < i + K; j++) {
					sum += arr[j];
				}
				memo[i] = sum;
			}
			
			dfs(0, 0, new int[] {-1, -1});
			
			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
	
	private static void dfs(int depth, int idx, int[] memoIdx) {
		if(depth == 2) {
			if(memoIdx[1] - memoIdx[0] < K) return;
			
			int sum = memo[memoIdx[0]] + memo[memoIdx[1]];	
			if(result < sum) result = sum;
			return;
		}
		
		for(int i = idx; i < N - K + 1; i++) {
			memoIdx[depth] = i;
			
			dfs(depth + 1, i + 1, memoIdx);
			
			memoIdx[depth] = -1;
		}
	}
}