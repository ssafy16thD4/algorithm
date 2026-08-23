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