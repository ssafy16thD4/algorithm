class Solution
{
    public int solution(String s)
    {
        char[] charList = s.toCharArray();
        int len = charList.length;
        
        int answer = 1;
        boolean[][] dp = new boolean[len + 1][len];
        
        for(int i = 0; i < len; i++) dp[0][i] = dp[1][i] = true;
        
        for(int i = 2; i <= len; i++) {
            for(int j = 0; j <= len - i; j++) {
                if(charList[j] == charList[j + i - 1] && dp[i - 2][j + 1]) {
                    dp[i][j] = true;
                    answer = i;
                }
            }
        }
        
        return answer;
    }
}