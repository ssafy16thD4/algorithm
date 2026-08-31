import java.util.*;
import java.lang.*;
import java.io.*;
/*
    디저트를 가장 많이 먹는 대각선모양 사각형
    :가장 많이 이동한
    출발 -> 도착(출발점)이 대각선 모양이어야함
    
    1. bfs
    1.1 출발지점 -> 도착지점(출발지점)
    1.2 배열안의 수가 같으면 안됨
*/
class Main { // 우하 좌하 좌상 우상
    static int[] dx = {1, 1, -1, -1};
    static int[] dy = {1, -1, -1, 1};
    static int[][] graph;
    static boolean[][] vis;
    static int[] visNum;
    static int n;
    static int maxCnt;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=t; test_case++) {
            n = Integer.parseInt(br.readLine());
            graph = new int[n][n];
            
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<n; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                }
            }
            
            maxCnt = -1;
            for(int i=1; i<n-1; i++) {
                for(int j=1; j<n-1; j++) {
                    visNum = new int[101];
                    visNum[graph[i][j]] = 1;
                    bfs(i, j);
                }
            }
            
            sb.append("#").append(test_case).append(" ").append(maxCnt).append("\n");
        }
        System.out.print(sb);
    }
    static void bfs(int x, int y) {
        Deque<int[]> q = new ArrayDeque<>();
        vis = new boolean[n][n];
        vis[x][y] = true;
        q.offer(new int[]{x, y});
        int cnt=0;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            cnt++;
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if(!vis[nx][ny] && visNum[graph[nx][ny]] == 0) {
                    vis[nx][ny] = true;
                    visNum[graph[nx][ny]] = 1;
                    q.offer(new int[]{nx, ny});
                }
            }
        }
        maxCnt = Math.max(maxCnt, cnt);
    }
}
