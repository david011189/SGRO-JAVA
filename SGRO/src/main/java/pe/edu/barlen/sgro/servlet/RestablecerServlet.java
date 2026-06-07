package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.RecuperacionDAO;
import pe.edu.barlen.sgro.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

// RF-03 (paso 2): el usuario abre el enlace y define su nueva contraseña
@WebServlet("/RestablecerServlet")
public class RestablecerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String token     = request.getParameter("token");
        String clave     = request.getParameter("contrasena");
        String confirmar = request.getParameter("confirmar");

        request.setAttribute("token", token);

        try {
            RecuperacionDAO recDAO = new RecuperacionDAO();
            int idUsuario = (token == null || token.isBlank()) ? 0 : recDAO.validarToken(token);

            // Token inexistente, ya usado o expirado
            if (idUsuario == 0) {
                request.setAttribute("tokenInvalido", true);
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
                return;
            }

            // Validación de la nueva contraseña
            if (clave == null || clave.length() < 6 || clave.length() > 100) {
                request.setAttribute("error", "La contraseña debe tener entre 6 y 100 caracteres.");
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
                return;
            }
            if (!clave.equals(confirmar)) {
                request.setAttribute("error", "Las contraseñas no coinciden.");
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
                return;
            }

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            boolean actualizado = usuarioDAO.actualizarContrasena(idUsuario, clave);

            if (actualizado) {
                recDAO.marcarUsado(token);                // el enlace ya no sirve más
                response.sendRedirect(request.getContextPath() + "/login.jsp?reset=1");
            } else {
                request.setAttribute("error", "No se pudo actualizar la contraseña. Intenta de nuevo.");
                request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Error interno del sistema. Por favor, intenta más tarde.");
            request.getRequestDispatcher("/restablecer.jsp").forward(request, response);
        }
    }
}
