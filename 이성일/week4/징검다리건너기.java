import java.util.*;

class Solution {
    public int solution(int[] stones, int k) {
        int answer = Integer.MAX_VALUE;

        Deque<Integer> deq = new ArrayDeque<>();

        for (int i = 0; i < stones.length; i++) {

            while (!deq.isEmpty() && deq.peekFirst() < i - k + 1)
                deq.pollFirst();

            while (!deq.isEmpty() && stones[deq.peekLast()] <= stones[i])
                deq.pollLast();

            deq.offerLast(i);
            if (i >= k - 1) {
                answer = Math.min(answer, stones[deq.peekFirst()]);
            }
        }

        return answer;
    }
}
