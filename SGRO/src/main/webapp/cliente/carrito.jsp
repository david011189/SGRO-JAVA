<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Carrito" %>
<%@ page import="pe.edu.barlen.sgro.modelo.ItemCarrito" %>
<%
    request.setAttribute("activo", "carrito");
    String cp = request.getContextPath();
    Carrito carrito = (Carrito) session.getAttribute("carrito");
    boolean vacio = (carrito == null || carrito.estaVacio());
    String aviso = (String) request.getAttribute("aviso");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Carrito de reservas — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
    <link rel="stylesheet" href="<%= cp %>/css/cliente.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Tu carrito de reservas</h1>

        <div class="pasos">
            <div class="paso hecho"><span class="num">&#10003;</span> Cotizar</div>
            <div class="paso activo"><span class="num">2</span> Carrito</div>
            <div class="paso"><span class="num">3</span> Pago</div>
            <div class="paso"><span class="num">4</span> Confirmación</div>
        </div>

        <% if (aviso != null) { %>
        <div class="alerta exito"><span>&#10004;</span><%= aviso %></div>
        <% } %>
        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <% if (vacio) { %>
            <div class="vacio">
                <div class="icono">&#128722;</div>
                <h3>Tu carrito está vacío</h3>
                <p>Agrega habitaciones desde la cotización para continuar.</p>
                <p style="margin-top:16px;">
                    <a class="btn btn-primario" href="<%= cp %>/cliente/cotizacion">Ir a cotizar</a>
                </p>
            </div>
        <% } else { %>
            <div class="layout-2col">

                <!-- Lista de habitaciones -->
                <div class="panel">
                    <div class="panel-encabezado">
                        <h2>Habitaciones seleccionadas</h2>
                        <form action="<%= cp %>/cliente/carrito" method="POST"
                              onsubmit="return confirm('¿Vaciar todo el carrito?');">
                            <input type="hidden" name="accion" value="vaciar">
                            <button type="submit" class="btn btn-sec btn-mini">Vaciar carrito</button>
                        </form>
                    </div>
                    <table class="tabla">
                        <thead>
                            <tr><th>Habitación</th><th>Tipo</th><th>Noches</th><th>Subtotal</th><th></th></tr>
                        </thead>
                        <tbody>
                        <% for (ItemCarrito it : carrito.getItems()) { %>
                            <tr>
                                <td data-label="Habitación"><strong>Hab. <%= it.getNumero() %></strong></td>
                                <td data-label="Tipo"><%= it.getTipo() %></td>
                                <td data-label="Noches"><%= it.getNoches() %></td>
                                <td data-label="Subtotal">S/ <%= it.getSubtotal() %></td>
                                <td class="acciones" data-label="">
                                    <form action="<%= cp %>/cliente/carrito" method="POST">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="idHabitacion" value="<%= it.getIdHabitacion() %>">
                                        <button type="submit" class="btn btn-peligro btn-mini">Quitar</button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>

                <!-- Resumen -->
                <div class="resumen">
                    <h3>Resumen</h3>
                    <div class="fila"><span class="dato">Entrada</span><span><%= carrito.getFechaEntrada() %></span></div>
                    <div class="fila"><span class="dato">Salida</span><span><%= carrito.getFechaSalida() %></span></div>
                    <div class="fila"><span class="dato">Noches</span><span><%= carrito.getNoches() %></span></div>
                    <div class="fila"><span class="dato">Huéspedes</span><span><%= carrito.getNumHuespedes() %></span></div>
                    <div class="fila"><span class="dato">Habitaciones</span><span><%= carrito.getCantidad() %></span></div>
                    <div class="fila total"><span>Total</span><span>S/ <%= carrito.getTotal() %></span></div>
                    <a class="btn btn-primario" href="<%= cp %>/cliente/pago">Continuar al pago</a>
                    <a class="btn btn-sec" href="<%= cp %>/cliente/cotizacion">Seguir cotizando</a>
                </div>

            </div>
        <% } %>

    </main>

</body>
</html>
