package algorithm;
import java.util.*;
import java.io.*;
/*
 * 1번부터 다음 숫자들을 지나가야함. 방향이 회전할 때마다 카운팅
 * 
 * 1. bfs(int x, int y, int d)
 * 1.1 가장 큰 수의 인덱스를 만나면 return;
   1.2 1을 향해 이동하면서 방향을 회전할때마다 카운팅
 * 2. 카운팅 갯수 출력
 */
public class Solution {
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int[][] graph;
	static boolean[][][] vis;
	static int n;
	static int num;
	static int maxNum;
	static int dirCnt;
	static int endX, endY;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			n = Integer.parseInt(br.readLine());
			
			graph = new int[n][n];
			vis = new boolean[n][n][4];
			
			for(int i=0; i<n; i++) {
				String str = br.readLine();
				for(int j=0; j<str.length(); j++) {
					int a = str.charAt(j) - '0';
					graph[i][j] = a;
					if(maxNum < a) {
						maxNum = a;
						endX = i;
						endY = j;
					}
				}
			}
			
			num = 1;
			
			sb.append(num).append("\n");
		}
		System.out.print(sb);
	}
	static void bfs(int x, int y, int d) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[]{x, y, d, 0});
		vis[x][y][d] = true;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			int r = cur[0];
			int c = cur[1];
			int dir = cur[2];
			int cnt = cur[3];
			
			int nx = r + dx[dir];
			int ny = c + dy[dir];
			System.out.println("nx: " + nx + " ny: " + ny);
			if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
			if(vis[nx][ny][dir]) continue;
			if(graph[nx][ny] == num) {
				vis[nx][ny][dir] = true;
				q.offer(new int[]{nx, ny, dir, cnt});
				num++;
			}
			
			if(nx == endX && ny == endY) {
				vis[nx][ny][(dir + 1) % 4] = true;
				q.offer(new int[]{nx, ny, dir, cnt});
				return;
			}
		}
	}
}
