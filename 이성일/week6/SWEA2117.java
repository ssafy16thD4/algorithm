import java.io.*;
import java.util.*;
 
public class Solution {
    /*
     * 모든 인덱스에서 BFS 시작
     * * 집 발생 시, 카운트, 비용 계산, 손해 안볼 때 최대 집이면 갱신
     * *
     */
     
    static int[][] board;
    static int N;
    static int M;
    static int maxHome;
    static int answer;
     
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1}; // 상하좌우
     
    public static void main(String[] args) throws Exception{
         
         
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
        int T = Integer.parseInt(br.readLine());
         
        for (int tc = 1; tc <= T; tc++) {
            String[] nm = br.readLine().trim().split(" ");
            N = Integer.parseInt(nm[0]);
            M = Integer.parseInt(nm[1]);
            answer = 1;
             
            board = new int[N][N];
             
            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine().trim());
                for (int j = 0; j < N; j++) {
                    board[i][j] = Integer.parseInt(st.nextToken());
                }
            }
             
            for (int i = 0; i < N; i++) {
                for(int j = 0; j < N; j++) {
                    if (board[i][j] == 1) maxHome++;
                }
            }
             
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    bfs(i, j);
                    if (answer == maxHome) break;
                }
            }
             
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);
             
        }
    }
     
    static int costCalc(int k) {
        return k * k + (k-1) * (k -1);
    }
     
    static boolean inRange(int x, int y) {
        return (x >= 0 && x < board.length && y >= 0 && y < board[0].length);
    }
     
    static boolean isProfitable(int k, int homes) {
        return (homes * M - costCalc(k) >= 0);
    }
     
    static void bfs(int x, int y) {
         
        int k = 1;
        boolean[][] visited = new boolean[board.length][board[0].length];
        Deque<int[]> deq = new ArrayDeque<>();
        int[] homeByDepth = new int[2 * N];
         
        visited[x][y] = true;
        if (board[x][y] == 1) homeByDepth[k] = 1;
        deq.offerLast(new int[] {x,y,k});
         
         
        while (!deq.isEmpty()) {
            int[] idx = deq.pollFirst();
             
            x = idx[0];
            y = idx[1];
            k = idx[2];
            for (int i = 0;  i < dx.length; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (!inRange(nx,ny)) continue;
                if (visited[nx][ny]) continue;
                 
                if (board[nx][ny] == 1) {
                    homeByDepth[k+1]++;
                }
                 
                visited[nx][ny] = true;
                deq.offerLast(new int[] {nx,ny,k+1});
                 
            }
        }
         
        for (int i = 1; i < homeByDepth.length; i++) {
            homeByDepth[i] += homeByDepth[i-1];
            if (isProfitable(i, homeByDepth[i])) {
                answer = Math.max(answer, homeByDepth[i]);
            }
        }
         
    }
}
