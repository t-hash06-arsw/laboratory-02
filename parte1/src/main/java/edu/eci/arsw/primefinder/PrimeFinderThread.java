package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {

	int a, b;
	String id;

	private List<Integer> primes = new LinkedList<Integer>();

	public PrimeFinderThread(int a, int b, String id) {
		super();
		this.a = a;
		this.b = b;
		this.id = id;
	}

	public void run() {
		for (int i = a; i <= b; i++) {
			if (isPrime(i)) {
				primes.add(i);
				// System.out.println(i);
			}
		}

		System.out.println("Thread " + id + " ended");
	}

	boolean isPrime(int n) {
		if (n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
}
