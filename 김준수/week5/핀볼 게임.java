import java.io.*;
import java.util.*;

public class SWEA5650 {
    static int N;
    static int[][] board;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};

    static int[][] block = {
        {},
        {2, 0, 3, 1}, 
        {2, 3, 1, 0}, 
        {1, 3, 0, 2}, 
        {3, 2, 0, 1}, 
        {2, 3, 0, 1}    
    };

    // 웜홀 번호(6~10)별 위치 두 개씩 미리 저장
    static int[][][] wormholePos = new int[11][2][2];
    static int[] wormholeCount = new int[11];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine().trim());
        for (int t = 1; t <= T; t++) {
            N = Integer.parseInt(br.readLine().trim());
            board = new int[N][N];
            for (int v = 0; v <= 10; v++) wormholeCount[v] = 0;

            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    int v = Integer.parseInt(st.nextToken());
                    board[i][j] = v;
                    if (v >= 6 && v <= 10) {
                        wormholePos[v][wormholeCount[v]++] = new int[]{i, j};
                    }
                }
            }

            int max = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (board[i][j] != 0) continue; // 블록/웜홀/블랙홀 위치는 출발 불가
                    for (int d = 0; d < 4; d++) {
                        max = Math.max(max, simulate(i, j, d));
                    }
                }
            }
            sb.append("#").append(t).append(" ").append(max).append("\n");
        }
        System.out.print(sb);
    }

    private static int simulate(int sx, int sy, int dir) {
        int x = sx, y = sy;
        int score = 0;
        // 이론상 반드시 출발점 복귀 or 블랙홀 낙하로 끝나지만, 안전 상한을 둔다.
        int limit = N * N * 4 + 10;

        for (int step = 0; step < limit; step++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            // 벽
            if (!inRange(nx, ny)) {
                dir = block[5][dir];
                score++;
                continue;
            }

            int cell = board[nx][ny];

            if (cell == -1) {
                return score; // 블랙홀 낙하, 그때까지의 점수 확정
            } else if (cell >= 1 && cell <= 5) {
                dir = block[cell][dir];
                score++;
                continue; // 위치는 그대로, 방향만 바뀜
            } else if (cell >= 6 && cell <= 10) {
                int[] other = getPair(nx, ny, cell);
                x = other[0];
                y = other[1];
                // 방향 유지, 점수 미포함
            } else {
                x = nx;
                y = ny;
            }

            if (x == sx && y == sy) return score; // 출발 위치 복귀 -> 종료
        }
        return score;
    }

    private static int[] getPair(int cx, int cy, int num) {
        int[][] pos = wormholePos[num];
        if (pos[0][0] == cx && pos[0][1] == cy) return pos[1];
        return pos[0];
    }

    private static boolean inRange(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }
}ㅊ