import java.util.*;
/*
    표의 행을 선택,삭제,복구하는 프로그램
    :처음 표와 비교해서 삭제되었으면 X 아니면 O

    알고리즘: 시뮬레이션, 이중 연결 리스트

    ArrayList 의 remove/add(idx, ...) 는 뒤 원소를 전부 밀어서 O(N)이다.
    prev/next 배열로 연결만 끊었다 붙이면 삭제·복구가 O(1)이 된다.

    1. 프로그램
     1.1 U X : 현재 선택된 행에서 X칸 위에 있는 행 선택
     1.2 D X : 현재 선택된 행에서 X칸 아래에 있는 행 선택
     1.3 C   : 현재 선택된 행 삭제 후, 바로 아래 행 선택
               만약 마지막행이면 삭제된 후 마지막행 선택
     1.4 Z   : 가장 최근에 삭제된 행을 원래대로 복구. 단, 현재 선택된 행은 바뀌지 않습니다.

    n: 표의 행 개수
    k: 처음에 선택된 행의 위치
    cmd: 명령어
*/
class Solution {
    public String solution(int n, int k, String[] cmd) {
        int[] prev = new int[n];
        int[] next = new int[n];
        for(int i=0; i<n; i++) {
            prev[i] = i - 1;    // -1 = 위쪽 끝
            next[i] = i + 1;    //  n = 아래쪽 끝
        }

        boolean[] deleted = new boolean[n];
        Deque<Integer> removed = new ArrayDeque<>(); // 삭제된 "행 번호"를 쌓는다
        int cursor = k;

        for(int i=0; i<cmd.length; i++) {
            char command = cmd[i].charAt(0);

            if(command == 'U') {
                int move = Integer.parseInt(cmd[i].substring(2));
                for(int j=0; j<move; j++) cursor = prev[cursor];
            } else if(command == 'D') {
                int move = Integer.parseInt(cmd[i].substring(2));
                for(int j=0; j<move; j++) cursor = next[cursor];
            } else if(command == 'C') {
                // 현재 행 삭제 — 행 번호 자체를 쌓아둔다 (커서가 아니다)
                deleted[cursor] = true;
                removed.push(cursor);

                int p = prev[cursor];
                int nx = next[cursor];
                if(p >= 0) next[p] = nx;
                if(nx < n) prev[nx] = p;

                // 바로 아래 행 선택, 마지막 행이었으면 위쪽 행 선택
                cursor = (nx < n) ? nx : p;
            } else { // Z
                // 가장 최근에 삭제된 행을 원래 자리에 다시 끼운다. 커서는 그대로.
                int r = removed.pop();
                deleted[r] = false;

                int p = prev[r];
                int nx = next[r];
                if(p >= 0) next[p] = r;
                if(nx < n) prev[nx] = r;
            }
        }

        // 비교 후, 삭제됐으면 X 남아있으면 O
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<n; i++) {
            sb.append(deleted[i] ? 'X' : 'O');
        }
        return sb.toString();
    }
}
