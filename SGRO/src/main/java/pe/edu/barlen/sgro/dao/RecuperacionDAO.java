package pe.edu.barlen.sgro.dao;

import pe.edu.barlen.sgro.util.ConexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

// RF-03: gestión de tokens de recuperación de contraseña
public class RecuperacionDAO {

    // Guarda un nuevo token asociado a un usuario, con su fecha de expiración
    public boolean crearToken(int idUsuario, String token, Timestamp expira) throws SQLException {
        String sql = "INSERT INTO recuperacion_token (id_usuario, token, expira) VALUES (?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.setString(2, token);
            ps.setTimestamp(3, expira);
            return ps.executeUpdate() == 1;
        }
    }

    // Valida un token: devuelve el id_usuario si es válido (existe, no usado y no expirado), o 0 si no.
    public int validarToken(String token) throws SQLException {
        String sql = "SELECT id_usuario FROM recuperacion_token " +
                     "WHERE token = ? AND usado = 0 AND expira > NOW()";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt("id_usuario") : 0;
            }
        }
    }

    // Marca un token como usado para que no pueda reutilizarse
    public boolean marcarUsado(String token) throws SQLException {
        String sql = "UPDATE recuperacion_token SET usado = 1 WHERE token = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            return ps.executeUpdate() == 1;
        }
    }

    // Invalida los tokens activos previos de un usuario (al pedir uno nuevo)
    public void invalidarAnteriores(int idUsuario) throws SQLException {
        String sql = "UPDATE recuperacion_token SET usado = 1 WHERE id_usuario = ? AND usado = 0";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }
}
