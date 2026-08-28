import java.util.*;
import java.lang.*;
import java.io.*;
/*
    가장 적은 비용으로 수영장 이용방법 찾기
    : 가장 적은 비용출력

    경우의수
    : 1일이용권, 1달이용권, 3달이용권, 1년이용권
    
    1. 1~12월 이용계획을 보고 경우의 수를 탐색한다.
     1.1 1일 이용권을 구매하는 경우
     1.2 1달 이용권만 구매하는 경우
     1.3 3달 이용권만 구매하는 경우
     1.4 1년 이용권만 구매하는 경우
   2. 가장 최소 값을 출력한다.
*/
class Main {
    static int[] charge;
    static int[] month;
    static int minMoney;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int test_case=1; test_case<=t; test_case++) {
            charge = new int[4]; // 요금 4개
            st = new StringTokenizer(br.readLine());
            for(int i=0; i<4; i++) {
                charge[i] = Integer.parseInt(st.nextToken());
            }

            month = new int[12];
            st = new StringTokenizer(br.readLine());
            for(int i=0; i<12; i++) {
                month[i] = Integer.parseInt(st.nextToken());
            }

            minMoney = Integer.MAX_VALUE;
            dfs(0, 0);

            minMoney = Math.min(minMoney, charge[3]);

            sb.append("#").append(test_case).append(" ").append(minMoney).append("\n");
        }
        System.out.print(sb);
    }
    static void dfs(int money, int depth) {
        if(depth >= 12) {
            minMoney = Math.min(minMoney, money);
            return;
        }

        dfs(money + month[depth] * charge[0], depth+1); // 1일
        dfs(money + charge[1], depth+1);
        dfs(money + charge[2], depth+3);
    }
}
