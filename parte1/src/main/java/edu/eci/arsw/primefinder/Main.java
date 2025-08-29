package edu.eci.arsw.primefinder;

public class Main {

	public static void main(String[] args) {
		// Parte I:
		// 1) Ejecutar con múltiples hilos (3) dividiendo el rango.
		// 2) A los 5s pausar todos los hilos, mostrar cuántos primos van,
		// esperar ENTER y luego reanudar hasta finalizar.

		final int threadCount = 3;
		final int finalNumber = 30_000_000; // como indica el enunciado
		final int range = finalNumber / threadCount;
		final PrimeFinderThread[] threads = new PrimeFinderThread[threadCount];

		for (int i = 0; i < threadCount; i++) {
			int start = i * range;
			int end = (i == threadCount - 1) ? finalNumber : (start + range - 1);
			threads[i] = new PrimeFinderThread(start, end, "Hilo-" + i);
			threads[i].start();
		}

		try {
			// Esperar 5 segundos y pausar todos los hilos
			Thread.sleep(5000);
			PrimeFinderThread.pausarTodos();

			// Contar primos encontrados hasta el momento
			int parciales = 0;
			for (PrimeFinderThread t : threads) {
				parciales += t.getPrimes().size();
			}
			System.out.println("[PAUSA] Primos encontrados hasta ahora: " + parciales);
			System.out.println("Presione ENTER para continuar...");

			// Esperar ENTER del usuario
			try {
				while (System.in.available() > 0) {
					System.in.read(); // limpiar buffer previo si lo hay
				}
				System.in.read();
			} catch (Exception ignored) {
			}

			// Reanudar y esperar a que terminen
			PrimeFinderThread.continuarTodos();
			for (PrimeFinderThread t : threads) {
				t.join();
			}

			int total = 0;
			for (PrimeFinderThread t : threads) {
				total += t.getPrimes().size();
			}
			System.out.println("[FIN] Total de primos encontrados: " + total);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
