package jinwoo.m09.A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.StringTokenizer;

/**
 * 전략
 * 1. 각 섬마다 번호매기기
 * 2. 전 맵을 순회하면서 만들어질 수 있는 다리 완전탐색
 *  2-1. 다리는 객체로 저장(출발지, 도착지, 다리길이)
 *  2-2. 다리를 만들때마다 List에 저장(출발지, 도착지가 같은 다리가 있다면 길이를 비교 후, 짧은것으로 갱신)
 * 3. 다리를 기준으로 dfs (섬 개수 - 1)개의 다리설치 필요
 * 	3-1. (섬개수-1)개의 다리를 뽑는 모든 경우의 수 탐색
 * 	3-2. union find로 다 연결되어있는지 확인
 * 	3-3. 다 연결되어있다면 다리 길이 answer에 담기
 * 	3-4. 최솟값으로 갱신
 */
public class 다리만들기2 {
	
	static int[] parent;
	static int[][] map;
	static int mapR;
	static int mapC;
	// 우하좌상
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	static int islandNum;
	static boolean[][] visited;
	static List<Bridge> bridgeList;
	static boolean[] dfsVisited;
	static int answer;
	static List<Bridge> selecBridgeList;
	
	static class Bridge{
		int start;
		int finish;
		int len;
		
		public Bridge(int start, int finish, int len) {
			this.start = start;
			this.finish = finish;
			this.len = len;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		StringTokenizer st = new StringTokenizer(in.readLine().trim());
		mapR = Integer.parseInt(st.nextToken());
		mapC = Integer.parseInt(st.nextToken());
		
		map = new int[mapR][mapC];
		
		for(int i=0; i<mapR; i++) {
			st = new StringTokenizer(in.readLine().trim());
			for(int j=0; j<mapC; j++) {
				map[i][j] = Integer.parseInt(st.nextToken());
			}
		}
		
		islandNum = 0;
		visited = new boolean[mapR][mapC];
		
		// 섬마다 번호매기기
		for(int i=0; i<mapR; i++) {
			for(int j=0; j<mapC; j++) {
				if(map[i][j] == 0 || visited[i][j]) continue;
				visited[i][j] = true;
				islandNum++;
				bfs(i,j);
			}
		}
		
		bridgeList = new ArrayList<>();
		// 다리만들기
		// 만들어진 다리는 List에 담기, 같은 섬끼리의 다리가 있을경우 길이 적은것으로 갱신
		for(int i=0; i<mapR; i++) {
			for(int j=0; j<mapC; j++) {
				if(map[i][j] == 0) continue;		
				buildBridge(i,j);
			}
		}
		
		// 다리 경우의 수 뽑기
		dfsVisited = new boolean[bridgeList.size()];
		selecBridgeList = new ArrayList<>();
		answer = -1;
		dfs(0, 0, 0);
		
		System.out.println(answer);
	}
	
	static public void dfs(int start, int depth, int len) {
		
		if(answer != -1 && answer <= len) return;
		
		if(depth == islandNum - 1) {
			
			parent = new int[islandNum+1];
			
			for(int i=0; i<=islandNum; i++) {
				parent[i] = i;
			}
			
			for(int i=0; i<selecBridgeList.size(); i++) {
				Bridge bg = selecBridgeList.get(i);
				union(bg.start, bg.finish);
			}
			
			int root = find(1);
			for(int i=2; i<parent.length; i++) {
				if(find(i) != root) return;
			}
			if(answer == -1 || answer > len) answer = len;
			return;
		}
		
		for(int i=start; i<bridgeList.size(); i++) {
			if(dfsVisited[i]) continue;
			
			dfsVisited[i] = true;
			Bridge bg = bridgeList.get(i);
			selecBridgeList.add(bg);
			
			dfs(i+1, depth+1, len+bg.len);
			
			selecBridgeList.remove(selecBridgeList.size() - 1);
			dfsVisited[i] = false;
		}
	}
	
	static public void buildBridge(int r, int c) {
		for(int i=0; i<4; i++) {
			int start = map[r][c];
			int finish = 0;
			boolean isFinish = false;
			int nx = r;
			int ny = c;
			int bridgeLen = 0;
			while(!isFinish) {
				nx += dx[i];
				ny += dy[i];
				
				if(!inRange(nx, ny)) break;
				if(map[nx][ny] == start) break;
				if(map[nx][ny] != 0) {
					if(bridgeLen < 2) break;
					isFinish = true;
					finish = map[nx][ny];
				} else {
					bridgeLen++;
				}
			}
			
			if(isFinish) {
				if(start>finish) {
					int n = start;
					start = finish;
					finish = n;
				}
				
				Bridge newBridge = new Bridge(start, finish, bridgeLen);
				
				if(bridgeList.isEmpty()) {
					bridgeList.add(newBridge);
				} else {
					boolean isBuild = false;
					for(int j=0; j<bridgeList.size(); j++) {
						Bridge bridge = bridgeList.get(j);
						if(bridge.start == start && bridge.finish == finish) {
							isBuild = true;
							if(bridgeLen < bridge.len) bridge.len = bridgeLen;
						}
					}
					if(!isBuild) bridgeList.add(newBridge);
				}
			}
		}
	}
	
	static public void bfs(int r, int c) {
		Deque<int[]> dq = new ArrayDeque<int[]>();
		dq.offer(new int[] {r,c});
		map[r][c] = islandNum;
		
		while(!dq.isEmpty()) {
			int[] loc = dq.poll();
			for(int i=0; i<4; i++) {
				int nx = loc[0] + dx[i];
				int ny = loc[1] + dy[i];
				
				if(!inRange(nx, ny)) continue;
				if(visited[nx][ny]) continue;
				if(map[nx][ny] != 1) continue;
				
				visited[nx][ny] = true;
				map[nx][ny] = islandNum;
				dq.offer(new int[] {nx,ny});
			}
		}
	}
	
	static boolean inRange(int r, int c) {
		return (r>=0 && r < mapR && c >= 0 && c < mapC);
	}
	
	static public void union (int x, int y) {
		int a = find(x);
		int b = find(y);
		
		if(a != b) {
			parent[b] = a;
		}
	}
	
	static public int find(int x) {
		
		if(parent[x] == x) {
			return x;
		}
		
		return parent[x] = find(parent[x]);
	}
}
