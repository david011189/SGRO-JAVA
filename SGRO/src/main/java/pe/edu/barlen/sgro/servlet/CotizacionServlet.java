package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.HabitacionDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

// RF-07 a RF-13: cotización — consulta de disponibilidad y tarifas por fechas
@WebServlet("/cliente/cotizacion")
public class CotizacionServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/cliente/cotizacion.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String sEntrada  = request.getParameter("fechaEntrada");
        String sSalida   = request.getParameter("fechaSalida");
        String sHuesp    = request.getParameter("huespedes");
        String tipo      = request.getParameter("tipo");

        // Conservar lo elegido para repintar el formulario
        request.setAttribute("fechaEntrada", sEntrada);
        request.setAttribute("fechaSalida",  sSalida);
        request.setAttribute("huespedes",    sHuesp);
        request.setAttribute("tipo",         tipo);

        LocalDate entrada, salida;
        int huespedes;
        try {
            entrada   = LocalDate.parse(sEntrada);
            salida    = LocalDate.parse(sSalida);
            huespedes = Integer.parseInt(sHuesp);
        } catch (DateTimeParseException | NumberFormatException | NullPointerException e) {
            error(request, response, "Completa las fechas y el número de huéspedes.");
            return;
        }

        // US03: validación de fechas
        if (entrada.isBefore(LocalDate.now())) {
            error(request, response, "La fecha de entrada no puede ser anterior a hoy.");
            return;
        }
        if (!salida.isAfter(entrada)) {
            error(request, response, "La fecha de salida debe ser posterior a la de entrada (mínimo 1 noche).");
            return;
        }
        if (huespedes < 1 || huespedes > 20) {
            error(request, response, "Indica un número de huéspedes válido (entre 1 y 20).");
            return;
        }

        try {
            int noches = (int) ChronoUnit.DAYS.between(entrada, salida);
            request.setAttribute("noches", noches);
            request.setAttribute("habitaciones",
                    new HabitacionDAO().buscarDisponibles(entrada, salida, huespedes, tipo));
            request.setAttribute("busquedaRealizada", true);
            request.getRequestDispatcher("/cliente/cotizacion.jsp").forward(request, response);

        } catch (SQLException e) {
            error(request, response, "Error al consultar la disponibilidad. Intenta más tarde.");
        }
    }

    private void error(HttpServletRequest req, HttpServletResponse resp, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/cliente/cotizacion.jsp").forward(req, resp);
    }
}
