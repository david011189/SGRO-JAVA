package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.RecuperacionDAO;
import pe.edu.barlen.sgro.dao.UsuarioDAO;
import pe.edu.barlen.sgro.util.TokenUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Pattern;

// RF-03 (paso 1): el usuario solicita recuperar su contraseña indicando su correo
@WebServlet("/RecuperarServlet")
public class RecuperarServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    // Validez del enlace: 1 hora
    private static final long VIGENCIA_MS = 60 * 60 * 1000L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String correo = request.getParameter("correo");
        correo = correo == null ? null : correo.trim();
        request.setAttribute("correo", correo);

        if (correo == null || correo.isBlank() || correo.length() > 100
                || !PATRON_CORREO.matcher(correo).matches()) {
            request.setAttribute("error", "Ingresa un correo electrónico válido.");
            request.getRequestDispatcher("/recuperar.jsp").forward(request, response);
            return;
        }

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int idUsuario = usuarioDAO.obtenerIdPorCorreo(correo);

            if (idUsuario == 0) {
                request.setAttribute("error", "No encontramos una cuenta activa con ese correo.");
                request.getRequestDispatcher("/recuperar.jsp").forward(request, response);
                return;
            }

            RecuperacionDAO recDAO = new RecuperacionDAO();
            recDAO.invalidarAnteriores(idUsuario);             // un solo enlace válido a la vez

            String token  = TokenUtil.generar();
            Timestamp exp = new Timestamp(System.currentTimeMillis() + VIGENCIA_MS);
            recDAO.crearToken(idUsuario, token, exp);

            // En producción este enlace se enviaría por correo. Aquí se muestra en pantalla.
            String enlace = request.getRequestURL().toString()
                    .replace("/RecuperarServlet", "/restablecer.jsp?token=" + token);

            request.setAttribute("enlace", enlace);
            request.getRequestDispatcher("/recuperar.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Error interno del sistema. Por favor, intenta más tarde.");
            request.getRequestDispatcher("/recuperar.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/recuperar.jsp").forward(request, response);
    }
}
