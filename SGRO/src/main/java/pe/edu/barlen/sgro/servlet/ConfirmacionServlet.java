package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.ReservaDAO;
import pe.edu.barlen.sgro.modelo.Reserva;
import pe.edu.barlen.sgro.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// RF-25/30: confirmación de la reserva (paso 4 — asignación concretada)
@WebServlet("/cliente/confirmacion")
public class ConfirmacionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String codigo = request.getParameter("codigo");
        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");

        if (codigo == null || cliente == null) {
            response.sendRedirect(request.getContextPath() + "/cliente/reservas");
            return;
        }

        try {
            Reserva reserva = new ReservaDAO().obtenerPorCodigo(codigo, cliente.getIdUsuario());
            if (reserva == null) {
                response.sendRedirect(request.getContextPath() + "/cliente/reservas");
                return;
            }
            request.setAttribute("reserva", reserva);
            request.getRequestDispatcher("/cliente/confirmacion.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "No se pudo cargar la confirmación.");
            request.getRequestDispatcher("/cliente/reservas").forward(request, response);
        }
    }
}
