package algorithm;
import java.util.*;
import java.io.*;
/*
 * 동일한 특성의 셀들이 세로로 K이상 연속적으로 있으면 통과 
 * 약품 투입 횟수 최소 값 구하기
 * 
 * 약품을 넣으면 한쪽 행이 모두 약품 색으로 변한다 (1 or 0)
 * 
 * 알고리즘: DFS
 * 
 * 1. 각 행마다 3가지 선택 (그대로 두기 / A약품 0 / B약품 1)
 * 2. 행 0부터 d-1까지 3갈래로 뻗는다
 * 3. check()가 통과하면 answer 갱신 후 return
 * 4. cnt >= answer면 가지치기
*/
public class algo {
	static int[][] arr;
	static int d, w, k;
	static int answer;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine());
		
		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());
			d = Integer.parseInt(st.nextToken()); // 보호필름 두께
			w = Integer.parseInt(st.nextToken()); // 가로크기
			k = Integer.parseInt(st.nextToken()); // 합격기준
			
			arr = new int[d][w];
			
			for(int i=0; i<d; i++) { // 보호필름 단면 정보
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<w; j++) {
					arr[i][j] = Integer.parseInt(st.nextToken());
				}
			}
			
			answer = k;
			dfs(0, 0);
			
			sb.append("#").append(test_case).append(" ").append(answer).append("\n");
		}
		System.out.print(sb);
	}
	
	// 행 번호, 약품 투입횟수
	static void dfs(int row, int cnt) {
		if(check()) {
			answer = Math.min(answer, cnt);
			return;
		}
		
		// 이미 찾아둔 답만큼 써버렸으면
		if(cnt >= answer) return;
		
		// 행을 전부 소진했는데 위에서 합격이 안 났다
		if(row == d) return;
		
		// 1. 약품을 넣지 않음
		dfs(row + 1, cnt);
		
		int[] backup = arr[row].clone();
		
		// 2. 0으로 전체 덮기
		Arrays.fill(arr[row], 0);
		dfs(row+1, cnt+1);
		
		
		// 3. 1로 전체 덮기
		Arrays.fill(arr[row], 1);
		dfs(row+1, cnt+1);
		
		// 원래대로 되돌림
		arr[row] = backup;
	}
	
	static boolean check() {
		for(int i=0; i<w; i++) {
			int zeroCnt = 0;
			int oneCnt = 0;
			boolean flag = false;
			for(int j=0; j<d; j++) {
				if(arr[j][i] == 1) {
					oneCnt++;
					zeroCnt = 0;
					if(oneCnt >= k) {
						flag = true;
						break;
					}
				} else {
					zeroCnt++;
					oneCnt = 0;
					if(zeroCnt >= k) {
						flag = true;
						break;
					}
				}
			}
			if(!flag) return false;
		}
		return true;
	}
}
