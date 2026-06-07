package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.ReservaDAO;
import pe.edu.barlen.sgro.dao.ReservaException;
import pe.edu.barlen.sgro.modelo.Carrito;
import pe.edu.barlen.sgro.modelo.Reserva;
import pe.edu.barlen.sgro.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

// RF-21 a RF-30: pago (simulado) de la reserva y registro formal
@WebServlet("/cliente/pago")
public class PagoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Set<String> MEDIOS = Set.of("tarjeta", "yape", "efectivo");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Carrito carrito = (Carrito) request.getSession().getAttribute("carrito");
        if (carrito == null || carrito.estaVacio()) {
            response.sendRedirect(request.getContextPath() + "/cliente/carrito");
            return;
        }
        request.getRequestDispatcher("/cliente/pago.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession sesion = request.getSession();
        Carrito carrito = (Carrito) sesion.getAttribute("carrito");

        // RF-07: no se puede pagar sin carrito
        if (carrito == null || carrito.estaVacio()) {
            response.sendRedirect(request.getContextPath() + "/cliente/carrito");
            return;
        }

        String medio       = request.getParameter("medioPago");
        String numeroTarjeta = request.getParameter("numeroTarjeta");

        if (medio == null || !MEDIOS.contains(medio)) {
            error(request, response, "Selecciona un medio de pago válido.");
            return;
        }

        // RF-23/24: simulación del procesamiento del pago
        if ("tarjeta".equals(medio)) {
            String soloDigitos = numeroTarjeta == null ? "" : numeroTarjeta.replaceAll("\\s+", "");
            if (!soloDigitos.matches("\\d{16}")) {
                // US09 escenario 2: pago rechazado
                error(request, response, "El pago fue rechazado. Verifica el número de tarjeta (16 dígitos).");
                return;
            }
        }

        Usuario cliente = (Usuario) sesion.getAttribute("usuarioLogueado");

        try {
            Reserva reserva = new ReservaDAO().crearReserva(carrito, cliente.getIdUsuario(), medio);
            // RF-29: la disponibilidad queda actualizada (las fechas quedan bloqueadas por la reserva)
            sesion.removeAttribute("carrito"); // RF-20: el carrito se consume
            response.sendRedirect(request.getContextPath() + "/cliente/confirmacion?codigo=" + reserva.getCodigo());

        } catch (ReservaException e) {
            // Alguna habitación dejó de estar disponible (RF-19/RF-32)
            sesion.setAttribute("errorCarrito", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cliente/carrito");

        } catch (SQLException e) {
            error(request, response, "Error al procesar el pago. Intenta nuevamente en unos minutos.");
        }
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/cliente/pago.jsp").forward(req, resp);
    }
}
