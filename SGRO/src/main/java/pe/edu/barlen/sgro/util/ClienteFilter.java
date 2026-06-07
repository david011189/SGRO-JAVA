package pe.edu.barlen.sgro.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// RF-02/RF-05: el área del cliente requiere haber iniciado sesión.
@WebFilter("/cliente/*")
public class ClienteFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession sesion = request.getSession(false);
        boolean autenticado = sesion != null && sesion.getAttribute("usuarioLogueado") != null;

        if (autenticado) {
            response.setHeader("Cache-Control", "no-store");
            chain.doFilter(req, res);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
    }
}
