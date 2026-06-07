package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.ReservaDAO;
import pe.edu.barlen.sgro.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// RF-33: el cliente consulta el estado de sus reservas
@WebServlet("/cliente/reservas")
public class MisReservasServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario cliente = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        try {
            request.setAttribute("reservas",
                    new ReservaDAO().listarPorCliente(cliente.getIdUsuario()));
        } catch (SQLException e) {
            request.setAttribute("error", "No se pudieron cargar tus reservas.");
        }
        request.getRequestDispatcher("/cliente/reservas.jsp").forward(request, response);
    }
}
