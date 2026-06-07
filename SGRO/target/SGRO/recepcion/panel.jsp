<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Usuario" %>
<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    if (u == null || !"recepcionista".equals(session.getAttribute("rolUsuario"))) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel de Recepción — Barlen</title>
    <style>
        body { font-family: system-ui, sans-serif; margin: 0; background: #f4f6f9; }
        header { background: #2e7d5b; color: #fff; padding: 1rem 2rem; display: flex; justify-content: space-between; align-items: center; }
        a.salir { color: #fff; text-decoration: none; border: 1px solid #fff; padding: .4rem .9rem; border-radius: 6px; }
        main { padding: 2rem; }
        .placeholder { background: #fff; border-radius: 10px; padding: 2rem; box-shadow: 0 2px 8px rgba(0,0,0,.06); }
    </style>
</head>
<body>
    <header>
        <strong>SGRO · Recepción</strong>
        <span>Hola, <%= u.getNombre() %> (<%= u.getRol() %>) · <a class="salir" href="<%= request.getContextPath() %>/logout.jsp">Cerrar sesión</a></span>
    </header>
    <main>
        <div class="placeholder">
            <h1>Panel de Recepción</h1>
            <p>Página en construcción. Aquí irán el check-in/check-out, registro de huéspedes y gestión de reservas del día.</p>
        </div>
    </main>
</body>
</html>
