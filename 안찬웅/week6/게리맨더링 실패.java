package practive;
import java.util.*;
import java.lang.*;
import java.io.*;
/*
     모르겠습니다.
     입력 받기만 완료
 */
class Solution {
	static List<Integer>[] graph;
	static int[] pop;
	static int n;
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        
        n = Integer.parseInt(br.readLine()); // 구역 개수

        graph = new List[n+1];
        for(int i=1; i<=n; i++) {
        	graph[i] = new ArrayList<>();
        }
        
        pop = new int[n+1];
        st = new StringTokenizer(br.readLine());
        for(int i=1; i<=n; i++) {
        	pop[i] = Integer.parseInt(st.nextToken());
        }
        
        for(int i=1; i<=n; i++) {
        	st = new StringTokenizer(br.readLine());
        	int cnt = Integer.parseInt(st.nextToken());
            for(int j=0; j<cnt; j++) {
                int next = Integer.parseInt(st.nextToken());
                graph[i].add(next);
            }
        }
    }
}
