#include <bits/stdc++.h>
using namespace std;

static bool isPrimeTrial(long long n) {
    if (n < 2) return false;
    if ((n & 1LL) == 0) return n == 2;
    for (long long i = 3; i * i <= n; i += 2) {
        if (n % i == 0) return false;
    }
    return true;
}

static vector<int> simpleSieve(int limit) {
    int n = max(limit + 1, 2);
    vector<bool> mark(n, true);
    mark[0] = false; if (n > 1) mark[1] = false;
    for (int p = 2; p * p <= limit; ++p) {
        if (mark[p]) {
            for (int i = p * p; i <= limit; i += p) mark[i] = false;
        }
    }
    vector<int> primes;
    for (int i = 2; i <= limit; ++i) if (mark[i]) primes.push_back(i);
    return primes;
}

static void segmentedSieve(long long a, long long b) {
    if (b < 2 || b < a) return;
    a = max(2LL, a);

    long long limit = static_cast<long long>(sqrt((long double)b)) + 1;
    vector<int> base = simpleSieve((int)limit);

    const long long segment_size = 1 << 20; // ~1M numbers per segment
    vector<bool> isPrime(segment_size);

    for (long long low = a; low <= b; low += segment_size) {
        long long high = min(low + segment_size - 1, b);
        fill(isPrime.begin(), isPrime.end(), true);

        for (int p : base) {
            long long start = max(low + ((low % p) ? (p - (low % p)) : 0), 1LL * p * p);
            if (start > high) continue;
            for (long long j = start; j <= high; j += p) {
                isPrime[j - low] = false;
            }
        }

        for (long long x = low; x <= high; ++x) {
            if (isPrime[x - low]) cout << x << '\n';
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    long long a = 0;
    long long b = 500'000'000; // match Java default upper bound

    segmentedSieve(a, b);
    return 0;
}
