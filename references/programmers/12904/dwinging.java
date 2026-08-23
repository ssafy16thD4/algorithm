// ref-title: DWinging — 가장 긴 팰린드롬 (DP)
// ref-url: https://github.com/DWinging/Algorithm/blob/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/05/14/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EA%B0%80%EC%9E%A5_%EA%B8%B4_%ED%8C%B0%EB%A6%B0%EB%93%9C%EB%A1%AC/Solution.java
// ref-note: 길이 i 부분문자열이 팰린드롬인지를 dp[i][j] 로 채워 올라가는 O(N^2) DP. dp[0]/dp[1] 을 전부 true 로 깔아두고 양 끝 문자 비교 + 안쪽 결과만 보는 점화식이라 분기가 한 줄로 끝난다. 원저자가 같은 폴더 solve.md 에 풀이 근거를 적어두었다(https://github.com/DWinging/Algorithm/tree/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/05/14/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EA%B0%80%EC%9E%A5_%EA%B8%B4_%ED%8C%B0%EB%A6%B0%EB%93%9C%EB%A1%AC). 저장소에 라이선스 표기가 없어 코드 원문은 커밋 02325ae 기준 사본이며, 출처 링크를 지우지 말 것.
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