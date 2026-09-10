package algorithm;
import java.io.*;
import java.util.*;
/*
 * 헌터가 몬스터를 (1~4개)를 잡고 고객의 집으로 배달함
 * 조건 1) 고객의 집과 몬스터의 수는 같음
 * 조건 2) 몬스터를 잡고 고객의 집으로 배달해도 되고 다 잡고 배달도 가능
 *     
 * 음수: 고객
 * 양수: 몬스터
 * 
 * 알고리즘: BFS
 * 
 * 출발지점 (0, 0)
 * bfs() 
 * 2.1 몬스터를 전부 잡고 집에 배달하는 경우 
 * 2.2 몬스터를 하나씩 잡고 배달하는 경우 
 * 2.3 몬스터를 1,2개 잡고 배달하고 3번째 잡고 배달하는 경우 
 * 
 * 설계 실패
 * BFS + DFS 같이 쓰는 문제임을 확인. 어떻게 풀지 감이 아예 안잡힘
 * 추후 다시 풀 예정
 * 
*/
public class Solution {
	static int[][] graph;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		int T = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=T; test_case++) {
			int n = Integer.parseInt(br.readLine()); // 농장의 크기
			
			graph = new int[n][n];
			for(int i=0; i<n; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<n; j++) {
					graph[i][j] = Integer.parseInt(st.nextToken());
				}
			}
				
			sb.append("#").append(test_case).append(" ").append(total).append("\n");
		}
		System.out.print(sb);
	}
}
