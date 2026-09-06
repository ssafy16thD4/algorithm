package algorithm;
import java.util.*;
import java.io.*;
/*
 * n개중에 m개를 맞췄을때 총점이 최소 인 경우 점수
 *
 * 알고리즘: 그리디 + 시뮬레이션
 *
 * 틀린 문제(X)는 카운터를 끊는 칸막이다. 칸막이를 최대한 흩뿌려
 * 맞춘 문제(O)를 여러 덩어리로 쪼개면 2배가 덜 터진다.
 * 덩어리 하나에 k-1개까지는 2배가 안 터진다.
 * 정원을 넘는 인원은 한 덩어리에 몰아야 2배 횟수가 최소가 되고,
 * 그 덩어리를 맨 앞에 두어야 2배가 작은 점수에 걸린다.
 *
 * 1. wrong = n - m           : 칸막이 개수
 * 2. group = min(wrong+1, m) : 덩어리 개수
 * 3. cap = group * (k-1)     : 2배 없이 담을 수 있는 정원
 * 4. over = max(0, m - cap)  : 정원 초과 인원
 * 5. 맨 앞 덩어리에 (k-1)+over, 나머지는 앞에서부터 k-1씩
 * 6. 완성된 배열을 문제 규칙대로 한 번 시뮬레이션
 */
public class Solution {
	static boolean[] isCorrect;
	static int n, m, k;
	static int minScore;
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		StringTokenizer st;

		int t = Integer.parseInt(br.readLine());

		for(int test_case=1; test_case<=t; test_case++) {
			st = new StringTokenizer(br.readLine());

			n = Integer.parseInt(st.nextToken()); // 문제수
			m = Integer.parseInt(st.nextToken()); // 맞춘수
			k = Integer.parseInt(st.nextToken()); // 카운터수

			isCorrect = new boolean[n];
			minScore = 0;

			build();
			minScore = calcScore();

			sb.append("#").append(test_case).append(" ").append(minScore).append("\n");
		}
		System.out.print(sb);
	}

	// 최소 점수가 나오는 O/X 배치를 직접 구성
	static void build() {
		int wrong = n - m;
		int group = Math.min(wrong + 1, m);
		int cap = group * (k - 1);
		int over = Math.max(0, m - cap);

		int rest = m;
		int idx = 0;

		for(int g=0; g<group; g++) {
			if(g > 0) idx++; // 덩어리 사이 칸막이 한 칸 건너뛰기

			int len = Math.min(k - 1, rest);
			rest -= len;
			if(g == 0) { // 초과 인원은 맨 앞 덩어리에 몰아넣기
				len += over;
				rest -= over;
			}

			for(int i=0; i<len; i++) {
				isCorrect[idx++] = true;
			}
		}
	}
	static int calcScore() {
		int count = 0;
		int score = 0;
		for(int i=0; i<n; i++) {
			if(!isCorrect[i]) {
				count = 0;
				continue;
			}
			count++;
			score += 1;
			if(count == k) {
				score *= 2;
				count = 0;
			}
		}
		return score;
	}
}
