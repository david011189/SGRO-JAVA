<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Habitacion" %>
<%
    request.setAttribute("activo", "cotizacion");
    String cp = request.getContextPath();

    String vEntrada = request.getAttribute("fechaEntrada") != null ? (String) request.getAttribute("fechaEntrada") : "";
    String vSalida  = request.getAttribute("fechaSalida")  != null ? (String) request.getAttribute("fechaSalida")  : "";
    String vHuesp   = request.getAttribute("huespedes")    != null ? (String) request.getAttribute("huespedes")    : "1";
    String vTipo    = request.getAttribute("tipo")         != null ? (String) request.getAttribute("tipo")         : "";
    String error    = (String) request.getAttribute("error");
    boolean hecho   = request.getAttribute("busquedaRealizada") != null;
    String hoy      = LocalDate.now().toString();

    int noches = request.getAttribute("noches") != null ? (Integer) request.getAttribute("noches") : 0;
    @SuppressWarnings("unchecked")
    List<Habitacion> habitaciones = (List<Habitacion>) request.getAttribute("habitaciones");

    String[] tipos = {"Simple", "Doble", "Matrimonial", "Familiar"};
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cotizar estadía — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
    <link rel="stylesheet" href="<%= cp %>/css/cliente.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Cotiza tu estadía</h1>
        <p class="admin-subtitulo">Elige tus fechas y descubre las habitaciones disponibles.</p>

        <!-- Pasos -->
        <div class="pasos">
            <div class="paso activo"><span class="num">1</span> Cotizar</div>
            <div class="paso"><span class="num">2</span> Carrito</div>
            <div class="paso"><span class="num">3</span> Pago</div>
            <div class="paso"><span class="num">4</span> Confirmación</div>
        </div>

        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <!-- Formulario de búsqueda -->
        <form class="cotizar-form" action="<%= cp %>/cliente/cotizacion" method="POST">
            <div class="form-grupo">
                <label for="fechaEntrada">Entrada</label>
                <input type="date" id="fechaEntrada" name="fechaEntrada" value="<%= vEntrada %>" min="<%= hoy %>" required>
            </div>
            <div class="form-grupo">
                <label for="fechaSalida">Salida</label>
                <input type="date" id="fechaSalida" name="fechaSalida" value="<%= vSalida %>" min="<%= hoy %>" required>
            </div>
            <div class="form-grupo">
                <label for="huespedes">Huéspedes</label>
                <input type="number" id="huespedes" name="huespedes" value="<%= vHuesp %>" min="1" max="20" required>
            </div>
            <div class="form-grupo crece">
                <label for="tipo">Tipo (opcional)</label>
                <select id="tipo" name="tipo">
                    <option value="">Todos</option>
                    <% for (String t : tipos) { %>
                    <option value="<%= t %>" <%= t.equals(vTipo) ? "selected" : "" %>><%= t %></option>
                    <% } %>
                </select>
            </div>
            <button type="submit" class="btn btn-primario">Buscar disponibilidad</button>
        </form>

        <!-- Resultados -->
        <% if (hecho) { %>
            <% if (habitaciones == null || habitaciones.isEmpty()) { %>
                <div class="vacio">
                    <div class="icono">&#128683;</div>
                    <h3>Sin disponibilidad</h3>
                    <p>No hay habitaciones libres para las fechas y filtros seleccionados. Prueba con otras fechas.</p>
                </div>
            <% } else { %>
                <p class="admin-subtitulo">
                    <strong><%= habitaciones.size() %></strong> habitación(es) disponible(s) ·
                    <strong><%= noches %></strong> noche(s)
                </p>
                <div class="habs-grid">
                <% for (Habitacion h : habitaciones) {
                       BigDecimal totalHab = h.getTarifa().multiply(BigDecimal.valueOf(noches));
                %>
                    <div class="hab-card">
                        <div class="hab-cabecera">
                            <div class="hab-num">Hab. <%= h.getNumero() %></div>
                            <div class="hab-tipo"><%= h.getTipo() %></div>
                        </div>
                        <div class="hab-cuerpo">
                            <div class="hab-meta">
                                <span>&#128101; <%= h.getCapacidad() %> pers.</span>
                            </div>
                            <div class="hab-desc"><%= h.getDescripcion() != null ? h.getDescripcion() : "" %></div>
                            <div class="hab-precio">
                                <span class="tarifa">S/ <%= h.getTarifa() %></span>
                                <span class="por">/ noche</span>
                                <div class="hab-total">Total <%= noches %> noche(s): <strong>S/ <%= totalHab %></strong></div>
                            </div>
                        </div>
                        <div class="hab-pie">
                            <form action="<%= cp %>/cliente/carrito" method="POST">
                                <input type="hidden" name="accion" value="agregar">
                                <input type="hidden" name="idHabitacion" value="<%= h.getIdHabitacion() %>">
                                <input type="hidden" name="fechaEntrada" value="<%= vEntrada %>">
                                <input type="hidden" name="fechaSalida" value="<%= vSalida %>">
                                <input type="hidden" name="huespedes" value="<%= vHuesp %>">
                                <button type="submit" class="btn btn-primario">Agregar al carrito</button>
                            </form>
                        </div>
                    </div>
                <% } %>
                </div>
            <% } %>
        <% } %>

    </main>

</body>
</html>
