import java.util.*;
import java.lang.*;
import java.io.*;
/*
    출발지(S)에서 도착지(G)로 이동하는데 복구 시간이 가장 짧은 경로 구하기
    -깊이가 3이면 복구에 드는 시간3
    -0인 곳은 복구작업 불필요

    알고리즘: 가중치 BFS
    시간: 20초 O(n^2)

    1. 입력
    1.1 테스트케이스 수 입력받기
    1.2 지도의 크기 입력 받기
    1.3 지도 입력받기 (0100)
    2. BFS
*/
class Main {
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] board;
    static int[][] dis;
    static boolean[][] vis;
    static int n;
    static int minNum;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int t = Integer.parseInt(br.readLine());

        for(int tc=1; tc<=t; tc++) {
            sb.append("#").append(tc).append(" ");
            n = Integer.parseInt(br.readLine());

            board = new int[n][n];
            dis = new int[n][n];
            vis = new boolean[n][n];

            for(int i=0; i<n; i++) { // 0100
                String str = br.readLine();
                for(int j=0; j<str.length(); j++) {
                    board[i][j] = str.charAt(j) - '0';
                }
            }
            
            minNum = Integer.MAX_VALUE;
            bfs(0, 0);
            
            debug();
            
            sb.append(minNum).append("\n");
        }
        System.out.print(sb.toString());
    }
    static void bfs(int x, int y) {
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{x, y});
        vis[x][y] = true;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if(!vis[nx][ny]) {
                    q.add(new int[]{nx, ny});
                    dis[nx][ny] = board[nx][ny] + dis[cur[0]][cur[1]];
                    vis[nx][ny] = true;

                    if(nx == (n-1) && ny == (n-1)) {
                        //System.out.println(dis[nx][ny]);
                        minNum = Math.min(minNum, dis[nx][ny]);    
                    }
                }
            }
        }
    }
    static void debug() {
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                System.out.print(dis[i][j]);
            }
            System.out.println();
        }
    }
}