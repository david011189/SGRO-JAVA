package pe.edu.barlen.sgro.dao;

import pe.edu.barlen.sgro.modelo.Carrito;
import pe.edu.barlen.sgro.modelo.DetalleReserva;
import pe.edu.barlen.sgro.modelo.ItemCarrito;
import pe.edu.barlen.sgro.modelo.Reserva;
import pe.edu.barlen.sgro.util.ConexionDB;
import pe.edu.barlen.sgro.util.TokenUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// RF-28 a RF-33 y RF-38/39: persistencia de reservas, detalle y pago
public class ReservaDAO {

    // RF-23/24/28/30: crea la reserva completa (reserva + detalle + pago) en UNA transacción.
    // Verifica disponibilidad de cada habitación dentro de la transacción (RF-19/RF-32).
    public Reserva crearReserva(Carrito carrito, int idCliente, String medioPago)
            throws SQLException, ReservaException {

        if (carrito == null || carrito.estaVacio()) {
            throw new ReservaException("El carrito está vacío.");
        }

        Connection con = null;
        try {
            con = ConexionDB.obtener();
            con.setAutoCommit(false);

            // 1) Revalidar disponibilidad de cada habitación (evita doble reserva)
            for (ItemCarrito it : carrito.getItems()) {
                if (hayConflicto(con, it.getIdHabitacion(),
                                 carrito.getFechaEntrada(), carrito.getFechaSalida())) {
                    con.rollback();
                    throw new ReservaException(
                        "La habitación " + it.getNumero() + " ya no está disponible en esas fechas.");
                }
            }

            int        noches = carrito.getNoches();
            BigDecimal total  = carrito.getTotal();
            String     codigo = TokenUtil.codigoReserva();

            // 2) Insertar la reserva (estado pagada)
            int idReserva;
            String sqlR = "INSERT INTO reserva " +
                "(codigo, id_cliente, fecha_entrada, fecha_salida, num_huespedes, noches, monto_total, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, 'pagada')";
            try (PreparedStatement ps = con.prepareStatement(sqlR, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, codigo);
                ps.setInt   (2, idCliente);
                ps.setDate  (3, Date.valueOf(carrito.getFechaEntrada()));
                ps.setDate  (4, Date.valueOf(carrito.getFechaSalida()));
                ps.setInt   (5, carrito.getNumHuespedes());
                ps.setInt   (6, noches);
                ps.setBigDecimal(7, total);
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    idReserva = rs.next() ? rs.getInt(1) : 0;
                }
            }

            // 3) Insertar el detalle (una fila por habitación)
            String sqlD = "INSERT INTO detalle_reserva (id_reserva, id_habitacion, noches, subtotal) " +
                          "VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlD)) {
                for (ItemCarrito it : carrito.getItems()) {
                    ps.setInt(1, idReserva);
                    ps.setInt(2, it.getIdHabitacion());
                    ps.setInt(3, it.getNoches());
                    ps.setBigDecimal(4, it.getSubtotal());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            // 4) Registrar el pago aprobado
            String sqlP = "INSERT INTO pago (id_reserva, monto, medio_pago, estado_pago) " +
                          "VALUES (?, ?, ?, 'aprobado')";
            try (PreparedStatement ps = con.prepareStatement(sqlP)) {
                ps.setInt(1, idReserva);
                ps.setBigDecimal(2, total);
                ps.setString(3, medioPago);
                ps.executeUpdate();
            }

            con.commit();

            Reserva reserva = new Reserva();
            reserva.setIdReserva(idReserva);
            reserva.setCodigo(codigo);
            reserva.setIdCliente(idCliente);
            reserva.setFechaEntrada(carrito.getFechaEntrada());
            reserva.setFechaSalida(carrito.getFechaSalida());
            reserva.setNumHuespedes(carrito.getNumHuespedes());
            reserva.setNoches(noches);
            reserva.setMontoTotal(total);
            reserva.setEstado("pagada");
            return reserva;

        } catch (SQLException e) {
            if (con != null) try { con.rollback(); } catch (SQLException ig) { /* ignore */ }
            throw e;
        } finally {
            if (con != null) try { con.setAutoCommit(true); con.close(); } catch (SQLException ig) { /* ignore */ }
        }
    }

    // ¿La habitación tiene una reserva activa que se solapa con el rango?
    private boolean hayConflicto(Connection con, int idHabitacion,
                                 java.time.LocalDate entrada, java.time.LocalDate salida) throws SQLException {
        String sql = "SELECT 1 FROM detalle_reserva dr JOIN reserva r ON r.id_reserva = dr.id_reserva " +
                     "WHERE dr.id_habitacion = ? AND r.estado <> 'cancelada' " +
                     "AND r.fecha_entrada < ? AND r.fecha_salida > ? LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt (1, idHabitacion);
            ps.setDate(2, Date.valueOf(salida));
            ps.setDate(3, Date.valueOf(entrada));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // RF-33: reservas de un cliente (con su detalle)
    public List<Reserva> listarPorCliente(int idCliente) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE id_cliente = ? ORDER BY fecha_registro DESC";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            return cargarLista(con, ps);
        }
    }

    // RF-38: todas las reservas (panel admin), con datos del cliente
    public List<Reserva> listarTodas() throws SQLException {
        String sql = "SELECT r.*, u.nombre AS cli_nombre, u.apellido AS cli_apellido, u.correo AS cli_correo " +
                     "FROM reserva r JOIN usuario u ON u.id_usuario = r.id_cliente " +
                     "ORDER BY r.fecha_registro DESC";
        List<Reserva> lista = new ArrayList<>();
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reserva r = mapear(rs);
                r.setNombreCliente(rs.getString("cli_nombre") + " " + rs.getString("cli_apellido"));
                r.setCorreoCliente(rs.getString("cli_correo"));
                cargarDetalles(con, r);
                lista.add(r);
            }
        }
        return lista;
    }

    // Una reserva por código, restringida a su dueño (confirmación)
    public Reserva obtenerPorCodigo(String codigo, int idCliente) throws SQLException {
        String sql = "SELECT * FROM reserva WHERE codigo = ? AND id_cliente = ?";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigo);
            ps.setInt(2, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Reserva r = mapear(rs);
                cargarDetalles(con, r);
                return r;
            }
        }
    }

    // RF-39: cambiar el estado de una reserva (p. ej. cancelar)
    public boolean cambiarEstado(int idReserva, String estado) throws SQLException {
        String sql = "UPDATE reserva SET estado = ? WHERE id_reserva = ?";
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setInt(2, idReserva);
            return ps.executeUpdate() == 1;
        }
    }

    public int contarTotales() throws SQLException {
        try (Connection con = ConexionDB.obtener();
             PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM reserva WHERE estado <> 'cancelada'");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // ---- helpers ----
    private List<Reserva> cargarLista(Connection con, PreparedStatement ps) throws SQLException {
        List<Reserva> lista = new ArrayList<>();
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Reserva r = mapear(rs);
                cargarDetalles(con, r);
                lista.add(r);
            }
        }
        return lista;
    }

    private void cargarDetalles(Connection con, Reserva r) throws SQLException {
        String sql = "SELECT dr.id_detalle, dr.id_habitacion, dr.noches, dr.subtotal, h.numero, h.tipo " +
                     "FROM detalle_reserva dr JOIN habitacion h ON h.id_habitacion = dr.id_habitacion " +
                     "WHERE dr.id_reserva = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getIdReserva());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleReserva d = new DetalleReserva();
                    d.setIdDetalle(rs.getInt("id_detalle"));
                    d.setIdReserva(r.getIdReserva());
                    d.setIdHabitacion(rs.getInt("id_habitacion"));
                    d.setNoches(rs.getInt("noches"));
                    d.setSubtotal(rs.getBigDecimal("subtotal"));
                    d.setNumeroHabitacion(rs.getString("numero"));
                    d.setTipoHabitacion(rs.getString("tipo"));
                    r.getDetalles().add(d);
                }
            }
        }
    }

    private Reserva mapear(ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setIdReserva(rs.getInt("id_reserva"));
        r.setCodigo(rs.getString("codigo"));
        r.setIdCliente(rs.getInt("id_cliente"));
        r.setFechaEntrada(rs.getDate("fecha_entrada").toLocalDate());
        r.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
        r.setNumHuespedes(rs.getInt("num_huespedes"));
        r.setNoches(rs.getInt("noches"));
        r.setMontoTotal(rs.getBigDecimal("monto_total"));
        r.setEstado(rs.getString("estado"));
        r.setFechaRegistro(rs.getTimestamp("fecha_registro").toLocalDateTime());
        return r;
    }
}
