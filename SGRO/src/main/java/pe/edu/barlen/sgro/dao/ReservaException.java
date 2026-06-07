package pe.edu.barlen.sgro.dao;

// Se lanza cuando una reserva no puede concretarse por reglas de negocio
// (p. ej. una habitación dejó de estar disponible). RF-19, RF-32.
public class ReservaException extends Exception {
    private static final long serialVersionUID = 1L;
    public ReservaException(String mensaje) { super(mensaje); }
}
