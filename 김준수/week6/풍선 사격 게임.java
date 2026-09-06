/*
전략
- 백트래킹 완전탐색
- 더블 링크드 리스트 직접 구현
    - N 최대값을 몰라서 안전하게 진행
- 풍선 삭제 시 규칙대로 값 추가하고 양쪽을 연결. 삭제한 풍선의 포인터는 -1로 처리하지 않고 그대로 둠
- 풍선 복구 시 값 추가한만큼 빼고, 해당 위치에 다시 추가. 삭제했다가 복구하는 풍선의 포인터를 활용해서 다시 연결
- 풍선 삭제 및 복구 시 양쪽 끝의 -1을 회피
- 점수 계산 시 양쪽 다 있을 때, 왼쪽만 있을때, 오른쪽만 있을 때, 양쪽 다 없을때로 구분해서 계산
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 풍선사격게임 {
    static int[] next;
    static int[] prev;
    static int[] balloons;
    static boolean[] visited;
    static int N;
    static int result;
	
	public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        for(int t = 1; t <= T; t++) {
        	N = Integer.parseInt(br.readLine().trim());
        	prev = new int[N];
        	next = new int[N];
            balloons = new int[N];
        	visited = new boolean[N];
            result = 0;
        	
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            for(int i = 0; i < N; i++) {
            	balloons[i] = Integer.parseInt(st.nextToken());
            	prev[i] = i - 1;
            	next[i] = i + 1;
            }
            next[N - 1] = -1;
            
            dfs(0, 0);
            
            sb.append("#").append(t).append(" ").append(result).append("\n");
        }
        System.out.print(sb);
    }
	
	private static void dfs(int depth, int score) {
		if(depth == N) {
			if(score > result) result = score;
			return;
		}
		
		for(int i = 0; i < N; i++) {
			if(visited[i]) continue;
			
			int curr;
			// 양쪽 다 있을 때
			if(prev[i] != -1 && next[i] != -1) {
				curr = balloons[prev[i]] * balloons[next[i]];
			}
			// 왼쪽만 있을 때
			else if(prev[i] != -1) {
				curr = balloons[prev[i]];
			}
			// 오른쪽만 있을 때
			else if(next[i] != -1) {
				curr = balloons[next[i]];
			}
			// 양쪽 다 없을 때
			else {
				curr = balloons[i];
			}
			
			visited[i] = true;
			delete(i);
			
			dfs(depth + 1, score + curr);
			
			visited[i] = false;
			add(i);
		}
	}
	
	private static void delete(int idx) {
		// 양쪽 끝만 갱신 회피
		if(prev[idx] != -1) {
			next[prev[idx]] = next[idx];
		}
		if(next[idx] != -1) {
			prev[next[idx]] = prev[idx];
		}
	}
	
	private static void add(int idx) {
		// 양쪽 끝만 갱신 회피
		if(prev[idx] != -1) {
			next[prev[idx]] = idx;
		}
		if(next[idx] != -1) {
			prev[next[idx]] = idx;
		}
	}
}