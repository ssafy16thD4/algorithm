package coding;

import java.io.*;
import java.util.*;

public class 헌터 {

    static int answer;
    static int N;
    static int[][] board;
    static List<int[]> list;
    static int monsterCnt;
    static boolean[] visited;
    static boolean[] isHunted;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int T = Integer.parseInt(br.readLine());

        for (int tc = 1; tc <= T; tc++) {
            N = Integer.parseInt(br.readLine());
            board = new int[N + 1][N + 1];

            list = new ArrayList<>();

            for (int i = 1; i <= N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 1; j <= N; j++) {
                    board[i][j] = Integer.parseInt(st.nextToken());
                    if (board[i][j] != 0)
                        list.add(new int[] { i, j });
                }
            }
            monsterCnt = list.size() / 2;
            isHunted = new boolean[monsterCnt + 1];
            visited = new boolean[list.size()];
            /*
             * 노드 최대 8개로 (집,몬스터)
             * 방문 순서는 몬스터 먼저 그 후 집
             * * 몬스터 boolean 배열에 몬스터가 true 면 집 탐색 가능
             * * 방문 배열도 필요
             * * 탐색은 현재 노드 위치에서 다음 노드 위치 hamilton-dist구하기
             * * 모든 노드 돌 떄까지 시간을 갱신하기
             * * 가지 치기 -> 탐색 중에 answer 값 보다 길면 탐색 종료
             * * 시작 노드 위치는 1,1로 고정
             * 최단 시간 구하기
             * 
             */

            answer = Integer.MAX_VALUE;
            dfs(0, 1, 1, 0);

            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);

        }
    }

    static void dfs(int idx, int r, int c, int distCnt) {
        if (idx == list.size()) {
            answer = Math.min(answer, distCnt);
            return;
        }
        System.out.println(" distCnt : " + distCnt);
        // 탐색이 남았는데 이미 최솟값이 아니면 가지치기
        if (distCnt > answer)
            return;

        for (int i = 0; i < list.size(); i++) {
            if (visited[i])
                continue;

            int[] xy = list.get(i);
            int x = xy[0];
            int y = xy[1];
            int vertex = board[x][y];

            if (vertex < 0) {
                if (isHunted[-1 * vertex]) {
                    visited[idx] = true;
                    int dist = Math.abs(r - x) + Math.abs(c - y);
                    dfs(idx + 1, x, y, distCnt + dist);
                    visited[idx] = false;
                } else {
                    continue;
                }
            }
            if (vertex > 0) {
                visited[idx] = true;
                isHunted[vertex] = true;
                int dist = Math.abs(r - x) + Math.abs(c - y);
                dfs(idx + 1, x, y, distCnt + dist);
                visited[idx] = false;
                isHunted[vertex] = false;
            }
        }

    }

}
