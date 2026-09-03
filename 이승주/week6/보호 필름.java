package jinwoo.m09.swea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SWEA2112 {
	static int[][] film;
	static int d;
	static int w;
	static int k;
	static int answer;
	
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();
		
		int T = Integer.parseInt(in.readLine().trim());
		
		for(int t=0; t<T; t++) {
			StringTokenizer st = new StringTokenizer(in.readLine().trim());
			
			d = Integer.parseInt(st.nextToken());
			w = Integer.parseInt(st.nextToken());
			k = Integer.parseInt(st.nextToken());
			
			film = new int[d][w];
			
			for(int i=0; i<d; i++) {
				StringTokenizer fSt = new StringTokenizer(in.readLine().trim());
				for(int j=0; j<w; j++) {
					film[i][j] = Integer.parseInt(fSt.nextToken());
				}
			}
			answer = 1000000;
			dfs(0, 0);
		sb.append("#").append(t+1).append(" ").append(answer).append("\n");
		}
		System.out.println(sb);
	}
	
	static public void dfs(int depth, int drugAdministrationNum) {
		
		if(drugAdministrationNum >= answer) return;
		
		if(depth == d) {
			if(testFilm()) {
				if(answer > drugAdministrationNum) {
					answer = drugAdministrationNum;
				}
			}
			return;
		}
		
		// 약물 투여 X의 경우
			dfs(depth+1, drugAdministrationNum);
		for(int i=0; i<2; i++) {
			// 여기에 A,B로 채워넣기
			int[] preFilm = drugAdministration(i, depth, false, null);
			// dfs 후
			dfs(depth+1, drugAdministrationNum+1);
			// 백트래킹
			drugAdministration(i, depth, true, preFilm);
		}
	}
	
	static public int[] drugAdministration(int type, int depth, boolean isBack, int[] preFilm) {
		int[] newFilm = new int[w];
		if(!isBack) {
			for(int i=0; i<w; i++) {
				newFilm[i] = film[depth][i];
				film[depth][i] = type;
			}
			return newFilm;
		} else {
			for(int i=0; i<w; i++) {
				film[depth][i] = preFilm[i];
			}
			return null;
		}
	}
	
	static public boolean testFilm() {
		int[] test = new int[w];
		for(int i=0; i<d; i++) {
			for(int j=0; j<w; j++) {
				if(test[j] == k) continue;
				
				if(i==0) {
					test[j] = 1;
					continue;
				}
				
				if(film[i][j] == film[i-1][j]) {
					test[j]++;
				} else {
					test[j] = 1;
				}
			}
		}
		boolean isPass = true;
		
		for(int i=0; i<w; i++) {
			if(test[i] != k) isPass = false;
		}
		
		return isPass;
	}
	
}
