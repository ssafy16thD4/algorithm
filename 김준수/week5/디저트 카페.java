/*
전략
- 대각선 이동을 dx dy로 똑같이 구현 가능. inRange도 마찬가지
    - 반드시 {1, 1, -1, -1}, {1, -1, -1, 1} 순으로 이동함
- 한쪽 방향으로 방문하다가 끝을 만나거나 이미 방문한 숫자를 만나면 다음 방향으로 꺾음
- 최종적으로 기존 시작 지점에 도달하면 결과 갱신

시행착오
- 첫 지점을 visited true 해버리니까 최종적으로 기존 시작 지점에 도달을 못하는 문제가 있었음
    - 전역변수 start를 따로 둬서 해결
*/

package algorithm;

import java.io.*;
import java.util.*;

public class SWEA2105 {
	static final int[] dx = new int[] {1, 1, -1, -1};
	static final int[] dy = new int[] {1, -1, -1, 1};
	
	static int[][] board;
	static int[] start;
	static int result;
	static boolean[][] visited;
	static List<Integer> cafes;
	
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        for(int t = 1; t <= T; t++) {
        	int N = Integer.parseInt(br.readLine().trim());
        	board = new int[N][N];
        	start = new int[2];
        	visited = new boolean[N][N];
        	cafes = new LinkedList<>();
        	result = -1;
        	
        	for(int i = 0; i < N; i++) {
        		StringTokenizer st = new StringTokenizer(br.readLine());
        		for(int j = 0; j < N; j++) {
        			board[i][j] = Integer.parseInt(st.nextToken());
        		}
        	}
            
            for(int i = 0; i < N - 2; i++) {
            	for(int j = 1; j < N - 1; j++) {
            		visited[i][j] = true;
            		cafes.add(board[i][j]);
            		start[0] = i;
            		start[1] = j;
            		
            		dfs(i, j, 0, 0);
            		
            		visited[i][j] = false;
            		cafes.clear();
            	}
            }
            
            sb.append("#").append(t).append(" ").append(result).append("\n");
        }
        System.out.print(sb);
    }
    
    private static boolean inRange(int x, int y) {
    	return (x >= 0 && x < board.length && y >= 0 && y < board.length);
    }
    
    private static void dfs(int x, int y, int depth, int dir) {
    	if(dir == 4) {
    		if(x == start[0] && y == start[1]) {
    			if(result < depth) result = depth;
    		}
    		return;
    	}
    	
    	List<int[]> newCafes = new ArrayList<>(); // 이번 이동에 새로 방문한 카페
    	int cnt = 0; // 이번에 이동한 횟수
    	
    	while(true) {
    		int nx = x + dx[dir];
    		int ny = y + dy[dir];
    		
    		if(nx == start[0] && ny == start[1]) {
    			if(dir == 3) {
    				dfs(nx, ny, depth + cnt + 1, dir + 1);
    			}
    			break;
    		}
    		
    		if(!inRange(nx, ny)) break;
    		if(visited[nx][ny] || cafes.contains(board[nx][ny])) break;
    		
    		x = nx;
    		y = ny;
    		cnt++;
    		
    		visited[x][y] = true;
    		cafes.add(board[x][y]);
    		newCafes.add(new int[]{x, y});
    		
    		dfs(x, y, depth + cnt, dir + 1);
    	}
    	
    	// 백트래킹 복원
    	for(int[] p : newCafes) {
    		visited[p[0]][p[1]] = false;
    	}
    	for(int i = 0; i < newCafes.size(); i++) {
    		cafes.remove(cafes.size() - 1);
    	}
    }
}