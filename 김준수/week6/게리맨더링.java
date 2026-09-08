/*
전략
- 순열
- 모든 선거구를 A 혹은 B에 배치해서 인구 차이 최솟값 찾기
    - 선거구의 개수가 10개니까 2^10 = 1024개의 경우의 수 발생
- 모든 구역 배치 완료 했으면 나눠진 구역이 있는지 검사하고 나눠졌으면 스킵
    - BFS로 각 선거구에서 1개 노드를 골라서 BFS 돌렸을 때 선거구 내 모든 구역에 방문 가능하면 OK
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 게리맨더링 {
	static int N;
	static int[] areas;
	static List<Integer>[] graph;
	static int[] areaTypes;
	static int[] typeCnts;
	static int[] typeSums;
	static int result;
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		
		result = Integer.MAX_VALUE;
		N = Integer.parseInt(br.readLine().trim());
		areaTypes = new int[N + 1];
		
		areas = new int[N + 1];
		StringTokenizer st = new StringTokenizer(br.readLine().trim());
		for(int i = 1; i <= N; i++) {
			areas[i] = Integer.parseInt(st.nextToken());
		}
		
		graph = new List[N + 1];
		for(int i = 1; i <= N; i++) {
			graph[i] = new ArrayList<>();
		}
		for(int i = 1; i <= N; i++) {
			st = new StringTokenizer(br.readLine().trim());
			int M = Integer.parseInt(st.nextToken());
			for(int j = 0; j < M; j++) {
				graph[i].add(Integer.parseInt(st.nextToken()));
			}
		}
		
		typeCnts = new int[2];
		typeSums = new int[2];
		dfs(1);
		
		if(result == Integer.MAX_VALUE) result = -1;
		System.out.println(result);
	}
	
	private static void dfs(int idx) {
		if(idx == N + 1) {			
			int aStart = -1;
			int bStart = -1;
			
			for(int i = 1; i <= N; i++) {
				if(aStart != -1 && bStart != -1) break;
				
				if(areaTypes[i] == 0) {
					aStart = i;
				}
				else {
					bStart = i;
				}
			}
			
			if(aStart == -1 || bStart == -1) return;
			
			if(bfs(aStart) && bfs(bStart)) {
				if(result > Math.abs(typeSums[0] - typeSums[1])) {
					result = Math.abs(typeSums[0] - typeSums[1]);
				}
			}

			return;
		}
		
		// 다음 선거구를 A로 재귀 호출
		typeCnts[0]++;
		typeSums[0] += areas[idx];
		areaTypes[idx] = 0;
		
		dfs(idx + 1);
		
		typeCnts[0]--;
		typeSums[0] -= areas[idx];
		
		// 다음 선거구를 B로 재귀 호출
		typeCnts[1]++;
		typeSums[1] += areas[idx];
		areaTypes[idx] = 1;
		
		dfs(idx + 1);
		
		typeCnts[1]--;
		typeSums[1] -= areas[idx];
	}
	
	// start가 자기 지역구의 모든 노드를 bfs로 다 방문 가능한지 여부 반환
	private static boolean bfs(int start) {
		boolean[] visited = new boolean[N + 1];
		int cnt = 0;
		Queue<Integer> queue = new ArrayDeque<>();
		queue.offer(start);
		cnt++;
		visited[start] = true;
		
		while(!queue.isEmpty()) {
			int curr = queue.poll();
			for(int next : graph[curr]) {
				if(visited[next] || areaTypes[next] != areaTypes[curr]) continue;
				
				queue.offer(next);
				cnt++;
				visited[next] = true;
			}
		}
		
		// 전부 다 방문했으면 true 반환
		if(cnt == typeCnts[areaTypes[start]]) {
			return true;
		}
		return false;
	}
}