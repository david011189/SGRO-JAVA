package pe.edu.barlen.sgro.modelo;

import java.io.Serializable;
import java.math.BigDecimal;

// RF-14: una habitación agregada al carrito de reservas
public class ItemCarrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private int        idHabitacion;
    private String     numero;
    private String     tipo;
    private int        capacidad;
    private BigDecimal tarifa;
    private int        noches;
    private BigDecimal subtotal;   // tarifa * noches

    public ItemCarrito() {}

    public ItemCarrito(Habitacion h, int noches) {
        this.idHabitacion = h.getIdHabitacion();
        this.numero       = h.getNumero();
        this.tipo         = h.getTipo();
        this.capacidad    = h.getCapacidad();
        this.tarifa       = h.getTarifa();
        this.noches       = noches;
        this.subtotal     = h.getTarifa().multiply(BigDecimal.valueOf(noches));
    }

    public int        getIdHabitacion() { return idHabitacion; }
    public String     getNumero()       { return numero; }
    public String     getTipo()         { return tipo; }
    public int        getCapacidad()    { return capacidad; }
    public BigDecimal getTarifa()       { return tarifa; }
    public int        getNoches()       { return noches; }
    public BigDecimal getSubtotal()     { return subtotal; }

    public void setIdHabitacion(int v) { this.idHabitacion = v; }
    public void setNumero(String v)    { this.numero = v; }
    public void setTipo(String v)      { this.tipo = v; }
    public void setCapacidad(int v)    { this.capacidad = v; }
    public void setTarifa(BigDecimal v){ this.tarifa = v; }
    public void setNoches(int v)       { this.noches = v; }
    public void setSubtotal(BigDecimal v) { this.subtotal = v; }
}
