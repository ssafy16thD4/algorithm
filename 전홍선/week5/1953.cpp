#include <bits/stdc++.h>
using namespace std;

int dx[4] = {-1, 1, 0, 0};          // 상,하,좌,우
int dy[4] = {0, 0, -1, 1};          // 0,1,2,3
int opposite_dir[4] = {1, 0, 3, 2}; //(5 - dir) % 4;
bool tunnel_can_go[8][4] = {
    {false, false, false, false},
    {true, true, true, true},
    {true, true, false, false},
    {false, false, true, true},
    {true, false, false, true},
    {false, true, false, true},
    {false, true, true, false},
    {true, false, true, false},
};

int answer = 0;

int board[55][55];
int dist[55][55];

int n, m, r, c, l;

int main(void)
{
    int t;
    cin >> t;
    for (int tc = 1; tc <= t; tc++)
    {
        answer = 0;
        fill(board[0], board[0] + 55 * 55, 0);
        fill(dist[0], dist[0] + 55 * 55, -1);
        cin >> n >> m >> r >> c >> l;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                cin >> board[i][j];
            }
        }

        queue<pair<int, int>> q;
        q.push({r, c});
        dist[r][c] = 0;
        while (!q.empty())
        {
            auto cur = q.front();
            q.pop();

            if (dist[cur.first][cur.second] + 1 <= l)
                answer++;

            for (int dir = 0; dir < 4; dir++)
            {
                if (!tunnel_can_go[board[cur.first][cur.second]][dir])
                    continue;

                int nx = cur.first + dx[dir];
                int ny = cur.second + dy[dir];

                if (nx < 0 || nx >= n || ny < 0 || ny >= m)
                    continue;

                if (board[nx][ny] == 0 || dist[nx][ny] != -1)
                    continue;
                if (!tunnel_can_go[board[nx][ny]][opposite_dir[dir]])
                    continue;

                dist[nx][ny] = dist[cur.first][cur.second] + 1;
                q.push({nx, ny});
            }
        }

        cout << "#" << tc << " " << answer << "\n";
    }
}