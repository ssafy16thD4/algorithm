import java.util.*;

class Solution {

    static boolean[] visited;

    static String[][] tickets;

    static String[] answer;

    static boolean found;

    public String[] solution(String[][] tickets) {
        Solution.tickets = tickets;
        answer = new String[tickets.length + 1];
        visited = new boolean[tickets.length];
        answer[0] = "ICN";
        Arrays.sort(tickets, (o1, o2) -> (o1[0].equals(o2[0]) ? o1[1].compareTo(o2[1]) : o1[0].compareTo(o2[0])));
        dfs("ICN", 1);
        return answer;
    }

    static void dfs(String currentAirport, int cnt) {
        if (found)
            return;
        if (cnt == tickets.length + 1) {
            found = true;
            return;
        }
        for (int i = 0; i < tickets.length; i++) {
            if (visited[i])
                continue;
            if (!tickets[i][0].equals(currentAirport))
                continue;
            visited[i] = true;
            String temp = tickets[i][1];
            answer[cnt++] = temp;
            dfs(temp, cnt);
            if (found)
                return;
            cnt--;
            visited[i] = false;
        }
    }
}
