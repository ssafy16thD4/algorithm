/*
전략
- 시뮬레이션 구현, 우선순위 큐
- 격자의 범위 제한 [351][351]
    - 최대 초기 영역이 50 x 50, 최대 시간이 300
    - 150 - 50 - 150 만큼 50 영역에서 양쪽으로 뻗을 수 있다
    - 따라서 최대 격자 크기는 350 x 350
    - 시작 범위는 [151, 151] ~ [200, 200] ⇒ [150 + x][150 + y]
- 비활성 상태 노드 큐, 활성 상태 노드 우선순위 큐, 사망 예정 큐로 총 큐 3개를 사용
    - 우선순위 큐로 최대 생명력 수치가 높은 세포에 우선권을 부여
- 비활성 만들어질 때 세포 수를 세고, 사망할 때 세포 수를 세서 비활성 - 사망을 하면 결과

자료구조
- Cell
    - x, y
    - hp = 해당 세포의 생명력 수치
    - currHp = 해당 세포의 현재 생명력 수치(매턴 1 감소, 0 되면 다음 상태로 전환)
    - Comparable<Cell> 구현 → compareTo(Cell c) 오버라이드
- 비활성 상태 노드 큐, 활성 상태 노드 우선순위 큐, 사망 예정 노드 큐
    - 생명력 수치 높은게 우선 점거할 수 있도록 활성 큐는 우선순위 큐로 구현
*/

import java.io.;
import java.util.;

class Cell implements Comparable<Cell>{
public int x;
public int y;
public int hp;
public int currHp;

Cell(int x, int y, int hp){
this.x = x;
this.y = y;
this.hp = hp;
this.currHp = hp;
}

@Override
public int compareTo(Cell c) {
// hp가 더 큰 쪽이 앞으로 오게 정렬
// 같은 턴이면 hp가 더 큰 쪽이 셀 점유에 우선권이 있음
return -1 * Integer.compare(hp, c.hp);
}
}

public class SWEA5653 {	
static int[] dx = new int[] {-1, 1, 0, 0};
static int[] dy = new int[] {0, 0, -1, 1};

public static void main(String[] args) throws Exception{
BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
StringBuilder sb = new StringBuilder();

int T = Integer.parseInt(br.readLine());

for(int t = 1; t <= T; t++) {
StringTokenizer st = new StringTokenizer(br.readLine());
int N = Integer.parseInt(st.nextToken());
int M = Integer.parseInt(st.nextToken());
int K = Integer.parseInt(st.nextToken());

boolean[][] visited = new boolean[351][351];
Queue<Cell> sleeps = new ArrayDeque<>();
Queue<Cell> unactives = new ArrayDeque<>();
PriorityQueue<Cell> actives = new PriorityQueue<>();

int dead = 0;
int live = 0;

// 초기 상태 입력
for(int i = 0; i < N; i++) {
st = new StringTokenizer(br.readLine());
for(int j = 0; j < M; j++) {
int curr = Integer.parseInt(st.nextToken());

if(curr != 0) {
visited[i + 150][j + 150] = true;
unactives.add(new Cell(i + 150, j + 150, curr));
live++;
}
}
}

// K만큼 턴 진행
int turn = 0;
while(turn < K) {
turn++;

// 사망 예정 큐 처리
// 사망 예정 = 더이상 번식이 불가능한 활성 세포
int len = sleeps.size();
for(int l = 0; l < len; l++) {
Cell c = sleeps.poll();
c.currHp--;

if(c.currHp > 0) {
sleeps.offer(c);
}
// 생명력이 0이 됐으면 사망 처리
else {
dead++;
}
}


// 활성 큐 처리
len = actives.size();
for(int l = 0; l < len; l++) {
Cell c = actives.poll();
c.currHp--;

// 상하좌우 4방향에 번식
for(int i = 0; i < 4; i++) {
int nx = c.x + dx[i];
int ny = c.y + dy[i];

// 비어있는 곳에 번식
if(!visited[nx][ny]) {
visited[nx][ny] = true;
live++;

// 아래 비활성 큐 로직 처리에서 같은 턴에 처리되는 거 방지용 -1 처리
Cell next = new Cell(nx, ny, c.hp);
next.currHp = -1;
unactives.offer(next);
}
}

// 활성 세포의 현재 생명력이 0이면 바로 사망
if(c.currHp == 0) {
dead++;
}
// 아닌 경우 번식 완료했으므로 사망 예정으로 이동
else {
sleeps.offer(c);
}
}

// 비활성 큐 처리
len = unactives.size();
for(int l = 0; l < len; l++) {
Cell c = unactives.poll();

// 이번 턴에 추가된 세포면 처리하지 않음
if(c.currHp == -1) {
c.currHp = c.hp;
unactives.offer(c);
continue;
}

c.currHp--;

// 현재 생명력이 0이면 비활성->활성 전환
if(c.currHp == 0) {
c.currHp = c.hp;
actives.offer(c);
}
else {
unactives.offer(c);
}
}	
}
sb.append("#").append(t).append(" ").append(live - dead).append("\n");
}
System.out.print(sb);
}
}
