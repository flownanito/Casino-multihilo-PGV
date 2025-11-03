abstract class Jugador implements Runnable {
    protected String nombre;
    protected double saldo;
    protected Banca banca;
    protected boolean activo;

    public Jugador(String nombre, double saldoInicial, Banca banca) {
        this.nombre = nombre;
        this.saldo = saldoInicial;
        this.banca = banca;
        this.activo = true;
    }

    protected synchronized boolean apostar(double cantidad) {
        if (saldo >= cantidad) {
            saldo -= cantidad;
            banca.recibirApuesta(cantidad);
            return true;
        }
        return false;
    }

    protected synchronized void ganar(double cantidad) {
        if (banca.pagarPremio(cantidad)) {
            saldo += cantidad;
        }
    }

    public synchronized double getSaldo() {
        return saldo;
    }

    protected void mostrarEstado(String accion) {
        System.out.printf("%s - %s | Saldo: %.2f€\n", nombre, accion, saldo);
    }
}