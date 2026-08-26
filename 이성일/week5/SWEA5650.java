package com.ssafy.swb;

import java.io.*;
import java.util.*;

/*
 * 아이디어 40분 + 15분 + 36분 + 15분
 * 1. 시작 위치와 방향을 선택할 수 있다.
 * * 입력 배열을 돌며 0이면 핀볼을 놓고 4방향으로 게임을 시작한다.
 * * 게임 진행은 한칸씩 방향으로 한칸씩 이동
 * * 같은 디렉션으로 방문한적있는 칸에 도달하면 이 때 그것이 시작점이 아니면
 * * 해당 디렉션 포인트는 루프를 생성하므로 점수반영없이 게임을 종료한다.
 * * 디렉션별 방문 배열 필요함
 * * 블록을 만나면 카운트 1을 더하고 방향을 이동한다.
 * * 매 게임마다 카운트 변수가 필요하다
 * 2. 블록에 부딧히면 반사하고 점수를 센다. 벽에 부딪히면 반사만한다.
 * * 디렉션 배열 필요 상, 우, 좌, 하 (4 - dir - 1) -> 반대디렉션
 * * 1 -> 2,3 | 2 -> 0, 2 | 3 -> 0, 1 | 4 -> 1, 3 방향 디렉션으로 접근 시
 * * 각각 0, 1 | 1, 3 | 2, 3 | 0, 2 방향으로 반사된다.
 * * 1~4블록은 각각 디렉션 변화를 관리해주어야 한다.
 * * map<블록번호, 맵>으로 관리하기
 * 3. 웜홀에 부딧히면 pair웜홀로 이동한다, 이때 진행방향은 유지된다.
 * * map<블록번호, 리스트<인덱스 나열>> f(i,j) -> int[] {newi, newj};
 * * 리스트에서 처음 두 원소가 현재 인덱스라면 다음 두원소가 새로운 인덱스
 * * 아니라면 지금 두 원소가 새 인덱스
 * 4. 블랙홀에 부딧히거나, 처음위치에 돌아오면 게임은 종료한다.
 * * answer 변수를 두어 카운트값을 갱신한다.
 * 5. 모든 위치 모든 방향에 대해 게임을 하고 게임이 종료되면 점수의 최댓값을 구한다.
 */

public class SWEA5650 {

    static int[][] board;

    static int[] dx = { -1, 0, 0, 1 };
    static int[] dy = { 0, 1, -1, 0 };

    static Map<Integer, Map<Integer, Integer>> dirMapper;

    static Map<Integer, List<int[]>> pairWHoles;

    static boolean[][][] visited;

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        dirMapper = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            dirMapper.computeIfAbsent(i, k -> new HashMap<>());
        }
        dirMapInit();
        pairWHoles = new HashMap<>();
        for (int i = 6; i <= 10; i++) {
            pairWHoles.computeIfAbsent(i, k -> new ArrayList<>());
        }

        for (int tc = 1; tc <= T; tc++) {
            // tc start
            int boardSize = Integer.parseInt(br.readLine());
            int answer = 0;
            board = new int[boardSize][boardSize];

            for (int r = 0; r < boardSize; r++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int c = 0; c < boardSize; c++) {
                    int type = Integer.parseInt(st.nextToken());
                    if (type >= 6) {
                        pairWHoles.get(type).add(new int[] { r, c });
                    }
                    board[r][c] = type;
                }
            }
            
            visited = new boolean[4][boardSize][boardSize];
            
            for (int dir = 0; dir < 4; dir++) {
                for (int i = 0; i < boardSize; i++) {
                    for (int j = 0; j < boardSize; j++) {
                        if (board[i][j] == 0) {
                        	// 빈공간에서만 게임 시작 가능
//                            visited = new boolean[4][boardSize][boardSize];
                            answer = Math.max(answer, game(i, j, dir, board));
                        }
                        for (int d = 0; d < 4; d++) {
                        	for (int r = 0; r < board.length; r++) {
                        		Arrays.fill(visited[d][r], false);
                        	}
                        }
                    }
                }
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);
            
            for (int i = 6; i <= 10; i++) {
            	pairWHoles.get(i).clear();
            }
            
        }
    }

    static int game(int x, int y, int dir, int[][] board) {
		
		int startX = x, startY = y;
		int cnt = 0;
		x = x + dx[dir];
		y = y + dy[dir];
		
		while (true) {
			// 현재 위치와 방향 검증 먼저
			
			// 인레인지여야함 진행 시, inRange 확인 후 아웃 시, 방향 바꾸기
			if (!inRange(x,y,board.length)) {
				dir = 4 - dir - 1;
				x = x + dx[dir];
				y = y + dy[dir];
				cnt++;
			}
			// 종료 처리
			if (board[x][y] == -1) return cnt;
			if ((x == startX) && (y == startY)) return cnt;
			// 루프 형성 시 
			if (visited[dir][x][y]) return 0;
			
			// 이상없으면 방문 처리
			visited[dir][x][y] = true;
			
			// 새 방향과 좌표 설정
			if (board[x][y] == 0) {
				// 진행
				x = x + dx[dir];
				y = y + dy[dir];
				
			} else if (board[x][y] > 0 && board[x][y] < 5) {
				// 삼각형 블록일 시
				if (dirMapper.get(board[x][y]).get(dir) == null) {
					// 평행변 충돌 시
					dir = 4 - dir - 1;
				} else {					
					dir  = dirMapper.get(board[x][y]).get(dir);
				}
				x = x + dx[dir];
				y = y + dy[dir];
				cnt++;
				
				
			} else if (board[x][y] == 5) {
				// 디렉션만 반대로
				dir = 4 - dir - 1; 
				x = x + dx[dir];
				y = y + dy[dir];
				cnt++;
				
			} else if (board[x][y] >= 6) {
				if ((pairWHoles.get(board[x][y]).get(0)[0] == x) &&
						(pairWHoles.get(board[x][y]).get(0)[1] == y)) {
					// 웜홀 리스트 첫번째 원소가 현재 인덱스 일 떄
					int nx = pairWHoles.get(board[x][y]).get(1)[0];
					int ny = pairWHoles.get(board[x][y]).get(1)[1];
					nx += dx[dir];
					ny += dy[dir];
					x = nx; y = ny;
				} else {
					int nx = pairWHoles.get(board[x][y]).get(0)[0];
					int ny = pairWHoles.get(board[x][y]).get(0)[1];
					nx += dx[dir];
					ny += dy[dir];
					x = nx;
					y = ny;
				}
			}
			
		}
	}

    static boolean inRange(int x, int y, int n) {
        return ((x >= 0 && x < n) && (y >= 0 && y < n));
    }

    static void dirMapInit() {
        dirMapper.get(1).put(2, 0);
        dirMapper.get(1).put(3, 1);
        dirMapper.get(2).put(0, 1);
        dirMapper.get(2).put(2, 3);
        dirMapper.get(3).put(0, 2);
        dirMapper.get(3).put(1, 3);
        dirMapper.get(4).put(1, 0);
        dirMapper.get(4).put(3, 2);
    }
}
