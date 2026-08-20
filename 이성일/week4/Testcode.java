import java.util.*;

public class Testcode {
    public static void main(String[] args) {
        String[][] tickets = { { "ICN", "SFO" }, { "ICN", "ATL" }, { "SFO", "ATL" }, { "ATL", "ICN" },
                { "ATL", "SFO" } };
        Map<String, List<String>> routes = new HashMap<>();
        List<String> sources;
        Set<String> set = new HashSet<>();
        for (String[] ticket : tickets) {
            if (routes.containsKey(ticket[0])) {
                routes.get(ticket[0]).add(ticket[1]);
            } else {
                routes.put(ticket[0], new ArrayList<>());
                routes.get(ticket[0]).add(ticket[1]);
            }
            set.add(ticket[0]);
            set.add(ticket[1]);
        }
        sources = new ArrayList<>(set);
        sources.sort((o1, o2) -> o1.compareTo(o2));
        for (String source : sources) {
            System.out.println(source);
        }
        for (String source : sources) {
            routes.get(source).sort((o1, o2) -> o1.compareTo(o2));
        }
    }
}
