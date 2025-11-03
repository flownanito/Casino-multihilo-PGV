import java.util.Random;

class JugadorMartingala extends Jugador {
    private Random random;
    private double apuestaActual;
    private Integer numeroElegido;

    public JugadorMartingala(String nombre, Banca banca) {
        super(nombre, 1000, banca);
        this.random = new Random();
        this.apuestaActual = 10;
        this.numeroElegido = null;
    }

    @Override
    public void run() {
        try {
            while (activo) {
                if (saldo < apuestaActual) {
                    System.out.println(nombre + " - SIN FONDOS SUFICIENTES (necesita " + apuestaActual + "€)");
                    activo = false;
                    break;
                }

                if (numeroElegido == null) {
                    numeroElegido = random.nextInt(36) + 1;
                }

                apostar(apuestaActual);
                System.out.printf("%s apuesta %.0f€ al número %d (Martingala)\n",
                        nombre, apuestaActual, numeroElegido);

                Integer numeroGanador = banca.esperarNumeroGanador();

                if (numeroGanador == 0) {
                    mostrarEstado("Salió el 0 - PIERDE TODO");
                    apuestaActual = 10;
                    numeroElegido = null;
                } else if (numeroGanador.equals(numeroElegido)) {
                    double premio = apuestaActual * 36;
                    ganar(premio);
                    mostrarEstado("¡GANÓ! +" + premio + "€");
                    apuestaActual = 10;
                    numeroElegido = null;
                } else {
                    mostrarEstado("Perdió - Duplica apuesta");
                    apuestaActual *= 2;
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
