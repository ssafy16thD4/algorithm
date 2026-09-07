import java.util.*;
/*
    캐릭터가 아이템으로 이동하는 최단거리 구하기
    :(characterX, characterY) -> (itemX, itemY)

    알고리즘: 좌표 2배 확대 + BFS

    1. 좌표를 2배로 늘린다.
       - 직사각형이 변끼리 딱 붙어 있을 때, 정수 격자에서는 두 테두리가 한 칸에 겹쳐
         지나갈 수 있는지 없는지 구분이 안 된다. 2배로 늘리면 사이에 칸이 하나 생겨 구분된다.
    2. 모든 직사각형의 테두리를 1로 칠한다.
    3. 그다음 모든 직사각형의 내부를 2로 덮어쓴다.
       - 어떤 테두리가 다른 직사각형 안쪽에 들어가면 지나갈 수 없는 선이 되는데,
         "테두리 전부 -> 내부 전부" 순서로 칠하면 그게 자동으로 지워진다.
    4. 값이 1인 칸만 밟으며 BFS. 도착 거리를 2로 나눈 값이 답.
*/
class Solution {
    static int[] dx = {1, -1, 0, 0};
    static int[] dy = {0, 0, 1, -1};
    static final int SIZE = 102; // 좌표 최대 50 * 2 + 여유

    public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
        int[][] board = new int[SIZE][SIZE];

        // 2. 테두리 칠하기
        for(int[] r : rectangle) {
            int x1 = r[0] * 2, y1 = r[1] * 2, x2 = r[2] * 2, y2 = r[3] * 2;
            for(int x=x1; x<=x2; x++) {
                board[x][y1] = 1;
                board[x][y2] = 1;
            }
            for(int y=y1; y<=y2; y++) {
                board[x1][y] = 1;
                board[x2][y] = 1;
            }
        }

        // 3. 내부 덮어쓰기 (다른 사각형 안에 갇힌 테두리를 지운다)
        for(int[] r : rectangle) {
            int x1 = r[0] * 2, y1 = r[1] * 2, x2 = r[2] * 2, y2 = r[3] * 2;
            for(int x=x1+1; x<x2; x++) {
                for(int y=y1+1; y<y2; y++) {
                    board[x][y] = 2;
                }
            }
        }

        // 4. BFS
        int[][] dist = new int[SIZE][SIZE];
        for(int[] row : dist) Arrays.fill(row, -1);

        int sx = characterX * 2, sy = characterY * 2;
        int ex = itemX * 2, ey = itemY * 2;

        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{sx, sy});
        dist[sx][sy] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            if(cur[0] == ex && cur[1] == ey) {
                return dist[ex][ey] / 2;
            }
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || nx >= SIZE || ny < 0 || ny >= SIZE) continue;
                if(board[nx][ny] != 1) continue;   // 테두리만 밟는다
                if(dist[nx][ny] != -1) continue;
                dist[nx][ny] = dist[cur[0]][cur[1]] + 1;
                q.offer(new int[]{nx, ny});
            }
        }
        return 0;
    }
}
