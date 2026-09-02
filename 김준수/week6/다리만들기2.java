/*
전략
- 각 섬과 섬 사이의 최단 거리를 찾는다
    - getDist(Island A, Island B) : 큐에 A의 모든 섬을 넣고 B섬에 도달할때까지의 거리를 BFS로 계산. 다음 노드로 갈때는 방향을 바꿀 수 없음. 만약 두 섬이 연결 불가능하다면 -1
- 각 섬들을 연결하는 최소신장트리를 만든다
    - Edge 클래스에 u, v, cost 저장
    - 모든 섬 간에 대해 Edge를 만들어서 PQ에 저장. -1이면 안넣음
    - 작은 Edge부터 꺼내서 유니온

시행착오
- 섬과 섬 사이의 거리를 찾을 때 BFS로 한쪽 방향으로만 가게, 섬의 모든 좌표에 대해 4방향으로 탐색함
    - 이 경우 십자가 섬이 있을 때 예외가 발생함. 실패코드 getDistBfs
    - 탐색 로직은 동일하나, visited를 섬의 모든 좌표가 공유하는게 아니라 한 좌표에 대해서만 적용한 getDist 함수 추가 구현
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 다리만들기2 {
	static final int[] dx = new int[] { 0, 1, 0, -1 };
	static final int[] dy = new int[] { 1, 0, -1, 0 };

	static int N;
	static int M;
	static int[][] board;
	static boolean[][] visited;
	static List<Island> islandList;
	static List<Edge> edgeList;
	static int[] parent;

	static class Island {
		List<int[]> pos;
		int islandNum;

		Island(int islandNum, List<int[]> pos) {
			this.islandNum = islandNum;
			this.pos = pos;
		}
	}

	static class Edge implements Comparable<Edge> {
		int u, v, cost;

		Edge(int u, int v, int cost) {
			this.u = u;
			this.v = v;
			this.cost = cost;
		}

		@Override
		public int compareTo(Edge e) {
			return Integer.compare(this.cost, e.cost);
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st = new StringTokenizer(br.readLine().trim());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		board = new int[N][M];
		visited = new boolean[N][M];
		edgeList = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine().trim());
			for (int j = 0; j < M; j++) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		// 섬들 찾아서 추가
		int cnt = 1;
		islandList = new ArrayList<>();
		islandList.add(null); // 섬 번호 유지용
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (board[i][j] == 1 && !visited[i][j]) {
					islandList.add(new Island(cnt, findIslandBfs(i, j, cnt)));
					cnt++;
				}
			}
		}

		// 각 섬 간의 최단 거리 구하기
		for (int i = 1; i < islandList.size(); i++) {
			for (int j = i + 1; j < islandList.size(); j++) {

				// 시작 섬의 좌표에서 직선으로 최단거리 찾기
				int minDist = Integer.MAX_VALUE;
				for (int[] p : islandList.get(i).pos) {
					for (int k = 0; k < 4; k++) {
						int dist = getDist(p, k, j);
						if (dist != -1 && dist < minDist)
							minDist = dist;
					}
				}

				if (minDist != Integer.MAX_VALUE) {
					edgeList.add(new Edge(i, j, minDist));
				}
			}
		}
		Collections.sort(edgeList);

		// 최소 신장 트리 만들기
		int result = kruskal();

		sb.append(result);
		System.out.print(sb);
	}

	// 섬을 표시하는 BFS
	private static List<int[]> findIslandBfs(int x, int y, int islandNum) {
		List<int[]> result = new ArrayList<>();
		Queue<int[]> queue = new ArrayDeque<>();

		visited[x][y] = true;
		board[x][y] = islandNum;
		queue.offer(new int[] { x, y });
		result.add(new int[] { x, y });

		while (!queue.isEmpty()) {
			int[] curr = queue.poll();

			for (int i = 0; i < 4; i++) {
				int nx = curr[0] + dx[i];
				int ny = curr[1] + dy[i];

				if (!inRange(nx, ny) || visited[nx][ny] || board[nx][ny] == 0)
					continue;

				board[nx][ny] = islandNum;
				visited[nx][ny] = true;
				queue.offer(new int[] { nx, ny });
				result.add(new int[] { nx, ny });
			}
		}

		return result;
	}

	// 섬 간의 거리를 찾는 함수
	private static int getDist(int[] s, int dir, int endIdx) {
		int[][] dist = new int[N][M];
		int[] start = new int[] { s[0], s[1] };
		for (int[] p : islandList.get(endIdx).pos) {
			dist[p[0]][p[1]] = -1;
		}

		int result = -1;
		while (true) {
			int nx = start[0] + dx[dir];
			int ny = start[1] + dy[dir];

			// 해당 방향에 섬 없으면 중단
			if (!inRange(nx, ny))
				break;

			// 목표 섬 찾았으면 갱신 후 종료
			if (dist[nx][ny] == -1) {
				result = dist[start[0]][start[1]];
				break;
			}

			// 다른 섬에 막혔으면 중단
			if (board[nx][ny] >= 1)
				break;

			dist[nx][ny] = dist[start[0]][start[1]] + 1;
			start[0] = nx;
			start[1] = ny;
		}

		return result;
	}

	// 실패 코드
	// 섬 간의 거리를 찾는 BFS
	private static int getDistBfs(int a, int b) {
		int result = -1;
		int[][] dist = new int[N][M];
		Queue<int[]> queue = new ArrayDeque<>();

		for (int i = 0; i < islandList.get(a).pos.size(); i++) {
			int[] p = islandList.get(a).pos.get(i);
			// 섬의 각 땅에 대해 4방향으로 큐에 넣음
			// 어차피 안되는 방향은 빠르게 제거됨
			for (int j = 0; j < 4; j++) {
				queue.offer(new int[] { p[0], p[1], j });
			}
			dist[p[0]][p[1]] = -1;
		}

		for (int i = 0; i < islandList.get(b).pos.size(); i++) {
			int[] p = islandList.get(b).pos.get(i);
			dist[p[0]][p[1]] = -2;
		}

		boolean flag = false;
		while (!queue.isEmpty()) {
			int[] curr = queue.poll();
			int nx = curr[0] + dx[curr[2]];
			int ny = curr[1] + dy[curr[2]];
			
			// 범위 밖이거나 이미 방문했으면 스킵
			if (!inRange(nx, ny))
				continue;
			if (dist[nx][ny] != 0) {
				// 목표 섬이면 종료
				if (dist[nx][ny] == -2) {
					// 목표 섬이긴 한데 거리가 1이면 다른 거리도 테스트해야해서 스킵
					if (dist[curr[0]][curr[1]] == 1)
						continue;
					// 그 외에는 정상적으로 종료
					result = dist[curr[0]][curr[1]];
					flag = true;
					break;
				}
				continue;
			}

			if (flag) break;
			
			// 목표 섬 외 다른 섬을 만났으면 스킵
			if (board[nx][ny] >= 1)
				continue;

			// 원본 방향 그대로 유지해서 다음으로 진행
			queue.offer(new int[] { nx, ny, curr[2] });

			// 만약에 기존이 시작 섬이었으면 바로 1부터 시작
			if (dist[curr[0]][curr[1]] == -1) {
				dist[nx][ny] = 1;
			}
			else {
				dist[nx][ny] = dist[curr[0]][curr[1]] + 1;
			}
		}

		return result;
	}

	private static boolean inRange(int x, int y) {
		return (x >= 0 && x < N && y >= 0 && y < M);
	}

	private static int find(int x) {
		if (x == parent[x])
			return x;
		return parent[x] = find(parent[x]);
	}

	private static void union(int x, int y) {
		int rootX = find(x);
		int rootY = find(y);

		if (rootX != rootY) {
			parent[rootY] = rootX;
		}
	}

	private static int kruskal() {
		parent = new int[islandList.size()];
		int edgeCnt = 0;
		int costSum = 0;
		for (int i = 1; i < islandList.size(); i++) {
			parent[i] = i;
		}

		for (Edge e : edgeList) {
			if (e.cost < 2)
				continue;
			if (find(e.u) == find(e.v))
				continue;

			union(e.u, e.v);
			edgeCnt++;
			costSum += e.cost;

			if (edgeCnt == islandList.size() - 2) {
				break;
			}
		}

		if (edgeCnt != islandList.size() - 2)
			return -1;
		return costSum;
	}
}