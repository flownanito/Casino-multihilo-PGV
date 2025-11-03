
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class Banca {
    private double saldo;
    private Integer numeroGanador;
    private final ReentrantLock lock;
    private final Condition numeroDisponible;
    private final Condition todasApuestasRecibidas;
    private int apuestasRecibidas;
    private final int totalJugadores;

    public Banca(double saldoInicial, int totalJugadores) {
        this.saldo = saldoInicial;
        this.numeroGanador = null;
        this.lock = new ReentrantLock();
        this.numeroDisponible = lock.newCondition();
        this.todasApuestasRecibidas = lock.newCondition();
        this.apuestasRecibidas = 0;
        this.totalJugadores = totalJugadores;
    }

    public void girarRuleta() throws InterruptedException {
        lock.lock();
        try {
            // Esperar a que todos los jugadores hagan su apuesta
            while (apuestasRecibidas < totalJugadores) {
                todasApuestasRecibidas.await();
            }

            Random random = new Random();
            numeroGanador = random.nextInt(37); // 0 a 36
            System.out.println("\n🎰 ¡RULETA GIRA! Número ganador: " + numeroGanador);

            // Resetear contador de apuestas para la siguiente ronda
            apuestasRecibidas = 0;

            // Notificar a todos los jugadores
            numeroDisponible.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public Integer esperarNumeroGanador() throws InterruptedException {
        lock.lock();
        try {
            apuestasRecibidas++;

            if (apuestasRecibidas == totalJugadores) {
                todasApuestasRecibidas.signal();
            }

            while (numeroGanador == null || apuestasRecibidas > 0) {
                numeroDisponible.await();
            }

            return numeroGanador;
        } finally {
            lock.unlock();
        }
    }

    public synchronized void recibirApuesta(double cantidad) {
        saldo += cantidad;
    }

    public synchronized boolean pagarPremio(double cantidad) {
        if (cantidad <= saldo) {
            saldo -= cantidad;
            return true;
        }
        return false;
    }

    public synchronized double getSaldo() {
        return saldo;
    }

    public void limpiarNumero() {
        lock.lock();
        try {
            numeroGanador = null;
        } finally {
            lock.unlock();
        }
    }
}
