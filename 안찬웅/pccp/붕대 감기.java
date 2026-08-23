import java.util.*;
/*
    붕대 감기: 1초마다 x만큼 체력 회복 + t초 연속으로 붕대를 감는 데 성공하면 y만큼 쳬력을 추가로 회복

    bandage: [기술의 시전시간, 1초당 회복량, 추가 회복량]
    attacks: [몬스터의 공격시간, 피해량]
    
    1. attacks 1초마다 순환
    1.1 몬스터의 공격
     1.1.1 현재시간과 몬스터의 공격시간이 일치할 때, 현재체력 - 피해량
    1.2 체력회복 bandage[1] 만큼 회복
     1.2.1 연속 성공 cnt++
     1.2.2 bandage[0]만큼 연속으로 성공하면 bandage[2]만큼 추가 회복
     1.2.3 만약 현재체력 + 피해량이 최대체력을 넘으면 현재체력 = 최대체력
*/
class Solution {
    static int curHealth = 0; // 현재체력
    public int solution(int[] bandage, int health, int[][] attacks) {
        curHealth = health;
        int cnt = 0; // 연속 성공
        int time = 0;
        int t = bandage[0]; // 시전 시간
        int x = bandage[1]; // 초당 회복량
        int y = bandage[2]; // 추가 회복량
        int attackTime = attacks[0][0]; // 공격 시간
        int damage = attacks[0][1]; // 피해량
        int bandageIdx = 0; // bandage 인덱스
        int attacksIdx = 0; // attacksIdx 인덱스
        int maxTime = attacks[attacks.length-1][0];
        while(curHealth != -1 && time < maxTime) {
            time++;
            if(time == attackTime) { // 몬스터의 공격
                // 1.1 현재체력 - 피해량
                curHealth -= damage;
                if(curHealth <= 0) { // 다이
                    curHealth = -1;
                }
                cnt = 0;
                attacksIdx++;
                if(attacksIdx < attacks.length) {
                    attackTime = attacks[attacksIdx][0];
                    damage = attacks[attacksIdx][1];
                }
            } else {
                // 체력회복 x 만큼 회복               
                // 2.1 연속 성공 cnt++
                curHealth += x;
                cnt++;
                if(curHealth > health) {
                    curHealth = health;
                }
                // 2.2 t만큼 연속으로 성공하면 y만큼 추가 회복
                if(cnt == t) {
                    curHealth += y;
                    if(curHealth > health) { // 현재체력+피해량이 최대체력을 넘으면 현재체력=최대체력
                        curHealth = health;
                    }
                    cnt = 0;
                }
            }
            //System.out.println("time: " + time + " curHealth: " + curHealth + " cnt: " + cnt);
        }
        return curHealth;
    }
}
