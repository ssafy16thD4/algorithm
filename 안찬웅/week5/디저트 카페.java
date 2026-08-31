import java.util.*;
import java.io.*;
/*
    1. dfs(x, y, cnt, direction)
    1.1 각 칸에서 직진(dir) 또는 꺾기(dir+1) 두 갈래
    1.2 격자 밖 or 디저트 번호 중복이면 가지치기
    1.3 dir == 3에서 시작점 복귀 -> 사각형 완성
    1.4 cnt가 maxCnt보다 크면 갱신
*/
class Main {
    static int[] dx = {1, 1, -1, -1};
    static int[] dy = {1, -1, -1, 1};
    static int[][] graph;
    static boolean[] vis;
    static int n;
    static int maxCnt;
    static int startX, startY;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=t; test_case++) {
            n = Integer.parseInt(br.readLine());

            graph = new int[n][n];
            vis = new boolean[101];
            maxCnt = -1;
            
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<n; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            for(int i=0; i<n-2; i++) {
                for(int j=1; j<n-1; j++) {
                    startX = i;
                    startY = j;
                    vis[graph[i][j]] = true;
                	dfs(i, j, 1, 0);
                    vis[graph[i][j]] = false;
                }
            }
            sb.append("#").append(test_case).append(" ").append(maxCnt).append("\n");
        }
        System.out.print(sb);
    }
    
    static void dfs(int x, int y, int cnt, int direction) {
    	for(int dir=direction; dir<=direction+1 && dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
            
            if(nx == startX && ny == startY) {
                if(dir == 3) {
                    maxCnt = Math.max(maxCnt, cnt);
                }
                continue;
            }
            if(!vis[graph[nx][ny]]) {
                vis[graph[nx][ny]] = true;
                dfs(nx, ny, cnt+1, dir);
                vis[graph[nx][ny]] = false;
            }
        }
    }
}
