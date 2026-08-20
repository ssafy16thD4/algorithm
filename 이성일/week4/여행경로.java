import java.util.*;

class Solution {

    static boolean[] visited;

    public String[] solution(String[][] tickets) {
        String[] answer = {};
        visited = new boolean[tickets.length];

        Arrays.sort(tickets, (o1, o2) -> (o1[0].equals(o2[0]) ? o1[1].compareTo(o2[1]) : o1[0].compareTo(o2[0])));

        return answer;
    }
}
