import java.io.*;
import java.util.*;

public class SWEA5656 {

    // N: 쏠 수 있는 구슬 수, W/H: 벽돌판의 너비/높이
    static int N;
    static int W;
    static int H;
    // 모든 구슬 투하 경우를 확인하며 남길 수 있는 최소 벽돌 수를 저장한다.
    static int answer = Integer.MAX_VALUE;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int tc = 1; tc <= T; tc++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            N = Integer.parseInt(st.nextToken());
            W = Integer.parseInt(st.nextToken());
            H = Integer.parseInt(st.nextToken());

            int[][] board = new int[H][W];

            for (int i = 0; i < H; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < W; j++) {
                    board[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            dfs(0, board);

            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);

            answer = Integer.MAX_VALUE;
        }
    }

    /*
     * 구슬을 어느 열에 떨어뜨릴지 DFS로 모두 시도한다.
     * 각 단계마다 현재 board를 복사한 뒤,
     * 선택한 열의 첫 벽돌을 터뜨리고 중력을 적용한 상태로 다음 구슬을 진행한다.
     */
    static void dfs(int n, int[][] board) {
        // N개의 구슬을 모두 사용하면 현재 상태의 벽돌 수로 정답을 갱신한다.
        if (n == N) {
            int cnt = countBrick(board);
            answer = Math.min(answer, cnt);
            return;
        }

        for (int i = 0; i < W; i++) {

            // 현재 열에 맞출 벽돌이 있는지 확인한다.
            boolean flag = false;
            for (int r = 0; r < H; r++) {

                if (board[r][i] > 0) {

                    // 다른 열 선택 경우에 영향을 주지 않도록 현재 상태를 복사한다.
                    int[][] temp = new int[H][W];
                    for (int x = 0; x < H; x++) {
                        for (int y = 0; y < W; y++) {
                            temp[x][y] = board[x][y];
                        }
                    }

                    // 벽돌 연쇄 제거 후 빈 공간을 아래로 정리한다.
                    build(r, i, temp);
                    clean(temp);
                    dfs(n + 1, temp);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                // 빈 열에 구슬을 떨어뜨린 경우: 판 변화 없이 구슬 횟수만 소모한다.
                dfs(n + 1, board);
            }
        }
    }

    // 현재 board에 남아 있는 벽돌 개수를 센다.
    static int countBrick(int[][] board) {
        int cnt = 0;
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                if (board[i][j] > 0) cnt++;
            }
        }
        return cnt;
    }

    // 시작 벽돌의 숫자만큼 상하좌우로 퍼지며 제거할 벽돌을 BFS로 찾는다.
    static void build(int r, int c, int[][] temp) {
        Deque<int[]> deq = new ArrayDeque<>();
        deq.add(new int[] {r, c});
        boolean[][] visited = new boolean[temp.length][temp[0].length];

        while (!deq.isEmpty()) {

            int[] point = deq.poll();
            int x = point[0], y = point[1];
            visited[x][y] = true;

            // 세로 방향 연쇄 폭발 범위 확인
            for (int i = (x - temp[x][y] + 1); i <= (temp[x][y] - 1 + x); i++) {
                if (!inRange(i, y)) continue;
                if (visited[i][y]) continue;
                if (temp[i][y] > 1) deq.offerLast(new int[] {i, y});
                else {
                    temp[i][y] = 0;
                    visited[i][y] = true;
                }
            }

            // 가로 방향 연쇄 폭발 범위 확인
            for (int j = (y - temp[x][y] + 1); j <= (temp[x][y] - 1 + y); j++) {
                if (!inRange(x, j)) continue;
                if (visited[x][j]) continue;
                if (temp[x][j] > 1) deq.offerLast(new int[] {x, j});
                else {
                    temp[x][j] = 0;
                    visited[x][j] = true;
                }
            }

            // 현재 벽돌 제거
            temp[x][y] = 0;
        }
    }

    // 각 열마다 빈칸 위에 있는 벽돌을 아래로 떨어뜨린다.
    static void clean(int[][] temp) {
        for (int j = 0; j < W; j++) {
            int floor = H;

            // 아래에서부터 처음 만나는 빈칸을 벽돌이 내려갈 위치로 잡는다.
            for (int i = floor - 1; i >= 0; i--) {
                if (temp[i][j] == 0) {
                    floor = i;
                    break;
                }
            }
            if (floor == H) continue;

            // 빈칸 위쪽의 벽돌을 차례대로 floor 위치로 내린다.
            for (int i = floor - 1; i >= 0; i--) {
                if (temp[i][j] > 0) {
                    temp[floor][j] = temp[i][j];
                    temp[i][j] = 0;
                    floor--;
                }
            }
        }
    }

    // 좌표가 벽돌판 내부인지 확인한다.
    static boolean inRange(int r, int c) {
        return ((r >= 0 && r < H) && (c >= 0 && c < W));
    }
}
