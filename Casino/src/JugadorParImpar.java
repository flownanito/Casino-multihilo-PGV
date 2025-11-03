
import java.util.Random;

class JugadorParImpar extends Jugador {
    private Random random;

    public JugadorParImpar(String nombre, Banca banca) {
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

                boolean apostaPar = random.nextBoolean();
                apostar(10);

                System.out.printf("%s apuesta 10€ a %s\n", nombre, apostaPar ? "PAR" : "IMPAR");

                Integer numeroGanador = banca.esperarNumeroGanador();

                if (numeroGanador == 0) {
                    mostrarEstado("Salió el 0 - PIERDE TODO");
                } else {
                    boolean esPar = numeroGanador % 2 == 0;
                    if (apostaPar == esPar) {
                        ganar(20);
                        mostrarEstado("¡GANÓ! +20€");
                    } else {
                        mostrarEstado("Perdió");
                    }
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}