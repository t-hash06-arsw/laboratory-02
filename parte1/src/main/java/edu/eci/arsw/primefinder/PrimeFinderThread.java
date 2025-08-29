package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread {

	int a, b;
	String id;

	private List<Integer> primes = new LinkedList<Integer>();

	// Control global de pausa/reanudación para todos los hilos de esta clase
	private static final Object monitor = new Object();
	private static volatile boolean pausado = false;

	public PrimeFinderThread(int a, int b, String id) {
		super();
		this.a = a;
		this.b = b;
		this.id = id;
	}

	public void run() {
		for (int i = a; i <= b; i++) {
			esperarSiPausado();
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

	private static void esperarSiPausado() {
		synchronized (monitor) {
			while (pausado) {
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return;
				}
			}
		}
	}

	public static void pausarTodos() {
		synchronized (monitor) {
			pausado = true;
		}
	}

	public static void continuarTodos() {
		synchronized (monitor) {
			pausado = false;
			monitor.notifyAll();
		}
	}
}
