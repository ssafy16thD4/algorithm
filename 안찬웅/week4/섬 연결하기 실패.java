import java.util.*;
/*
    최소의 비용으로 모든 섬이 서로 통행 가능하도록
    
    알고리즘: 다익스트라, 우선순위큐
    
    섬의개수: n
    다리건설 비용: costs -> 길이: (n-1) * n/2
    cost[i][0] costs[i][1]: 다리가 연결되는 두 섬의 번호
    cost[i][2]: 다리를 건설할 때 드는 비용

    1. 인접리스트 만들기
    2. BFS로 돌리면서 비용이 가장 적은 최단거리 구하기
*/
class Solution {
    public int solution(int n, int[][] costs) {
        int answer = 0;
        
        List<Edge>[] graph = new List[n];
        
        for(int i=0; i<n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for(int i=0; i<costs.length; i++) {
            int a = costs[i][0];
            int b = costs[i][1];
            int cost = costs[i][2];
            graph[a].add(new Edge(b, cost));
            graph[b].add(new Edge(a, cost));
        }
        
        System.out.println(Arrays.toString(graph));
        
        // 출발하는 지점은 어디인가
        dijkstra(0);
        
        return answer;
    }
    static void dijkstra(int node) {
        PriorityQueue<int[]> pq = new PriorityQueue<>();
        vis[x][y] = true;
        
        while(!pq.isEmpty()) {
            int cur = pq.poll();
            
        }
    }
    class Edge {
        int node;
        int cost;
        
        public Edge(int node, int cost) {
            this.node = node;
            this.cost = cost;
        }
        
        @Override
        public String toString() {
            return node + " " + cost;
        }
    }
}
