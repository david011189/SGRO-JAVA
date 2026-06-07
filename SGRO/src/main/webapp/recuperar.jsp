<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    String error  = (String) request.getAttribute("error");
    String enlace = (String) request.getAttribute("enlace");
    String vCorreo = request.getAttribute("correo") != null ? (String) request.getAttribute("correo") : "";
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar contraseña — Hospedaje Barlen</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/login.css">
</head>
<body>

    <div class="login-wrapper">

        <div class="login-panel-izquierdo" aria-hidden="true">
            <div class="overlay">
                <div class="marca">
                    <div class="marca-icono">&#127968;</div>
                    <h1 class="marca-nombre">Hospedaje Barlen</h1>
                    <p class="marca-ubicacion">Anco Huallo, Chincheros · Apurímac</p>
                </div>
                <blockquote class="marca-lema">
                    "Recupera el acceso a tu cuenta<br>de forma rápida y segura."
                </blockquote>
            </div>
        </div>

        <div class="login-panel-derecho">
            <div class="login-caja">

                <div class="login-encabezado">
                    <div class="login-logo-movil">&#127968;</div>
                    <h2 class="login-titulo">Recuperar contraseña</h2>
                    <p class="login-subtitulo">Te generaremos un enlace para restablecerla</p>
                </div>

                <% if (error != null && !error.isEmpty()) { %>
                <div class="alerta alerta-error" role="alert">
                    <span class="alerta-icono">&#9888;</span>
                    <%= error %>
                </div>
                <% } %>

                <% if (enlace != null) { %>
                    <!-- Enlace generado. En producción se enviaría por correo. -->
                    <div class="alerta alerta-exito" role="status">
                        <span class="alerta-icono">&#10004;</span>
                        Generamos tu enlace de restablecimiento (válido por 1 hora).
                    </div>
                    <p class="login-subtitulo" style="margin-bottom:12px;">
                        En un sistema con correo configurado, este enlace llegaría a tu bandeja.
                        Para esta versión, ábrelo directamente:
                    </p>
                    <a href="<%= enlace %>" class="btn-ingresar" style="text-decoration:none; margin-bottom:16px;">
                        <span class="btn-texto">Restablecer mi contraseña</span>
                    </a>
                    <div class="separador">
                        <span class="separador-linea"></span>
                        <span class="separador-texto">o copia el enlace</span>
                        <span class="separador-linea"></span>
                    </div>
                    <input class="campo-input" style="padding-left:12px; font-size:0.78rem;"
                           type="text" value="<%= enlace %>" readonly
                           onclick="this.select()">
                <% } else { %>
                    <!-- Formulario para solicitar el enlace -->
                    <form id="formRecuperar"
                          action="<%= request.getContextPath() %>/RecuperarServlet"
                          method="POST" novalidate>
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
                        <button type="submit" class="btn-ingresar" id="btnEnviar">
                            <span class="btn-texto">Generar enlace</span>
                            <span class="btn-spinner" id="spinner" hidden>&#9696;</span>
                        </button>
                    </form>
                <% } %>

                <div class="separador">
                    <span class="separador-linea"></span>
                    <span class="separador-texto">¿Ya la recordaste?</span>
                    <span class="separador-linea"></span>
                </div>

                <a href="<%= request.getContextPath() %>/login.jsp" class="btn-registrarse">
                    Volver a iniciar sesión
                </a>

                <p class="login-pie">SGRO &copy; 2026 · Hospedaje Barlen</p>

            </div>
        </div>

    </div>

    <script src="<%= request.getContextPath() %>/js/recuperar.js"></script>
</body>
</html>
