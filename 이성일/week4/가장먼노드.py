from collections import deque
def solution(n, edge):
    answer = 0
    q = deque()
    # 모든 edge를 따라 탐색
    # edge max 50000 * 2 -> BFS 완전 탐색
        
    # 그래프 정보 초기화
    
    graph = [[] for _ in range(n+1)]
    for e in edge:
        graph[e[0]].append(e[1])
        graph[e[1]].append(e[0])
    
    
    visited = [False] * (n+1)
    visited[1] = True
    
    dists = [0]*(n+1)
    q.append(1)
    
    while q:
        start = q.popleft()
        
        for node in graph[start]:
            if visited[node]:
                continue
            q.append(node)
            dists[node] = dists[start] + 1
            visited[node] = True
    
    answer = dists.count(max(dists))
    
    
    return answer
