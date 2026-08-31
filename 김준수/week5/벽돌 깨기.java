/*
전략
- 완전탐색 백트래킹
- 구슬이 갈 수 있는 모든 열의 경우의 수를 완전탐색
- board의 상태를 저장해두었다가 백트래킹할 때 복구하기

시행착오
- System.arraycopy로 바로 2차원 배열을 복사했었는데 실제로 배열 내의 배열까지 깊은 복사가 안되고 있었다.
  이를 해결하기 위해 2차원 배열의 각 1차원 배열을 for문으로 순회하며 깊은 복사를 진행해서 해결했다.
- 구현 과정과 디버깅이 많이 복잡해서 변수나 조건문의 수치를 조정하는 데에 많은 시간을 썼다.
*/

import java.io.*;
import java.util.*;

public class SWEA5656 {
	static int[] dx = new int[] { -1, 1, 0, 0 };
	static int[] dy = new int[] { 0, 0, -1, 1 };
	static int N, W, H;
	static int[][] board;
	static int result;
	static int total;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		int T = Integer.parseInt(br.readLine());

		for (int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			N = Integer.parseInt(st.nextToken());
			W = Integer.parseInt(st.nextToken());
			H = Integer.parseInt(st.nextToken());

			board = new int[H][W];
			result = Integer.MAX_VALUE;
			total = 0;

			for (int i = 0; i < H; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < W; j++) {
					board[i][j] = Integer.parseInt(st.nextToken());
				
					if(board[i][j] > 0) {
						total++;
					}
				}
			}


			// 2차원 배열이라 각 배열 요소에 깊은 복사 필요
			int[][] prevBoard = new int[H][W];
			for(int i = 0; i < H; i++) {
				System.arraycopy(board[i], 0, prevBoard[i], 0, W);
			}

			for (int w = 0; w < W; w++) {
				// 백트래킹 깊이, 어느 열에 던질지, 현재까지 파괴한 벽돌 수
				backtrack(0, w, 0); 
				
				// board 복구
				for(int i = 0; i < H; i++) {
					System.arraycopy(prevBoard[i], 0, board[i], 0, W);
				}
			}

			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}

	private static void backtrack(int depth, int index, int brick) {
		if(depth == N) {
			result = Math.min(result, (total - brick));
			return;
		}
		
		// index 열의 가장 위의 벽돌 찾기
		int[] top = null;
		for(int i = 0; i < H; i++) {
			if(board[i][index] >= 1) {
				top = new int[3];
				top[0] = i;
				top[1] = index;
				top[2] = board[i][index];
				break;
			}
		}
		
		// 만약 빈 열이면 현재를 기준으로 결과 갱신
		// ex) 0 0 0 1인 문제인데 공 3개 던져야 하면 이런 경우 필요함
		if(top == null) {
			result = Math.min(result, (total - brick));
			return;
		}
		
		Queue<int[]> queue = new ArrayDeque<>();
		queue.offer(top);
		brick++;
		board[top[0]][top[1]] = 0;
		
		// 벽돌 제거
		while(!queue.isEmpty()) {
			int[] curr = queue.poll();
			
			// 벽돌 숫자가 1이면 해당 벽돌만 제거하고 종료
			if(curr[2] == 1) {
				continue;
			}
			
			// 벽돌 숫자가 2 이상이면 십자로 벽돌을 제거하고
			// 터트린 벽돌을 queue에 추가해서 연쇄 제거 구현
			for(int i = 0; i < 4; i++) {
				for(int j = 1; j < curr[2]; j++) {
					int nx = curr[0] + dx[i] * j;
					int ny = curr[1] + dy[i] * j;
					
					if(!inRange(nx, ny)) {
						break;
					}
					
					if(board[nx][ny] > 0) {
						queue.offer(new int[] {nx, ny, board[nx][ny]});
						brick++;
						board[nx][ny] = 0;
					}
				}
			}
		}
		
		gravity();
		
		// 현재 board를 백업
		int[][] prevBoard = new int[H][W];
		for(int i = 0; i < H; i++) {
			System.arraycopy(board[i], 0, prevBoard[i], 0, W);
		}
		
		// 모든 열에 다음 구슬 던지기
		for(int w = 0; w < W; w++) {
			
			backtrack(depth + 1, w, brick);
			
			// board 복구하기
			for(int i = 0; i < H; i++) {
				System.arraycopy(prevBoard[i], 0, board[i], 0, W);
			}
		}
	}

	private static void gravity() {
		// board의 모든 칸을 역순으로 돌며
		for (int i = H - 1; i >= 0; i--) {
			for (int j = W - 1; j >= 0; j--) {
				// 현재 칸이 맨 밑이면 스킵
				if(i == H - 1) {
					continue;
				}
				
				// 만약 현재 칸이 벽돌이면서 밑이 비어있으면 아래로 이동
				if (board[i][j] >= 1 && board[i + 1][j] == 0) {
					for(int k = 1; k < H; k++) {
						// 비어있는 맨 밑까지 도달했으면
						if(i + k == H - 1 && board[i + k][j] == 0) {
							board[i + k][j] = board[i][j];
							board[i][j] = 0;
							break;
						}
						// 다음 벽돌까지 도달했으면
						else if(board[i + k][j] >= 1) {
							board[i + k - 1][j] = board[i][j];
							board[i][j] = 0;
							break;
						}
					}
				}
			}
		}
	}

	private static boolean inRange(int nx, int ny) {
		return nx >= 0 && nx < H && ny >= 0 && ny < W;
	}
}
