package algorithm;

import java.util.*;
import java.io.*;

/*
 * 모든 섬을 연결하는 다리 길이의 최소값
 * 불가능하면 -1 출력
 * 
 * 알고리즘: BFS, 최소신장트리(MST), 크루스칼
 * 
 * 섬과 섬사이의 최단거리를 구한다.
 * 구한 값으로 섬과 섬사이를 잇는 모든 다리 길이의 최소값을 구한다.
 * 
 * 1. 맵이 1인지점을 BFS로 모두 색칠
 * 2. 길이 2이상인 부분을 노드, 다리길이 정보 저장
 * 3. 다리길이가 짧은 것부터 조회하며 크루스칼로 모든 섬 연결
 * 
 * 어떻게 만들면 좋을지 까진 생각 ok
 * 구현 전혀 실패 Gpt 옮기기..
 */
public class Solution {
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0};

	static int[][] graph;
	static boolean[][] vis;
	static int[] p, s;
	static List<Edge> edges = new ArrayList<>();
	static int n, m;
	static int islandCnt;

	static class Edge implements Comparable<Edge> {
		int u, v, w;

		Edge(int u, int v, int w) {
			this.u = u;
			this.v = v;
			this.w = w;
		}

		@Override
		public int compareTo(Edge o) {
			return Integer.compare(this.w, o.w);
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		st = new StringTokenizer(br.readLine());
    
		n = Integer.parseInt(st.nextToken()); // 세로
		m = Integer.parseInt(st.nextToken()); // 가로

		graph = new int[n][m];
		vis = new boolean[n][m];

		for(int i=0; i<n; i++) {

			st = new StringTokenizer(br.readLine());

			for(int j=0; j<m; j++) {
				graph[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		islandCnt = 0;

		for(int i=0; i<n; i++) {
			for(int j=0; j<m; j++) {
				if (graph[i][j] == 1 && !vis[i][j]) {
					islandCnt++;
					bfs(i, j, islandCnt + 1);
				}
			}
		}

		buildingEdges();
		make();
		int answer = kruskal();

		System.out.println(answer);
	}

	static void bfs(int x, int y, int label) {
		Queue<int[]> q = new ArrayDeque<>();
		q.offer(new int[]{x, y});
		vis[x][y] = true;
		graph[x][y] = label;
		while(!q.isEmpty()) {
			int[] cur = q.poll();
			for(int dir=0; dir<4; dir++) {
				int nx = cur[0] + dx[dir];
				int ny = cur[1] + dy[dir];
				if(nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
				if(!vis[nx][ny] && graph[nx][ny] == 1) {
					q.offer(new int[]{nx, ny});
					vis[nx][ny] = true;
					graph[nx][ny] = label;
				}
			}
		}
	}

	static void make() {
		p = new int[islandCnt + 2];
		s = new int[islandCnt + 2];

		for(int i=2; i<=islandCnt + 1; i++) {
			p[i] = i;
			s[i] = 1;
		}
	}

	/*
     * 다리 후보 생성
     *
     * 각 섬의 모든 칸에서
     * 4방향으로 쭉 가본다.
     *
     * 바다를 2칸 이상 지나서 다른 섬을 만나면
     * 다리 후보로 등록한다.
     */
	static void buildingEdges() {
		int[][] lens = new int[islandCnt + 2][islandCnt + 2];

		for (int i = 0; i < lens.length; i++) {
			Arrays.fill(lens[i], Integer.MAX_VALUE);
		}

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (graph[i][j] < 2) continue;

				int from = graph[i][j];

				for (int dir = 0; dir < 4; dir++) {
					int nx = i + dx[dir];
					int ny = j + dy[dir];

					int length = 0;
					while (nx >= 0 && nx < n && ny >= 0 && ny < m) {
						if (graph[nx][ny] == from) { break; }
						if (graph[nx][ny] >= 2) {
							int to = graph[nx][ny];

							if (length >= 2) {
								lens[from][to] = Math.min(lens[from][to], length);
							}

							break;
						}

						length++;

						nx += dx[dir];
						ny += dy[dir];
					}
				}
			}
		}

		for (int i = 2; i <= islandCnt + 1; i++) {
			for (int j = i + 1; j <= islandCnt + 1; j++) {
				if (lens[i][j] != Integer.MAX_VALUE) {
					edges.add(new Edge(i, j, lens[i][j])
					);
				}
			}
		}
	}

	static int find(int x) {
		if(p[x] == x) return x;
		return p[x] = find(p[x]);
	}

	private static boolean union(int a, int b) {
		int ra = find(a);
		int rb = find(b);

		if (ra == rb) return false;
		if (s[ra] < s[rb]) {
			int t = ra;
			ra = rb;
			rb = t;
		}
		p[rb] = ra;
		s[ra] += s[rb];

		return true;
	}

	private static int kruskal() {
		Collections.sort(edges);

		int mstCost = 0;
		int usedEdges = 0;

		for (Edge e : edges) {
			if (union(e.u, e.v)) {
				mstCost += e.w;
				if (++usedEdges == islandCnt - 1) {
					return mstCost;
				}
			}
		}

		return -1;
	}
}
