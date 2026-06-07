package pe.edu.barlen.sgro.dao;

import pe.edu.barlen.sgro.modelo.Habitacion;
import pe.edu.barlen.sgro.util.ConexionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// RF-34 a RF-37: acceso a datos de habitaciones
public class HabitacionDAO {

    // RF-38 base: lista todas las habitaciones ordenadas por número
    public List<Habitacion> listar() throws SQLException {
        String sql = "SELECT id_habitacion, numero, tipo, capacidad, descripcion, estado, tarifa " +
                     "FROM habitacion ORDER BY numero";
        List<Habitacion> lista = new ArrayList<>();

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public Habitacion obtenerPorId(int id) throws SQLException {
        String sql = "SELECT id_habitacion, numero, tipo, capacidad, descripcion, estado, tarifa " +
                     "FROM habitacion WHERE id_habitacion = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapear(rs) : null;
            }
        }
    }

    // ¿Existe ya el número? (excluye un id al editar). RF-34/35 integridad
    public boolean existeNumero(String numero, int excluirId) throws SQLException {
        String sql = "SELECT 1 FROM habitacion WHERE numero = ? AND id_habitacion <> ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, numero);
            ps.setInt(2, excluirId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // RF-34: registrar nueva habitación. Devuelve el id generado o 0.
    public int crear(Habitacion h) throws SQLException {
        String sql = "INSERT INTO habitacion (numero, tipo, capacidad, descripcion, estado, tarifa) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            enlazar(ps, h);
            if (ps.executeUpdate() == 0) return 0;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    // RF-35/36/37: editar habitación (incluye tarifa y estado/disponibilidad)
    public boolean actualizar(Habitacion h) throws SQLException {
        String sql = "UPDATE habitacion SET numero = ?, tipo = ?, capacidad = ?, " +
                     "descripcion = ?, estado = ?, tarifa = ? WHERE id_habitacion = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            enlazar(ps, h);
            ps.setInt(7, h.getIdHabitacion());
            return ps.executeUpdate() == 1;
        }
    }

    // RF-35: eliminar habitación
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM habitacion WHERE id_habitacion = ?";

        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    // RF-09 a RF-13: habitaciones disponibles para un rango de fechas.
    // Disponible = estado 'disponible', capacidad suficiente, tipo opcional,
    // y sin ninguna reserva activa que se solape con el rango pedido.
    public List<Habitacion> buscarDisponibles(java.time.LocalDate entrada, java.time.LocalDate salida,
                                              int huespedes, String tipo) throws SQLException {
        StringBuilder sql = new StringBuilder(
            "SELECT h.id_habitacion, h.numero, h.tipo, h.capacidad, h.descripcion, h.estado, h.tarifa " +
            "FROM habitacion h " +
            "WHERE h.estado = 'disponible' AND h.capacidad >= ? " +
            "AND NOT EXISTS ( " +
            "    SELECT 1 FROM detalle_reserva dr " +
            "    JOIN reserva r ON r.id_reserva = dr.id_reserva " +
            "    WHERE dr.id_habitacion = h.id_habitacion " +
            "      AND r.estado <> 'cancelada' " +
            "      AND r.fecha_entrada < ? AND r.fecha_salida > ? ) ");
        boolean filtraTipo = tipo != null && !tipo.isBlank();
        if (filtraTipo) sql.append("AND h.tipo = ? ");
        sql.append("ORDER BY h.numero");

        List<Habitacion> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            ps.setInt (1, huespedes);
            ps.setDate(2, java.sql.Date.valueOf(salida));
            ps.setDate(3, java.sql.Date.valueOf(entrada));
            if (filtraTipo) ps.setString(4, tipo);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) lista.add(mapear(rs));
            }
        }
        return lista;
    }

    // Estadísticas para el panel: conteos por estado + total y tarifa promedio
    public Map<String, Object> estadisticas() throws SQLException {
        String sql = "SELECT " +
                     "COUNT(*) AS total, " +
                     "SUM(estado = 'disponible')    AS disponibles, " +
                     "SUM(estado = 'ocupada')       AS ocupadas, " +
                     "SUM(estado = 'mantenimiento') AS mantenimiento, " +
                     "COALESCE(AVG(tarifa), 0)      AS tarifa_prom " +
                     "FROM habitacion";

        Map<String, Object> stats = new LinkedHashMap<>();
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                stats.put("total",         rs.getInt("total"));
                stats.put("disponibles",   rs.getInt("disponibles"));
                stats.put("ocupadas",      rs.getInt("ocupadas"));
                stats.put("mantenimiento", rs.getInt("mantenimiento"));
                stats.put("tarifaProm",    rs.getBigDecimal("tarifa_prom"));
            }
        }
        return stats;
    }

    // ---- helpers ----
    private Habitacion mapear(ResultSet rs) throws SQLException {
        return new Habitacion(
            rs.getInt       ("id_habitacion"),
            rs.getString    ("numero"),
            rs.getString    ("tipo"),
            rs.getInt       ("capacidad"),
            rs.getString    ("descripcion"),
            rs.getString    ("estado"),
            rs.getBigDecimal("tarifa")
        );
    }

    private void enlazar(PreparedStatement ps, Habitacion h) throws SQLException {
        ps.setString(1, h.getNumero());
        ps.setString(2, h.getTipo());
        ps.setInt   (3, h.getCapacidad());
        ps.setString(4, h.getDescripcion());
        ps.setString(5, h.getEstado());
        ps.setBigDecimal(6, h.getTarifa() != null ? h.getTarifa() : BigDecimal.ZERO);
    }
}
