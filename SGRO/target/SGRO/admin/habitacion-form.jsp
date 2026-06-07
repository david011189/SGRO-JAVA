<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Habitacion" %>
<%
    request.setAttribute("activo", "habitaciones");
    String cp = request.getContextPath();

    Habitacion h = (Habitacion) request.getAttribute("habitacion");
    if (h == null) h = new Habitacion();
    boolean editar = "editar".equals(request.getAttribute("modo"));
    String error = (String) request.getAttribute("error");

    String vNumero = h.getNumero()      != null ? h.getNumero()      : "";
    String vTipo   = h.getTipo()        != null ? h.getTipo()        : "";
    String vEstado = h.getEstado()      != null ? h.getEstado()      : "disponible";
    String vDesc   = h.getDescripcion() != null ? h.getDescripcion() : "";
    String vCap    = h.getCapacidad()   > 0 ? String.valueOf(h.getCapacidad()) : "";
    String vTarifa = h.getTarifa()      != null ? h.getTarifa().toPlainString() : "";

    String[] tipos = {"Simple", "Doble", "Matrimonial", "Familiar"};
    String[][] estados = {{"disponible","Disponible"}, {"ocupada","Ocupada"}, {"mantenimiento","En mantenimiento"}};
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= editar ? "Editar" : "Nueva" %> habitación — SGRO Barlen</title>
    <link rel="stylesheet" href="<%= cp %>/css/admin.css">
</head>
<body>

    <%@ include file="_header.jspf" %>

    <main class="admin-main">

        <h1 class="admin-titulo-pagina"><%= editar ? "Editar habitación" : "Nueva habitación" %></h1>
        <p class="admin-subtitulo">
            <%= editar ? "Modifica los datos de la habitación seleccionada." : "Completa los datos para registrar una nueva habitación." %>
        </p>

        <% if (error != null) { %>
        <div class="alerta error"><span>&#9888;</span><%= error %></div>
        <% } %>

        <div class="form-panel">
            <form action="<%= cp %>/admin/habitaciones" method="POST" novalidate>
                <input type="hidden" name="accion" value="guardar">
                <input type="hidden" name="id" value="<%= editar ? h.getIdHabitacion() : 0 %>">

                <div class="form-fila">
                    <div class="form-grupo">
                        <label for="numero">Número de habitación</label>
                        <input type="text" id="numero" name="numero" value="<%= vNumero %>"
                               placeholder="Ej. 101" maxlength="10" required>
                    </div>
                    <div class="form-grupo">
                        <label for="tipo">Tipo</label>
                        <select id="tipo" name="tipo" required>
                            <option value="">— Selecciona —</option>
                            <% for (String t : tipos) { %>
                            <option value="<%= t %>" <%= t.equals(vTipo) ? "selected" : "" %>><%= t %></option>
                            <% } %>
                        </select>
                    </div>
                </div>

                <div class="form-fila">
                    <div class="form-grupo">
                        <label for="capacidad">Capacidad (personas)</label>
                        <input type="number" id="capacidad" name="capacidad" value="<%= vCap %>"
                               min="1" max="20" placeholder="Ej. 2" required>
                    </div>
                    <div class="form-grupo">
                        <label for="tarifa">Tarifa por noche</label>
                        <div class="input-monto">
                            <span>S/</span>
                            <input type="number" id="tarifa" name="tarifa" value="<%= vTarifa %>"
                                   min="0" step="0.01" placeholder="20.00" required>
                        </div>
                    </div>
                </div>

                <div class="form-grupo">
                    <label for="estado">Estado / disponibilidad</label>
                    <select id="estado" name="estado" required>
                        <% for (String[] e : estados) { %>
                        <option value="<%= e[0] %>" <%= e[0].equals(vEstado) ? "selected" : "" %>><%= e[1] %></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-grupo">
                    <label for="descripcion">Descripción</label>
                    <textarea id="descripcion" name="descripcion" maxlength="255"
                              placeholder="Características, camas, servicios..."><%= vDesc %></textarea>
                    <p class="ayuda">Opcional. Máximo 255 caracteres.</p>
                </div>

                <div class="form-acciones">
                    <button type="submit" class="btn btn-primario">
                        <%= editar ? "Guardar cambios" : "Registrar habitación" %>
                    </button>
                    <a class="btn btn-sec" href="<%= cp %>/admin/habitaciones">Cancelar</a>
                </div>
            </form>
        </div>

    </main>

</body>
</html>
