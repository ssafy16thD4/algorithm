/*
실패 코드
- 현재 User와 바라보는 방향을 기준으로 보드를 반으로 갈랐을 떄 중 어디에 사과가 위치하냐에 따라 회전수가 정해짐
    - 위를 바라보고 있으면 사과가 왼쪽 반에 속하면 3번, 오른쪽 반에 속하면 2번
    - 아래를 바라보고 있으면 사과가 왼쪽 반에 속하면 2번, 오른쪽 반에 속하면 3번
    - 왼쪽을 바라보고 있으면 사과가 위쪽 반에 속하면 2번, 아래쪽 반에 속하면 3번
    - 오른쪽을 바라보고 있으면 사과가 위쪽 반에 속하면 3번, 아래쪽 반에 속하면 3번
    - 첫번째 사과는 반드시 1회 회전으로 아래를 보게 도착함. 다음 사과부터 해당 규칙 적용
- 이걸 코드화하면 {상 하 좌 우} 일때 기준으로
    - dir = 0일때 User의 y값보다 사과 y값이 작으면 3번 방향 왼쪽, 크면 2번 방향 아래쪽
    - dir = 1일때 User의 y값보다 사과 y값이 작으면 2번 방향 위쪽, 크면 3번 방향 오른쪽
    - dir = 2일때 User의 x값보다 사과 x값이 작으면 2번 방향 오른쪽, 크면 3번 방향 아래쪽
    - dir = 3일때 User의 x값보다 사과 x값이 작으면 3번 방향 위쪽, 크면 2번 방향 왼쪽
 */

package algorithm;

import java.io.*;
import java.util.*;

public class 사과먹기게임실패 {
	static final int[] dx = {-1, 1, 0, 0};
	static final int[] dy = {0, 0, -1, 1};
	
	static class User{
		int x, y, dir, cnt;
		
		User(int x, int y){
			this.x = x;
			this.y = y;
			this.dir = 1; // 첫 사과 먹으면 무조건 아래 방향
			cnt = 1;
		}
		
		public void move(int[] apple){
			switch(dir) {
			case 0: // 위쪽 보고 있을 때
				if(y > apple[1]) { // 왼쪽 반에 사과가 있을 때 
					cnt += 3;
					dir = 2;
				}
				else { // 오른쪽 반에 사과가 있을 때
					cnt += 2;
					dir = 1;
				}
				break;
			case 1: // 아래쪽 보고 있을 떄
				if(y > apple[1]) { // 왼쪽 반에 사과가 있을 때
					cnt += 2;
					dir = 0;
				}
				else { // 오른쪽 반에 사과가 있을 때
					cnt += 3;
					dir = 3;
				}
				
				break;
			case 2: // 왼쪽 보고 있을 때
				if(x > apple[0]) { // 위쪽 반에 사과가 있을 때
					cnt += 2;
					dir = 3;
				}
				else { // 아래쪽 반에 사과가 있을 때
					cnt+= 3;
					dir = 1;
				}
				break;
			case 3: // 오른쪽 보고 있을 때
				if(x > apple[0]) { // 위쪽 반에 사과가 있을 때
					cnt += 3;
					dir = 0;
				}
				else { // 아래쪽 반에 사과가 있을 때
					cnt += 2;
					dir = 2;
				}
				
				break;
			}
			
			x = apple[0];
			y = apple[1];
		}
	}
	
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine().trim());
		for(int t = 1; t <= T; t++) {
			int N = Integer.parseInt(br.readLine().trim());
			int[][] board = new int[N][N];
			List<int[]> apples = new ArrayList<>();
			
			for(int i = 0; i < N; i++) {
				char[] line = br.readLine().trim().toCharArray();
				for(int j = 0; j < N; j++) {
					board[i][j] = line[j] - '0';
					if(board[i][j] > 0) apples.add(new int[] {i, j, board[i][j]});
				}
			}
			
			apples.sort((a, b) -> {
				return a[2] - b[2];
			});
			
			for(int i = 0; i < 3; i++) {
				System.out.println(Arrays.toString(apples.get(i)));
			}
			
			User user = new User(apples.get(0)[0], apples.get(0)[1]);
			System.out.println(user.cnt);
			for(int i = 1; i < apples.size(); i++) {
				user.move(apples.get(i));
			}
			
			sb.append("#").append(t).append(" ").append(user.cnt).append("\n");
		}
		System.out.print(sb);
	}
}