package practive;
import java.util.*;
import java.io.*;
/*
 * 핀볼로 얻을 수 있는 점수의 최대값 구하기
 * 
 * 알고리즘: DFS
 * 
 * 1. 출발지점을 임의로 선정합니다.
 * 2. DFS
 *  2.1 다음 칸이 블록일 경우
 *   2.1.1 수평/수직면일 경우 점수카운트++ 반대 방향으로 돌아옴 
 *   2.1.2 경사면일 경우 직각으로 방향이 꺾임 점수카운트++
 *   2.1.3 벽일 경우 다시 돌아옴 
 *  2.2 다음 칸이 웜홀일 경우 동일한 숫자를 가진 다른 웜홀로 나옴
 *  2.3 다음 칸이 블랙홀일 경우: 게임 종료
 *  2.4 다음 칸이 출발 위치일 경우: 게임 종료
 * 3. 최대 점수 출력
 */
class Main {
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int[][] graph;
    static int n;
    static int maxScore;
    static int startX, startY;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=t; test_case++) {
            n = Integer.parseInt(br.readLine().trim());

            graph = new int[n][n];
            
            for(int i=0; i<n; i++) {
            	st = new StringTokenizer(br.readLine());
            	for(int j=0; j<n; j++) {
            		graph[i][j] = Integer.parseInt(st.nextToken());
            	}
            }
            
            for(int i=0; i<n; i++) {
            	for(int j=0; j<n; j++) {
            		// 블록, 웜홀, 블랙혹 출발지점 불가능
            		if(1 <= graph[i][j] && graph[i][j] <= 10) continue;
            		if(graph[i][j] == -1) continue;
            		maxScore = 0;
            		startX = i;
            		startY = j;
            		dfs(i, j, 0, 0);
            	}
            }
            sb.append("#").append(test_case).append(" ").append(maxScore).append("\n");
        }
        System.out.print(sb);
    }
    // 현재 내방향, 현재 내점수
    static void dfs(int x, int y, int dir, int score) {
    /*  
	 *  점수: 벽이나 블록에 부딪히면 점수 카운트
     * 
     *  2.1 다음 칸이 블록일 경우: o
     *   2.1.1 수평/수직면일 경우: 점수카운트++ 반대 방향으로 돌아옴 
     *   2.1.2 경사면일 경우: 직각으로 방향이 꺾임 점수카운트++
     *  2.2 다음 칸이 벽일 경우: 다시 돌아옴 o 점수카운트++
     *  2.3 다음 칸이 웜홀 경우: 동일한 숫자를 가진 다른 웜홀로 나옴
     *  2.4 다음 칸이 블랙홀일 경우: 게임 종료
     *  2.5 다음 칸이 출발 위치일 경우: 게임 종료
     */
    	System.out.println("x: " + x + " y: " + y + " dir: " + dir + " score: " + score);
    	int nx = x + dx[dir];
    	int ny = y + dy[dir];
    	
    	// 2.2 다음 칸이 벽일 경우 다시 돌아옴 
    	if(nx < 0 || nx >= n || ny < 0 || ny >= n) {
    		// 다음 칸이 벽이면 원래 왔던 방향을 바꾸고 점수카운트
    		// 우<->좌 하<->상
    		// 0<->2 1<->3
    		dir = (dir + 2) % 4;
    	}

    	// 2.4 다음 칸이 블랙홀일 경우: 게임 종료
    	else if(graph[nx][ny] == -1) {
    		maxScore = Math.max(maxScore, score);
    		return;
    	}
    	
//      2.5 다음 칸이 출발 위치일 경우: 게임 종료
    	else if(graph[nx][ny] == graph[startX][startY]) {
    		maxScore = Math.max(maxScore, score);
    		return;
    	}
    	
    	// 2.2 다음 칸이 벽일 경우 다시 돌아옴 
    	if(nx < 0 || nx >= n || ny < 0 || ny >= n) {
    		// 다음 칸이 벽이면 원래 왔던 방향을 바꾸고 점수카운트
    		// 우<->좌 하<->상
    		// 0<->2 1<->3
    		dir = (dir + 2) % 4;
    	}
    	
    	// 2.1 다음 칸이 블록일 경우
    	else if(1 <= graph[nx][ny] && graph[nx][ny] <= 5) {
    		if(graph[nx][ny] == 1) {
    			// dir이 우상일 경우 돌아옴
    			if(dir == 0 || dir == 3) {
    				dir = (dir+2) % 4;
    			}
    			else { // dir이 우상이 아닐경우
	    			// dir이 좌일때 상으로 2->3
    				if(dir == 2) dir = (dir + 1) % 4;
	    			// dir이 하일때 우로 1->0
    				if(dir == 1) dir = dir - 1;
    			}
    		}
    		else if(graph[nx][ny] == 2) { 
    			// dir이 우하일 경우 돌아옴
    			if(dir == 0 || dir == 1) {
    				dir = (dir + 2) % 4;
    			}
    			else {
    			// dir이 우하가 아닐경우 
	    			// dir이 좌일때 하
    				if(dir == 2) dir = dir - 1;
	    			// dir이 상일때 우
    				if(dir == 3) dir = (dir + 1) % 4;
    			}
    		}
    		else if(graph[nx][ny] == 3) {
    			// dir이 좌하일 경우 돌아옴
    			if(dir == 1 || dir == 2) {
    				dir = (dir + 2) % 4;
    			}
    			// dir이 좌하가 아닐경우
    			else {
    				// dir이 우 -> 하 
    				if(dir == 0) dir = (dir + 1) % 4;
    				// dir이 상 -> 좌
    				if(dir == 3) dir = dir - 1;
    			}
    		}
    		else if(graph[nx][ny] == 4) {
    			// dir이 좌상일 경우 돌아옴
    			if(dir == 3 || dir == 4) {
    				dir = (dir + 2) % 4;
    			}
    			// dir이 좌상이 아닐경우
    			else {
    				// dir이 우 -> 상
    				if(dir == 0) dir = (dir + 3) % 4;
    				// dir이 하 -> 좌
    				if(dir == 1) dir = (dir + 1) % 4;
    			}
    		}
    		else if(graph[nx][ny] == 5) {
    			// 모든방향이 돌아옴
    			dir = (dir + 2) % 4;
    		}
    		dfs(nx, ny, dir, score+1);
    	}
    	
//      2.3 다음 칸이 웜홀 경우: 동일한 숫자를 가진 다른 웜홀로 나옴
    	else if(6 <= graph[nx][ny] && graph[nx][ny] <= 10) {
    		// 현재 웜홀에 있는 수와 같은 다른 웜홀수로 이동함
    		return;
    	}
    }
}
