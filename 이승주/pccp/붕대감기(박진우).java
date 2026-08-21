import java.io.*;
import java.util.*;

class Solution {
    public int solution(int[] bandage, int health, int[][] attacks) {
        
        Deque<int[]> q = new ArrayDeque<>();
        
        for(int i=0; i<attacks.length; i++) {
            q.offer(attacks[i]);
        }
        
        int time = 0;
        int restHealth = health;
        int successTime = 0;
        
        while(!q.isEmpty() && restHealth > 0) {
            time++;
            successTime++;
            // 공격시 체력 깍고, 시전시간 초기화, 넘어가기
            if(q.peek()[0] == time) {
                int[] attack = q.poll();
                successTime = 0;
                restHealth -= attack[1];
                continue;
            }
            
            if(successTime != bandage[0]) {
                restHealth += bandage[1];
            } else {
                restHealth += (bandage[1] + bandage[2]);
            }
            
            if(restHealth >= health) restHealth = health;
            
        }
        
        if(restHealth <= 0) restHealth = -1;
        
        int answer = restHealth;
        return answer;
    }
}
