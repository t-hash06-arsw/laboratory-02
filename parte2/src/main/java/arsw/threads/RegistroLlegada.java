package arsw.threads;

public class RegistroLlegada {

	private int ultimaPosicionAlcanzada = 1;

	private String ganador = null;

	private boolean pausado = false;

	private final Object monitor = new Object();

	public String getGanador() {
		return ganador;
	}

	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	public int getUltimaPosicionAlcanzada() {
		return ultimaPosicionAlcanzada;
	}

	public void setUltimaPosicionAlcanzada(int ultimaPosicionAlcanzada) {
		this.ultimaPosicionAlcanzada = ultimaPosicionAlcanzada;
	}

	public synchronized int tomarYAvanzarPosicion() {
		int actual = ultimaPosicionAlcanzada;
		ultimaPosicionAlcanzada = actual + 1;
		return actual;
	}

	public synchronized void intentarRegistrarGanador(String nombre, int posicion) {
		if (posicion == 1 && ganador == null) {
			ganador = nombre;
		}
	}

	public void pausar() {
		synchronized (monitor) {
			pausado = true;
		}
	}

	public void continuar() {
		synchronized (monitor) {
			pausado = false;
			monitor.notifyAll();
		}
	}

	public void esperarSiPausado() throws InterruptedException {
		synchronized (monitor) {
			while (pausado) {
				monitor.wait();
			}
		}
	}
}
