package edu.eci.arsw.primefinder;

public class Main {

	public static void main(String[] args) {

		int threadCount = 100;
		int finalNumber = 100_000_000;
		int range = finalNumber / threadCount;
		PrimeFinderThread[] threads = new PrimeFinderThread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			int start = i * range;
			int end = (i == threadCount - 1) ? finalNumber : (start + range - 1);
			threads[i] = new PrimeFinderThread(start, end, "Thread-" + i);
			threads[i].start();
		}
	}
}
