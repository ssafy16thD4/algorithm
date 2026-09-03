package practive;
import java.util.*;
import java.lang.*;
import java.io.*;
/*
 * 서비스 영역이이 K일 때 최대로 서비스할 수 있는 집의 개수
 * :손해를 보지 않는 한 최대한 많은 집에 홈방범 서비스 제공
 * 
 * 도시크기 N 지불비용 M 서비스영역 K
 * 집의위치: 1
 * 나머지: 0
 * 운영 비용 = K * K + (K - 1) * (K - 1)
 * 이익: K * M - 운영비용
 * 
 * 모든 경우의 수에 대해
 * 1. k가 하나씩 증가하며 테스트한다
 * 1.1 모든 경우의 수에 대해 k범위 만큼 테스트한다
 *     운영비용 구하기
 *     이익 구하기
 * 1.2 이익이 0보다 크거나 같으면 현재의 집의 수를 구한다.
 * 1.3 최대의 집의 수보다 현재의 집 수가 더 크면 갱신한다.
 */
class Solution {
	static int[][] graph;
	static int n, m;
	static int k;
	static int maxHouseCnt;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=10; test_case++) {        	
        	st = new StringTokenizer(br.readLine());
        	
        	n = Integer.parseInt(st.nextToken());
        	m = Integer.parseInt(st.nextToken());
        	
        	graph = new int[n][n];
        	
        	for(int i=0; i<n; i++) {
        		st = new StringTokenizer(br.readLine());
        		for(int j=0; j<n; j++) {
        			graph[i][j] = Integer.parseInt(st.nextToken());
        		}
        	}
        	
        	maxHouseCnt = 0;
        	k = 0;        	
        	while(k <= 1) {
        		k++;
	        	for(int i=0; i<n; i++) {
	        		for(int j=0; j<n; j++) {
	        			simulation(i, j);
	        		}
	        	}
        	}
            
        	sb.append("#").append(test_case).append(" ").append(maxHouseCnt).append("\n");
        }
        System.out.print(sb);
    }
    static void simulation(int x, int y) {
//      도시크기 N 지불비용 M 서비스영역 K
//      집의위치: 1, 나머지: 0
//    	운영 비용 = K * K + (K - 1) * (K - 1)
//    	이익: K * M - 운영비용
    	int fee = (k * k) + (k - 1) * (k - 1);
    	int profit = k * m - fee;
    	int houseCnt = 0; // 집의 개수
    	
    	if(fee < 0) return; // 요금이 더 적으면 볼필요 없음
    	//System.out.println("fee: " + fee + " profit: " + profit);
    	for(int i=y-k; i<=y+k; i++) {
    		//System.out.println("x: " + x + " i: " + i);
    		if(i < 0 || i >= n) continue;
    		if(graph[x][i] == 1) {
    			houseCnt++;
    		}
    	}
    	
    	for(int i=x-k; i<=x+k; i++) {
    		if(i < 0 || i >= n) continue;
    		if(graph[i][y] == 1) {
    			houseCnt++;
    		}
    	}
    	
    	if(graph[x][y] == 1) houseCnt--; 
    	
    	if(fee >= 0) {
    		maxHouseCnt = Math.max(maxHouseCnt, houseCnt);
    		System.out.println("houseCnt: " + houseCnt + " fee: " + fee + " profit: " + profit);
    	}
    }
}
