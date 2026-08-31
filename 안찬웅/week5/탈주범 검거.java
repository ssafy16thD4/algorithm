import java.util.*;
import java.io.*;
/*
    탈주범이 있을 수 있는 위치의 개수

    1~7: 해당 위치의 터널 구조물 타입
    0: 터널이 없는 장소

    1. dfs(맨홀뚜겅 세로위치, 맨홀뚜겅 가로위치)에서 출발
    2. dfs(x, y)
    2.1 소요된 시간이 L이 되면 return;
    2.2 graph[x][y]의 숫자가 1~7이면 해당에 맞게 다음 터널로 이동
    3. vis에 방문처리 된 것만큼 카운트
*/
class Main {
    static int[][] graph;
    static boolean[][] vis;
    static int n, m, r, c, l;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=t; test_case++) {
            st = new StringTokenizer(br.readLine());

            n = Integer.parseInt(st.nextToken()); // 지하터널지도 세로 크기
            m = Integer.parseInt(st.nextToken()); // 지하터널지도 가로 크기
            r = Integer.parseInt(st.nextToken()); // 맨홀뚜겅 세로위치
            c = Integer.parseInt(st.nextToken()); // 맨홀뚜겅 가로위치
            l = Integer.parseInt(st.nextToken()); // 탈출 후 소요된 시간

            graph = new int[n][m];
            vis = new boolean[n][m];
            
            // 지하 터널 지도 정보
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<m; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            dfs(r, c, 1);
            //System.out.println("#" + test_case);
            int cnt = 0;
            for(int i=0; i<n; i++) {
                for(int j=0; j<m; j++) {
                    //System.out.print(vis[i][j] + " ");
                    if(vis[i][j]) {
                        cnt++;
                    }
                }
                //System.out.println();
            }
            
            sb.append("#").append(test_case).append(" ").append(cnt).append("\n");
        }
        System.out.print(sb);
    }
    static void dfs(int x, int y, int time) {
        /* 
            1: 상하좌우
            2: 상하
            3: 좌우
            4: 상우
            5: 하우
            6: 하좌
            7: 상좌
        */
        if(time >= l) return;
        if(x < 0 || x >= n || y < 0 || y >= m) return;
        if(graph[x][y] == 0) return;
        
        if(!vis[x][y]) {
            vis[x][y] = true;
            if(graph[x][y] == 1) {
                dfs(x-1, y, time+1);
                dfs(x+1, y, time+1);
                dfs(x, y-1, time+1);
                dfs(x, y+1, time+1);
            }
            else if(graph[x][y] == 2) {
                dfs(x+1, y, time+1);
                dfs(x-1, y, time+1);
            }
            else if(graph[x][y] == 3) {
                dfs(x, y+1, time+1);
                dfs(x, y-1, time+1);
            }
            else if(graph[x][y] == 4) {
                dfs(x, y+1, time+1);
                dfs(x-1, y, time+1);
            }
            else if(graph[x][y] == 5) {
                dfs(x, y+1, time+1);
                dfs(x+1, y, time+1);
            }
            else if(graph[x][y] == 6) {
                dfs(x, y-1, time+1);
                dfs(x+1, y, time+1);
            }
            else if(graph[x][y] == 7) {
                dfs(x-1, y, time+1);
                dfs(x, y-1, time+1);
            }
        }
    }
}
