<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Habitacion" %>
<%
    request.setAttribute("activo", "habitaciones");
    @SuppressWarnings("unchecked")
    List<Habitacion> habitaciones = (List<Habitacion>) request.getAttribute("habitaciones");
    String error = (String) request.getAttribute("error");
    String cp = request.getContextPath();

    String msg = request.getParameter("msg");
    String aviso = null;
    if ("creada".equals(msg))       aviso = "Habitación registrada correctamente.";
    else if ("actualizada".equals(msg)) aviso = "Habitación actualizada correctamente.";
    else if ("eliminada".equals(msg))   aviso = "Habitación eliminada.";
    else if ("noexiste".equals(msg))    error = "La habitación indicada no existe.";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Habitaciones — Administración SGRO Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Gestión de habitaciones</h1>
        <p class="admin-subtitulo">Registra, edita o elimina las habitaciones del hospedaje (RF-34 a RF-37).</p>

        <% if (aviso != null) { %>
        <div class="alerta exito"><span>&#10004;</span><%= aviso %></div>
        <% } %>
        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <div class="panel">
            <div class="panel-encabezado">
                <h2>Listado de habitaciones</h2>
                <a class="btn btn-primario" href="<%= cp %>/admin/habitaciones?accion=nueva">
                    &#43; Nueva habitación
                </a>
            </div>

            <table class="tabla">
                <thead>
                    <tr>
                        <th>N°</th>
                        <th>Tipo</th>
                        <th>Capacidad</th>
                        <th>Estado</th>
                        <th>Tarifa</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <% if (habitaciones == null || habitaciones.isEmpty()) { %>
                    <tr><td colspan="6" style="text-align:center; color:#6b7280; padding:24px;">
                        No hay habitaciones registradas.
                    </td></tr>
                <% } else {
                    for (Habitacion h : habitaciones) {
                %>
                    <tr>
                        <td data-label="N°"><strong><%= h.getNumero() %></strong></td>
                        <td data-label="Tipo"><%= h.getTipo() %></td>
                        <td data-label="Capacidad"><%= h.getCapacidad() %> pers.</td>
                        <td data-label="Estado">
                            <span class="badge <%= h.getEstado() %>"><%= h.getEstado() %></span>
                        </td>
                        <td data-label="Tarifa">S/ <%= h.getTarifa() %></td>
                        <td class="acciones" data-label="Acciones">
                            <a class="btn btn-sec btn-mini"
                               href="<%= cp %>/admin/habitaciones?accion=editar&id=<%= h.getIdHabitacion() %>">Editar</a>
                            <form action="<%= cp %>/admin/habitaciones" method="POST"
                                  onsubmit="return confirm('¿Eliminar la habitación <%= h.getNumero() %>? Esta acción no se puede deshacer.');"
                                  style="display:inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id" value="<%= h.getIdHabitacion() %>">
                                <button type="submit" class="btn btn-peligro btn-mini">Eliminar</button>
                            </form>
                        </td>
                    </tr>
                <%  } } %>
                </tbody>
            </table>
        </div>

        <p style="margin-top:18px;">
            <a class="btn btn-sec" href="<%= cp %>/admin/dashboard">&#8592; Volver al panel</a>
        </p>

    </main>

</body>
</html>
