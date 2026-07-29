import java.util.*;
import java.io.*;

/*
 * 각 Core의 위치에 상하좌우중에 가장 가까운 곳 길이의 합
 * 
 * 멕시노스: n * n
 * 빈 cell: 0, core: 1
 * 
 * 알고리즘: 백트래킹
 * 
 * 1. 
 */
import java.util.*;
import java.io.*;
/*
 * 각 Core를 상하좌우 중 한 방향으로 격자 밖까지 연결 (전선끼리 교차 불가)
 * 1순위: 연결한 코어 개수 최대 / 2순위: 전선 길이 합 최소
 * 
 * 멕시노스: n * n
 * 빈 cell: 0, core: 1, 전선: 2
 * 
 * 알고리즘: 백트래킹
 */
public class SWEA1767 {
	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, -1, 0, 1};
	static int[][] arr; // 격자판
	static int n;
	public static void main(String args[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		for(int tc=1; tc<=t; tc++) {
			n = Integer.parseInt(br.readLine());
			
			
			
			for(int i=0; i<n; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<n; j++) {
					arr[i][j] = Integer.parseInt(st.nextToken()); 
				}
			}
			
		}
	}
}