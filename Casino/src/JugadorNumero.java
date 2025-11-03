
import java.util.Random;

public class JugadorNumero extends Jugador {
    private Random random;

    public JugadorNumero(String nombre, Banca banca) {
        super(nombre, 1000, banca);
        this.random = new Random();
    }

    @Override
    public void run() {
        try {
            while (activo) {
                if (saldo < 10) {
                    System.out.println(nombre + " - SIN FONDOS SUFICIENTES");
                    activo = false;
                    break;
                }

                int numeroElegido = random.nextInt(36) + 1; // 1 a 36
                apostar(10);

                System.out.printf("%s apuesta 10€ al número %d\n", nombre, numeroElegido);

                Integer numeroGanador = banca.esperarNumeroGanador();

                if (numeroGanador == 0) {
                    mostrarEstado("Salió el 0 - PIERDE TODO");
                } else if (numeroGanador.equals(numeroElegido)) {
                    ganar(360);
                    mostrarEstado("¡GANÓ! +360€");
                } else {
                    mostrarEstado("Perdió");
                }

                Thread.sleep(100); // Pequeña pausa
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}