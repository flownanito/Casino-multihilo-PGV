class Croupier implements Runnable {
    private Banca banca;
    private volatile boolean activo;
    private int rondas;

    public Croupier(Banca banca, int rondas) {
        this.banca = banca;
        this.activo = true;
        this.rondas = rondas;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < rondas && activo; i++) {
                System.out.println("\n" + "=".repeat(60));
                System.out.println("RONDA " + (i + 1) + " - Banca: " + banca.getSaldo() + "€");
                System.out.println("=".repeat(60));

                Thread.sleep(3000);
                banca.girarRuleta();
                Thread.sleep(500); // Tiempo para que los jugadores procesen
                banca.limpiarNumero();
            }

            System.out.println("\n" + "=".repeat(60));
            System.out.println("FIN DE LA SIMULACIÓN");
            System.out.println("Saldo final de la banca: " + banca.getSaldo() + "€");
            System.out.println("=".repeat(60));

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void detener() {
        activo = false;
    }
}
