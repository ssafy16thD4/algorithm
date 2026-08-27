import java.io.*;
import java.util.*;
/*
	배치 t ~ t+X : 비활성
	t+X : 활성화
	t+X+1 : 이 시점에 자식이 태어남 (활성화 후 첫 1시간 동안 번식)
	t+2X : 사망

	1. 활성화 시각만 이벤트로 관리 -> 매 시각 격자 전체 순회 회피
	2. 충돌은 if가 아니라 pq 정렬로 흡수 (시각 오름차순, 생명력 내림차순)
	3. 음수 인덱스는 offset K로 흡수
	4. 정답 = 총 생성 수 - 사망 수
	   단, 태어날 시각이 K를 넘으면 생성 자체를 하지 않는다
*/
public class Solution {
	static int size;
	static int[][] graph;
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, -1, 0, 1};
	static class Cell implements Comparable<Cell> {
		int r, c, life, time;

		Cell(int r, int c, int life, int time) {
			this.r = r;
			this.c = c;
			this.life = life;
			this.time = time;
		}
		@Override
		public int compareTo(Cell o) {
			if (this.time != o.time) {
				return Integer.compare(this.time, o.time);
			}
			return Integer.compare(o.life, this.life);
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine().trim());
		for (int test = 1; test <= t; test++) {
			st = new StringTokenizer(br.readLine());
			int n = Integer.parseInt(st.nextToken());
			int m = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());
			size = Math.max(n, m) + 2 * k + 2;
			graph = new int[size][size];

			PriorityQueue<Cell> pq = new PriorityQueue<>();
			int total = 0;
			int dead = 0;
			for (int i = 0; i < n; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < m; j++) {
					int x = Integer.parseInt(st.nextToken());
					if (x == 0) continue;
					int r = k + i;
					int c = k + j;
					graph[r][c] = x;
					pq.add(new Cell(r, c, x, x));
					total++;
				}
			}
			for (int time = 1; time <= k; time++) {
				while (!pq.isEmpty() && pq.peek().time == time) {
					Cell cur = pq.poll();
					if (cur.time + cur.life <= k) {
						dead++;
					}
					// 자식은 time+1에 태어남, K를 넘으면 아예 생성 안 함
					if (time + 1 > k) continue;

					for (int d = 0; d < 4; d++) {
						int nx = cur.r + dx[d];
						int ny = cur.c + dy[d];
						if (nx < 0 || nx >= size || ny < 0 || ny >= size) continue;
						if (graph[nx][ny] != 0) continue;
						graph[nx][ny] = cur.life;
						pq.add(new Cell(nx, ny, cur.life, time + 1 + cur.life));
						total++;
					}
				}
			}

			sb.append("#").append(test).append(" ").append(total - dead).append("\n");
		}
		System.out.print(sb);
	}
}
