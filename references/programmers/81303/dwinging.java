import java.util.*;

class Solution {
    
    public String solution(int n, int k, String[] cmd) {
        Stack<Integer> stack = new Stack<>();
        int[] pre = new int[n + 2];
        int[] next = new int[n + 2];
        boolean[] check = new boolean[n + 2];
        
        for(int i = 1; i <= n; i++) {
            pre[i] = i - 1;
            next[i] = i + 1;
        }
        
        k++;
        for(String s : cmd) {
            char c = s.charAt(0);
            if(c == 'C') {
                check[k] = true;
                stack.push(k);
                
                next[pre[k]] = next[k];
                pre[next[k]] = pre[k];
                
                if(next[k] == n + 1) {
                    k = pre[k];
                } else {
                    k = next[k];
                }
            } else if(c == 'Z') {
                int cur = stack.pop();
                
                next[pre[cur]] = cur;
                pre[next[cur]] = cur;
                
                check[cur] = false;
                
            } else {
                int move = 0;
                for (int j = 2; j < s.length(); j++) {
                    move = move * 10 + (s.charAt(j) - '0');
                }
                
                if(c == 'D') {
                    while(move-- >= 1) {
                        k = next[k];
                    }
                } else {
                    while(move-- >= 1) {
                        k = pre[k];
                    }
                }
            }
        }
        
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i <= n; i++) {
            sb.append((check[i] ? 'X' : 'O'));
        }
        
        return sb.toString();
    }
}