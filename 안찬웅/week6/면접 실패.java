import java.util.*;
import java.lang.*;
import java.io.*;
/*
 * n개중에 m개를 맞췄을때 총점이 최소 인 경우 점수
 * 
 * 알고리즘: 조합 DFS
 * 
 * n개중 m개를 맞추는 모든 경우의수
 * 시작인덱스, 깊이
 * 1.dfs(start, depth)
 *  1.1 depth == m개를 맞추는 경우
 *   가장 최소값일 때
 *   return;
 *  1.2 카운터 = 0;
 *  1.3 맞췄으면 카운터++
 *  1.4 틀렸으면 카운터 = 0
 *  1.5 카운터가 k가 되면 
 *   점수 = (점수 + 1) * 2
 *   카운터 = 0
 *  
 * 2. 가장 최소 점수 출력
*/
public class Main {
	static int[] arr;
	static int n, m, k;
	static int minScore;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());
			
			n = Integer.parseInt(st.nextToken()); // 문제수
			m = Integer.parseInt(st.nextToken()); // 맞춘수
			k = Integer.parseInt(st.nextToken()); // 카운터수
			
			arr = new int[n];
			minScore = Integer.MAX_VALUE;
			
			dfs(0, 0);
			
			sb.append("#").append(test_case).append(" ").append(minScore).append("\n");
		}
		System.out.print(sb);
	}
	static void dfs(int start, int depth) {
		if(depth > m) return;
        if(depth == m) {
            int answer = count(arr);
    		minScore = Math.min(minScore, answer);
			return;
		}
		
		for(int i=start; i<n; i++) {
			arr[depth] = i; // 맞춤
			dfs(i+1, depth+1);
        }
	}
    static int count(int[] arr) {
        int count = 0;
        int score = 0;
        for(int i=0; i<arr.length-1; i++) {
            score += 1;
            if(arr[i] + 1 == arr[i+1]) {
                count++;
                if(count == k) {
                    score *= 2;
                    count = 0;
                }
            } else {
                count = 0;
            }           
        }
        return score;
    }
}
