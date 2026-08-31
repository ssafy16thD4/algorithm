// ref-title: <SWEA> #5653 줄기세포배양 java
// ref-url: https://velog.io/@kimmy/SWEA-5653-%EC%A4%84%EA%B8%B0%EC%84%B8%ED%8F%AC%EB%B0%B0%EC%96%91-java
// ref-note: 우선순위 큐 기반 시뮬레이션. PriorityQueue<>((p1, p2) -> p2.power - p1.power) 로 생명력 내림차순 정렬해, 같은 시각에 여러 세포가 한 칸을 노릴 때 생명력 큰 쪽이 먼저 꺼내져 칸을 차지하도록 만든다(안찬웅 풀이와 같은 전략). 좌표를 무한히 늘리는 대신 격자를 [N+2K][M+2K] 로 잡고 초기 세포를 (i+K, j+K) 로 offset — 좌표만으로 관리하면 시간초과가 난다는 점을 명시한다. 활성화 시각 k에 사방 자식을 k+1+power 로 큐에 넣고, 활성 세포는 k+power 에 사망 처리해 마지막에 ACTIVE/INACTIVE 만 센다. 개인 블로그라 저작권상 코드 원문은 저장하지 않으니 위 링크에서 직접 확인.
