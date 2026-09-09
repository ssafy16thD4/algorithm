#include <bits/stdc++.h>
using namespace std;

int main(void)
{
    int n = 5;
    int m = 2;
    int k = 3;

    int count = max(m - (n % k) - (k - 1) * (n / k), 0);
    int left = m - count * k;

    int dp[10];
    dp[0] = 0;

    for (int i = 1; i <= count; i++)
    {
        dp[i] = (dp[i - 1] + k) * 2;
    }

    cout << dp[count] + left;
}