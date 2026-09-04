package week6;

import java.io.*;
import java.util.*;

public class 면접 {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for (int tc = 1; tc <= T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());
			int K = Integer.parseInt(st.nextToken());
		
			int[] board = new int[N+1];
			int idx = 1;
			while (M > 0 && idx < board.length) {
				
				for (int i = 0; i < K-1; i++) {
					if (idx + i >= board.length) break;
					if (M == 0) break;
					board[idx + i] = 1;
					M--;
				}
				
				idx += K;
			}
			
			if (M > 0) {
				for (int i = 1; i < board.length; i++) {
					if (M == 0) break;
					if (board[i] == 0 && M > 0) {
						board[i] = 1;
						M--;
					}
				}
			}
			
			int answer = 0;
			int streak = 0;
			for (int i = 1; i < board.length; i++) {
				if (board[i] == 0) {
					streak = 0;
					continue;
				} 
				if (streak == K-1 && board[i] == 1) {
					answer += 1;
					answer *= 2;
					streak = 0;
					continue;
				}
				streak++;
				answer++;
			}
			
			System.out.println("#" + tc + " " + answer);
		}
	}
}
