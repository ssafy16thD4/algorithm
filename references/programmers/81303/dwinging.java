// ref-title: DWinging — 표 편집 (연결 리스트 + 되돌리기 스택)
// ref-url: https://github.com/DWinging/Algorithm/blob/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/06/21/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%ED%91%9C_%ED%8E%B8%EC%A7%91/Solution.java
// ref-note: 각 행의 앞/뒤 인덱스를 배열 두 개로 들고 삭제를 O(1) 로 처리한다. 복구는 삭제 순서를 스택에 쌓았다가 되감는다. Z 명령마다 전체를 다시 세지 않는 게 핵심. 원저자가 같은 폴더 solve.md 에 풀이 근거를 적어두었다(https://github.com/DWinging/Algorithm/tree/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/06/21/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%ED%91%9C_%ED%8E%B8%EC%A7%91). 저장소에 라이선스 표기가 없어 코드 원문은 커밋 02325ae 기준 사본이며, 출처 링크를 지우지 말 것.
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