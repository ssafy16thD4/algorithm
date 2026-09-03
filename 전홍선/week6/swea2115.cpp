#include <bits/stdc++.h>
using namespace std;

int n, m, c;
int board[15][15];
int max_board[15][15];
int ans = 0;

int find_max(int i, int j, int k, int total, int max_value)
{
    if (k == m)
    {
        return max_value;
    }

    int not_choose = find_max(i, j, k + 1, total, max_value);
    int choose = -1;
    if (total + board[i][j + k] <= c)
    {
        choose = find_max(i, j, k + 1, total + board[i][j + k], max_value + (board[i][j + k]) * (board[i][j + k]));
    }

    return max(not_choose, choose);
}

int main(void)
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    int t;
    cin >> t;

    for (int tc = 1; tc <= t; tc++)
    {
        ans = 0;
        cin >> n >> m >> c;

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                cin >> board[i][j];
            }
        }

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j <= n - m; j++)
            {
                max_board[i][j] = find_max(i, j, 0, 0, 0);
            }
        }

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j <= n - m; j++)
            {
                int temp = max_board[i][j];
                for (int k = i; k < n; k++)
                {
                    for (int l = 0; l <= n - m; l++)
                    {
                        if (k == i)
                        {
                            if (l < j + m)
                                continue;
                        }
                        ans = max(ans, temp + max_board[k][l]);
                    }
                }
            }
        }

        cout << "#" << tc << " " << ans << "\n";
    }
}