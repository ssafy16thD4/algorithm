import java.util.*;

class Solution {
    
    private class Node implements Comparable<Node> {
        int y, x, cost, d;
        
        Node(int y, int x, int cost, int d) {
            this.y = y;
            this.x = x;
            this.cost = cost;
            this.d = d;
        }
        
        @Override
        public int compareTo(Node n) {
            return this.cost - n.cost;
        }
    }
    
    public int solution(int[][] board) {
        int n = board.length;
        int[][][] visited = new int[n][n][4];
        int[][] dist = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(0, 0, 0, 0));
        pq.add(new Node(0, 0, 0, 1));
        
        int INF = Integer.MAX_VALUE;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                for(int k = 0; k < 4; k++) {
                    visited[i][j][k] = INF;
                }
            }
        }
        
        for(int i = 0; i < 4; i++) {
            visited[0][0][i] = 0;
        }
        
        while(!pq.isEmpty()) {
            Node cur = pq.poll();
            int y = cur.y;
            int x = cur.x;
            int cost = cur.cost;
            int d = cur.d;
            
            if(visited[y][x][d] < cost) continue;
            if(y == n-1 && x == n-1) return cost;
            
            int ny = y + dist[d][0];
            int nx = x + dist[d][1];
            if(check(ny, nx, n) && board[ny][nx] == 0 && visited[ny][nx][d] > cost + 100) {
                pq.add(new Node(ny, nx, cost + 100, d));
                visited[ny][nx][d] = cost + 100;
            }
            
            for(int i = 1; i < 4; i += 2) {
                int nd = (d + i) % 4;
                ny = y + dist[nd][0];
                nx = x + dist[nd][1];
                
                if(check(ny, nx, n) && board[ny][nx] == 0 && visited[ny][nx][nd] > cost + 600) {
                    pq.add(new Node(ny, nx, cost + 600, nd));
                    visited[ny][nx][nd] = cost + 600;
                }
            }
        }
        
        return -1;
    }
    
    private static boolean check(int y, int x, int n) {
        return y >= 0 && y < n && x >= 0 && x < n;
    }
}