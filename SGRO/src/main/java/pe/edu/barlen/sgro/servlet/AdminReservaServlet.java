package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.ReservaDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// RF-38/39: el administrador visualiza y cancela reservas
@WebServlet("/admin/reservas")
public class AdminReservaServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("reservas", new ReservaDAO().listarTodas());
        } catch (SQLException e) {
            request.setAttribute("error", "No se pudieron cargar las reservas.");
        }
        request.getRequestDispatcher("/admin/reservas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String msg = "";
        try {
            if ("cancelar".equals(accion)) {
                int id = Integer.parseInt(request.getParameter("id"));
                new ReservaDAO().cambiarEstado(id, "cancelada");
                msg = "?msg=cancelada";
            }
        } catch (SQLException | NumberFormatException e) {
            msg = "?msg=error";
        }
        response.sendRedirect(request.getContextPath() + "/admin/reservas" + msg);
    }
}
