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
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mis reservas — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
    <link rel="stylesheet" href="<%= cp %>/css/cliente.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Mis reservas</h1>
        <p class="admin-subtitulo">Consulta el estado y el detalle de tus reservas (RF-33).</p>

        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <% if (reservas == null || reservas.isEmpty()) { %>
            <div class="vacio">
                <div class="icono">&#128203;</div>
                <h3>Aún no tienes reservas</h3>
                <p>Cuando completes una reserva, aparecerá aquí.</p>
                <p style="margin-top:16px;">
                    <a class="btn btn-primario" href="<%= cp %>/cliente/cotizacion">Cotizar ahora</a>
                </p>
            </div>
        <% } else {
               for (Reserva r : reservas) {
        %>
            <div class="panel" style="margin-bottom:18px;">
                <div class="panel-encabezado">
                    <h2><%= r.getCodigo() %>
                        <span class="badge <%= "cancelada".equals(r.getEstado()) ? "mantenimiento" : "disponible" %>"
                              style="margin-left:8px; text-transform:capitalize;"><%= r.getEstado() %></span>
                    </h2>
                    <span class="dato"><%= r.getFechaEntrada() %> → <%= r.getFechaSalida() %> · <%= r.getNoches() %> noche(s)</span>
                </div>
                <table class="tabla">
                    <thead>
                        <tr><th>Habitación</th><th>Tipo</th><th>Noches</th><th>Subtotal</th></tr>
                    </thead>
                    <tbody>
                    <% for (DetalleReserva d : r.getDetalles()) { %>
                        <tr>
                            <td data-label="Habitación">Hab. <%= d.getNumeroHabitacion() %></td>
                            <td data-label="Tipo"><%= d.getTipoHabitacion() %></td>
                            <td data-label="Noches"><%= d.getNoches() %></td>
                            <td data-label="Subtotal">S/ <%= d.getSubtotal() %></td>
                        </tr>
                    <% } %>
                        <tr>
                            <td colspan="3" style="text-align:right;"><strong>Total</strong></td>
                            <td><strong>S/ <%= r.getMontoTotal() %></strong></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        <%     }
           } %>

    </main>

</body>
</html>
