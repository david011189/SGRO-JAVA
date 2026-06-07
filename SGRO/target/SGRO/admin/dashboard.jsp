<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.math.BigDecimal" %>
<%
    request.setAttribute("activo", "dashboard");

    @SuppressWarnings("unchecked")
    Map<String, Object> stats = (Map<String, Object>) request.getAttribute("stats");
    int total = 0, disponibles = 0, ocupadas = 0, mantenimiento = 0;
    BigDecimal tarifaProm = BigDecimal.ZERO;
    if (stats != null) {
        total         = (Integer) stats.getOrDefault("total", 0);
        disponibles   = (Integer) stats.getOrDefault("disponibles", 0);
        ocupadas      = (Integer) stats.getOrDefault("ocupadas", 0);
        mantenimiento = (Integer) stats.getOrDefault("mantenimiento", 0);
        Object tp = stats.get("tarifaProm");
        if (tp instanceof BigDecimal) tarifaProm = (BigDecimal) tp;
    }
    Object cli = request.getAttribute("clientes");
    int clientes = cli instanceof Integer ? (Integer) cli : 0;
    Object res = request.getAttribute("reservas");
    int reservas = res instanceof Integer ? (Integer) res : 0;
    String errorDatos = (String) request.getAttribute("errorDatos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard — Administración SGRO Barlen</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina">Panel de control</h1>
        <p class="admin-subtitulo">Resumen general del hospedaje y accesos rápidos a la gestión.</p>

        <% if (errorDatos != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= errorDatos %></div>
        <% } %>

        <!-- KPIs -->
        <section class="kpi-grid">
            <div class="kpi">
                <div class="kpi-valor"><%= total %></div>
                <div class="kpi-label">Habitaciones en total</div>
            </div>
            <div class="kpi verde">
                <div class="kpi-valor"><%= disponibles %></div>
                <div class="kpi-label">Disponibles</div>
            </div>
            <div class="kpi azul">
                <div class="kpi-valor"><%= ocupadas %></div>
                <div class="kpi-label">Ocupadas</div>
            </div>
            <div class="kpi ambar">
                <div class="kpi-valor"><%= mantenimiento %></div>
                <div class="kpi-label">En mantenimiento</div>
            </div>
            <div class="kpi">
                <div class="kpi-valor">S/ <%= tarifaProm.setScale(2, java.math.RoundingMode.HALF_UP) %></div>
                <div class="kpi-label">Tarifa promedio</div>
            </div>
            <div class="kpi">
                <div class="kpi-valor"><%= clientes %></div>
                <div class="kpi-label">Clientes registrados</div>
            </div>
            <div class="kpi azul">
                <div class="kpi-valor"><%= reservas %></div>
                <div class="kpi-label">Reservas activas</div>
            </div>
        </section>

        <!-- Accesos a módulos -->
        <h2 class="admin-titulo-pagina" style="font-size:1.15rem;">Gestión</h2>
        <p class="admin-subtitulo">Módulos de administración del sistema.</p>

        <section class="modulos-grid">
            <a class="modulo" href="<%= request.getContextPath() %>/admin/habitaciones">
                <div class="modulo-icono">&#128716;</div>
                <h3>Habitaciones</h3>
                <p>Registra, edita o elimina habitaciones, y administra tarifas y disponibilidad.</p>
            </a>
            <a class="modulo" href="<%= request.getContextPath() %>/admin/reservas">
                <div class="modulo-icono">&#128197;</div>
                <h3>Reservas</h3>
                <p>Listado y cancelación de reservas registradas por los clientes.</p>
            </a>
            <div class="modulo deshabilitado">
                <div class="modulo-icono">&#128101;</div>
                <h3>Clientes</h3>
                <p>Historial de clientes y de sus reservas.</p>
                <span class="pronto">Próximamente</span>
            </div>
            <div class="modulo deshabilitado">
                <div class="modulo-icono">&#128203;</div>
                <h3>Walk-in</h3>
                <p>Registro de reservas presenciales en recepción.</p>
                <span class="pronto">Próximamente</span>
            </div>
        </section>

    </main>

</body>
</html>
