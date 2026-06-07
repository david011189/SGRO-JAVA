<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.modelo.Usuario" %>
<%
    // RF-06: si ya hay sesión, redirigir
    if (session.getAttribute("usuarioLogueado") != null) {
        String rol = (String) session.getAttribute("rolUsuario");
        if ("administrador".equals(rol)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        } else {
            response.sendRedirect(request.getContextPath() + "/cliente/cotizacion");
        }
        return;
    }
    String error = (String) request.getAttribute("error");
    // RF-01: aviso de cuenta creada con éxito (llega desde RegistroServlet)
    boolean registrado = "1".equals(request.getParameter("registrado"));
    // RF-03: aviso de contraseña restablecida (llega desde RestablecerServlet)
    boolean reseteado  = "1".equals(request.getParameter("reset"));
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>

    <div class="login-wrapper">

        <!-- Panel izquierdo: imagen de fondo con información del hospedaje -->
        <div class="login-panel-izquierdo" aria-hidden="true">
            <div class="overlay">
                <div class="marca">
                    <div class="marca-icono">&#127968;</div>
                    <h1 class="marca-nombre">Hospedaje Barlen</h1>
                    <p class="marca-ubicacion">Anco Huallo, Chincheros · Apurímac</p>
                </div>
                <blockquote class="marca-lema">
                    "Tu estadía, nuestra prioridad.<br>Reserva cómodo, desde cualquier lugar."
                </blockquote>
            </div>
        </div>

        <!-- Panel derecho: formulario de inicio de sesión -->
        <div class="login-panel-derecho">
            <div class="login-caja">

                <!-- Encabezado -->
                <div class="login-encabezado">
                    <div class="login-logo-movil">&#127968;</div>
                    <h2 class="login-titulo">Iniciar sesión</h2>
                    <p class="login-subtitulo">Accede a tu cuenta del SGRO Barlen</p>
                </div>

                <!-- Mensaje de éxito tras registrarse (RF-01) -->
                <% if (registrado) { %>
                <div class="alerta alerta-exito" role="status">
                    <span class="alerta-icono">&#10004;</span>
                    ¡Cuenta creada con éxito! Ya puedes iniciar sesión.
                </div>
                <% } %>

                <!-- Mensaje de contraseña restablecida (RF-03) -->
                <% if (reseteado) { %>
                <div class="alerta alerta-exito" role="status">
                    <span class="alerta-icono">&#10004;</span>
                    Tu contraseña se actualizó. Inicia sesión con la nueva.
                </div>
                <% } %>

                <!-- Mensaje de error (CA-02 escenario de fallo) -->
                <% if (error != null && !error.isEmpty()) { %>
                <div class="alerta alerta-error" role="alert">
                    <span class="alerta-icono">&#9888;</span>
                    <%= error %>
                </div>
                <% } %>

                <!-- Formulario (RF-02) -->
                <form id="formLogin"
                      action="<%= request.getContextPath() %>/LoginServlet"
                      method="POST"
                      novalidate>

                    <!-- Campo correo -->
                    <div class="campo-grupo">
                        <label for="correo" class="campo-etiqueta">Correo electrónico</label>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#9993;</span>
                            <input
                                type="email"
                                id="correo"
                                name="correo"
                                class="campo-input"
                                placeholder="ejemplo@correo.com"
                                autocomplete="email"
                                required
                                maxlength="100"
                            >
                        </div>
                        <span class="campo-error" id="errCorreo"></span>
                    </div>

                    <!-- Campo contraseña -->
                    <div class="campo-grupo">
                        <div class="campo-etiqueta-fila">
                            <label for="contrasena" class="campo-etiqueta">Contraseña</label>
                            <a href="<%= request.getContextPath() %>/recuperar.jsp"
                               class="enlace-recuperar">¿Olvidaste tu contraseña?</a>
                        </div>
                        <div class="campo-contenedor">
                            <span class="campo-icono">&#128274;</span>
                            <input
                                type="password"
                                id="contrasena"
                                name="contrasena"
                                class="campo-input"
                                placeholder="Ingresa tu contraseña"
                                autocomplete="current-password"
                                required
                                maxlength="100"
                            >
                            <button type="button"
                                    class="btn-ojo"
                                    id="btnOjo"
                                    aria-label="Mostrar u ocultar contraseña"
                                    onclick="toggleContrasena()">
                                &#128065;
                            </button>
                        </div>
                        <span class="campo-error" id="errContrasena"></span>
                    </div>

                    <!-- Recordarme -->
                    <div class="recordarme-fila">
                        <label class="checkbox-label">
                            <input type="checkbox" name="recordar" id="recordar">
                            <span class="checkbox-texto">Recordarme</span>
                        </label>
                    </div>

                    <!-- Botón de envío -->
                    <button type="submit" class="btn-ingresar" id="btnIngresar">
                        <span class="btn-texto">Ingresar</span>
                        <span class="btn-spinner" id="spinner" hidden>&#9696;</span>
                    </button>

                </form>

                <!-- Separador -->
                <div class="separador">
                    <span class="separador-linea"></span>
                    <span class="separador-texto">¿No tienes cuenta?</span>
                    <span class="separador-linea"></span>
                </div>

                <!-- Enlace de registro -->
                <a href="<%= request.getContextPath() %>/registro.jsp" class="btn-registrarse">
                    Crear cuenta nueva
                </a>

                <!-- Pie -->
                <p class="login-pie">
                    SGRO &copy; 2026 · Hospedaje Barlen
                </p>

            </div>
        </div>

    </div>

    <script src="<%= request.getContextPath() %>/js/login.js"></script>
</body>
</html>
