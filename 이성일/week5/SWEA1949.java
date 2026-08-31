
import java.io.*;
import java.util.*;

public class SWEA1949 {

    static int[] dx = { -1, 0, 1, 0 };
    static int[] dy = { 0, 1, 0, -1 };
    static int answer;

    public static void main(String[] args) throws NumberFormatException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int tc = 1; tc <= T; tc++) {

            StringTokenizer st = new StringTokenizer(br.readLine());

            int n = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());

            int highest = 0;
            int[][] map = new int[n][n];
            boolean[][] visited = new boolean[n][n];
            List<int[]> highPoints = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < n; j++) {
                    int num = Integer.parseInt(st.nextToken());

                    if (num > highest)
                        highest = num;

                    map[i][j] = num;

                }
            }

            // high points 저장
            findHighPoints(highest, highPoints, map);

            answer = 0;

            // 모든 칸 공사 기준 dfs
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int v = 1; v <= k; v++) {
                        map[i][j] -= v;
                        for (int[] point : highPoints) {
                            visited[point[0]][point[1]] = true;
                            dfs(map, visited, point, 1);
                            visited[point[0]][point[1]] = false;
                        }
                        map[i][j] += v;
                    }
                }
            }

            // 공사 안할 때 dfs
            for (int[] point : highPoints) {
                dfs(map, visited, point, 0);
            }

            // 출력
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);
            // 초기화
            answer = 0;
        }
    }

    static boolean inRange(int x, int y, int n) {
        return (x >= 0 && x < n && y >= 0 && y < n);
    }

    static void findHighPoints(int highest, List<int[]> highPoints, int[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == highest)
                    highPoints.add(new int[] { i, j });
            }
        }
    }

    /*
     * * 시간복잡도 64 * K (1~5) * max (5) * 64 = 약 10만
     * 1. 모든 칸을 돌면서 공사를 K 범위만큼 진행한다 (공사 아예 안하는 케이스도 있다)
     * * 모든 칸별로 k 값별로 공사하며 탐색 진행
     * * 최고봉우리 값에서 시작해 방문 여부 확인 필요, 인덱스 범위 확인 필요, 4방향 탐색 dfs
     * * 더이상 방문 불가능 시, 갱신 후 종료
     * * 방문 시 방문 여부 변경, 탐색 완료 시 방문 여부 초기화
     * * dfs 종료 후, k값 복구
     * 2. 최대 높이 봉우리에서 등산로 건설을 시작하며 건설 최대 길이를 갱신한다. (DFS)
     * * 입력 동시에 최대 높이의 봉우리 인덱스 저장하기 int[]로 좌표관리 List<int[]>에 공사 포인트 저장하기
     * 
     * 3. 등산로는 높은 곳에서 아래로만 진행할 수 있다.
     */
    static void dfs(int[][] map, boolean[][] visited, int[] point, int cnt) {

        int x = point[0], y = point[1];

        answer = Math.max(answer, cnt);

        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (!inRange(nx, ny, map.length))
                continue;
            if (visited[nx][ny])
                continue;
            if (map[nx][ny] >= map[x][y])
                continue;
            visited[nx][ny] = true;
            dfs(map, visited, new int[] { nx, ny }, cnt + 1);
            visited[nx][ny] = false;
        }

        // cnt -= 1;

    }
}
