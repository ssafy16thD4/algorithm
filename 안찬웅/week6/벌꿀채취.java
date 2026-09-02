/*
 * 가로로 연속한 m칸 덩어리를 2개 고른다 (서로 겹치면 안 됨)
 * 각 덩어리에서 일부 칸만 채취하되, 채취한 값의 합이 c 이하여야 한다
 * 채취한 값들의 제곱합이 최대가 되도록 한다
 *
 * 알고리즘: DFS(부분집합) + 조합
 *
 * 1. 모든 시작 좌표에서 덩어리 하나의 최대 수익을 구해 bestArr에 저장
 * 2. bestArr에서 겹치지 않는 두 좌표를 골라 합의 최대를 구한다
*/
class Solution {
	static int[][] graph;
	static int[][] bestArr;
	static int n, m, c;
	static int maxSum;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;
		int t = Integer.parseInt(br.readLine());

		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());

			n = Integer.parseInt(st.nextToken()); // 벌통 판 크기
			m = Integer.parseInt(st.nextToken()); // 덩어리 길이
			c = Integer.parseInt(st.nextToken()); // 최대 채취량

			graph = new int[n][n];
			bestArr = new int[n][n];

			for(int i=0; i<n; i++) {
				st = new StringTokenizer(br.readLine());
				for(int j=0; j<n; j++) {
					graph[i][j] = Integer.parseInt(st.nextToken());
				}
			}

			// 1. 시작 좌표별 덩어리 하나의 최대 수익 미리 계산
			for(int i=0; i<n; i++) {
				for(int j=0; j<=n-m; j++) { // j+m <= n 인 곳만 유효
					maxSum = 0;
					dfs(i, j, 0, 0, 0);
					bestArr[i][j] = maxSum;
				}
			}

			// 2. 겹치지 않는 두 덩어리 조합
			int answer = 0;
			for(int i1=0; i1<n; i1++) {
				for(int j1=0; j1<=n-m; j1++) {
					for(int i2=i1; i2<n; i2++) {
						// 같은 행이면 j1+m 부터, 다른 행이면 0부터
						int start = (i1 == i2) ? j1 + m : 0;
						for(int j2=start; j2<=n-m; j2++) {
							answer = Math.max(answer, bestArr[i1][j1] + bestArr[i2][j2]);
				}
			}

			sb.append("#").append(test_case).append(" ").append(answer).append("\n");
		}
		System.out.print(sb);
	}				

	// x, y: 덩어리 시작 좌표 / idx: 덩어리 안에서 몇 번째 칸 차례
	// sum: 채취량 합(c 검사용) / score: 제곱합(수익)
	static void dfs(int x, int y, int idx, int sum, int score) {

		if(sum > c) return;

		if(idx == m) {
			maxSum = Math.max(maxSum, score);
			return;
		}

		int v = graph[x][y + idx];

		dfs(x, y, idx+1, sum + v, score + v * v); // 채취한다
		dfs(x, y, idx+1, sum, score);             // 채취하지 않는다
	}
}
