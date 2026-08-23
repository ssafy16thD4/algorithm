// ref-title: DWinging — 아이템 줍기 (좌표 2배 + 테두리 BFS)
// ref-url: https://github.com/DWinging/Algorithm/blob/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/07/30/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EC%95%84%EC%9D%B4%ED%85%9C_%EC%A4%8D%EA%B8%B0/Solution.java
// ref-note: 모든 좌표에 2를 곱해 격자를 늘린 뒤(<<1) 테두리만 걸어 BFS 하고 마지막에 이동 횟수를 절반으로 줄인다(>>1). 테두리 판정은 "채워진 칸인데 8방향에 빈 칸이 있다" 이며, 이 조건이라야 두 직사각형이 맞붙은 이음매를 막을 수 있다. 원저자가 같은 폴더 solve.md 에 풀이 근거를 적어두었다(https://github.com/DWinging/Algorithm/tree/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/07/30/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EC%95%84%EC%9D%B4%ED%85%9C_%EC%A4%8D%EA%B8%B0). 저장소에 라이선스 표기가 없어 코드 원문은 커밋 02325ae 기준 사본이며, 출처 링크를 지우지 말 것.
class Solution {
    
    final static int MAX_SIZE = 100;
    final static int[][] DIST = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
    
    int[][] que;
    
    public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
        int[][] map = new int[MAX_SIZE + 2][MAX_SIZE + 2];
        que = new int[(MAX_SIZE + 1) * (MAX_SIZE + 1)][3];
        for(int[] rec : rectangle) {
            int lx = rec[0] << 1;
            int ly = rec[1] << 1;
            int rx = rec[2] << 1;
            int ry = rec[3] << 1;
            
            for(int y = ly; y <= ry; y++) {
                for(int x = lx; x <= rx; x++) {
                    map[y][x] = 1;
                }    
            }
        }
        
        setOutLine(map, characterX << 1, characterY << 1);        
        int res = bfs(map, characterX << 1, characterY << 1, itemX << 1, itemY << 1);
        return res;
    }
    
    private void setOutLine(int[][] map, int x, int y) {
        int head = 0, tail = 0;
        que[tail][0] = y;
        que[tail][1] = x;
        tail++;
        
        map[y][x] = 3;
        
        while(head < tail) {
            int cy = que[head][0];
            int cx = que[head][1];
            head++;
            
            for(int d = 0; d < 4; d++) {
                int ny = cy + DIST[d][0];
                int nx = cx + DIST[d][1];
                
                if(check(ny, nx) && map[ny][nx] == 1) {
                    map[ny][nx] = checkOutLine(map, ny, nx);
                    que[tail][0] = ny;
                    que[tail][1] = nx;
                    tail++;
                }
            }
        }        
    }
    
    private int checkOutLine(int[][] map, int y, int x) {
        for(int[] d : DIST) {
            int ny = y + d[0];
            int nx = x + d[1];
            
            if(!check(ny, nx) || map[ny][nx] == 0) {
                return 3;
            }
        }   
        return 2;
    }
    
    private int bfs(int[][] map, int charX, int charY, int itemX, int itemY) {
        int head = 0, tail = 0;
        que[tail][0] = charY;
        que[tail][1] = charX;
        que[tail][2] = 0;
        tail++;
        
        map[charY][charX] = 2;
        
        while(head < tail) {
            int cy = que[head][0];
            int cx = que[head][1];
            int move = que[head][2];
            head++;
            
            if(cy == itemY && cx == itemX) return move >> 1;
            
            for(int d = 0; d < 4; d++) {
                int ny = cy + DIST[d][0];
                int nx = cx + DIST[d][1];
                
                if(check(ny, nx) && map[ny][nx] == 3) {
                    map[ny][nx] = 2;
                    que[tail][0] = ny;
                    que[tail][1] = nx;
                    que[tail][2] = move + 1;
                    tail++;
                }
            }
        }
        return -1;
    }
    
    private boolean check(int y, int x) {
        return y >= 0 && y <= MAX_SIZE && x >= 0 && x <= MAX_SIZE;
    }
    
}