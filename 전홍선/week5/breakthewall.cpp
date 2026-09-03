#include <bits/stdc++.h>
using namespace std;

int n, w, h;
int ans;
int dx[4] = {-1, 1, 0, 0};
int dy[4] = {0, 0, -1, 1};
int block_total;
// int history[10];
// int flag1, flag2, flag3;

// void draw(vector<vector<bool>> &board)
// {
//     for (int i = 0; i < h; i++)
//     {
//         for (int j = 0; j < w; j++)
//         {
//             cout << board[i][j] << " ";
//         }
//         cout << "\n";
//     }
//     cout << "\n";
// }

// void draw(vector<vector<int>> &board)
// {
//     for (int i = 0; i < h; i++)
//     {
//         for (int j = 0; j < w; j++)
//         {
//             cout << board[i][j] << " ";
//         }
//         cout << "\n";
//     }
//     cout << "\n";
// }

void gravity(vector<vector<int>> &board)
{
    for (int col = 0; col < w; col++)
    {
        vector<int> col_vector;
        for (int i = h - 1; i >= 0; i--)
        {
            if (board[i][col] > 0)
            {
                col_vector.push_back(board[i][col]);
            }
            board[i][col] = 0;
        }

        int row = h - 1;
        for (int value : col_vector)
        {
            board[row][col] = value;
            row--;
        }
    }

    // draw(board);
}

// board배열에 col에서 쐈을때 board 상태 변경 시키고 그때 터트린 보드 수 반환
int shoot(vector<vector<int>> &board, int col)
{
    int break_cnt = 0;
    int start_row = h;
    // vector<vector<bool>> visited(h, vector<bool>(w));
    // draw(visited);
    for (int i = 0; i < h; i++)
    {
        if (board[i][col] > 0)
        {
            start_row = i;
            break;
        }
    }

    if (start_row == h)
        return 0;

    // cout << "start row : " << start_row << "\n";

    queue<tuple<int, int, int>> q;

    int power = board[start_row][col];
    q.push({start_row, col, power});
    board[start_row][col] = 0;

    while (!q.empty())
    {
        auto cur = q.front();
        q.pop();
        int x = get<0>(cur);
        int y = get<1>(cur);
        int range = get<2>(cur);

        break_cnt += 1;

        for (int dir = 0; dir < 4; dir++)
        {
            for (int step = 1; step < range; step++)
            {
                int nx = x + dx[dir] * step;
                int ny = y + dy[dir] * step;

                if (nx < 0 || nx >= h || ny < 0 || ny >= w)
                    break;
                if (board[nx][ny] == 0)
                    continue;
                q.push({nx, ny, board[nx][ny]});
                board[nx][ny] = 0;
            }
        }
    }

    // draw(visited);

    gravity(board);

    return break_cnt;
}

void solution(int k, const vector<vector<int>> &cur, int cnt)
{
    // if (history[0] == 2)
    // {
    //     if (!flag1)
    //     {
    //         draw(cur);
    //         cout << cnt << "\n";
    //         flag1 = 1;
    //     }
    //     if (history[1] == 2)
    //     {
    //         if (!flag2)
    //         {
    //             draw(cur);
    //             cout << cnt << "\n";
    //             flag2 = 1;
    //         }
    //         if (history[2] == 6 && !flag3)
    //         {
    //             draw(cur);
    //             cout << cnt << "\n";
    //             flag3 = 1;
    //         }
    //     }
    // }
    ans = max(ans, cnt);
    if (cnt == block_total || k == n)
    {

        return;
    }

    for (int col = 0; col < w; col++)
    {
        // history[k] = col;

        vector<vector<int>> next_board = cur;

        int broken = shoot(next_board, col);
        if (broken == 0)
            continue;

        // cout << "col : " << col << " cnt : " << cnt << " next_cnt : " << next_cnt << "\n";
        // draw(next_board);

        solution(k + 1, next_board, broken + cnt);
    }
}

int main(void)
{
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);
    int t;
    cin >> t;
    for (int tc = 1; tc <= t; tc++)
    {
        block_total = 0;
        ans = 0;
        cin >> n >> w >> h;

        vector<vector<int>> board(h, vector<int>(w));
        for (int i = 0; i < h; i++)
        {
            for (int j = 0; j < w; j++)
            {
                cin >> board[i][j];
                if (board[i][j])
                    block_total++;
            }
        }

        solution(0, board, 0);

        // cout << block_total << "\n";
        // cout << ans << "\n";
        cout << "#" << tc << " " << block_total - ans << "\n";
    }
}