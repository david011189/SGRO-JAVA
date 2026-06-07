package pe.edu.barlen.sgro.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

// RF-14 a RF-20: carrito de reservas, vive en la sesión del cliente.
// Una sola estadía (un rango de fechas y nº de huéspedes) con varias habitaciones.
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private int       numHuespedes;
    private final List<ItemCarrito> items = new ArrayList<>();

    public boolean estaVacio() { return items.isEmpty(); }

    public int getNoches() {
        if (fechaEntrada == null || fechaSalida == null) return 0;
        return (int) ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    // RF-14: agrega una habitación. Devuelve un mensaje de error o null si todo bien.
    public String agregar(Habitacion h, LocalDate entrada, LocalDate salida, int huespedes) {
        if (!estaVacio() && (!entrada.equals(fechaEntrada) || !salida.equals(fechaSalida))) {
            return "Ya tienes una cotización para otras fechas. Vacía el carrito para cambiar de fechas.";
        }
        for (ItemCarrito it : items) {
            if (it.getIdHabitacion() == h.getIdHabitacion()) {
                return "La habitación " + h.getNumero() + " ya está en tu carrito.";
            }
        }
        if (estaVacio()) {
            this.fechaEntrada = entrada;
            this.fechaSalida  = salida;
            this.numHuespedes = huespedes;
        }
        items.add(new ItemCarrito(h, getNoches()));
        return null;
    }

    // RF-16: elimina una habitación del carrito
    public void eliminar(int idHabitacion) {
        items.removeIf(it -> it.getIdHabitacion() == idHabitacion);
        if (items.isEmpty()) {
            fechaEntrada = null;
            fechaSalida  = null;
            numHuespedes = 0;
        }
    }

    public void vaciar() {
        items.clear();
        fechaEntrada = null;
        fechaSalida  = null;
        numHuespedes = 0;
    }

    // RF-18: total del carrito
    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito it : items) total = total.add(it.getSubtotal());
        return total;
    }

    public int getCantidad() { return items.size(); }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public LocalDate getFechaSalida()  { return fechaSalida; }
    public int       getNumHuespedes() { return numHuespedes; }
    public List<ItemCarrito> getItems() { return items; }
}
