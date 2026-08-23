// ref-title: DWinging — 양과 늑대 (비트마스크 DFS)
// ref-url: https://github.com/DWinging/Algorithm/blob/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/07/18/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EC%96%91%EA%B3%BC_%EB%8A%91%EB%8C%80/Solution.java
// ref-note: 방문한 노드 집합을 비트마스크로 들고 다니며, 갈 수 있는 자식 후보를 매번 다시 계산해 DFS 한다. 늑대 수가 양 수 이상이면 가지를 친다. 원저자가 같은 폴더 solve.md 에 풀이 근거를 적어두었다(https://github.com/DWinging/Algorithm/tree/02325ae360ea1563d6cd81735e5eb1e570e12354/Algorithm/2026/07/18/%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%A8%B8%EC%8A%A4_%EC%96%91%EA%B3%BC_%EB%8A%91%EB%8C%80). 저장소에 라이선스 표기가 없어 코드 원문은 커밋 02325ae 기준 사본이며, 출처 링크를 지우지 말 것.
class Solution {
    
    int res = 0;
    
    public int solution(int[] info, int[][] edges) {
        int n = info.length;
        
        int[][] tree = settingTree(info, edges, n);        
        dfs(0, 0, 1, n, info, tree);
        
        return res;
    }
    
    private int[][] settingTree(int[] info, int[][] edges, int n) {
        int[][] tree = new int[n][2];
        for(int[] edge : edges) {
            int parent = edge[0];
            int child = edge[1];
            
            if(tree[parent][0] == 0) {
                tree[parent][0] = child;
            } else {
                tree[parent][1] = child;
            }
        }
        return tree;
    }
    
    private void dfs(int sheep, int wolf, int bit, int n, int[] info, int[][] tree) {
        if(res < sheep) res = sheep;
        
        for(int i = 0; i < n; i++) {
            if((bit & (1 << i)) > 0) {
                int ns = sheep + (info[i] ^ 1);
                int nw = wolf + info[i];
                
                if(nw >= ns) continue;
                
                int nextBit = bit & (~(1 << i));
                
                for(int next : tree[i]) {
                    if(next == 0) break;
                    nextBit |= (1 << next);
                }
                
                dfs(ns, nw, nextBit, n, info, tree);
            }
        }
    }
}