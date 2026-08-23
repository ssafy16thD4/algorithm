class Solution {
    public int solution(int[] stones, int k) {
        int answer = 0;
        int minimum = 200000001, maximum = 0;
        for (int i = 0; i < stones.length; i++) {
            if (stones[i] < minimum) {
                minimum = stones[i];
            }
            if (stones[i] > maximum)
                maximum = stones[i];
        }

        answer = bSearch(minimum, maximum, stones, k);

        return answer;
    }

    static int bSearch(int start, int end, int[] stones, int k) {

        while (start <= end) {

            int mid = (start + end) / 2;
            int streak = 0;
            boolean retry = false;

            for (int i = 0; i < stones.length; i++) {
                if (stones[i] < mid) {
                    streak++;
                } else {
                    streak = 0;
                }
                if (streak == k) {
                    // 현재 숫자보다 작은 범위를 탐색 해야한다.
                    retry = true;
                    break;
                }
            }
            if (retry) {
                end = mid - 1;
                continue;
            }

            start = mid + 1;

        }

        return end;

    }
}
