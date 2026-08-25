package coding;

import java.io.*;
import java.util.*;

public class SWEA5653 {
	/*
	 * 시뮬레이션
	 * 줄기세포를 노드로 관리
	 * * 인덱스, 값, 현재 값
	 * * k시간동안 시뮬레이션 진행
	 * * * 살아있는 노드를 관리하는 덱
	 * 살아있는 줄기세포를 관리
	 * * 생성된 노드들을 관리하는 map 인덱스가 노드(equals overriding)
	 * * * true/false로 생존 여부 표현
	 * 죽어있는 줄기세포는 공간을 차지
	 * * 죽은 노드는 덱에서 상태관리 제외
	 * 번식 경합 상황 시, 큰 값으로 교체
	 * * 번식 시, map에서 세포 존재 확인 후 번식 존재 및 생존 세포일 경우, 
	 * * 해당 세포의 값만 수정
	 * 
	 */ // 아이디어 47분 걸림, 구현 15분 하다가멈춤
	// 멈춤 상황 입출력 받으며 노드를 덱에 넣는 중, 그리고 생성되었던 노드 관리 map 생성 계획 중
	
	
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
		public boolean equals(Object obj) {
			return (this.x == ((Node) obj).x && this.y == ((Node)obj).y);
		}
		
		
	}
	
	static Deque<Node> deq;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());
		
		for (int tc = 1; tc <= T; tc++) {
			// test loop
			deq = new ArrayDeque<>();
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
						deq.offerLast(new Node(i,j,v,v));
					}
				}
			}
		}
		
	}
}





















