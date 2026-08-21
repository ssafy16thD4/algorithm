/*
초가 지날때 분에 1/60, 시에 1/3600을 추가
1초 지나기 전과 후에 해당 분침과 시침을 지나는지 보고 지나면 추가하는거 모듈화

3번 다시 풀었는데 다 틀림
제일 많이 맞춘 B안으로 진행하고 4번 봐보기

1시간에 60 * 2 + 1번 울리는데
2852 / 24 = 2400 + 452 = 2400(100) + 240(10) + 112(96(4) + 16) 114 ~ 115번 정도 울린다

*/

import java.util.*;

class Solution {
    public int solution(int h1, int m1, int s1, int h2, int m2, int s2) {
        h1 = (h1 * 5) % 120; 
        h2 = (h2 * 5) % 120;
        
        int alarmCnt = 0;
        if(h1 % 60 == m1){
            alarmCnt++;
        }
        if(m1 == s1){
            alarmCnt++;
        }
        if(h1 % 60 == s1){
            alarmCnt++;
        }
        if(m1 == h1 % 60 && h1 % 60 == s1){
            if(m1 == 0) alarmCnt--;
            alarmCnt--;
        }
        
        // System.out.println("처음 " + alarmCnt);
        
        while(h1 != h2 || m1 != m2){    
            m1++;
            alarmCnt += 2;
            
            if(h1 % 60 == m1) alarmCnt -= 2;
            
            if(m1 == 60) {
                h1 = (h1 + 5) % 120;
                m1 = 0;
            }
            
            // System.out.println(h1 + " " + m1 + " " + s1 + " " + alarmCnt);
        }
        
        if(s1 < s2){
            while(s1 != s2 - 1){        
                s1++;
                
                if(m1 == s1){
                    alarmCnt++;
                }
                if(h1 % 60 == s1){
                    alarmCnt++;
                }
                if(m1 == h1 % 60 && h1 % 60 == s1){
                    alarmCnt -= 1;
                }
                
                // System.out.println("초계산 " + h1 + " " + m1 + " " + s1 + " " + alarmCnt);
            }
            
        }
        else if(s1 > s2){
            while(s1 != s2 + 1){
                s1--;
                
                if(m1 == s1){
                    alarmCnt--;
                }
                if(h1 % 60== s1){
                    alarmCnt--;
                }
                if(m1 == h1 % 60 && h1 % 60 == s1){
                    alarmCnt += 1;
                }
                
                // System.out.println("초계산 " + h1 + " " + m1 + " " + s1 + " " + alarmCnt);
            }
            if(h1 % 60 == 0){
                alarmCnt--;
            }
        }
        
        return alarmCnt;
    }
}