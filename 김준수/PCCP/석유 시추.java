/*
[8, [0, 2]], [7, [3, 6]], [2, [6, 7]]
이런 식으로 각 석유 덩어리에 대해 [석유 수, [시작 열, 종료 열]]로 정리(BFS 활용)
각 열 0~N에 대해서 각 석유 덩어리를 돌면서 이 덩어리에 닿을 수 있는지 체크 후 더하기
각 열의 결과를 최종 결과와 비교해서 갱신
*/

import java.util.*;

class Solution {
    static final int[] dx = {0, 1, 0, -1};
    static final int[] dy = {1, 0, -1, 0};
    static int[][] board;
    static int result;
    static List<Oil> oilList;
    static boolean[][] visited;
    
    class Oil {
        int cnt;
        int start;
        int end;
        
        Oil(int cnt, int start, int end){
            this.cnt = cnt;
            this.start = start;
            this.end = end;
        }
        
        public int get(int col){
            if(start <= col && col <= end){
                return cnt;
            }
            return 0;
        }
    }
    
    public int solution(int[][] land) {
        board = land;
        result = 0;
        oilList = new ArrayList<>();
        visited = new boolean[land.length][land[0].length];
        
        for(int i = 0; i < land.length; i++){
            for(int j = 0; j < land[0].length; j++){
                if(board[i][j] == 1 && !visited[i][j]){
                    bfs(i, j);
                }
            }
        }
        
        int[] cols = new int[land[0].length];
        for(Oil o : oilList){
            for(int i = o.start; i <= o.end; i++){
                cols[i] += o.cnt;
            }
        }
        result = cols[0];
        for(int i = 1; i < land[0].length; i++){
            if(result < cols[i]) result = cols[i];
        }
        
        return result;
    }
    
    private void bfs(int x, int y){
        int cnt = 1;
        int start = y;
        int end = y;
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{x, y});
        visited[x][y] = true;
        
        while(!queue.isEmpty()){
            int[] curr = queue.poll();
            
            for(int i = 0; i < 4; i++){
                int nx = curr[0] + dx[i];
                int ny = curr[1] + dy[i];
                
                if(!inRange(nx, ny)) continue;
                if(board[nx][ny] == 0 || visited[nx][ny]) continue;
                
                queue.offer(new int[]{nx, ny});
                visited[nx][ny] = true;
                cnt++;
                
                start = Math.min(start, ny);
                end = Math.max(end, ny);
            }
        }
        
        oilList.add(new Oil(cnt, start, end));
    }
    
    private boolean inRange(int x, int y){
        return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
    }
}