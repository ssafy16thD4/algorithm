import java.util.*;
/*
    가장 많은 석유를 뽑을 수 있는 시추관의 위치 찾기

    알고리즘: bfs (연결 성분 라벨링)

    1. bfs로 석유 덩어리마다 고유 ID를 칠하고, ID별 크기를 compSize에 저장한다.
    2. 열마다 위에서 아래로 훑으며 처음 보는 ID만 크기를 더한다.
     2.1 같은 덩어리가 한 열을 여러 구간으로 지나가도(오목한 모양) 한 번만 세진다.
    3. 열별 합의 최댓값을 출력한다.

    세로 n * 가로 m
    0: 빈땅
    1: 석유
*/
class Solution {
    static int[][] land;
    static int[][] compId;          // 칸 -> 덩어리 ID (0 = 석유 없음)
    static List<Integer> compSize;  // 덩어리 ID -> 크기
    static boolean[][] vis;
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int maxSum;
    static int n, m;
    public int solution(int[][] land) {
        Solution.land = land;
        n = land.length;
        m = land[0].length;
        maxSum = 0;
        vis = new boolean[n][m];
        compId = new int[n][m];
        compSize = new ArrayList<>();
        compSize.add(0); // ID 0 은 "석유 없음" 자리

        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                if(!vis[i][j] && land[i][j] == 1) {
                    bfs(i, j, compSize.size());
                }
            }
        }

        // 열마다 "이미 센 덩어리 ID"를 기억하면서 합산한다.
        for(int col=0; col<m; col++) {
            Set<Integer> seen = new HashSet<>();
            int sum = 0;
            for(int row=0; row<n; row++) {
                int id = compId[row][col];
                if(id != 0 && seen.add(id)) {
                    sum += compSize.get(id);
                }
            }
            maxSum = Math.max(maxSum, sum);
        }

        return maxSum;
    }
    // (x, y)에서 시작하는 덩어리에 id를 칠하고 크기를 compSize에 기록한다
    static void bfs(int x, int y, int id) {
        Deque<int[]> q = new ArrayDeque<>();
        vis[x][y] = true;
        compId[x][y] = id;
        q.offer(new int[]{x, y});
        int cnt = 1;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            for(int dir=0; dir<4; dir++) {
                int nx = cur[0] + dx[dir];
                int ny = cur[1] + dy[dir];
                if(nx < 0 || nx >= n || ny < 0 || ny >= m) continue;
                if(!vis[nx][ny] && land[nx][ny] == 1) {
                    vis[nx][ny] = true;
                    compId[nx][ny] = id;
                    cnt++;
                    q.offer(new int[]{nx, ny});
                }
            }
        }
        compSize.add(cnt);
    }
}
