import java.io.*;
import java.util.*;
 
public class SWEA1952 {
     
     
    public static void main(String[] args) throws Exception {
         
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
         
        int T = Integer.parseInt(br.readLine().trim());
         
        for (int tc = 1; tc <= T; tc++) {
            int[] pees = new int[4];
            int[] months = new int[13];
            int[] dp = new int[13];
             
            StringTokenizer st = new StringTokenizer(br.readLine().trim());
            for (int i = 0; i < 4; i++) {
                pees[i] = Integer.parseInt(st.nextToken());
            }
             
            st = new StringTokenizer(br.readLine().trim());
            for (int i = 1; i < months.length; i++) {
                months[i] = Integer.parseInt(st.nextToken());
            }
             
             
            dp[1] = Math.min(pees[0]*months[1], pees[1]);
            dp[2] = Math.min(pees[0]*months[2] + dp[1], pees[1] + dp[1]);
             
            for (int i = 3; i < months.length; i++) {
                dp[i] = Math.min(dp[i-3] + pees[2], dp[i-1] + Math.min(pees[0]*months[i], pees[1])); 
            }
             
            int answer = Math.min(dp[dp.length - 1],pees[pees.length - 1]);
             
            StringBuilder sb = new StringBuilder();
            sb.append("#").append(tc).append(" ").append(answer);
            System.out.println(sb);
             
        }
         
    }
}