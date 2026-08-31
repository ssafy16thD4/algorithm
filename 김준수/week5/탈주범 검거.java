/*
전략
- BFS로 시간 L에 대해 visited 가능한 개수 찾기
- 각 터널의 타입을 어떻게 해결할지
    - dx dy를 int[8][4]로 만들어두고
    - dx[0] dy[0]은 비워두고
    - dx[1] dy[1]은 {-1, 1, 0, 0} {0, 0, -1, 1}
    - dx[2] dy[2]는 {-1, 1, 0, 0} {0, 0, 0, 0} 이런 식으로

시행착오
- 현재 파이프에서 다음 파이프로 이동 가능하면 이동하게 했는데, 두 파이프가 연결되어 있는지 검사를 해봐야 했음
    - ex) + | 인데 +에서 오른쪽으로 이동하는 경우
*/

import java.io.*;
import java.util.*;

public class SWEA1953 {
	static final int[][] dx = new int[][] {
		{},
		{-1, 1, 0, 0},
		{-1, 1, 0, 0},
		{0, 0, 0, 0},
		{-1, 0, 0, 0},
		{1, 0, 0, 0},
		{1, 0, 0, 0},
		{-1, 0, 0, 0}
	};
	static final int[][] dy = new int[][] {
		{},
		{0, 0, -1, 1},
		{0, 0, 0, 0},
		{0, 0, -1, 1},
		{0, 0, 0, 1},
		{0, 0, 0, 1},
		{0, 0, 0, -1},
		{0, 0, 0, -1}
	};
	
	static int[][] board;
	static int result;
	static int L;
	
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        for(int t = 1; t <= T; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            int R = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());
            L = Integer.parseInt(st.nextToken());
            
            board = new int[N][M];
            
            result = 0;
            
            for(int i = 0; i < N; i++) {
            	st = new StringTokenizer(br.readLine());
            	for(int j = 0; j < M; j++) {
            		board[i][j] = Integer.parseInt(st.nextToken());
            	}
            }
            
            bfs(R, C);
            sb.append("#").append(t).append(" ").append(result).append("\n");
        }
        System.out.print(sb);
    }
    
    private static void bfs(int x, int y) {
    	Queue<int[]> queue = new ArrayDeque<>();
    	int[][] dist = new int[board.length][board[0].length];
    	
    	queue.offer(new int[] {x, y});
    	dist[x][y] = 1;
    	result++;
    	
    	while(!queue.isEmpty()) {
    		int[] curr = queue.poll();
    		if(dist[curr[0]][curr[1]] == L) continue;
    		
    		int pipeType = board[curr[0]][curr[1]];
    		for(int i = 0; i < 4; i++) {
    			int nx = curr[0] + dx[pipeType][i];
    			int ny = curr[1] + dy[pipeType][i];
    			
    			if(!inRange(nx, ny) || dist[nx][ny] != 0 || board[nx][ny] == 0) continue;
    			if(!isConnect(curr[0], curr[1], nx, ny)) continue; // 파이프가 서로 연결되어 있지 않으면 스킵
    			
    			queue.offer(new int[] {nx, ny});
    			dist[nx][ny] = dist[curr[0]][curr[1]] + 1;
    			result++;
    		}
    	}
    }
    
    private static boolean inRange(int x, int y) {
    	return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
    }
    
    private static boolean isConnect(int x1, int y1, int x2, int y2) {
    	int pipeType1 = board[x1][y1];
    	int pipeType2 = board[x2][y2];
    	
    	boolean canGo1 = false;
    	boolean canGo2 = false;
    	
    	for(int i = 0; i < 4; i++) {
    		int nx1 = x1 + dx[pipeType1][i];
    		int ny1 = y1 + dy[pipeType1][i];
    		int nx2 = x2 + dx[pipeType2][i];
    		int ny2 = y2 + dy[pipeType2][i];
    	
    		if(nx1 == x2 && ny1 == y2) canGo1 = true;
    		if(nx2 == x1 && ny2 == y1) canGo2 = true;
    	}
    	
    	return (canGo1 && canGo2);
    }
}