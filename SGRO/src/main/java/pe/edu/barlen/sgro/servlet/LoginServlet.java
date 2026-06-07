package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.UsuarioDAO;
import pe.edu.barlen.sgro.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String correo     = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");

        // RF-42: validar campos obligatorios
        if (correo == null || correo.isBlank() || contrasena == null || contrasena.isBlank()) {
            request.setAttribute("error", "Por favor, completa todos los campos.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }

        try {
            UsuarioDAO dao    = new UsuarioDAO();
            Usuario    usuario = dao.autenticar(correo.trim(), contrasena);

            if (usuario != null) {
                // CA-02: credenciales válidas → iniciar sesión (RF-02 escenario exitoso)
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("usuarioLogueado", usuario);
                sesion.setAttribute("rolUsuario", usuario.getRol());

                // RF-05: redirigir según rol
                switch (usuario.getRol()) {
                    case "administrador":
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                        break;
                    case "recepcionista":
                        response.sendRedirect(request.getContextPath() + "/recepcion/panel.jsp");
                        break;
                    default:
                        response.sendRedirect(request.getContextPath() + "/cliente/cotizacion");
                }

            } else {
                // CA-02: credenciales incorrectas → mensaje de error (RF-02 escenario de fallo)
                request.setAttribute("error", "Correo o contraseña incorrectos. Verifica tus datos e intenta de nuevo.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Error interno del sistema. Por favor, intenta más tarde.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Si ya hay sesión activa, redirigir al área correspondiente
        HttpSession sesion = request.getSession(false);
        if (sesion != null && sesion.getAttribute("usuarioLogueado") != null) {
            String rol = (String) sesion.getAttribute("rolUsuario");
            if ("administrador".equals(rol)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                response.sendRedirect(request.getContextPath() + "/cliente/cotizacion");
            }
            return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
}
