package algorithm;
import java.util.*;
import java.io.*;
/*
 * 1번부터 순서대로 다음 숫자를 밟아야 함. 방향이 회전할 때마다 카운팅
 * 
 * 1. 상태 = (r, c, dir, tg)  tg는 지금 찾고 있는 목표 숫자
 *    - 목표 숫자를 전역으로 두면 큐에 남은 옛 상태들이 깨짐 -> 상태에 포함
 *    - tg 차원이 곧 "2 먹으면 vis 초기화"를 대신함
 * 2. 0-1 BFS
 *    - 전진 = 비용 0 -> offerFirst
 *    - 회전 = 비용 1 -> offerLast, 시계/반시계 둘 다 1회
 * 3. tg == maxNum 인 칸을 pop하면 그때 cost가 정답
 */
public class Solution {
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int[][] graph;
	static int[][][][] dist;
	static int n, maxNum;
	static int startX, startY;
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
					
					if(a == 1) {
						startX = i;
						startY = j;
					}
					
					if(maxNum < a) {
						maxNum = a;
					}
				}
			}
			
			sb.append(bfs()).append("\n");
		}
		System.out.print(sb);
	}
	static int bfs() {
		dist = new int[n][n][4][maxNum + 2];
		
		for(int i=0; i<n; i++)
			for(int j=0; j<n; j++)
				for(int d=0; d<4; d++)
					Arrays.fill(dist[i][j][d], Integer.MAX_VALUE);
		
		Deque<int[]> q = new ArrayDeque<>();
		for(int d=0; d<4; d++) {
			dist[startX][startY][d][2] = 0;
			q.offerLast(new int[]{startX, startY, d, 2, 0});
		}
		
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int r = cur[0];
			int c = cur[1];
			int dir = cur[2];
			int tg = cur[3];
			int cost = cur[4];
			
			if(cost > dist[r][c][dir][tg]) continue;
			
			// 목표 숫자 도착
			if(graph[r][c] == tg) {
				if(tg == maxNum) return cost;
				
				int nt = tg + 1;
				if(cost < dist[r][c][dir][nt]) {
					dist[r][c][dir][nt] = cost;
					q.offerFirst(new int[]{r, c, dir, nt, cost});
				}
				continue;
			}
			
			int nx = r + dx[dir];
			int ny = c + dy[dir];
			
			// 전진
			if(nx >= 0 && nx < n && ny >= 0 && ny < n && cost < dist[nx][ny][dir][tg]) {
				dist[nx][ny][dir][tg] = cost;
				q.offerFirst(new int[]{nx, ny, dir, tg, cost});
			}
			
			// 회전 (시계)
			int cw = (dir + 1) % 4;
			if(cost + 1 < dist[r][c][cw][tg]) {
				dist[r][c][cw][tg] = cost + 1;
				q.offerLast(new int[]{r, c, cw, tg, cost + 1});
			}
			
			// 회전 (반시계)
			int ccw = (dir + 3) % 4;
			if(cost + 1 < dist[r][c][ccw][tg]) {
				dist[r][c][ccw][tg] = cost + 1;
				q.offerLast(new int[]{r, c, ccw, tg, cost + 1});
			}
		}
		return -1;
	}
}
