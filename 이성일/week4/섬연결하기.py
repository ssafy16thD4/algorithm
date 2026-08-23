def solution(n, costs):
    
    answer = 0
    
    parents = list(range(n))
    
    costs.sort(key = lambda x: x[2])
    
    def findRoot(v):
        nonlocal parents
        if parents[v] != v:
            parents[v] = findRoot(parents[v])
        return parents[v]    
    
    for a,b,w in costs:
        ar, br = findRoot(a), findRoot(b)
        if ar != br:
            answer += w
            if ar < br:
                parents[br] = ar
            else:
                parents[ar] = br

    
    return answer
