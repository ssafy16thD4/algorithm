class Solution {
    /*
     * k 사이즈의 슬라이딩 윈도우를 활용하여 0번부터 length - k인덱스 까지
     * 윈도우의 최댓값들 중 가장 작은 값이 뛸 수 있는 maximum 인원이다.
     * 
     * main 인덱스 범위 탐색, 윈도우 별로 최솟값 구하고 main에서 갱신
     * 
     * 윈도우 범위의 최댓값 구하는 함수
     * windowMax(int start, int end, stones) return maximum;
     * 
     * 
     */

    public int solution(int[] stones, int k) {
        int answer = Integer.MAX_VALUE;

        for (int i = 0; i <= stones.length - k; i++) {
            int start = i, end = i + k - 1;
            int[] val = windowMax(start, end, stones);
            if (val[1] < answer) {
                answer = val[1];
            }
            i = val[0];

        }
        return answer;
    }

    static int[] windowMax(int start, int end, int[] stones) {
        int[] maximum = new int[] { -1, 0 };
        for (int i = start; i <= end; i++) {
            if (stones[i] > maximum[1]) {
                maximum[0] = i;
                maximum[1] = stones[i];
            }
        }
        return maximum;
    }
}
