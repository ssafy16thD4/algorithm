from collections import defaultdict
def solution(tickets):

    airports = defaultdict(list)
    length = len(tickets)
    
    for ticket in tickets:
        airports[ticket[0]].append(ticket[1])
        
    for k in airports.keys():
        airports[k].sort()
        
    stack = ["ICN"]
    path = []
    
    while stack:
        point = stack[-1]
        
        if len(airports[point]) == 0:
            path.append(stack.pop())
        else:
            stack.append(airports[point].pop(0))
    answer = path[::-1]
    
    return answer
