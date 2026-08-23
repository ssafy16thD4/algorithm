/*
같은 좌표에 로봇이 2대 이상 모였을 때를 카운트
i 로봇은 각 포인트들 간의 최단거리를 기반으로 이동, 순서는 행좌표 먼저 이동, 열좌표 나중에 이동
BFS로 각 로봇의 경로를 파악하고, 해당 경로를 ArrayList[] robots[i]에 저장
robots의 배열들을 0부터 t까지 쭉 돌면서 robots[0].get(0), robots[1].get(0)
얘네를 다 받아서 Set에 넣고 Set의 크기가 1보다 크면 cnt++
*/

import java.util.*;

class Solution {
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static List<int[]>[] robots;
    static int[][] points;
    static int[][] routes;
      
    public int solution(int[][] points, int[][] routes) {
        this.points = points;
        this.routes = routes;
        
        robots = new List[routes.length];
        for(int i = 0; i < routes.length; i++){
            robots[i] = new ArrayList<>();
        }
        
        // 각 로봇의 경로를 한 포인트씩 따라가면서 BFS로 최단경로 기록
        for(int i = 0; i < routes.length; i++){ // i : 로봇의 인덱스
            for(int j = 1; j < routes[i].length; j++){ // j : 루트 내 포인트의 인덱스
                if(robots[i].size() == 0) robots[i].add(points[routes[i][j - 1] - 1]);
                bfs(i, routes[i][j - 1], routes[i][j]);
            }
        }
        
        int maxLen = -1;
        for(int i = 0; i < robots.length; i++){
            maxLen = Math.max(maxLen, robots[i].size());
        }
        
        int answer = 0;
        
        for(int i = 0; i < maxLen; i++){
            Map<String, Integer> map = new HashMap<>();
            int robotCnt = robots.length;
            
            for(int j = 0; j < robots.length; j++){
                if(robots[j].size() <= i){
                    robotCnt--;
                    continue;
                }
                
                map.compute(String.format("%d %d", robots[j].get(i)[0], robots[j].get(i)[1]), (k, v) -> (v == null) ? 1 : v + 1);
            }
            
            // System.out.println(robotCnt + " " + map.toString());
            
            if(map.keySet().size() != robotCnt) {
                for(String key : map.keySet()){
                    int cnt = map.get(key);
                    if(cnt > 1) answer++;
                }
            }
        }
        
        return answer;
    }
    
    private void bfs(int robotIdx, int startIdx, int endIdx){
        int startX = points[startIdx - 1][0];
        int startY = points[startIdx - 1][1];
        int endX = points[endIdx - 1][0];
        int endY = points[endIdx - 1][1];
        int[][][] before = new int[101][101][2];
        for(int i = 0; i < 101; i++){
            for(int j = 0; j < 101; j++){
                before[i][j][0] = -1;
                before[i][j][1] = -1;
            }
        }
        before[startX][startY][0] = -2;
        before[startX][startY][1] = -2;
        
        Queue<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[]{startX, startY});
        
        while(!queue.isEmpty()){
            int[] curr = queue.poll();
            
            for(int i = 0; i < 4; i++){
                int nx = curr[0] + dx[i];
                int ny = curr[1] + dy[i];
                
                if(!inRange(nx, ny)){
                    continue;
                }
                
                if(before[nx][ny][0] == -1 && before[nx][ny][1] == -1){
                    queue.offer(new int[]{nx, ny});
                    before[nx][ny][0] = curr[0];
                    before[nx][ny][1] = curr[1];
                }
            }
            
            if(before[endX][endY][0] != -1 || before[endX][endY][1] != -1){
                break;
            }
        }
   
        // 경로 추가
        List<int[]> way = new ArrayList<>();
        int[] curr = new int[]{endX, endY};
        way.add(curr);
        
        while(curr[0] != -2 || curr[1] != -2){
            curr = new int[]{before[curr[0]][curr[1]][0], before[curr[0]][curr[1]][1]};
            way.add(curr);
        }
        
        for(int i = way.size() - 3; i >= 0; i--){
            robots[robotIdx].add(way.get(i));
        }
        
        // System.out.println((robotIdx + 1) + "번째 로봇의 경로");
        // for(int i = 0; i < robots[robotIdx].size(); i++){
        //     System.out.println(robots[robotIdx].get(i)[0] + " " + robots[robotIdx].get(i)[1]);
        // }
        // System.out.println();
    }
    
    private boolean inRange(int x, int y){
        return (x >= 0 && x <= 100 && y >= 0 && y <= 100);
    }
}