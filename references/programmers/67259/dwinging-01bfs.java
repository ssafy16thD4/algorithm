// ref-title: DWinging — 경주로 건설 (0-1 BFS)
// ref-url: https://github.com/DWinging/Algorithm/blob/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/05/01/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EA%B2%BD%EC%A3%BC%EB%A1%9C%EA%B1%B4%EC%84%A4/Solution_01BFS.java
// ref-note: 비용이 100/600 두 가지뿐인 점을 이용해 우선순위 큐 대신 덱을 쓴다. 직진(+100)은 addFirst, 회전(+600)은 addLast. 상태를 (행, 열, 진행방향) 3차원으로 잡아 회전 비용을 정확히 구분한다. 원저자가 같은 폴더 solve.md 에 풀이 근거를 적어두었다(https://github.com/DWinging/Algorithm/tree/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/05/01/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EA%B2%BD%EC%A3%BC%EB%A1%9C%EA%B1%B4%EC%84%A4). 저장소에 라이선스 표기가 없어 코드 원문은 커밋 02325ae 기준 사본이며, 출처 링크를 지우지 말 것.
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
        
        Deque<Node> deque = new ArrayDeque<>();
        deque.addLast(new Node(0, 0, 0, 0));
        deque.addLast(new Node(0, 0, 0, 1));
        
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
        
        while(!deque.isEmpty()) {
            Node cur = deque.pollFirst();
            int y = cur.y;
            int x = cur.x;
            int cost = cur.cost;
            int d = cur.d;
            
            if(visited[y][x][d] < cost) continue;
            if(y == n - 1 && x == n - 1) continue;
            
            int ny = y + dist[d][0];
            int nx = x + dist[d][1];
            if(check(ny, nx, n) && board[ny][nx] == 0 && visited[ny][nx][d] > cost + 100) {
                deque.addFirst(new Node(ny, nx, cost + 100, d));
                visited[ny][nx][d] = cost + 100;
            }
            
            for(int i = 1; i < 4; i += 2) {
                int nd = (d + i) % 4;
                ny = y + dist[nd][0];
                nx = x + dist[nd][1];
                
                if(check(ny, nx, n) && board[ny][nx] == 0 && visited[ny][nx][nd] > cost + 600) {
                    deque.addLast(new Node(ny, nx, cost + 600, nd));
                    visited[ny][nx][nd] = cost + 600;
                }
            }
        }
        
        int res = visited[n-1][n-1][0] > visited[n-1][n-1][1] ? visited[n-1][n-1][1] : visited[n-1][n-1][0];
        return res;
    }
    
    private static boolean check(int y, int x, int n) {
        return y >= 0 && y < n && x >= 0 && x < n;
    }
}