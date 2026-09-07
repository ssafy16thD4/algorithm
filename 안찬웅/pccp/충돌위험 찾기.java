import java.util.*;
/*
    로봇들이 같은 시각에 같은 칸에 두 대 이상 있으면 그 (시각, 칸)이 위험이다.
    위험이 몇 번 발생하는지 센다.

    알고리즘: 경로 펼치기 + 시각별 카운팅

    1. 로봇마다 경로를 1초 단위 좌표 목록으로 미리 펼친다.
     1.1 이동 규칙은 "행(r)을 먼저 맞추고 그다음 열(c)" 이다. 최단경로가 여러 개일 때
         어느 것을 쓰는지가 답을 바꾸므로 이 순서를 지켜야 한다.
    2. 시각 t 마다 살아있는 로봇들의 좌표를 세서, 2대 이상인 칸의 개수를 더한다.
     2.1 경로가 끝난 로봇은 사라지므로 세지 않는다.

    좌표는 100 이하라 r*1000+c 로 한 칸을 정수 하나로 눌러 담는다.
*/
class Solution {
    public int solution(int[][] points, int[][] routes) {
        List<List<int[]>> paths = new ArrayList<>();
        int maxT = 0;

        for(int i=0; i<routes.length; i++) {
            List<int[]> path = new ArrayList<>();
            int[] start = points[routes[i][0] - 1];
            int r = start[0];
            int c = start[1];
            path.add(new int[]{r, c});

            for(int j=1; j<routes[i].length; j++) {
                int[] to = points[routes[i][j] - 1];
                while(r != to[0]) {            // 행 먼저
                    r += (to[0] > r) ? 1 : -1;
                    path.add(new int[]{r, c});
                }
                while(c != to[1]) {            // 그다음 열
                    c += (to[1] > c) ? 1 : -1;
                    path.add(new int[]{r, c});
                }
            }
            paths.add(path);
            maxT = Math.max(maxT, path.size());
        }

        int answer = 0;
        for(int t=0; t<maxT; t++) {
            Map<Integer, Integer> cnt = new HashMap<>();
            for(List<int[]> p : paths) {
                if(t >= p.size()) continue;    // 이미 도착해 사라진 로봇
                int[] pos = p.get(t);
                cnt.merge(pos[0] * 1000 + pos[1], 1, Integer::sum);
            }
            for(int v : cnt.values()) {
                if(v >= 2) answer++;
            }
        }
        return answer;
    }
}
