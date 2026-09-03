#include <bits/stdc++.h>
using namespace std;
int dx[4] = {-1, 1, 0, 0};
int dy[4] = {0, 0, -1, 1};
int change_dir[4] = {1, 0, 3, 2};

struct Unit
{
    int x;
    int y;
    int size;
    int dir;
};

struct Point
{
    int x;
    int y;
};

int n, m, k;
vector<Unit> v;
vector<Point> used_loc;
int board_size[105][105];
int board_dir[105][105];

int main(void)
{
    int t;
    cin >> t;
    for (int tc = 1; tc <= t; tc++)
    {
        int ans = 0;
        fill(board_size[0], board_size[0] + 105 * 105, -1);
        fill(board_dir[0], board_dir[0] + 105 * 105, -1);
        cin >> n >> m >> k;
        v.resize(k);
        used_loc.clear();
        for (int i = 0; i < k; i++)
        {
            cin >> v[i].x >> v[i].y >> v[i].size >> v[i].dir;
            v[i].dir--;
        }

        for (int time = 1; time <= m; time++)
        {
            sort(v.begin(), v.end(), [](const Unit &a, const Unit &b)
                 { return a.size < b.size; });
            used_loc.clear();
            for (int i = 0; i < v.size(); i++)
            {
                // Unit &cur = v[i];
                v[i].x += dx[v[i].dir];
                v[i].y += dy[v[i].dir];

                // 비어있을 경우
                if (board_size[v[i].x][v[i].y] == -1)
                {
                    board_size[v[i].x][v[i].y] = v[i].size;
                    board_dir[v[i].x][v[i].y] = v[i].dir;
                    used_loc.push_back({v[i].x, v[i].y});
                    if (v[i].x == 0 || v[i].x == n - 1 || v[i].y == 0 || v[i].y == n - 1)
                    {
                        board_size[v[i].x][v[i].y] /= 2;
                        board_dir[v[i].x][v[i].y] = change_dir[v[i].dir];
                    }
                }

                else
                {
                    board_size[v[i].x][v[i].y] += v[i].size;
                    board_dir[v[i].x][v[i].y] = v[i].dir;
                }
            }

            v.clear();
            for (int i = 0; i < used_loc.size(); i++)
            {
                int x = used_loc[i].x;
                int y = used_loc[i].y;

                v.push_back({x, y, board_size[x][y], board_dir[x][y]});
                board_size[x][y] = -1;
                board_dir[x][y] = -1;
            }
        }

        for (int i = 0; i < v.size(); i++)
        {
            ans += v[i].size;
        }

        cout << "#" << tc << " " << ans << "\n";
    }
}