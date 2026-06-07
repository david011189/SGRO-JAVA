<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Carrito" %>
<%@ page import="pe.edu.barlen.sgro.modelo.ItemCarrito" %>
<%
    request.setAttribute("activo", "carrito");
    String cp = request.getContextPath();
    Carrito carrito = (Carrito) session.getAttribute("carrito");
    String error = (String) request.getAttribute("error");
    // El servlet ya redirige si el carrito está vacío; este es un resguardo
    if (carrito == null || carrito.estaVacio()) {
        response.sendRedirect(cp + "/cliente/carrito");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pago de la reserva — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
    <link rel="stylesheet" href="<%= cp %>/css/cliente.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Pago de tu reserva</h1>

        <div class="pasos">
            <div class="paso hecho"><span class="num">&#10003;</span> Cotizar</div>
            <div class="paso hecho"><span class="num">&#10003;</span> Carrito</div>
            <div class="paso activo"><span class="num">3</span> Pago</div>
            <div class="paso"><span class="num">4</span> Confirmación</div>
        </div>

        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <div class="layout-2col">

            <!-- Formulario de pago -->
            <div class="form-panel" style="max-width:none;">
                <form action="<%= cp %>/cliente/pago" method="POST" id="formPago">

                    <h3 style="color:var(--color-acento); margin-bottom:14px;">Medio de pago</h3>

                    <div class="form-grupo">
                        <label>Selecciona cómo deseas pagar</label>
                        <select id="medioPago" name="medioPago" onchange="toggleTarjeta()">
                            <option value="tarjeta">Tarjeta de crédito / débito</option>
                            <option value="yape">Yape</option>
                            <option value="efectivo">Efectivo (pago en recepción)</option>
                        </select>
                    </div>

                    <div id="camposTarjeta">
                        <div class="form-grupo">
                            <label for="numeroTarjeta">Número de tarjeta</label>
                            <input type="text" id="numeroTarjeta" name="numeroTarjeta"
                                   placeholder="16 dígitos" maxlength="19" inputmode="numeric">
                            <p class="ayuda">Pago simulado: ingresa 16 dígitos para aprobar (cualquier otro valor se rechaza).</p>
                        </div>
                        <div class="form-fila">
                            <div class="form-grupo">
                                <label for="vence">Vence</label>
                                <input type="text" id="vence" name="vence" placeholder="MM/AA" maxlength="5">
                            </div>
                            <div class="form-grupo">
                                <label for="cvv">CVV</label>
                                <input type="text" id="cvv" name="cvv" placeholder="123" maxlength="4">
                            </div>
                        </div>
                    </div>

                    <div class="form-acciones">
                        <button type="submit" class="btn btn-primario">Pagar S/ <%= carrito.getTotal() %></button>
                        <a class="btn btn-sec" href="<%= cp %>/cliente/carrito">Volver al carrito</a>
                    </div>
                </form>
            </div>

            <!-- Resumen -->
            <div class="resumen">
                <h3>Resumen de la reserva</h3>
                <div class="fila"><span class="dato">Entrada</span><span><%= carrito.getFechaEntrada() %></span></div>
                <div class="fila"><span class="dato">Salida</span><span><%= carrito.getFechaSalida() %></span></div>
                <div class="fila"><span class="dato">Noches</span><span><%= carrito.getNoches() %></span></div>
                <div class="fila"><span class="dato">Huéspedes</span><span><%= carrito.getNumHuespedes() %></span></div>
                <% for (ItemCarrito it : carrito.getItems()) { %>
                <div class="fila"><span class="dato">Hab. <%= it.getNumero() %> (<%= it.getNoches() %> n.)</span><span>S/ <%= it.getSubtotal() %></span></div>
                <% } %>
                <div class="fila total"><span>Total</span><span>S/ <%= carrito.getTotal() %></span></div>
            </div>

        </div>

    </main>

    <script>
        function toggleTarjeta() {
            var medio = document.getElementById("medioPago").value;
            document.getElementById("camposTarjeta").style.display = (medio === "tarjeta") ? "block" : "none";
        }
        toggleTarjeta();
    </script>

</body>
</html>
