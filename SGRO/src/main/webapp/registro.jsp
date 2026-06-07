<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    // Si ya hay sesión activa, no tiene sentido registrarse
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String error = (String) request.getAttribute("error");

    // Valores escritos (para repintar tras un error) — vacíos en la carga inicial
    String vNombre   = request.getAttribute("nombre")   != null ? (String) request.getAttribute("nombre")   : "";
    String vApellido = request.getAttribute("apellido") != null ? (String) request.getAttribute("apellido") : "";
    String vCorreo   = request.getAttribute("correo")   != null ? (String) request.getAttribute("correo")   : "";
    String vTelefono = request.getAttribute("telefono") != null ? (String) request.getAttribute("telefono") : "";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Crear cuenta — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>

    <div class="login-wrapper">

        <!-- Panel izquierdo: marca -->
        <div class="login-panel-izquierdo" aria-hidden="true">
            <div class="overlay">
                <div class="marca">
                    <div class="marca-icono">&#127968;</div>
                    <h1 class="marca-nombre">Hospedaje Barlen</h1>
                    <p class="marca-ubicacion">Anco Huallo, Chincheros · Apurímac</p>
                </div>
                <blockquote class="marca-lema">
                    "Crea tu cuenta y reserva tu próxima estadía<br>en pocos clics, desde donde estés."
                </blockquote>
            </div>
        </div>

        <!-- Panel derecho: formulario de registro -->
        <div class="login-panel-derecho">
            <div class="login-caja">

                <div class="login-encabezado">
                    <div class="login-logo-movil">&#127968;</div>
                    <h2 class="login-titulo">Crear cuenta</h2>
                    <p class="login-subtitulo">Regístrate para reservar en el SGRO Barlen</p>
                </div>

                <% if (error != null && !error.isEmpty()) { %>
                <div class="alerta alerta-error" role="alert">
                    <span class="alerta-icono">&#9888;</span>
                    <%= error %>
                </div>
                <% } %>

                <form id="formRegistro"
                      action="<%= request.getContextPath() %>/RegistroServlet"
                      method="POST"
                      novalidate>

                    <!-- Nombre y apellido (dos columnas) -->
                    <div class="campo-fila">
                        <div class="campo-grupo">
                            <label for="nombre" class="campo-etiqueta">Nombre</label>
                            <div class="campo-contenedor">
                                <span class="campo-icono">&#128100;</span>
                                <input type="text" id="nombre" name="nombre" class="campo-input"
                                       placeholder="Tu nombre" autocomplete="given-name"
                                       value="<%= vNombre %>" required maxlength="80">
                            </div>
                            <span class="campo-error" id="errNombre"></span>
                        </div>

                        <div class="campo-grupo">
                            <label for="apellido" class="campo-etiqueta">Apellido</label>
                            <div class="campo-contenedor">
                                <span class="campo-icono">&#128100;</span>
                                <input type="text" id="apellido" name="apellido" class="campo-input"
                                       placeholder="Tu apellido" autocomplete="family-name"
                                       value="<%= vApellido %>" required maxlength="80">
                            </div>
                            <span class="campo-error" id="errApellido"></span>
                        </div>
                    </div>

                    <!-- Correo -->
                    <div class="campo-grupo">
                        <label for="correo" class="campo-etiqueta">Correo electrónico</label>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#9993;</span>
                            <input type="email" id="correo" name="correo" class="campo-input"
                                   placeholder="ejemplo@correo.com" autocomplete="email"
                                   value="<%= vCorreo %>" required maxlength="100">
                        </div>
                        <span class="campo-error" id="errCorreo"></span>
                    </div>

                    <!-- Teléfono (opcional) -->
                    <div class="campo-grupo">
                        <label for="telefono" class="campo-etiqueta">Teléfono <span class="campo-opcional">(opcional)</span></label>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#128222;</span>
                            <input type="tel" id="telefono" name="telefono" class="campo-input"
                                   placeholder="987654321" autocomplete="tel"
                                   value="<%= vTelefono %>" maxlength="20">
                        </div>
                        <span class="campo-error" id="errTelefono"></span>
                    </div>

                    <!-- Contraseña -->
                    <div class="campo-grupo">
                        <label for="contrasena" class="campo-etiqueta">Contraseña</label>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#128274;</span>
                            <input type="password" id="contrasena" name="contrasena" class="campo-input"
                                   placeholder="Mínimo 6 caracteres" autocomplete="new-password"
                                   required maxlength="100">
                            <button type="button" class="btn-ojo" id="btnOjo"
                                    aria-label="Mostrar u ocultar contraseña"
                                    onclick="toggleContrasena('contrasena','btnOjo')">&#128065;</button>
                        </div>
                        <span class="campo-error" id="errContrasena"></span>
                    </div>

                    <!-- Confirmar contraseña -->
                    <div class="campo-grupo">
                        <label for="confirmar" class="campo-etiqueta">Confirmar contraseña</label>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#128274;</span>
                            <input type="password" id="confirmar" name="confirmar" class="campo-input"
                                   placeholder="Repite tu contraseña" autocomplete="new-password"
                                   required maxlength="100">
                            <button type="button" class="btn-ojo" id="btnOjo2"
                                    aria-label="Mostrar u ocultar contraseña"
                                    onclick="toggleContrasena('confirmar','btnOjo2')">&#128065;</button>
                        </div>
                        <span class="campo-error" id="errConfirmar"></span>
                    </div>

                    <button type="submit" class="btn-ingresar" id="btnRegistrar">
                        <span class="btn-texto">Crear cuenta</span>
                        <span class="btn-spinner" id="spinner" hidden>&#9696;</span>
                    </button>

                </form>

                <div class="separador">
                    <span class="separador-linea"></span>
                    <span class="separador-texto">¿Ya tienes cuenta?</span>
                    <span class="separador-linea"></span>
                </div>

                <a href="<%= request.getContextPath() %>/login.jsp" class="btn-registrarse">
                    Iniciar sesión
                </a>

                <p class="login-pie">SGRO &copy; 2026 · Hospedaje Barlen</p>

            </div>
        </div>

    </div>

    <script src="<%= request.getContextPath() %>/js/registro.js"></script>
</body>
</html>
