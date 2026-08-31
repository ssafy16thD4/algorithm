import java.util.*;
import java.lang.*;
import java.io.*;
/*
    가장 높은 봉우리에서 시작해서 자신보다 낮은 곳으로 이동해서 가장 긴 거리 만들기
    
    알고리즘: bfs, 격자탐색
    시간복잡도: 

    1. 가장 높은 봉우리에서 출발한다 (모든 경우의 수)
    2. bfs
     2.1 자신보다 낮을 때만 이동가능하다.
     2.2 딱 한곳만 봉우리를 k만큼 깎을 수 있다.
     2.3 각 봉우리마다 가장 긴 거리를 구한다.
    3. 가장 긴 거리를 출력
*/
class Main {
    static int[][] dist;
    static boolean[][] vis;
    static int[] dx = {1, 0, -1, 0}; // 우하좌상
    static int[] dy = {0, 1, 0, -1};
    static int n, k;
    static int maxCnt;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());
        for(int test_case=1; test_case<=t; test_case++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            k = Integer.parseInt(st.nextToken());

            maxCnt = 0;
            int maxHeight = 0;
            int[][] graph = new int[n][n];
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<n; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                    if(maxHeight < graph[i][j]) {
                        maxHeight = graph[i][j];
                    }
                }
            }
            for(int i=0; i<n; i++) {
                for(int j=0; j<n; j++) {
                    if(graph[i][j] == maxHeight) {
                        vis = new boolean[n][n];
                        dist = new int[n][n];
                        bfs(i, j, false, graph);
                    }
                }
            }
            sb.append("#").append(test_case).append(" ").append(maxCnt).append("\n");
        }
        System.out.print(sb.toString());
    }
    static void bfs(int x, int y, boolean flag, int[][] graph) {
        Deque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{x, y});
        vis[x][y] = true;
        dist[x][y] = 1;
        int cnt = 1;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            cnt++;
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if(!vis[nx][ny]) {
                   // 현재 위치 > 다음위치 일때만 이동가능
                   if(graph[cur[0]][cur[1]] > graph[nx][ny]) {
                       vis[nx][ny] = true;
                       dist[nx][ny] = dist[cur[0]][cur[1]] + 1;
                       q.offer(new int[]{nx, ny});
                   }
                   // 딱한곳만 정해서 k만큼 깎을 수 있음
                   else {
                       // int len = 다음위치 - 현재위치 + 1
                       // len이 k보다 크면 처리
                       // 작으면 처리 불가
                       if(!flag && graph[nx][ny] >= graph[cur[0]][cur[1]]) {
                           int len = graph[nx][ny] - graph[cur[0]][cur[1]] + 1;
                           if(len <= k) {
                               flag = true;
                               graph[nx][ny] -= len;
                           }
                       }
                   }
                }
            }
        }      
        // for(int i=0; i<n; i++) {
        //     for(int j=0; j<n; j++) {
        //         System.out.print(dist[i][j] + " ");
        //     }
        //     System.out.println();
        // }
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                maxCnt = Math.max(maxCnt, dist[i][j]);
            }
        }
    }
}
