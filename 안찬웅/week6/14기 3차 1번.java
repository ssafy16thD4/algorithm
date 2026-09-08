package practive;
import java.util.*;
import java.io.*;
/*
 * 두 부분에 속한 두 개의 길이가 k인 연속부분 수열의 최대 합 구하기
 * 
 * K개 까지 합의 배열을 만든다
 * 나머지의 합중 최대 구하기
 */
public class Solution1 {
	static int[] arr;
	static int n, k;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());
			
			n = Integer.parseInt(st.nextToken());
			k = Integer.parseInt(st.nextToken());
			
			arr = new int[n];
			st = new StringTokenizer(br.readLine());
			for(int i=0; i<n; i++) {
				arr[i] = Integer.parseInt(st.nextToken());
			}

			int[] arrSum = new int[n-k+1];
			for(int i=0; i<n-k+1; i++) {
				int sum = 0;
				for(int j=i; j<i+k; j++) {
					sum += arr[j];
				}
				arrSum[i] = sum;
			}
			
			int maxSum = Integer.MIN_VALUE;
			for(int i=0; i<n-k+1; i++) {
				for(int j=i+1; j<n-k+1; j++) {
					int twoSum = arrSum[i] + arrSum[j];
					maxSum = Math.max(maxSum, twoSum);
				}
			}
			
			sb.append("#").append(test_case).append(" ").append(maxSum).append("\n");
		}
		System.out.print(sb);
	}
}
