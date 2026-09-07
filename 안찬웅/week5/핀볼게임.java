import java.util.*;
import java.io.*;
/*
 * 핀볼로 얻을 수 있는 점수의 최대값 구하기
 *
 * 알고리즘: 시뮬레이션 (모든 시작칸 × 4방향)
 *
 * 1. 빈 칸(0)마다 상하좌우 4방향으로 각각 공을 쏴 본다.
 *    - 같은 칸이라도 발사 방향이 다르면 점수가 다르다. 한 방향만 보면 과소평가된다.
 * 2. 한 번 쏘면 끝날 때까지 진행한다.
 *  2.1 벽: 제자리에서 반대 방향으로, 점수 +1
 *  2.2 블록(1~5): 그 칸으로 들어가서 모양대로 꺾이고, 점수 +1
 *      1 = 왼/아래가 막힌 "\" , 2 = 왼/위가 막힌 "/" ,
 *      3 = 위/오른쪽이 막힌 "\" , 4 = 오른/아래가 막힌 "/" , 5 = 사각형(항상 반대)
 *  2.3 웜홀(6~10): 같은 숫자의 반대편 웜홀로 순간이동, 방향 유지, 점수 없음
 *  2.4 블랙홀(-1): 종료
 *  2.5 출발 칸으로 돌아오면: 종료
 * 3. 최대 점수 출력
 */
public class Solution {
	// 0=우, 1=하, 2=좌, 3=상
	static int[] dx = {0, 1, 0, -1};
	static int[] dy = {1, 0, -1, 0};
	static int[][] graph;
	static int n;
	static int[][][] hole; // 웜홀 번호(6~10) -> 좌표 2개

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine().trim());

		for(int test_case=1; test_case<=t; test_case++) {
			n = Integer.parseInt(br.readLine().trim());

			graph = new int[n][n];
			hole = new int[11][2][2];
			int[] holeCnt = new int[11];

			for(int i=0; i<n; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<n; j++) {
					graph[i][j] = Integer.parseInt(st.nextToken());
					int v = graph[i][j];
					if(6 <= v && v <= 10) {
						hole[v][holeCnt[v]][0] = i;
						hole[v][holeCnt[v]][1] = j;
						holeCnt[v]++;
					}
				}
			}

			int maxScore = 0;
			for(int i=0; i<n; i++) {
				for(int j=0; j<n; j++) {
					if(graph[i][j] != 0) continue;
					for(int dir=0; dir<4; dir++) { // 발사 방향도 4가지 전부
						maxScore = Math.max(maxScore, play(i, j, dir));
					}
				}
			}
			sb.append("#").append(test_case).append(" ").append(maxScore).append("\n");
		}
		System.out.print(sb);
	}

	// (sx, sy)에서 dir 방향으로 쏜 공의 점수
	static int play(int sx, int sy, int dir) {
		int x = sx, y = sy;
		int score = 0;

		while(true) {
			int nx = x + dx[dir];
			int ny = y + dy[dir];

			// 2.1 벽: 제자리에서 반대 방향, 점수 +1
			if(nx < 0 || nx >= n || ny < 0 || ny >= n) {
				dir = (dir + 2) % 4;
				score++;
				continue;
			}

			int v = graph[nx][ny];

			// 2.4 블랙홀
			if(v == -1) return score;

			// 2.5 출발 칸으로 복귀
			if(nx == sx && ny == sy) return score;

			// 2.2 블록
			if(1 <= v && v <= 5) {
				dir = reflect(v, dir);
				score++;
				x = nx; y = ny;
				continue;
			}

			// 2.3 웜홀: 같은 숫자의 반대편으로, 방향 유지
			if(6 <= v && v <= 10) {
				if(hole[v][0][0] == nx && hole[v][0][1] == ny) {
					x = hole[v][1][0]; y = hole[v][1][1];
				} else {
					x = hole[v][0][0]; y = hole[v][0][1];
				}
				continue;
			}

			// 빈 칸: 그냥 지나간다
			x = nx; y = ny;
		}
	}

	// 블록 번호와 들어온 방향 -> 나가는 방향
	static int reflect(int block, int dir) {
		switch(block) {
			case 1: // 왼/아래가 막힘 -> 우,상은 되돌아오고 좌->상, 하->우
				if(dir == 0 || dir == 3) return (dir + 2) % 4;
				return (dir == 2) ? 3 : 0;
			case 2: // 왼/위가 막힘 -> 우,하는 되돌아오고 좌->하, 상->우
				if(dir == 0 || dir == 1) return (dir + 2) % 4;
				return (dir == 2) ? 1 : 0;
			case 3: // 위/오른쪽이 막힘 -> 하,좌는 되돌아오고 우->하, 상->좌
				if(dir == 1 || dir == 2) return (dir + 2) % 4;
				return (dir == 0) ? 1 : 2;
			case 4: // 오른/아래가 막힘 -> 좌,상은 되돌아오고 우->상, 하->좌
				if(dir == 2 || dir == 3) return (dir + 2) % 4;
				return (dir == 0) ? 3 : 2;
			default: // 5 = 사각형
				return (dir + 2) % 4;
		}
	}
}
