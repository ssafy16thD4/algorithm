import java.io.*;
import java.util.*;

public class Solution {
    static int size;          
    static int[][] graph;     
    static int[] dx = {-1, 0, 1, 0};  
    static int[] dy = {0, -1, 0, 1};  

    static class Cell implements Comparable<Cell> {
        int r, c, life, time;         
        Cell(int r, int c, int life, int time) {
            this.r = r;
            this.c = c;
            this.life = life;          
            this.time = time;          
        }
        @Override
        public int compareTo(Cell o) {
            if (this.time != o.time) {
                return Integer.compare(this.time, o.time);   // 활성화 시각 오름차순
            }
            return Integer.compare(o.life, this.life);       // 생명력 내림차순
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int t = Integer.parseInt(br.readLine().trim());   

        for (int test = 1; test <= t; test++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());     
            int m = Integer.parseInt(st.nextToken());     
            int k = Integer.parseInt(st.nextToken());     

            size = Math.max(n, m) + 2 * k + 2;   // 사방 최대 k 확장 가정
            graph = new int[size][size];         

            PriorityQueue<Cell> pq = new PriorityQueue<>();
            int total = 0;    // 지금까지 태어난 세포 총합
            int dead = 0;     // K 시점까지 죽은 세포
            for (int i = 0; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < m; j++) {
                    int x = Integer.parseInt(st.nextToken());
                    if (x == 0) continue;        // 세포 없음

                    int r = k + i; 
                    int c = k + j;

                    graph[r][c] = x;                     
                    pq.add(new Cell(r, c, x, x));        
                    total++;
                }
            }

            for (int time = 1; time <= k; time++) {                  
                while (!pq.isEmpty() && pq.peek().time == time) {    
                    Cell cur = pq.poll();                            

                    if (cur.time + cur.life <= k) {   // 활성화 시각 + 생명력 = 사망 시각
                        dead++;                       // K 이하면 이미 죽음
                    }

                    if (time + 1 > k) continue;       // 자식은 time+1에 태어나니까 K 넘으면 생성 자체를 안 함

                    for (int d = 0; d < 4; d++) {
                        int nx = cur.r + dx[d];
                        int ny = cur.c + dy[d];

                        if (nx < 0 || nx >= size || ny < 0 || ny >= size) continue;
                        if (graph[nx][ny] != 0) continue;  

                        graph[nx][ny] = cur.life;  
                        pq.add(new Cell(nx, ny, cur.life, time + 1 + cur.life));
                        // 태어난 시각 + 비활성 기간 = 자식 활성화 시각
                        total++;
                    }
                }
            }

            sb.append("#").append(test).append(" ").append(total - dead).append("\n");
        }
        System.out.print(sb);
    }
}
