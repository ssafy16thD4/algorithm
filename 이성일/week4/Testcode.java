import java.util.*;

public class Testcode {
    public static void main(String[] args) {
        int[] stones = new int[] { 2, 4, 5, 3, 2, 1, 4, 2, 5, 1 };
        int minimum = 200000001, maximum = 0;
        for (int i = 0; i < stones.length; i++) {
            if (stones[i] < minimum) {
                minimum = stones[i];
            }
            if (stones[i] > maximum)
                maximum = stones[i];
        }
        System.out.println(minimum + "" + maximum);

        Deque<Integer> deq = new ArrayDeque<>();
        deq.peekFirst()
    }
}
