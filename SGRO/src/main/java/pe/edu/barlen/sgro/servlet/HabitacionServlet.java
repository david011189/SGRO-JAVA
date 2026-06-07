package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.HabitacionDAO;
import pe.edu.barlen.sgro.modelo.Habitacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Set;

// RF-34 a RF-37: gestión de habitaciones (listar / crear / editar / eliminar)
@WebServlet("/admin/habitaciones")
public class HabitacionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Set<String> TIPOS =
            Set.of("Simple", "Doble", "Matrimonial", "Familiar");
    private static final Set<String> ESTADOS =
            Set.of("disponible", "ocupada", "mantenimiento");

    private final HabitacionDAO dao = new HabitacionDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        accion = accion == null ? "listar" : accion;

        try {
            switch (accion) {
                case "nueva":
                    request.setAttribute("habitacion", new Habitacion());
                    request.setAttribute("modo", "crear");
                    request.getRequestDispatcher("/admin/habitacion-form.jsp").forward(request, response);
                    break;

                case "editar":
                    Habitacion h = dao.obtenerPorId(parseInt(request.getParameter("id")));
                    if (h == null) {
                        response.sendRedirect(request.getContextPath() + "/admin/habitaciones?msg=noexiste");
                        return;
                    }
                    request.setAttribute("habitacion", h);
                    request.setAttribute("modo", "editar");
                    request.getRequestDispatcher("/admin/habitacion-form.jsp").forward(request, response);
                    break;

                default: // listar
                    request.setAttribute("habitaciones", dao.listar());
                    request.getRequestDispatcher("/admin/habitaciones.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error al acceder a las habitaciones.");
            request.getRequestDispatcher("/admin/habitaciones.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String accion = request.getParameter("accion");

        try {
            if ("eliminar".equals(accion)) {
                dao.eliminar(parseInt(request.getParameter("id")));
                response.sendRedirect(request.getContextPath() + "/admin/habitaciones?msg=eliminada");
                return;
            }

            // accion = guardar (crear o editar)
            int    id          = parseInt(request.getParameter("id"));
            String numero      = trim(request.getParameter("numero"));
            String tipo        = trim(request.getParameter("tipo"));
            String descripcion = trim(request.getParameter("descripcion"));
            String estado      = trim(request.getParameter("estado"));
            Integer capacidad  = parseIntObj(request.getParameter("capacidad"));
            BigDecimal tarifa  = parseDecimal(request.getParameter("tarifa"));

            boolean editar = id > 0;

            Habitacion h = new Habitacion();
            h.setIdHabitacion(id);
            h.setNumero(numero);
            h.setTipo(tipo);
            h.setCapacidad(capacidad == null ? 0 : capacidad);
            h.setDescripcion(descripcion);
            h.setEstado(estado);
            h.setTarifa(tarifa);

            String error = validar(h, capacidad, tarifa);
            if (error == null && dao.existeNumero(numero, editar ? id : 0)) {
                error = "Ya existe una habitación con el número " + numero + ".";
            }

            if (error != null) {
                request.setAttribute("error", error);
                request.setAttribute("habitacion", h);
                request.setAttribute("modo", editar ? "editar" : "crear");
                request.getRequestDispatcher("/admin/habitacion-form.jsp").forward(request, response);
                return;
            }

            if (editar) {
                dao.actualizar(h);
                response.sendRedirect(request.getContextPath() + "/admin/habitaciones?msg=actualizada");
            } else {
                dao.crear(h);
                response.sendRedirect(request.getContextPath() + "/admin/habitaciones?msg=creada");
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Error interno al guardar la habitación.");
            request.setAttribute("modo", "crear");
            request.getRequestDispatcher("/admin/habitacion-form.jsp").forward(request, response);
        }
    }

    // ---- validación de servidor (RF-42) ----
    private String validar(Habitacion h, Integer capacidad, BigDecimal tarifa) {
        if (isBlank(h.getNumero()) || isBlank(h.getTipo()) || isBlank(h.getEstado())) {
            return "Completa el número, el tipo y el estado de la habitación.";
        }
        if (h.getNumero().length() > 10) {
            return "El número de habitación no puede superar los 10 caracteres.";
        }
        if (!TIPOS.contains(h.getTipo())) {
            return "Selecciona un tipo de habitación válido.";
        }
        if (!ESTADOS.contains(h.getEstado())) {
            return "Selecciona un estado válido.";
        }
        if (capacidad == null || capacidad < 1 || capacidad > 20) {
            return "La capacidad debe ser un número entre 1 y 20.";
        }
        if (tarifa == null || tarifa.signum() < 0) {
            return "La tarifa debe ser un monto válido (mayor o igual a 0).";
        }
        return null;
    }

    // ---- utilidades de parseo ----
    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

    private static int parseInt(String s) {
        try { return s == null ? 0 : Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private static Integer parseIntObj(String s) {
        try { return s == null || s.isBlank() ? null : Integer.valueOf(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private static BigDecimal parseDecimal(String s) {
        try { return s == null || s.isBlank() ? null : new BigDecimal(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }
}
