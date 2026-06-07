package pe.edu.barlen.sgro.modelo;

import java.math.BigDecimal;

// RF-34 a RF-37: entidad Habitación del hospedaje
public class Habitacion {

    private int        idHabitacion;
    private String     numero;
    private String     tipo;        // Simple | Doble | Matrimonial | Familiar
    private int        capacidad;
    private String     descripcion;
    private String     estado;      // disponible | ocupada | mantenimiento
    private BigDecimal tarifa;

    public Habitacion() {}

    public Habitacion(int idHabitacion, String numero, String tipo, int capacidad,
                      String descripcion, String estado, BigDecimal tarifa) {
        this.idHabitacion = idHabitacion;
        this.numero       = numero;
        this.tipo         = tipo;
        this.capacidad    = capacidad;
        this.descripcion  = descripcion;
        this.estado       = estado;
        this.tarifa       = tarifa;
    }

    public int        getIdHabitacion() { return idHabitacion; }
    public String     getNumero()       { return numero;       }
    public String     getTipo()         { return tipo;         }
    public int        getCapacidad()    { return capacidad;    }
    public String     getDescripcion()  { return descripcion;  }
    public String     getEstado()       { return estado;       }
    public BigDecimal getTarifa()       { return tarifa;       }

    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }
    public void setNumero      (String numero)    { this.numero       = numero;       }
    public void setTipo        (String tipo)      { this.tipo         = tipo;         }
    public void setCapacidad   (int capacidad)    { this.capacidad    = capacidad;    }
    public void setDescripcion (String desc)      { this.descripcion  = desc;         }
    public void setEstado      (String estado)    { this.estado       = estado;       }
    public void setTarifa      (BigDecimal tarifa){ this.tarifa       = tarifa;       }
}
