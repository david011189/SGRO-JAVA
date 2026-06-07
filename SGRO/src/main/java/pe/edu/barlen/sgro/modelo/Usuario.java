package pe.edu.barlen.sgro.modelo;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String contrasena;
    private String telefono;
    private String rol; // "cliente" | "administrador" | "recepcionista"

    public Usuario() {}

    public Usuario(int idUsuario, String nombre, String apellido,
                   String correo, String telefono, String rol) {
        this.idUsuario  = idUsuario;
        this.nombre     = nombre;
        this.apellido   = apellido;
        this.correo     = correo;
        this.telefono   = telefono;
        this.rol        = rol;
    }

    public int    getIdUsuario()  { return idUsuario;  }
    public String getNombre()     { return nombre;     }
    public String getApellido()   { return apellido;   }
    public String getCorreo()     { return correo;     }
    public String getContrasena() { return contrasena; }
    public String getTelefono()   { return telefono;   }
    public String getRol()        { return rol;        }

    public void setIdUsuario (int    idUsuario)  { this.idUsuario  = idUsuario;  }
    public void setNombre    (String nombre)     { this.nombre     = nombre;     }
    public void setApellido  (String apellido)   { this.apellido   = apellido;   }
    public void setCorreo    (String correo)     { this.correo     = correo;     }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setTelefono  (String telefono)   { this.telefono   = telefono;   }
    public void setRol       (String rol)        { this.rol        = rol;        }
}
