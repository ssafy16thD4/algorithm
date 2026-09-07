import java.util.*;
/*
    이익의 10% -> 추천인

    예외
    : 10%가 1원 미만이면 분배하지 않고 전액 본인이 갖는다
    : 추천인이 "-" 라도 10%는 본사 몫이라 본인은 90%만 갖는다

    알고리즘: DFS (추천 체인 거슬러 올라가기)

    1. parent<이름, 추천인> 을 미리 만들어 한 칸 올라가는 비용을 O(1)로 만든다
    2. 판매 건마다 distribute(판매자, amount*100) 를 호출한다
     2.1 up = money / 10
     2.2 up < 1 이면 전액 본인, 종료
     2.3 아니면 본인 money-up, 추천인이 있으면 up 을 들고 재귀
    3. total 을 enroll 순서대로 result 로 옮긴다
*/
class Solution { // 판매원이름, 다른 판매원이름, 판매량 집계 데이터의 판매원 이름
    static Map<String, Integer> total;  // 이름 -> 최종 금액
    static Map<String, String> parent;  // 이름 -> 추천인
    public int[] solution(String[] enroll, String[] referral, String[] seller, int[] amount) {
        total = new HashMap<>();
        parent = new HashMap<>();

        for(int i=0; i<enroll.length; i++) {
            parent.put(enroll[i], referral[i]);
            total.put(enroll[i], 0);
        }

        // 판매 건은 각각 따로 분배한다 (같은 판매원이 여러 번 팔 수 있다)
        for(int i=0; i<seller.length; i++) {
            distribute(seller[i], amount[i] * 100);
        }

        int[] result = new int[enroll.length];
        for(int i=0; i<enroll.length; i++) {
            result[i] = total.get(enroll[i]);
        }
        return result;
    }
    /*
        person 이 money 를 벌었을 때의 분배
        본인 90% / 추천인 10%. 10%가 1원 미만이면 분배를 멈춘다
    */
    static void distribute(String person, int money) {
        int up = money / 10;
        if(up < 1) { // 1원 미만이면 전액 본인
            total.merge(person, money, Integer::sum);
            return;
        }
        total.merge(person, money - up, Integer::sum);

        String p = parent.get(person);
        // 추천인이 없으면 up 은 본사 몫이라 버린다 (본인이 갖는 게 아니다)
        if(!p.equals("-")) {
            distribute(p, up);
        }
    }
}
