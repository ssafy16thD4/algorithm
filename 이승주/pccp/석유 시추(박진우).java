class Solution {
    static int[][] land;
    static boolean[][] visited;
    static int[] getOilNum;
    static int oilNum;
    static int minX;
    static int maxX;
    // 우 아 왼 위
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    
    public int solution(int[][] land) {
        this.land = land;
        visited = new boolean[land.length][land[0].length];
        getOilNum = new int[land[0].length];
        // 전략, 하나씩 확인 1이 나오면 findOil실행하여 해당 열에 얻을 수 있는 oil 담기
        // 조건, 1이거나 방문하지 않은 곳이여야만 함
        for(int i=0; i<land.length; i++) {
            for(int j=0; j<land[0].length; j++) {
                if(land[i][j] == 0) {continue;}
                
                if(visited[i][j]) {continue;}
                
                oilNum = 0;
                minX = 100000000;
                maxX = 0;
                
                findOil(i, j);
                
                
                for(int k = minX; k <= maxX; k++) {
                    getOilNum[k] = getOilNum[k] + oilNum;
                }
            }
        }
        int answer = 0;
        
        for(int i=0; i<getOilNum.length; i++) {
            answer = Math.max(answer, getOilNum[i]);
        }
        
        return answer;
    }
    
    static void findOil(int r, int c) {
        
        if(visited[r][c]) return;
        
        visited[r][c] = true; 
            
        minX = Math.min(minX, c);
        maxX = Math.max(maxX, c);
        oilNum++;
        
        for(int i=0; i<4; i++) {
            int nx = r + dx[i];
            int ny = c + dy[i];
            
            if(!(nx >= 0 && nx < land.length && ny >= 0 && ny < land[0].length)) {
                continue;
            }
            
            if(land[nx][ny] != 1) continue;
            
            
            
            findOil(nx, ny);
            
        }
    }
    
}
