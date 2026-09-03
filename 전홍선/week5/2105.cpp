#include <bits/stdc++.h>
using namespace std;

int board[22][22];
int n;
bool used[101];
int answer = -1;
int dx[4] = {-1, 1, 1, -1};
int dy[4] = {1, 1, -1, -1};
int startx;
int starty;

void backtrack(int x, int y, int dir, int cnt, int zero_cnt)
{
    // x,y를 dir방향으로 탐색하겠다는뜻
    // 즉 다음 탐색방향은 x+dx[dir], y+dy[dir]
    // 즉 호출이 되면 x,y를 used에 넣음. dir은 이미 이전시점에 정해졌다.
    // backtrack이 호출됐을때 확정된 것들
    // used[x][y] == false;
    // x, y 는 범위 이내.
    // dir방향으로 탐색을 이어나갈수 있는지는 아직 몰루?
    // cnt = 호출되기 직전 먹은 메뉴수.
    // cnt + 1 = 이번에 x,y 포함해서 여태까지 먹은 메뉴수
    used[board[x][y]] = true;
    if (dir == 2)
        zero_cnt--;

    int curcnt = cnt + 1;

    // if (startx == 3 && starty == 0)
    // {
    //     cout << startx << " " << starty << " " << x << " " << y << " " << dir << " " << curcnt << " " << zero_cnt << "\n";
    //     for (int i = 0; i < 101; i++)
    //     {
    //         if (used[i] == true)
    //         {
    //             cout << i << " ";
    //         }
    //     }
    //     cout << "\n";
    // }

    if (x == startx && y == starty && dir == 3)
    {
        answer = max(answer, curcnt - 1);
        return;
    }

    int nx = x + dx[dir];
    int ny = y + dy[dir];

    // dir 방향으로 탐색안되면 돌아옴
    if (nx < 0 || nx >= n || ny < 0 || ny >= n)
    {
        used[board[x][y]] = false;
        return;
    }

    // if (startx == 3 && starty == 0)
    //     cout << " " << board[nx][ny] << " " << used[board[nx][ny]] << "\n";

    // 다음 칸이 이미 먹은 메뉴면 돌아옴
    if ((nx != startx || ny != starty) && used[board[nx][ny]] == true)
    {
        used[board[x][y]] = false;
        return;
    }
    // 다음 탐색 방향은 현재칸이 무슨 방향이냐에 따라 다름
    // dir 0 : 0,1
    // dir 1 : 1,2
    // dir 2 : 0을 몇번 했느냐에 따라 다름
    // dir 3 : 1을 몇번 했느냐에 따라 다름

    if (dir <= 1)
    {
        backtrack(nx, ny, dir, curcnt, (dir == 0) + zero_cnt);
        backtrack(nx, ny, dir + 1, curcnt, (dir == 0) + zero_cnt);
    }

    if (dir == 2)
    {
        if (zero_cnt > 0)
            backtrack(nx, ny, dir, curcnt, zero_cnt);
        else
            backtrack(nx, ny, dir + 1, curcnt, zero_cnt);
    }

    if (dir == 3)
    {
        backtrack(nx, ny, dir, curcnt, zero_cnt);
    }

    used[board[x][y]] = false;
    return;
}

int main(void)
{
    int t;
    cin >> t;
    for (int tc = 1; tc <= t; tc++)
    {
        answer = -1;
        fill(used, used + 101, false);
        cin >> n;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                cin >> board[i][j];
            }
        }

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                startx = i;
                starty = j;
                backtrack(i, j, 0, 0, 0);
            }
        }

        cout << "#" << tc << " " << answer << "\n";
    }
}