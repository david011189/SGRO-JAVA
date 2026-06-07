package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.HabitacionDAO;
import pe.edu.barlen.sgro.modelo.Carrito;
import pe.edu.barlen.sgro.modelo.Habitacion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

// RF-14 a RF-20: carrito de reservas
@WebServlet("/cliente/carrito")
public class CarritoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession sesion = request.getSession();
        // Mostrar y limpiar el aviso de la última acción (patrón flash)
        Object aviso = sesion.getAttribute("avisoCarrito");
        if (aviso != null) { request.setAttribute("aviso", aviso); sesion.removeAttribute("avisoCarrito"); }
        Object err = sesion.getAttribute("errorCarrito");
        if (err != null) { request.setAttribute("error", err); sesion.removeAttribute("errorCarrito"); }

        request.getRequestDispatcher("/cliente/carrito.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession();
        Carrito carrito = obtenerCarrito(sesion);
        String accion = request.getParameter("accion");

        switch (accion == null ? "" : accion) {
            case "agregar":
                agregar(request, carrito, sesion);
                break;
            case "eliminar":
                carrito.eliminar(parseInt(request.getParameter("idHabitacion")));
                sesion.setAttribute("avisoCarrito", "Habitación eliminada del carrito.");
                break;
            case "vaciar":
                carrito.vaciar();
                sesion.setAttribute("avisoCarrito", "Carrito vaciado.");
                break;
            default:
                /* nada */
        }
        // PRG: redirigir para evitar reenvíos
        response.sendRedirect(request.getContextPath() + "/cliente/carrito");
    }

    private void agregar(HttpServletRequest request, Carrito carrito, HttpSession sesion) {
        try {
            int idHab     = parseInt(request.getParameter("idHabitacion"));
            int huespedes = parseInt(request.getParameter("huespedes"));
            LocalDate entrada = LocalDate.parse(request.getParameter("fechaEntrada"));
            LocalDate salida  = LocalDate.parse(request.getParameter("fechaSalida"));

            HabitacionDAO dao = new HabitacionDAO();

            // US06 escenario 2: revalidar disponibilidad antes de agregar
            List<Habitacion> disponibles = dao.buscarDisponibles(entrada, salida, huespedes, null);
            Habitacion elegida = disponibles.stream()
                    .filter(h -> h.getIdHabitacion() == idHab)
                    .findFirst().orElse(null);

            if (elegida == null) {
                sesion.setAttribute("errorCarrito",
                        "Esa habitación ya no está disponible para las fechas seleccionadas.");
                return;
            }

            String error = carrito.agregar(elegida, entrada, salida, huespedes);
            if (error != null) {
                sesion.setAttribute("errorCarrito", error);
            } else {
                sesion.setAttribute("avisoCarrito",
                        "Habitación " + elegida.getNumero() + " agregada al carrito.");
            }

        } catch (SQLException e) {
            sesion.setAttribute("errorCarrito", "Error al agregar la habitación. Intenta de nuevo.");
        } catch (Exception e) {
            sesion.setAttribute("errorCarrito", "Datos de la habitación o de fechas no válidos.");
        }
    }

    private Carrito obtenerCarrito(HttpSession sesion) {
        Carrito c = (Carrito) sesion.getAttribute("carrito");
        if (c == null) { c = new Carrito(); sesion.setAttribute("carrito", c); }
        return c;
    }

    private static int parseInt(String s) {
        try { return s == null ? 0 : Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return 0; }
    }
}
