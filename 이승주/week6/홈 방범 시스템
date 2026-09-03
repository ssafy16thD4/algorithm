package jinwoo.m09.swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA2117 {
	static int areaNum;
	//우하좌상 
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	
	static int houseInArea;
	static boolean[][] visited;
	static int mapSize;
	static int[][] map;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		
		int T = Integer.parseInt(in.readLine().trim());
		
		for(int t=0; t<T; t++) {
			StringTokenizer arrSt = new StringTokenizer(in.readLine().trim());
			
			mapSize = Integer.parseInt(arrSt.nextToken());
			int cost = Integer.parseInt(arrSt.nextToken());
			
			map = new int[mapSize][mapSize];
			int houseNum = 0;
			
			for(int i=0; i<mapSize; i++) {
				StringTokenizer mapSt = new StringTokenizer(in.readLine().trim());
				for(int j=0; j<mapSize; j++) {
					map[i][j] = Integer.parseInt(mapSt.nextToken());
					if(map[i][j] == 1) houseNum++;
				}
			}
			
			areaNum = 1;
			
			// 최대 서비스 구역 구하기
			while(true) {
				int areaSize = (areaNum * areaNum) + ((areaNum - 1) * (areaNum - 1));
				
				if(areaSize <= houseNum * cost) {
					areaNum++;
				} else {
					areaNum--;
					break;
				}
			}
			
			int result = -1;
			for(int k = areaNum; k >= 1; k--) {
				if(result != -1) break;
				int areaSize = (k * k) + ((k - 1) * (k - 1));
				for(int i=0; i<mapSize; i++) {
					for(int j=0; j<mapSize; j++) {
						houseInArea = 0;
						visited = new boolean[mapSize][mapSize];
						visited[i][j] = true;
						if(map[i][j]==1)houseInArea++;
						dfs(i,j, i, j, k);
						if(areaSize <= houseInArea * cost) {
							if(result < houseInArea) {
								result = houseInArea;
							}
						}
					}
				}
			}
			
			sb.append("#").append(t+1).append(" ").append(result).append("\n");
		}
		System.out.println(sb);
	}
	// x좌표 - x좌표  + y좌표 - y좌표 의 절대값이 <= areaNum 구역만 탐색하면 될듯?? dfs로??백트래킹 X
	static public void dfs(int r, int c, int x, int y, int areaN) {
		
		for(int i=0; i<4; i++) {
			int nr = r + dx[i];
			int nc = c + dy[i];
			
			if(!inRange(nr, nc)) continue;
			if(visited[nr][nc]) continue;
			if((Math.abs(nr - x) + Math.abs(nc - y)) > areaN)continue;
			
			visited[nr][nc] = true;
			
			if(map[nr][nc] == 1) houseInArea++;
			
			dfs(nr, nc, x, y, areaN);
		}
	}
	
	static boolean inRange(int r, int c) {
		return (r>=0 && r<mapSize && c>=0 && c<mapSize);
	}

}
