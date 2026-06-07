<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Cierra la sesión y vuelve al login
    session.invalidate();
    response.sendRedirect(request.getContextPath() + "/login.jsp");
%>
