/*
전략
- 1일 → 1달 → 3달 → 1년 순으로 계산 후 비교해서 선택
- 1일 이용권 가격 * 그 달에 이용할 날의 수 > 1달 이용권이면 1달 이용권 선택
- 슬라이딩 윈도우로 1~3, 2~4 …의 현재 가격 합 계산
    - 만약 현재 가격 합 > 3달 이용권이면 3달 이용권으로 바꾸고 슬라이딩 윈도우를 end + 1 ~ end + 4로 변경
- 총 결정된 가격 > 1년 이용권이면 1년 이용권 가격으로 리턴. 아니면 총 결정 가격으로 리턴

시행착오
- 47/50 3개월 비교할 때 항상 앞에서 만나면 바로 3개월로 변경했음. 근데 반례가 생김
    - ex) 6 7 8 9월이 5 3 3 6이고 3개월이 10이면 ⇒ 10 0 0 6으로 바뀜. 근데 제일 작은건 5 10 0 0임.
    - 앞에서부터 검사한 총합이랑 뒤에서부터 검사한 총합을 비교해서 낮은 쪽으로 하는 로직 추가해서 해결
*/

import java.io.*;
import java.util.*;

public class SWEA1952 {
	public static void main(String[] args) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		int T = Integer.parseInt(br.readLine().trim());
		for(int t = 1; t <= T; t++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int day = Integer.parseInt(st.nextToken());
			int month = Integer.parseInt(st.nextToken());
			int month3 = Integer.parseInt(st.nextToken());
			int year = Integer.parseInt(st.nextToken());
			
			st = new StringTokenizer(br.readLine());
			int[] plan = new int[12];
			for(int i = 0; i < 12; i++) {
				plan[i] = Integer.parseInt(st.nextToken());
			}
			
			int[] moneyPlan = new int[12];
			
			// 일간 - 월간 비교 후 책정
			for(int i = 0; i < 12; i++) {
				int dayCost = day * plan[i];
				moneyPlan[i] = (dayCost > month) ? month : dayCost;
			}
			
			// 월간 - 3개월간 비교 후 책정
			// 앞에서부터 비교 후 책정
			int start = 0;
			int frontSum = 0;
			while(start < 12) {
				int monthCost = moneyPlan[start];
				if(start + 1 < 12) monthCost += moneyPlan[start + 1];
				if(start + 2 < 12) monthCost += moneyPlan[start + 2];
				
				if(monthCost > month3) {
					frontSum += month3;
					start += 3;
				}
				else {
					frontSum += moneyPlan[start];
					start++;	
				}
			}
			
			// 뒤에서부터 비교 후 책정
			int end = 11;
			int backSum = 0;
			while(end >= 2) {
				int monthCost = moneyPlan[end];
				monthCost += moneyPlan[end - 1];
				monthCost += moneyPlan[end - 2];
				
				if(monthCost > month3) {
					backSum += month3;
					end -= 3;
				}
				else {
					backSum += moneyPlan[end];
					end--;	
				}
			}
			backSum += moneyPlan[1] + moneyPlan[0];
			
			// 앞부터랑 뒤부터의 결과를 비교해서 작은쪽으로 책정
			int result = (frontSum < backSum) ? frontSum : backSum;
			
			// 연간과 최종 비교
			if(result > year) result = year;
			
			sb.append("#").append(t).append(" ").append(result).append("\n");
		}
		System.out.print(sb);
	}
}
/*
1
10 40 100 300
0 0 2 9 1 5 0 0 0 0 0 0
*/