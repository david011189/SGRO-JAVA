package pe.edu.barlen.sgro.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// RF-30: reserva formal del cliente (una estadía con una o varias habitaciones)
public class Reserva {

    private int           idReserva;
    private String        codigo;
    private int           idCliente;
    private LocalDate     fechaEntrada;
    private LocalDate     fechaSalida;
    private int           numHuespedes;
    private int           noches;
    private BigDecimal    montoTotal;
    private String        estado;        // pendiente | pagada | cancelada
    private LocalDateTime fechaRegistro;

    private List<DetalleReserva> detalles = new ArrayList<>();

    // Datos de apoyo para el listado del admin
    private String nombreCliente;
    private String correoCliente;

    public int           getIdReserva()     { return idReserva; }
    public String        getCodigo()        { return codigo; }
    public int           getIdCliente()     { return idCliente; }
    public LocalDate     getFechaEntrada()  { return fechaEntrada; }
    public LocalDate     getFechaSalida()   { return fechaSalida; }
    public int           getNumHuespedes()  { return numHuespedes; }
    public int           getNoches()        { return noches; }
    public BigDecimal    getMontoTotal()    { return montoTotal; }
    public String        getEstado()        { return estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public List<DetalleReserva> getDetalles() { return detalles; }
    public String        getNombreCliente() { return nombreCliente; }
    public String        getCorreoCliente() { return correoCliente; }

    public void setIdReserva(int v)              { this.idReserva = v; }
    public void setCodigo(String v)              { this.codigo = v; }
    public void setIdCliente(int v)              { this.idCliente = v; }
    public void setFechaEntrada(LocalDate v)     { this.fechaEntrada = v; }
    public void setFechaSalida(LocalDate v)      { this.fechaSalida = v; }
    public void setNumHuespedes(int v)           { this.numHuespedes = v; }
    public void setNoches(int v)                 { this.noches = v; }
    public void setMontoTotal(BigDecimal v)      { this.montoTotal = v; }
    public void setEstado(String v)              { this.estado = v; }
    public void setFechaRegistro(LocalDateTime v){ this.fechaRegistro = v; }
    public void setDetalles(List<DetalleReserva> v) { this.detalles = v; }
    public void setNombreCliente(String v)       { this.nombreCliente = v; }
    public void setCorreoCliente(String v)       { this.correoCliente = v; }
}
