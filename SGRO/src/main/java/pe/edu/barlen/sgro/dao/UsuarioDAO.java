package pe.edu.barlen.sgro.dao;

import pe.edu.barlen.sgro.modelo.Usuario;
import pe.edu.barlen.sgro.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    // RF-02: valida correo + contraseña y devuelve el usuario o null si no coincide
    public Usuario autenticar(String correo, String contrasena) throws SQLException {
        String sql = "SELECT id_usuario, nombre, apellido, correo, telefono, rol " +
                     "FROM usuario WHERE correo = ? AND contrasena = SHA2(?, 256) AND activo = 1";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contrasena);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt   ("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("correo"),
                        rs.getString("telefono"),
                        rs.getString("rol")
                    );
                }
            }
        }
        return null; // credenciales inválidas — CA-02 escenario de fallo
    }

    // RF-01: verifica si ya existe un usuario con ese correo (correo es UNIQUE)
    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT 1 FROM usuario WHERE correo = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // RF-01: registra un nuevo cliente. La contraseña se almacena con SHA2-256.
    public boolean registrar(Usuario u, String contrasena) throws SQLException {
        String sql = "INSERT INTO usuario (nombre, apellido, correo, contrasena, telefono, rol) " +
                     "VALUES (?, ?, ?, SHA2(?, 256), ?, 'cliente')";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getApellido());
            ps.setString(3, u.getCorreo());
            ps.setString(4, contrasena);
            // teléfono es opcional: si viene vacío se guarda NULL
            if (u.getTelefono() == null || u.getTelefono().isBlank()) {
                ps.setNull(5, java.sql.Types.VARCHAR);
            } else {
                ps.setString(5, u.getTelefono());
            }

            return ps.executeUpdate() == 1;
        }
    }

    // RF-03: devuelve el id del usuario activo con ese correo, o 0 si no existe
    public int obtenerIdPorCorreo(String correo) throws SQLException {
        String sql = "SELECT id_usuario FROM usuario WHERE correo = ? AND activo = 1";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id_usuario") : 0;
            }
        }
    }

    // Cuenta usuarios activos por rol (para KPIs del panel admin)
    public int contarPorRol(String rol) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rol = ? AND activo = 1";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, rol);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // RF-03: actualiza la contraseña (SHA2-256) de un usuario
    public boolean actualizarContrasena(int idUsuario, String nuevaContrasena) throws SQLException {
        String sql = "UPDATE usuario SET contrasena = SHA2(?, 256) WHERE id_usuario = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContrasena);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() == 1;
        }
    }
}
