def solution(stones, k):
    answer = 0
    
    bridgeSize = len(stones)
    
    l,r = min(stones),max(stones)

    while l <= r:
        
        m = (l+r)//2
        sz = 0
        
        for stone in stones:
            if stone < m:
                sz += 1
                if sz >= k:
                    break
            else:
                sz = 0
            
        
        if sz >= k:
            r = m - 1
        else:
            answer = max(answer, m)
            l = m + 1
    
    return answer
