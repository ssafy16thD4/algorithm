import java.util.*;
import java.io.*;
/*
    탈주범이 있을 수 있는 위치의 개수

    1. dfs(맨홀뚜겅 세로위치, 맨홀뚜겅 가로위치)에서 출발
    2. dfs(x, y, time, from)
    2.1 from = 도착 칸 기준으로 뚫려있어야 하는 방향
    2.2 소요된 시간이 L이 되면 return;
    2.3 graph[x][y]의 숫자가 1~7이면 해당에 맞게 다음 터널로 이동
    3. vis에 방문처리 된 것만큼 카운트
*/
class Solution {
    static int[][] graph;
    static int[][] vis;
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
            vis = new int[n][m];
            for(int i=0; i<n; i++) {
                Arrays.fill(vis[i], Integer.MAX_VALUE);
            }
            
            // 지하 터널 지도 정보
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<m; j++) {
                    graph[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            dfs(r, c, 1, -1);

            int cnt = 0;
            for(int i=0; i<n; i++) {
                for(int j=0; j<m; j++) {
                    if(vis[i][j] != Integer.MAX_VALUE) {
                        cnt++;
                    }
                }
            }
            
            sb.append("#").append(test_case).append(" ").append(cnt).append("\n");
        }
        System.out.print(sb);
    }
    static void dfs(int x, int y, int time, int from) {
        if(x < 0 || x >= n || y < 0 || y >= m) return;
        if(graph[x][y] == 0) return;
        // 이전 칸에서 이동해왔을 때, 
        // 현재 칸이 해당 방향으로 뚫려있는지 확인 
        if(from != -1 && !isOpen(graph[x][y], from)) return;
        
        // 이미 더 빠른 시간에 방문했으면 종료
        if(time >= vis[x][y]) return;
        
        vis[x][y] = time;
        if(time >= l) return;
        
        if(graph[x][y] == 1) {
            dfs(x-1, y, time+1, 1); // 상으로 이동 -> 도착칸은 하가 뚫려야
            dfs(x+1, y, time+1, 0);
            dfs(x, y-1, time+1, 3);
            dfs(x, y+1, time+1, 2);
        }
        else if(graph[x][y] == 2) {
            dfs(x-1, y, time+1, 1);
            dfs(x+1, y, time+1, 0);
        }
        else if(graph[x][y] == 3) {
            dfs(x, y-1, time+1, 3);
            dfs(x, y+1, time+1, 2);
        }
        else if(graph[x][y] == 4) {
            dfs(x, y+1, time+1, 2);
            dfs(x-1, y, time+1, 1);
        }
        else if(graph[x][y] == 5) {
            dfs(x, y+1, time+1, 2);
            dfs(x+1, y, time+1, 0);
        }
        else if(graph[x][y] == 6) {
            dfs(x, y-1, time+1, 3);
            dfs(x+1, y, time+1, 0);
        }
        else if(graph[x][y] == 7) {
            dfs(x-1, y, time+1, 1);
            dfs(x, y-1, time+1, 3);
        }
    }
    // type이 dir 방향으로 뚫려있는지 (0상 1하 2좌 3우)
    static boolean isOpen(int type, int dir) {
        if(dir == 0) return type == 1 || type == 2 || type == 4 || type == 7; // 상 
        if(dir == 1) return type == 1 || type == 2 || type == 5 || type == 6; // 하 
        if(dir == 2) return type == 1 || type == 3 || type == 6 || type == 7; // 좌 
        return type == 1 || type == 3 || type == 4 || type == 5; // 우
    }
}
