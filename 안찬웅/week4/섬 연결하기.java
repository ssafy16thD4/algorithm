import java.util.*;
/*
    모든 섬을 최소 비용으로 연결하는 최소 신장 트리(MST) 비용 합 구하기
    
    알고리즘: 크루스칼(Kruskal), 정렬 + 유니온 파인드
    자료구조: int[][] costs 정렬, int[] parent
    
    1. 간선을 비용 오름차순 정렬
    2. 싼 것부터 집으면서 두 섬이 다른 덩어리면 연결하고 비용 누적
    3. 간선 n-1개를 채우면 종료
*/
class Solution {
    static int[] parent;
    public int solution(int n, int[][] costs) {
        int answer = 0;
        int edgeCnt = 0;     
        
        Arrays.sort(costs, (a, b) -> a[2] - b[2]);
        
        parent = new int[n];
        for(int i=0; i<n; i++) {
            parent[i] = i;
        }
        
        for(int i=0; i<costs.length; i++) {
            int a = costs[i][0];
            int b = costs[i][1];
            int cost = costs[i][2];
            
            if(union(a, b)) {
                answer += cost;
                edgeCnt++;
                if(edgeCnt == n-1) break;
            }
        }
        
        return answer;
    }
    static int find(int x) {
        if(parent[x] == x) {
            return x;
        }
        return parent[x] = find(parent[x]);
    }
    
    static boolean union(int a, int b) {
        a = find(a);
        b = find(b);
        
        if(a == b) {
            return false;
        }
        
        parent[b] = a;
        return true;
    }
}
