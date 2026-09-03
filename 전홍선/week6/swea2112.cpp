#include <bits/stdc++.h>
using namespace std;

vector<vector<int>> board;
int d, w, k;
int ans;
int is_used[20];

bool check(const vector<vector<int>> &board)
{
    for (int i = 0; i < w; i++)
    {
        int temp = 1;
        bool flag = false;
        int prev = board[0][i];
        for (int j = 1; j < d; j++)
        {
            if (board[j][i] == prev)
            {
                temp++;
            }
            else
            {
                temp = 1;
                prev = board[j][i];
            }

            if (temp == k)
            {
                flag = true;
                break;
            }
        }

        if (flag == false)
            return false;
    }
    return true;
}

void backtrack(int n, int last, const vector<vector<int>> &cur_board)
{
    // cout << n << " " << last << "\n";
    // if (is_used[2] && is_used[5])
    // {
    //     cout << "\n";
    //     for (int i = 0; i < d; i++)
    //     {
    //         for (int j = 0; j < w; j++)
    //         {
    //             cout << cur_board[i][j] << " ";
    //         }
    //         cout << "\n";
    //     }
    // }
    if (n >= ans)
    {
        return;
    }
    if (check(cur_board))
    {
        ans = min(ans, n);
        return;
    }

    if (n + 1 == ans)
    {
        return;
    }

    vector<vector<int>> next_board;

    for (int i = last + 1; i < d; i++)
    {
        if (n + 1 == ans)
        {
            return;
        }
        is_used[i] = true;
        next_board = cur_board;
        fill(next_board[i].begin(), next_board[i].end(), 0);
        backtrack(n + 1, i, next_board);
        fill(next_board[i].begin(), next_board[i].end(), 1);
        backtrack(n + 1, i, next_board);
        is_used[i] = false;
    }

    return;
}

int main(void)
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    int t;
    cin >> t;
    for (int tc = 1; tc <= t; tc++)
    {
        cin >> d >> w >> k;
        board.assign(d, vector<int>(w));
        ans = k;
        fill(is_used, is_used + 20, 0);

        for (int i = 0; i < d; i++)
        {
            for (int j = 0; j < w; j++)
            {
                cin >> board[i][j];
            }
        }

        backtrack(0, -1, board);
        cout << "#" << tc << " " << ans << "\n";
    }
}