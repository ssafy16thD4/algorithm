import java.util.*;
/*
    불량 사용자의 글자

    user_id: 이벤트 응모자 아이디 목록
    banned_id: 불량 사용자 아이디 목록

    묻는 것은 "banned_id 마다 서로 다른 user_id 를 하나씩 배정하는 경우의 수"다.
    banned_id 하나당 매칭 개수를 세서 max 를 취하는 것과는 전혀 다른 값이다.

    알고리즘: DFS(백트래킹) + Set 중복 제거

    1. banned_id[depth] 에 매칭되는 user_id 인덱스를 하나 고른다 (이미 쓴 인덱스는 건너뛴다)
    2. depth == banned_id.length 면 고른 인덱스 집합을 정렬해 Set 에 넣는다
     2.1 배정 순서만 다른 같은 집합은 하나로 합쳐진다
    3. Set 의 크기가 답
*/
class Solution {
    static Set<String> answers;
    static boolean[] used;
    static String[] user;
    static String[] banned;
    public int solution(String[] user_id, String[] banned_id) {
        answers = new HashSet<>();
        used = new boolean[user_id.length];
        user = user_id;
        banned = banned_id;

        dfs(0);

        return answers.size();
    }
    static void dfs(int depth) {
        if(depth == banned.length) {
            // 고른 인덱스 집합을 문자열 키로 (인덱스 오름차순이라 순서 차이가 사라진다)
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<used.length; i++) {
                if(used[i]) sb.append(i).append(',');
            }
            answers.add(sb.toString());
            return;
        }
        for(int i=0; i<user.length; i++) {
            if(used[i]) continue;
            if(!verse(user[i], banned[depth])) continue;
            used[i] = true;
            dfs(depth + 1);
            used[i] = false;
        }
    }
    // user 가 banned 패턴(* 는 아무 문자)과 맞는지
    static boolean verse(String str1, String str2) {
        if(str1.length() != str2.length()) return false;
        for(int i=0; i<str1.length(); i++) {
            char ch1 = str1.charAt(i);
            char ch2 = str2.charAt(i);
            if(ch2 != '*') {
                if(ch1 != ch2) {
                    return false;
                }
            }
        }
        return true;
    }
}
