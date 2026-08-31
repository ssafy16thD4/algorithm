/*
실패 코드
전략
- 시뮬레이션 구현
- 정확도는 100% 통과하나 시간 초과 발생

시행착오
- 2개가 만날때 둘을 소멸시키는 식으로 구현했었는데 
  이렇게 될 경우 3개의 원자가 만날때 3개가 다 소멸해야 하는데 1개만 남는 문제가 있었음.
*/

import java.io.*;
import java.util.*;

public class SWEA5648 {
	static final int[] dx = new int[] {0, 0, -1, 1};
	static final int[] dy = new int[] {1, -1, 0, 0};

	static int result;
	static int[][] board;
	static boolean[][] deleteBoard;
	static List<Atom> atomList;
	static int deleteCnt;
	static List<int[]> deleteLoc;
	static List<int[]> usedLoc;

	static class Atom{
		int id;
		int x;
		int y;
		int dir;
		int energy;
		boolean isDelete;

		Atom(int id, int x, int y, int dir, int energy){
			this.id = id;
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.energy = energy;
			isDelete = false;
		}

		public void move() {
			int nx = x + dx[dir];
			int ny = y + dy[dir];

			if(!inRange(nx, ny)) {
				board[x][y] = 0;
				isDelete = true;
				deleteCnt++;
				return;
			}

			if(board[nx][ny] == 0) {
				if(deleteBoard[nx][ny]) {
					isDelete = true;
					result += energy;
					deleteCnt++;
					return;
				}

				board[x][y] = 0;
				board[nx][ny] = id;
				usedLoc.add(new int[] {nx, ny});
				this.x = nx;
				this.y = ny;
			}
			else {
				Atom opponent = atomList.get(board[nx][ny] - 1);
				result += opponent.energy + this.energy;

				this.isDelete = true;
				opponent.isDelete = true;
				deleteCnt += 2;

				deleteLoc.add(new int[] {nx, ny});
				deleteBoard[nx][ny] = true;

				board[x][y] = 0;
				board[nx][ny] = 0;
				return;
			}
		}
	}

	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		board = new int[4001][4001];
		deleteBoard = new boolean[4001][4001];

		int T = Integer.parseInt(br.readLine());
		for(int t = 1; t <= T; t++) {
			int N = Integer.parseInt(br.readLine());
			atomList = new ArrayList<>(N + 1);
			result = 0;
			deleteCnt = 0;
			deleteLoc = new ArrayList<>();
			usedLoc = new ArrayList<>();

			for(int i = 1; i <= N; i++) {
				StringTokenizer st = new StringTokenizer(br.readLine());
				int x = 2000 + (Integer.parseInt(st.nextToken()) * 2);
				int y = 2000 + (Integer.parseInt(st.nextToken()) * 2);
				int dir = Integer.parseInt(st.nextToken());
				int energy = Integer.parseInt(st.nextToken());
				atomList.add(new Atom(i, x, y, dir, energy));
				board[x][y] = i;
				usedLoc.add(new int[] {x, y});
			}

			int moveCnt = 0;
			while(moveCnt++ <= 4000) {
				for(Atom a : atomList) {
					if(!a.isDelete) a.move();
				}

				if(deleteCnt == atomList.size()) break;

				for(int[] loc : deleteLoc) {
					deleteBoard[loc[0]][loc[1]] = false;
				}
				deleteLoc.clear();
			}

			for(int[] loc : usedLoc) {
				board[loc[0]][loc[1]] = 0;
			}
			for(int[] loc : deleteLoc) {
				deleteBoard[loc[0]][loc[1]] = false;
			}

			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}

	private static boolean inRange(int x, int y) {
		return (x >= 0 && x <= 4000 && y >= 0 && y <= 4000);
	}
}