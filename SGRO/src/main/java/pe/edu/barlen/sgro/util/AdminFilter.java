package pe.edu.barlen.sgro.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// RF-05: protege el área de administración. Solo entra un usuario con rol administrador.
@WebFilter("/admin/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession sesion = request.getSession(false);
        boolean esAdmin = sesion != null
                && sesion.getAttribute("usuarioLogueado") != null
                && "administrador".equals(sesion.getAttribute("rolUsuario"));

        if (esAdmin) {
            // Evita que el navegador cachee páginas privadas tras cerrar sesión
            response.setHeader("Cache-Control", "no-store");
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
