import java.util.*;
import java.lang.*;
import java.io.*;
/*
    구슬을 맞추면 벽돌에 써있는 숫자-1 만큼 상하좌우의 벽돌이 없어지고
    그 칸을 다시 채움

    알고리즘: 시뮬레이션

    1. n번 벽돌을 던질 수 있음
    2. 보이는 숫자중 가장 큰 수를 부신다
    3. 부셔진 벽돌은 0으로 바꾸고 위에 숫자들을 한칸씩 땡김
*/
class Main {
    static int[] dx = {-1, 0, 1, 0};
    static int[] dy = {0, -1, 0, 1};
    static int[][] arr;
    static int n, w, h;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int t = Integer.parseInt(br.readLine());

        for(int tc=1; tc<=t; tc++) {
            st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken()); // n번
            w = Integer.parseInt(st.nextToken()); 
            h = Integer.parseInt(st.nextToken());

            arr = new int[h][w];
            
            for(int i=0; i<h; i++) {
                st = new StringTokenizer(br.readLine());
                for(int j=0; j<w; j++) {
                    arr[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            while(n > 0) {
                for(int i=0; i<w; i++) {
                    for(int j=0; j<h; j++) {
                        // [0][i] 순으로 체크하면서 2이상이면 구슬 맞추기
                        if(arr[j][i] != 0) {
                            // 터트리는 함수 (갯수 -1 만큼 0으로 변경)
                            bun(j, i);
                        }
                        
                        // 빈공간들 땡기기
                        
                    }
                }
                n--;
            }

            // 남은 벽돌 개수 세기
            int rockCnt = count();

            sb.append("#").append(tc).append(" ").append(rockCnt).append("\n");
        }
        System.out.println(sb.toString());
    }
    static void bun(int x, int y) {
        int size = arr[x][y];
        for(int i=1; i<=size-1; i++) {
            if(x-i < 0 || x+i >= h || y-i < 0 || y+i >= w) continue;
            if(arr[x-i][y] >= 1) arr[x-i][y] = 0;
            if(arr[x+i][y] >= 1) arr[x+i][y] = 0;
            if(arr[x][y-i] >= 1) arr[x][y-i] = 0;
            if(arr[x][y+i] >= 1) arr[x][y+i] = 0;
        }
        arr[x][y] = 0;
    }
    static int count() {
        int cnt = 0;
        for(int i=0; i<h; i++) {
            for(int j=0; j<w; j++) {
                if(arr[i][j] >= 1) cnt++;
            }
        }
        return cnt;
    }
}
