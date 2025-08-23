package main

import (
	"bufio"
	"fmt"
	"math"
	"os"
)

func sieveOddBitset(n int) []int {
	if n < 2 {
		return nil
	}
	if n == 2 {
		return []int{2}
	}


	m := (n - 1) / 2

	bs := make([]uint64, (m+63)/64)

	set := func(i int) { bs[i>>6] |= 1 << (uint(i) & 63) }
	get := func(i int) bool { return (bs[i>>6]>>(uint(i)&63))&1 == 1 }

	limit := int(math.Sqrt(float64(n)))
	mlim := (limit - 1) / 2
	for i := 0; i <= mlim; i++ {
		if get(i) {
			continue
		}
		p := 2*i + 3
	
		start := (p*p - 3) / 2
		for j := start; j < m; j += p {
			set(j)
		}
	}

	primes := make([]int, 1, int(float64(n)/math.Log(float64(n)))+10)
	primes[0] = 2
	for i := 0; i < m; i++ {
		if !get(i) {
			primes = append(primes, 2*i+3)
		}
	}
	return primes
}

func main() {
	const upper = 500_000_000
	primes := sieveOddBitset(upper)

	w := bufio.NewWriterSize(os.Stdout, 1<<20)
	for _, p := range primes {
		fmt.Fprintln(w, p)
	}
	w.Flush()
}
