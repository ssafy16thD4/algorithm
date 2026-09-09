
import java.io.*;
import java.util.*;

public class SWEA2382 {

    static int N;
    static int M;
    static int K;

    static int[] dx = { 0, -1, 1, 0, 0 };
    static int[] dy = { 0, 0, 0, -1, 1 };

    static int[][][] board;
    static Deque<int[]> idxs;
    static int answer;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(br.readLine());
        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
            answer = 0;
            board = new int[4][N][N];
            // 값, 시간, 경쟁 군집의 최댓값, 방향
            idxs = new ArrayDeque<>();

            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int k = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());

                board[0][x][y] = k;
                board[3][x][y] = d;

                idxs.offerLast(new int[] { x, y });
            }

            simulation();

            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);
        }
    }

    static boolean isPoison(int x, int y) {
        return (x == 0 || x == N - 1 || y == 0 || y == N - 1);
    }

    static int dirMap(int d) {
        if (d % 2 == 0)
            return d - 1;
        else
            return d + 1;
    }

    static void simulation() {

        for (int t = 1; t <= M; t++) {
            int breadthCnt = 0;
            int breadthSize = idxs.size();

            int[][][] newBoard = new int[4][N][N];
            boolean[][] visited = new boolean[N][N];

            while (!idxs.isEmpty() && breadthCnt < breadthSize) {
                int[] idx = idxs.pollFirst();
                int x = idx[0];
                int y = idx[1];
                int currVal = board[0][x][y];
                int d = board[3][x][y];

                int nx = x + dx[d];
                int ny = y + dy[d];

                if (visited[nx][ny]) {
                    newBoard[0][nx][ny] += currVal;
                    if (newBoard[2][nx][ny] < currVal) {
                        newBoard[2][nx][ny] = currVal;
                        newBoard[3][nx][ny] = d;
                    }
                } else if (isPoison(nx, ny)) {
                    int val = currVal / 2;
                    if (val <= 0) {
                        breadthCnt++;
                        continue;
                    }
                    newBoard[0][nx][ny] = currVal / 2;
                    newBoard[1][nx][ny] = t;
                    newBoard[2][nx][ny] = currVal / 2;
                    newBoard[3][nx][ny] = dirMap(d);
                    visited[nx][ny] = true;

                    idxs.offer(new int[] { nx, ny });
                } else {
                    newBoard[0][nx][ny] = currVal;
                    newBoard[1][nx][ny] = t;
                    newBoard[2][nx][ny] = currVal;
                    newBoard[3][nx][ny] = d;
                    visited[nx][ny] = true;

                    idxs.offer(new int[] { nx, ny });
                }
                breadthCnt++;
            }

            board = newBoard;

            if (t == M) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        if (board[0][i][j] > 0) {
                            answer += board[0][i][j];
                        }
                    }
                }
            }
        }

    }

}
