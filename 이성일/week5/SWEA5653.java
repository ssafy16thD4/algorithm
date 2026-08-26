
import java.io.*;
import java.util.*;

public class SWEA5653 {

	static class Node {
		int x;
		int y;
		int val;
		int currVal;

		public Node(int x, int y, int val, int currVal) {
			this.x = x;
			this.y = y;
			this.val = val;
			this.currVal = currVal;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (!(o instanceof Node))
				return false;

			Node node = (Node) o;
			return x == node.x && y == node.y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}
	}

	static Deque<Node> deq;

	static Map<Node, Node> state;

	static int cnt;

	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int tc = 1; tc <= T; tc++) {
			// test loop
			deq = new ArrayDeque<>();
			state = new HashMap<>();

			String[] NMK = br.readLine().split(" ");
			int N = Integer.parseInt(NMK[0]);
			int M = Integer.parseInt(NMK[1]);
			int K = Integer.parseInt(NMK[2]);

			StringTokenizer st;
			for (int i = 0; i < N; i++) {
				st = new StringTokenizer(br.readLine());
				for (int j = 0; j < M; j++) {
					int v = Integer.parseInt(st.nextToken());
					if (v > 0) {
						Node nd = new Node(i, j, v, v);
						deq.offerLast(nd);
						state.put(nd, nd);
					}
				}
			}

			/*
			 * 시뮬레이션
			 * 줄기세포를 노드로 관리
			 * * 인덱스, 값, 현재 값
			 * * k시간동안 시뮬레이션 진행
			 * * * 살아있는 노드를 관리하는 덱
			 * 살아있는 줄기세포를 관리
			 * * 생성된 노드들을 관리하는 map 인덱스가 노드(equals overriding)
			 * 죽어있는 줄기세포는 공간을 차지
			 * * 죽은 노드는 덱에서 상태관리 제외
			 * 번식 경합 상황 시, 큰 값으로 교체
			 * * 번식 시, map에서 세포 존재 확인 후 번식 존재 및 생존 세포일 경우,
			 * * 해당 세포의 값만 수정
			 * 
			 */ // 아이디어 47분 걸림, 구현 15분 하다가멈춤 + 35분
			// 멈춤 상황 입출력 받으며 노드를 덱에 넣는 중, 그리고 생성되었던 노드 관리 map 생성 계획 중
			simulation(K);

			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(deq.size());
			System.out.println(sb);

		}

	}

	static void simulation(int k) {

		int t = 0;
		// 살아있는 덱을 담을 deque

		while (t < k) {

			Deque<Node> spares = new ArrayDeque<>();
			Map<Node, Node> births = new HashMap<>();

			while (!deq.isEmpty()) {
				// 노드 검증 (살아있는 지)
				// 활성화인지
				// 비활성화인지
				// currtime을 변경하며 val과 비교
				Node currNode = deq.pollFirst();
				if (currNode.currVal == 0) {
					// 번식 시작
					generate(currNode, births);

				}
				// currtime 변경 및 생존 여부 반영
				currNode.currVal -= 1;
				// 사망 시 제거
				if (currNode.currVal == (-1 * currNode.val)) {
					continue;
				}
				// 살아있는 노드는 일단 deq에 넣기
				spares.offerLast(currNode);

			}

			deq = spares;
			for (Node node : births.values()) {
				deq.offerLast(node);
				state.put(node, node);
			}

			t++;
		}

	}

	static void generate(Node node, Map<Node, Node> births) {

		int x = node.x;
		int y = node.y;

		for (int i = 0; i < 4; i++) {
			int newX = x + dx[i];
			int newY = y + dy[i];
			Node newNode = new Node(newX, newY, node.val, node.val);
			if (state.get(newNode) != null) {
				// 번식불가
				continue;
			} else {
				Node comp = births.get(newNode);
				if (comp == null) {
					births.put(newNode, newNode);
				} else {
					if (comp.currVal < newNode.currVal) {
						births.put(newNode, newNode);
					}
				}
			}

		}
	}

}
