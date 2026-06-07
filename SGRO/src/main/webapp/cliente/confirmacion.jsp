<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Reserva" %>
<%@ page import="pe.edu.barlen.sgro.modelo.DetalleReserva" %>
<%
    request.setAttribute("activo", "reservas");
    String cp = request.getContextPath();
    Reserva r = (Reserva) request.getAttribute("reserva");
    if (r == null) { response.sendRedirect(cp + "/cliente/reservas"); return; }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserva confirmada — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
    <link rel="stylesheet" href="<%= cp %>/css/cliente.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <div class="pasos">
            <div class="paso hecho"><span class="num">&#10003;</span> Cotizar</div>
            <div class="paso hecho"><span class="num">&#10003;</span> Carrito</div>
            <div class="paso hecho"><span class="num">&#10003;</span> Pago</div>
            <div class="paso activo"><span class="num">4</span> Confirmación</div>
        </div>

        <div class="confirma-caja">
            <div class="check">&#10004;</div>
            <h1 class="admin-titulo-pagina" style="text-align:center;">¡Reserva confirmada!</h1>
            <p class="admin-subtitulo" style="text-align:center;">Tu pago se procesó y la reserva quedó registrada.</p>

            <div class="codigo"><%= r.getCodigo() %></div>

            <div class="panel" style="margin-top:18px; text-align:left;">
                <table class="tabla">
                    <tbody>
                        <tr><td class="dato">Entrada</td><td><%= r.getFechaEntrada() %></td></tr>
                        <tr><td class="dato">Salida</td><td><%= r.getFechaSalida() %></td></tr>
                        <tr><td class="dato">Noches</td><td><%= r.getNoches() %></td></tr>
                        <tr><td class="dato">Huéspedes</td><td><%= r.getNumHuespedes() %></td></tr>
                        <tr><td class="dato">Estado</td><td><span class="badge disponible"><%= r.getEstado() %></span></td></tr>
                    </tbody>
                </table>
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
                            <td colspan="3" style="text-align:right;"><strong>Total pagado</strong></td>
                            <td><strong>S/ <%= r.getMontoTotal() %></strong></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <div style="margin-top:20px; display:flex; gap:12px; justify-content:center;">
                <a class="btn btn-primario" href="<%= cp %>/cliente/reservas">Ver mis reservas</a>
                <a class="btn btn-sec" href="<%= cp %>/cliente/cotizacion">Nueva cotización</a>
            </div>
        </div>

    </main>

</body>
</html>
