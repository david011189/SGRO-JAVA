package pe.edu.barlen.sgro.modelo;

import java.math.BigDecimal;

// RF-28: una línea de la reserva (una habitación dentro de la estadía)
public class DetalleReserva {

    private int        idDetalle;
    private int        idReserva;
    private int        idHabitacion;
    private int        noches;
    private BigDecimal subtotal;

    // Datos de apoyo para mostrar (no se persisten aquí)
    private String     numeroHabitacion;
    private String     tipoHabitacion;

    public DetalleReserva() {}

    public DetalleReserva(int idHabitacion, int noches, BigDecimal subtotal) {
        this.idHabitacion = idHabitacion;
        this.noches       = noches;
        this.subtotal     = subtotal;
    }

    public int        getIdDetalle()        { return idDetalle; }
    public int        getIdReserva()        { return idReserva; }
    public int        getIdHabitacion()     { return idHabitacion; }
    public int        getNoches()           { return noches; }
    public BigDecimal getSubtotal()         { return subtotal; }
    public String     getNumeroHabitacion() { return numeroHabitacion; }
    public String     getTipoHabitacion()   { return tipoHabitacion; }

    public void setIdDetalle(int v)         { this.idDetalle = v; }
    public void setIdReserva(int v)         { this.idReserva = v; }
    public void setIdHabitacion(int v)      { this.idHabitacion = v; }
    public void setNoches(int v)            { this.noches = v; }
    public void setSubtotal(BigDecimal v)   { this.subtotal = v; }
    public void setNumeroHabitacion(String v){ this.numeroHabitacion = v; }
    public void setTipoHabitacion(String v) { this.tipoHabitacion = v; }
}
