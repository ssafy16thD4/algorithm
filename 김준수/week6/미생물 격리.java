/*
전략
- 시뮬레이션
- 미생물 군집을 클래스화. Comparable 구현해서 정렬 가능하게 함
    - 군집을 큰 순서대로 정렬해두면 군집 합칠 때 앞에 있는 군집이 반드시 더 큰 군집이라 방향값을 지배함
- 미생물의 next 좌표가 같을 때 미리 군집을 병합
    - 이렇게 하면 3개 이상의 군집이 충돌할 때 방향값 이상을 방지 가능
*/

package algorithm;

import java.io.*;
import java.util.*;

public class SWEA2382 {
	static final int[] dx = {0, 1, 0, -1};
	static final int[] dy = {1, 0, -1, 0}; 
	
	static int[][] board;
	static int N, M, K;
	static List<Cluster> clusterList;
	
	static class Cluster implements Comparable<Cluster>{
		int x, y, num, dir; 
		boolean active;
		int[] next;
		
		Cluster(int x, int y, int num, int dir){
			this.x = x;
			this.y = y;
			this.num = num;
			active = true;
			
			switch(dir) {
			case 1:
				this.dir = 3;
				break;
			case 2:
				this.dir = 1;
				break;
			case 3:
				this.dir = 2;
				break;
			case 4:
				this.dir = 0;
				break;
			}
			
			next = new int[2];
			next[0] = x + dx[this.dir];
			next[1] = y + dy[this.dir];
		}
		
		// 바라보는 방향으로 한 칸 움직이기
		// 만약 움직인 후 위치가 외곽선이면 med 호출
		void move() {
			this.x = next[0];
			this.y = next[1];
			
			next[0] = x + dx[dir];
			next[1] = y + dy[dir];
			
			if(isOuter()) med();
		}
		
		// 외곽선일 때 약품 처리
		void med() {
			num /= 2;
			if(num == 0) {
				active = false;
				return;
			}
			
			dir = (dir + 2) % 4;
			
			next[0] = x + dx[dir];
			next[1] = y + dy[dir];
		}
		
		// 다른 군집과 합치기
		void combine(Cluster c) {
			this.num += c.num;
			c.active = false;
		}
		
		boolean isOuter() {
			return x == 0 || y == 0 || x == N - 1 || y == N-1;
		}
		
		@Override
		public int compareTo(Cluster c) {
			return Integer.compare(c.num, this.num);
		}
	}
	
    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine().trim());
        for(int t = 1; t <= T; t++) {
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            K = Integer.parseInt(st.nextToken());
        
            board = new int[N][N];
            clusterList = new ArrayList<>();
            
            for(int i = 0; i < K; i++) {
            	st = new StringTokenizer(br.readLine().trim());
            	clusterList.add(new Cluster(
            			Integer.parseInt(st.nextToken()), 
            			Integer.parseInt(st.nextToken()), 
            			Integer.parseInt(st.nextToken()),
            			Integer.parseInt(st.nextToken())));
            }
            // num이 큰 순서대로 정렬을 해두면 combine할 때 앞에 있는 군집이 항상 메인 방향을 가져감
            Collections.sort(clusterList);
         
            // M 시간 동안 군집 이동 시뮬레이션
            for(int i = 0; i < M; i++) {
            	doCombine();
            	
            	for(Cluster c : clusterList) {
            		if(c.active) c.move();
            	}
            	
            	Collections.sort(clusterList);
            }
            
            int result = 0;
            for(Cluster c : clusterList) {
            	if(c.active) result += c.num;
            	
        	}
            
            sb.append("#").append(t).append(" ").append(result).append("\n");
        }
        System.out.print(sb);
    }
    
    // 모든 군집을 돌며 군집 간 next가 같으면 미리 합치기
    private static void doCombine() {
    	for(int i = 0; i < clusterList.size(); i++) {
    		for(int j = i + 1; j < clusterList.size(); j++) {
    			Cluster a = clusterList.get(i);
    			Cluster b = clusterList.get(j);
    			
    			if(!a.active || !b.active) continue;
    			
    			if(a.next[0] == b.next[0] && a.next[1] == b.next[1]) {
    				a.num += b.num;
    				b.active = false;
    			}
    		}
    	}
    }
}