package algorithm;
import java.util.*;
import java.io.*;
/*
 * 1번부터 순서대로 다음 숫자를 밟아야 함. 회전할 때마다 카운팅
 * 
 * 유형: 0-1 BFS
 * 비용: 전진 0, 우회전 1
 * 
 * 1. 목표 숫자마다 bfs를 따로 돌린다
 * 1.1 상태 = (r, c, dir), 비용은 dist[n][n][4]로 관리
 * 1.2 전진 = 비용 0 -> offerFirst / 우회전 = 비용 1 -> offerLast
 * 2. bfs는 도착 지점의 (r, c, dir, cost)를 반환
 * 2.1 반환받은 위치와 방향이 다음 bfs의 시작 상태가 된다
 * 3. 모든 cost를 누적해서 출력
 */
public class Solution {
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int[][] graph;
	static int[][][] dist;
	static int n, maxNum;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			n = Integer.parseInt(br.readLine());
			
			graph = new int[n][n];
			maxNum = 0;
			
			for(int i=0; i<n; i++) {
				String str = br.readLine();
				for(int j=0; j<str.length(); j++) {
					int a = str.charAt(j) - '0';
					graph[i][j] = a;
					
					if(maxNum < a) {
						maxNum = a;
					}
				}
			}
			
			// 시작: (0, 0)에서 우향(dir 0)
			int curX = 0;
			int curY = 0;
			int curDir = 0;
			int dirCnt = 0;
			
			for(int target=1; target<=maxNum; target++) {
				int[] res = bfs(curX, curY, curDir, target);
				
				curX = res[0];
				curY = res[1];
				curDir = res[2];
				dirCnt += res[3];
			}
			
			sb.append(dirCnt).append("\n");
		}
		System.out.print(sb);
	}
	static int[] bfs(int x, int y, int d, int target) {
		dist = new int[n][n][4];
		
		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				Arrays.fill(dist[i][j], Integer.MAX_VALUE);
		
		Deque<int[]> q = new ArrayDeque<>();
		dist[x][y][d] = 0;
		q.offerLast(new int[]{x, y, d, 0});
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int r = cur[0];
			int c = cur[1];
			int dir = cur[2];
			int cnt = cur[3];
			
			if(cnt > dist[r][c][dir]) continue;
			
			// 목표 숫자 도착
			if(graph[r][c] == target) {
				return new int[]{r, c, dir, cnt};
			}
			
			int nx = r + dx[dir];
			int ny = c + dy[dir];
			
			// 전진 (비용 0)
			if(nx >= 0 && nx < n && ny >= 0 && ny < n && cnt < dist[nx][ny][dir]) {
				dist[nx][ny][dir] = cnt;
				q.offerFirst(new int[]{nx, ny, dir, cnt});
			}
			
			// 우회전 (비용 1)
			int nd = (dir + 1) % 4;
			if(cnt + 1 < dist[r][c][nd]) {
				dist[r][c][nd] = cnt + 1;
				q.offerLast(new int[]{r, c, nd, cnt + 1});
			}
		}
		return new int[]{x, y, d, 0};
	}
}
