import java.util.*;
/*
    N 을 i 번 써서 만들 수 있는 수를 dp.get(i) 에 모은다.
    dp.get(i) = { N 을 i개 이어붙인 수 } U { dp.get(j) (op) dp.get(i-j) }

    주의 두 가지
    1. num1 * num2 는 int 를 넘길 수 있다. long 으로 계산한 뒤 범위 안일 때만 담는다.
    2. 값의 상한을 1억으로 둔다. N 을 8개 이어붙인 수(99999999)까지는 살려야
       5555/5 같은 "큰 수를 나눠서 만드는" 경로가 막히지 않는다.
       음수와 0 은 어차피 답이 될 수 없어 버린다.
*/
class Solution {
    static final int LIMIT = 100000000;

    public int solution(int N, int number) {
        if (N == number) return 1;

        List<Set<Integer>> dp = new ArrayList<>();
        for (int i=0; i<=8; i++) {
            dp.add(new HashSet<>());
        }

        dp.get(1).add(N);

        for (int i=2; i<=8; i++) {
            // N 을 i개 이어붙인 수 (5555 같은 것)
            long repeat = 0;
            for (int j=0; j<i; j++) {
                repeat = repeat * 10 + N;
            }
            add(dp.get(i), repeat);

            for(int j = 1; j < i; j++) {
                int k = i - j;
                for(int num1 : dp.get(j)) {
                    for(int num2 : dp.get(k)) {
                        add(dp.get(i), (long) num1 + num2);
                        add(dp.get(i), (long) num1 - num2);
                        add(dp.get(i), (long) num1 * num2);
                        if (num2 != 0) {
                            add(dp.get(i), num1 / num2);
                        }
                    }
                }
            }

            if (dp.get(i).contains(number)) {
                return i;
            }
        }
        return -1;
    }
    // 1 ~ 1억 범위의 값만 담는다
    static void add(Set<Integer> set, long v) {
        if (v > 0 && v <= LIMIT) {
            set.add((int) v);
        }
    }
}
