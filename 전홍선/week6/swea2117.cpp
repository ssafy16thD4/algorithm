#include <bits/stdc++.h>
using namespace std;

int n, m;
int sum;
int ans = 0;
int findmax(const vector<int> &v)
{
    int v1[42];
    fill(v1, v1 + 42, 0);
    for (int dist : v)
    {
        v1[dist]++;
    }

    int left = sum;

    for (int k = 2 * n - 1; k >= 1; k--)
    {
        if (left <= ans)
            return ans;
        // cout << k << " " << left << "\n";
        if (left * m - ((k * k) + (k - 1) * (k - 1)) >= 0)
        {
            // cout << k << " " << left;
            return left;
        }
        else
        {
            left -= v1[k];
        }
    }
}

int main(void)
{
    int T;
    cin >> T;
    ios::sync_with_stdio(0);
    cin.tie(0);
    cout.tie(0);

    for (int tc = 1; tc <= T; tc++)
    {
        sum = 0;
        int local_max = 0;
        ans = 0;
        vector<pair<int, int>> v;
        cin >> n >> m;
        int temp;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                cin >> temp;
                if (temp)
                {
                    v.push_back({i, j});
                    sum++;
                }
            }
        }
        // cout << sum << "\n";

        // for (auto in = v.begin(); in != v.end(); in++)
        // {
        //     cout << (*in).first << " " << (*in).second << " ";
        // }
        // cout << "\n";

        int flag = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                vector<int> dist;
                for (auto house : v)
                {
                    dist.push_back(abs(house.first - i) + abs(house.second - j) + 1);
                }

                // cout << i << " " << j << " :";
                // for (auto in = dist.begin(); in != dist.end(); in++)
                // {
                //     cout << *in << " ";
                // }
                // cout << "\n";

                local_max = findmax(dist);
                ans = max(local_max, ans);
                if (ans == sum)
                {
                    flag = 1;
                    break;
                }
            }
            if (flag)
                break;
        }
        cout << "#" << tc << " " << ans << "\n";
    }
}