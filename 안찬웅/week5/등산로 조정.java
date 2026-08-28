import java.util.*;
import java.io.*;
/*
    가장 높은 봉우리에서 시작해서 자신보다 낮은 곳으로 이동해서 가장 긴 거리 만들기
    
    알고리즘: bfs, 격자탐색 (경로별 상태 스냅샷)
    시간복잡도: O(봉우리수 * 경로수 * N^2)

    1. 가장 높은 봉우리에서 출발한다 (모든 경우의 수)
    2. bfs
     2.1 자신보다 낮을 때만 이동가능하다.
     2.2 딱 한곳만 봉우리를 k만큼 깎을 수 있다.
     2.3 vis, graph를 큐 원소마다 들고 다녀서 갈래끼리 간섭 없앤다.
    3. 가장 긴 거리를 출력
*/
class Solution {
    // dist, vis static 삭제: 경로 길이는 cur.cnt가, 방문은 cur.vis가 들고 다님
    static int[] dx = {1, 0, -1, 0}; // 우하좌상
    static int[] dy = {0, 1, 0, -1};
    static int n, k;
    static int maxCnt;

    static class Node {
        int x;
        int y;
        int cnt;
        boolean isUsed;
        boolean[][] vis;
        int[][] graph;

        // 파라미터 순서 = 필드 순서로 맞춰두면 offer할 때 헷갈리지 않음
        Node(int x, int y, int cnt, boolean isUsed, boolean[][] vis, int[][] graph) {
            this.x = x;
            this.y = y;
            this.cnt = cnt;
            this.isUsed = isUsed;
            this.vis = vis;
            this.graph = graph;
        }
    }

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
                        bfs(i, j, graph); // vis, dist 초기화 불필요 (bfs 안에서 생성)
                    }
                }
            }
            sb.append("#").append(test_case).append(" ").append(maxCnt).append("\n");
        }
        System.out.print(sb);
    }

    static void bfs(int x, int y, int[][] graph) {
        Deque<Node> q = new ArrayDeque<>();
        boolean[][] vis = new boolean[n][n];
        vis[x][y] = true;                 // offer보다 먼저 찍기 (순서 헷갈리기 쉬움)
        q.offer(new Node(x, y, 1, false, vis, copyInt(graph)));
        // 시작할 때 copyInt: 원본 graph를 안 건드려야 다음 시작점이 온전한 지형을 봄

        while(!q.isEmpty()) {
            Node cur = q.poll();
            maxCnt = Math.max(maxCnt, cur.cnt); // 배열 스캔 아니라 poll 시점에 갱신

            for(int dir=0; dir<4; dir++) {
                int nx = cur.x + dx[dir];
                int ny = cur.y + dy[dir];
                if(nx < 0 || nx >= n || ny < 0 || ny >= n) continue;
                if(cur.vis[nx][ny]) continue; // static vis 아님, 반드시 cur.vis

                // 현재 위치 > 다음위치 일때만 이동가능
                // 양쪽 다 cur.graph여야 함. 파라미터 graph는 깎기 반영이 안 돼 있음
                if(cur.graph[cur.x][cur.y] > cur.graph[nx][ny]) {
                    boolean[][] nvis = copyBool(cur.vis); // 갈래마다 vis 분리
                    nvis[nx][ny] = true;
                    // graph는 안 건드리니 참조 그대로 넘김 (복사 비용 절약)
                    q.offer(new Node(nx, ny, cur.cnt + 1, cur.isUsed, nvis, cur.graph));
                    // isUsed는 cur.isUsed로 물려받기. false 하드코딩하면 여러 번 깎임
                }
                // 딱한곳만 정해서 k만큼 깎을 수 있음
                else if(!cur.isUsed) { // 이 경로에서 아직 안 깎았을 때만
                    // else 분기라 cur.graph[nx][ny] >= cur.graph[cur.x][cur.y]는 항상 참
                    int len = cur.graph[nx][ny] - cur.graph[cur.x][cur.y] + 1;
                    if(len <= k) {
                        boolean[][] nvis = copyBool(cur.vis);
                        nvis[nx][ny] = true;
                        int[][] ngraph = copyInt(cur.graph); // 깎을 때만 graph도 분리
                        ngraph[nx][ny] -= len;
                        q.offer(new Node(nx, ny, cur.cnt + 1, true, nvis, ngraph));
                    }
                }
            }
        }
    }
    static boolean[][] copyBool(boolean[][] arr) {
        boolean[][] res = new boolean[n][n];
        for(int i=0; i<n; i++) res[i] = arr[i].clone(); // 행 단위 clone이 2중 루프보다 빠름
        return res;
    }

    static int[][] copyInt(int[][] arr) {
        int[][] res = new int[n][n];
        for(int i=0; i<n; i++) res[i] = arr[i].clone();
        return res;
    }
}
