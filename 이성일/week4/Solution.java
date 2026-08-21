package coding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
/*
 * 1. t를 입력받고 tc 별로 N과 K를 입력받는다.
 * 2. String numbers로 번호를 입력받는다.
 * 		ArrayDeque<Character>에 저장
 * 3. 변별로 회전시키며 숫자를 저장한다.
 * 		rotateAndSave(ArrayDeque<Character>, int idx)
 * 		deq을 (n/4)개씩 읽으며 Set<Integer>에 저장
 * 		10진수로 변환하는 함수 toDecimal(char[]) -> int
 * 		
 * 4. 숫자를 정렬한다.
 */
public class Solution {
	
	static Deque<Character> deq;
	static Set<Integer> set;
	static Map<Character, Integer> map;
	
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int t = Integer.parseInt(br.readLine());
		map = new HashMap<>();
		int init = 10;
		for (Character c: new String("ABCDEF").toCharArray()) {
			map.put(c, init++);
		}
		
		for (int tc = 1; tc <= t; tc++) {
			deq = new ArrayDeque<>();
			set = new HashSet<>();
			String[] nk =  br.readLine().split(" ");
			int n = Integer.parseInt(nk[0]);
			int k = Integer.parseInt(nk[1]);
			
			String numbers = br.readLine().trim();
			
			for (Character c: numbers.toCharArray()) {
				deq.add(c);
			}
			
			for (int i = 0; i < (n/4); i++) {
				rotateAndSave(deq, i);
			}
			
			int[] arr = setToArr(set);
			Arrays.sort(arr);
			
			StringBuilder sb = new StringBuilder();
			sb.append("#").append(tc).append(" ").append(arr[k-1]);
			System.out.println(sb);
		}
	}
	
	static void rotateAndSave(Deque<Character> deq, int idx) {
		if (idx != 0) {
			// 회전
			char c = deq.pollLast();
			deq.offerFirst(c);
		}
		for (int j = 0; j < deq.size()/4; j++ ) {
			char[] num = new char[deq.size()/4];
			for (int i = 0; i < num.length; i++) {
				num[i] = deq.pollFirst();
			}
			int decimal = toDecimal(num);
			set.add(decimal);
			for (int i = 0; i < num.length; i++) {
				deq.offerLast(num[i]);
			}
			
		}
	}
	
	static int toDecimal(char[] num) {
		int cnt = 0;
		for (int i = num.length - 1; i >= 0; i--) {
			if (num[num.length - i - 1] >= '0' &&
					num[num.length - i - 1] <= '9') {				
				cnt += (num[num.length - i - 1] - '0') * (int) Math.pow(16, i);
			} else {
				cnt += (int) Math.pow(16, i) * map.get(num[num.length - i - 1]);
			}
		}
		return cnt;
	}
	
	static int[] setToArr(Set<Integer> set) {
		int[] arr = new int[set.size()];
		int idx = 0;
		for (int i: set) {
			arr[idx++] = i;
		}
		return arr;
	}
}
