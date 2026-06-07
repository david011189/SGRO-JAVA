package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.HabitacionDAO;
import pe.edu.barlen.sgro.dao.ReservaDAO;
import pe.edu.barlen.sgro.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

// Panel de administración: reúne los KPIs y muestra el dashboard
@WebServlet("/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Map<String, Object> stats = new HabitacionDAO().estadisticas();
            int clientes = new UsuarioDAO().contarPorRol("cliente");
            int reservas = new ReservaDAO().contarTotales();

            request.setAttribute("stats", stats);
            request.setAttribute("clientes", clientes);
            request.setAttribute("reservas", reservas);

        } catch (SQLException e) {
            request.setAttribute("errorDatos", "No se pudieron cargar las estadísticas.");
        }

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}
