package week6;

import java.io.*;
import java.util.*;

public class SWEA1767 {
    /*
     * core or line
     * * 입력받으며 코어 인덱스 리스트 생성
     * 직선 전선 to core 설치, 경로에 전선 설치 불가
     * * 모든 코어 dfs, 4방향 inrange까지 탐색,
     * * range 도달 시, 전선 길이 저장, 갯수 증가, 설치 불가 시, 다음 탐색
     * * max depth == 인덱스 전체 길이, 갱신 및 강제 리턴
     * * 중도 종료 시, 복구, 다음 탐색
     * * 현재 depth에서 탐색 완료 시, 현재 뎁스 (i-1) -> i 에서 최적값 갱신
     * * 최적값 배열 생성
     * * 모든 인덱스별로 값을 저장하는 answer 배열
     * core가 가장자리에 있으면 이미 전선 설치된 것으로 간주
     * 최대한 많은 코어에 전선 연결, 이 때 전선 길이의 합
     * 그 중 전선길이 합이 최소가 되는 경우, 모든 코어에 연결 불가능할 수도 있음
     */
    static int answer[];
    static int N;
    static int[][] maxinos;
    static List<int[]> idxs;
    static boolean[][] visited;

    static int maxCell;

    static int[] dx = { -1, 1, 0, 0 };
    static int[] dy = { 0, 0, -1, 1 };

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int tc = 1; tc <= T; tc++) {

            N = Integer.parseInt(br.readLine());
            maxinos = new int[N][N];
            visited = new boolean[N][N];
            idxs = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    maxinos[i][j] = Integer.parseInt(st.nextToken());
                    if (maxinos[i][j] > 0) {
                        idxs.add(new int[] { i, j });
                        visited[i][j] = true;
                    }
                }
            }

            answer = new int[idxs.size() + 1];
            Arrays.fill(answer, Integer.MAX_VALUE);

            maxCell = 0;
            dfs(0, 0, 0);

            int minCost = Integer.MAX_VALUE;
            for (int i = 0; i < answer.length; i++) {
                if (answer[i] != Integer.MAX_VALUE) {
                    minCost = answer[i];
                }
            }

            System.out.println("#" + tc + " " + minCost);

        }

    }

    static boolean isInRange(int x, int y) {
        return (x >= 0 && x < maxinos.length && y >= 0 && y < maxinos[0].length);
    }

    static void dfs(int depth, int cellCnt, int sum) {
        if (depth == idxs.size()) {
            answer[cellCnt] = Math.min(answer[cellCnt], sum);
            maxCell = Math.max(maxCell, cellCnt);
            return;
        }
        if (cellCnt + idxs.size() - depth < maxCell) return;

        int[] idx = idxs.get(depth);
        int x = idx[0];
        int y = idx[1];

        if (x == 0 || y == 0 || x == N - 1 || y == N - 1) {
            dfs(depth + 1, cellCnt + 1, sum);
            return;
        }

        for (int i = 0; i < dx.length; i++) {

            // 각 방향 별 설치 가능 여부 확인
            int nx = x + dx[i];
            int ny = y + dy[i];

            // 가능 여부 확인 및 복구 함수
            int dirSum = 0;
            boolean flag = false;

            while (isInRange(nx, ny)) {
                if (maxinos[nx][ny] == 0 && !visited[nx][ny]) {
                    dirSum++;
                    visited[nx][ny] = true;
                    nx += dx[i];
                    ny += dy[i];
                } else if (maxinos[nx][ny] != 0 || visited[nx][ny]) {
                    nx -= dx[i];
                    ny -= dy[i];
                    backward(i, nx, ny, x, y);
                    flag = true;
                    break;
                }
            }

            if (flag)
                continue;
            else {
                dfs(depth + 1, cellCnt + 1, sum + dirSum);
                nx -= dx[i];
                ny -= dy[i];
                backward(i, nx, ny, x, y);
            }
        }
        dfs(depth+1, cellCnt, sum);

    }

    static void backward(int dir, int nx, int ny, int x, int y) {
        while (nx != x || ny != y) {
            visited[nx][ny] = false;
            nx -= dx[dir];
            ny -= dy[dir];
        }
    }

}
