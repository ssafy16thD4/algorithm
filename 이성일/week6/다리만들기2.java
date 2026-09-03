package coding;

import java.util.*;
import java.io.*;

public class 다리만들기2 {

    /*
     * 섬을 하나의 노드로 해석한다.
     * * 입력 배열에서 bfs를 돌며, 1을 만나면 같은 영역으로 확산시킨다. visited 처리
     * * 영역 숫자의 최댓값을 통해 전체 영역의 개수를 구한다.
     * * bfs를 돌며 인덱스리스트에 좌표값 담기 최종 인덱스리스트를 관리하는 리스트에 담는다
     * 노드에서 노드 사이 인접 간선을 구한다.
     * * 리스트를 돌면서 인덱스 리스트를 꺼내고, 인덱스별로 탐색하여 간선값을 업데이트한다. 2차원배열에 값을 업데이트하고 값이 0이면 도달
     * 불가로 해석하기
     * * 2차원배열을 확인하면서 엣지배열에 담고 엣지 배열을 정렬한다. 엣지배열은 번호가 인덱스, 섬번호1, 섬번호2, 비용을 저장하는
     * 2차원배열
     * 가장 짧은 간선을 구하고, 이를 union find 하여 mst를 구축한다.
     * mst길이의 합을 구한다.
     */

    static int[] dx = { -1, 1, 0, 0 };
    static int[] dy = { 0, 0, -1, 1 };
    static int[][] board;
    static int[] parents;

    static List<List<int[]>> islandIdxs;

    public static void main(String[] args) throws Exception {
        islandIdxs = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] nm = br.readLine().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        board = new int[n][m];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int islandSize = 0;
        boolean[][] visited = new boolean[board.length][board[0].length];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (board[i][j] == 1 && !visited[i][j]) {
                    Deque<int[]> idxs = new ArrayDeque<>();
                    idxs.add(new int[] { i, j });
                    List<int[]> idxList = bfs(board, idxs, ++islandSize, visited);
                    islandIdxs.add(idxList);
                }
            }
        }

        parents = new int[islandSize + 1];
        for (int i = 1; i <= islandSize; i++) {
            parents[i] = i;
        }

        int[][] edges = new int[islandSize + 1][islandSize + 1];
        for (int[] edge : edges) {
            Arrays.fill(edge, Integer.MAX_VALUE);
        }

        for (int i = 1; i <= islandSize; i++) {
            getEdges(islandIdxs.get(i), edges, i);
        }

        PriorityQueue<int[]> edgesList = new PriorityQueue<>((o1, o2) -> Integer.compare(o1[2], o2[2]));
        for (int i = 1; i <= islandSize; i++) {
            for (int j = 1; j <= islandSize; j++) {
                if (edges[i][j] < Integer.MAX_VALUE) {
                    edgesList.offer(new int[] { i, j, edges[i][j] });
                }
            }
        }

        int answer = mst(edgesList);

    }

    static int mst(PriorityQueue<int[]> edgesList) {

        return 0;
    }

    static void getEdges(List<int[]> idxs, int[][] edges, int i) {

        for (int[] idx : idxs) {
            int x = idx[0];
            int y = idx[1];
            for (int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                int cost = 0;
                while (inRange(nx, ny, board.length, board[0].length)) {
                    if (board[nx][ny] == 0) {
                        cost++;
                        nx += dx[dir];
                        ny += dy[dir];
                        continue;
                    } else {
                        if (cost < 2) {
                            break;
                        } else {
                            edges[i][board[nx][ny]] = Math.min(edges[i][board[nx][ny]], cost);
                            break;
                        }
                    }

                }
            }
        }

    }

    static List<int[]> bfs(int[][] board, Deque<int[]> idxs, int islandSize, boolean[][] visited) {

        List<int[]> result = new ArrayList<>();
        int[] firstIdx = idxs.peek();
        visited[firstIdx[0]][firstIdx[1]] = true;
        result.add(firstIdx);

        while (!idxs.isEmpty()) {
            int[] idx = idxs.pollFirst();
            for (int i = 0; i < 4; i++) {
                int nx = idx[0] + dx[i];
                int ny = idx[1] + dy[i];
                if (!inRange(nx, ny, board.length, board[0].length) || visited[nx][ny] || board[nx][ny] == 0) {
                    continue;
                }
                board[nx][ny] = islandSize;
                visited[nx][ny] = true;
                int[] newidx = { nx, ny };
                idxs.offerLast(newidx);
                result.add(newidx);
            }
        }

        return result;
    }

    static boolean inRange(int x, int y, int r, int c) {
        return (x >= 0 && x < r && y >= 0 && y < c);
    }

    static void union(int v1, int v2) {
        if (v1 < v2) {
            parents[v2] = v1;
        } else {
            parents[v1] = v2;
        }
    }

    static int find(int v) {
        if (parents[v] == v)
            return parents[v];
        return parents[v] = find(parents[v]);
    }
}
