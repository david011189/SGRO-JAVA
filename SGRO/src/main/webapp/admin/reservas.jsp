<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Reserva" %>
<%@ page import="pe.edu.barlen.sgro.modelo.DetalleReserva" %>
<%
    request.setAttribute("activo", "reservas");
    String cp = request.getContextPath();
    @SuppressWarnings("unchecked")
    List<Reserva> reservas = (List<Reserva>) request.getAttribute("reservas");
    String error = (String) request.getAttribute("error");

    String msg = request.getParameter("msg");
    String aviso = null;
    if ("cancelada".equals(msg)) aviso = "Reserva cancelada correctamente.";
    else if ("error".equals(msg)) error = "No se pudo procesar la acción.";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reservas — Administración SGRO Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Gestión de reservas</h1>
        <p class="admin-subtitulo">Listado de reservas registradas y cancelación (RF-38, RF-39).</p>

        <% if (aviso != null) { %>
        <div class="alerta exito"><span>&#10004;</span><%= aviso %></div>
        <% } %>
        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <div class="panel">
            <div class="panel-encabezado">
                <h2>Reservas (<%= reservas == null ? 0 : reservas.size() %>)</h2>
            </div>
            <table class="tabla">
                <thead>
                    <tr>
                        <th>Código</th><th>Cliente</th><th>Fechas</th><th>Hab.</th>
                        <th>Total</th><th>Estado</th><th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                <% if (reservas == null || reservas.isEmpty()) { %>
                    <tr><td colspan="7" style="text-align:center; color:#6b7280; padding:24px;">
                        No hay reservas registradas.
                    </td></tr>
                <% } else {
                       for (Reserva r : reservas) {
                           StringBuilder habs = new StringBuilder();
                           for (DetalleReserva d : r.getDetalles()) {
                               if (habs.length() > 0) habs.append(", ");
                               habs.append(d.getNumeroHabitacion());
                           }
                %>
                    <tr>
                        <td data-label="Código"><strong><%= r.getCodigo() %></strong></td>
                        <td data-label="Cliente"><%= r.getNombreCliente() %><br>
                            <span class="dato" style="font-size:.78rem;color:#6b7280;"><%= r.getCorreoCliente() %></span></td>
                        <td data-label="Fechas"><%= r.getFechaEntrada() %> → <%= r.getFechaSalida() %></td>
                        <td data-label="Hab."><%= habs.toString() %></td>
                        <td data-label="Total">S/ <%= r.getMontoTotal() %></td>
                        <td data-label="Estado">
                            <span class="badge <%= "cancelada".equals(r.getEstado()) ? "mantenimiento" : ("pagada".equals(r.getEstado()) ? "disponible" : "ocupada") %>"
                                  style="text-transform:capitalize;"><%= r.getEstado() %></span>
                        </td>
                        <td class="acciones" data-label="Acciones">
                            <% if (!"cancelada".equals(r.getEstado())) { %>
                            <form action="<%= cp %>/admin/reservas" method="POST"
                                  onsubmit="return confirm('¿Cancelar la reserva <%= r.getCodigo() %>?');">
                                <input type="hidden" name="accion" value="cancelar">
                                <input type="hidden" name="id" value="<%= r.getIdReserva() %>">
                                <button type="submit" class="btn btn-peligro btn-mini">Cancelar</button>
                            </form>
                            <% } else { %>
                            <span class="dato" style="color:#6b7280;">—</span>
                            <% } %>
                        </td>
                    </tr>
                <%     } } %>
                </tbody>
            </table>
        </div>

        <p style="margin-top:18px;">
            <a class="btn btn-sec" href="<%= cp %>/admin/dashboard">&#8592; Volver al panel</a>
        </p>

    </main>

</body>
</html>
