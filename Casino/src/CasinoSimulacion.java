public class CasinoSimulacion {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("🎰 SIMULACIÓN DE CASINO - RULETA FRANCESA 🎰\n");

        final int TOTAL_JUGADORES = 12; // 4 de cada tipo
        final int RONDAS = 10; // Número de giros de ruleta

        Banca banca = new Banca(50000, TOTAL_JUGADORES);

        // Crear jugadores
        Thread[] jugadores = new Thread[TOTAL_JUGADORES];

        // 4 jugadores de número específico
        for (int i = 0; i < 4; i++) {
            jugadores[i] = new Thread(new JugadorNumero("JugNum" + (i+1), banca));
        }

        // 4 jugadores de par/impar
        for (int i = 4; i < 8; i++) {
            jugadores[i] = new Thread(new JugadorParImpar("JugParImpar" + (i-3), banca));
        }

        // 4 jugadores de martingala
        for (int i = 8; i < 12; i++) {
            jugadores[i] = new Thread(new JugadorMartingala("JugMartingala" + (i-7), banca));
        }

        // Crear croupier
        Croupier croupier = new Croupier(banca, RONDAS);
        Thread hiloCroupier = new Thread(croupier);

        // Iniciar todos los hilos
        hiloCroupier.start();
        for (Thread jugador : jugadores) {
            jugador.start();
        }

        // Esperar a que termine el croupier
        hiloCroupier.join();

        // Dar tiempo a que los jugadores terminen su última ronda
        Thread.sleep(1000);

        // Interrumpir los jugadores que sigan activos
        for (Thread jugador : jugadores) {
            if (jugador.isAlive()) {
                jugador.interrupt();
            }
        }

        System.out.println("\nSimulación terminada.");
    }
}