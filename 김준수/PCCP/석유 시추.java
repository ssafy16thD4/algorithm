/*
BFS
각 열에 시추관을 넣었을 때 석유의 수를 반환하는 bfs 함수
입력으로 열 번호를 받아서 해당 열의 모든 좌표를 큐에 넣고 BFS 시작
BFS 종료 후 oilCnt를 리턴해서 최대값 갱신

*/

import java.util.*;

class Solution {
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static int[][] board;
    static int result;
    
    public int solution(int[][] land) {
        board = land;
        result = 0;
        
        for(int i = 0; i < land[0].length; i++){
            bfs(i);
        }
        
        return result;
    }
    
    private void bfs(int col){
        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[board.length][board[0].length];
        int oilCnt = 0;
        
        for(int i = 0; i < board.length; i++){
            if(board[i][col] == 1){
                queue.offer(new int[]{i, col});
                visited[i][col] = true;
                oilCnt++;
            }
        }
        
        while(!queue.isEmpty()){
            int[] curr = queue.poll();
            
            for(int i = 0; i < 4; i++){
                int nx = curr[0] + dx[i];
                int ny = curr[1] + dy[i];
                
                if(!inRange(nx, ny)) continue;
                
                if(board[nx][ny] == 0 || visited[nx][ny]) continue;
                
                queue.offer(new int[]{nx, ny});
                visited[nx][ny] = true;
                oilCnt++;
            }
        }
        // System.out.println((col + 1) + "번 열 " + oilCnt);
        
        result = Math.max(oilCnt, result);
    }
    
    private boolean inRange(int x, int y){
        return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
    }
}