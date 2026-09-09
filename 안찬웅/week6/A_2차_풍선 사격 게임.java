package algorithm;
import java.util.*;
import java.io.*;
/*
 * (월) A_2차_풍선 사격 게임
 * 자신감을 가지자!! 원리 자체를 이해하는 것에 집중
 * 방문처리를 어떤자료구조로 어떻게 표시할지 이런것들은 도구 일뿐
 * 
 * 풍선을 하나씩 터트리며 3가지 경우의수로 dfs 탐색하며 최대 점수 출력
 * 
 * 알고리즘: DFS, 순열, 백트래킹
 * 
 * 터트리는 시작 인덱스, 풍선 터트린 개수, 현재 점수
 * dfs(int depth, int score, int score)
 * - 풍선의 전체 개수만큼 depth가 되면 return;
 * - 좌 우 모두 풍선이 있으면 좌 * 우 점수 획득
 * - 좌 우중 하나만 있으면 좌 or 우의 점수 획득
 * - 좌 우에 풍선이 없으면 터진 풍선에 적힌 숫자를 점수로 획득
 */
public class Solution {
	static int[] arr;
	static boolean[] vis;
	static int n;
	static int maxScore;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			n = Integer.parseInt(br.readLine()); // 풍선의 개수
			
			arr = new int[n];
			vis = new boolean[n];
			
			st = new StringTokenizer(br.readLine());
			for(int i=0; i<n; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}
			
			maxScore = 0;
			dfs(0, 0);
			
			System.out.println(maxScore);
		}
	}

	private static void dfs(int depth, int score) {
		if(depth == n) {
			maxScore = Math.max(maxScore, score);
			return;
		}
		for(int i=0; i<n; i++) {
			if(vis[i]) continue;
			vis[i] = true;
			boolean leftFlag = false;
			boolean rightFlag = false;
			int leftIndex = -1;
			int rightIndex = -1;
			
			int curScore = 0;
			for(int j=i-1; j>=0; j--) {
				if(!vis[j]) {
					leftFlag = true;
					leftIndex = j;
					break;
				}
			}
			
			for(int j=i+1; j<n; j++) {
				if(!vis[j]) {
					rightFlag = true;
					rightIndex = j;
					break;
				}
			}
			
			if(leftFlag && rightFlag) {
				curScore = arr[leftIndex] * arr[rightIndex];
			}
			else if(leftFlag && !rightFlag) {
				curScore = arr[leftIndex];
			}
			else if(!leftFlag && rightFlag) {
				curScore = arr[rightIndex];
			}
			else {
				curScore = arr[i];
			}

			dfs(depth+1, score + curScore);
			vis[i] = false;
		}
	}
}
