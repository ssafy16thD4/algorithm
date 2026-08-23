/*
최대 턴수를 파악해서 각 턴에 공격이 들어오는지 파악
[0, 0, 0, -10, 0, -15] 이렇게 되어있으면 3턴에 10뎀 5턴에 15뎀

0인 동안에는 체력을 x만큼 회복하고 healCnt++
healCnt가 t면 체력을 y만큼 회복하고 0으로 초기화
*/

import java.util.*;

class Solution {
    public int solution(int[] bandage, int health, int[][] attacks) {
        int maxTurn = 0;
        int maxHealth = health;
        for(int[] attack : attacks){
            maxTurn = Math.max(attack[0], maxTurn);
        }
        
        int[] turnList = new int[maxTurn + 1];
        for(int[] attack : attacks){
            int turn = attack[0];
            turnList[turn] = turnList[turn] + attack[1];
        }
        
        // System.out.println(Arrays.toString(turnList));
        
        int plusTime = bandage[0];
        int heal = bandage[1];
        int plusHeal = bandage[2];
        int healCnt = 0;
        for(int i = 1; i < turnList.length; i++){
            if(turnList[i] == 0){
                health += heal;
                healCnt++;
                
                if(healCnt == plusTime){
                    health += plusHeal;
                    healCnt = 0;
                }
                
                if(health > maxHealth) health = maxHealth;
            }
            else{
                health -= turnList[i];
                healCnt = 0; // 이거 빼먹음
                if(health <= 0) return -1;
            }
        }
        
        return health;
    }
}