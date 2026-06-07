<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="pe.edu.barlen.sgro.dao.RecuperacionDAO" %>
<%
    if (session.getAttribute("usuarioLogueado") != null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    // El token puede venir como atributo (reenvío del servlet) o como parámetro (enlace)
    String token = request.getAttribute("token") != null
            ? (String) request.getAttribute("token")
            : request.getParameter("token");

    String error = (String) request.getAttribute("error");

    // ¿Token inválido? Lo marca el servlet, o lo comprobamos aquí al abrir el enlace.
    boolean invalido = Boolean.TRUE.equals(request.getAttribute("tokenInvalido"));
    if (!invalido) {
        if (token == null || token.isBlank()) {
            invalido = true;
        } else {
            try {
                invalido = new RecuperacionDAO().validarToken(token) == 0;
            } catch (Exception e) {
                invalido = true;
            }
        }
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nueva contraseña — Hospedaje Barlen</title>
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
                    "Define una nueva contraseña<br>y vuelve a tu cuenta con seguridad."
                </blockquote>
            </div>
        </div>

        <div class="login-panel-derecho">
            <div class="login-caja">

                <div class="login-encabezado">
                    <div class="login-logo-movil">&#127968;</div>
                    <h2 class="login-titulo">Nueva contraseña</h2>
                    <p class="login-subtitulo">Elige una contraseña segura para tu cuenta</p>
                </div>

                <% if (invalido) { %>
                    <div class="alerta alerta-error" role="alert">
                        <span class="alerta-icono">&#9888;</span>
                        El enlace no es válido o ya expiró. Solicita uno nuevo.
                    </div>
                    <a href="<%= request.getContextPath() %>/recuperar.jsp" class="btn-ingresar" style="text-decoration:none;">
                        <span class="btn-texto">Solicitar un nuevo enlace</span>
                    </a>
                <% } else { %>

                    <% if (error != null && !error.isEmpty()) { %>
                    <div class="alerta alerta-error" role="alert">
                        <span class="alerta-icono">&#9888;</span>
                        <%= error %>
                    </div>
                    <% } %>

                    <form id="formRestablecer"
                          action="<%= request.getContextPath() %>/RestablecerServlet"
                          method="POST" novalidate>

                        <input type="hidden" name="token" value="<%= token %>">

                        <div class="campo-grupo">
                            <label for="contrasena" class="campo-etiqueta">Nueva contraseña</label>
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

                        <div class="campo-grupo">
                            <label for="confirmar" class="campo-etiqueta">Confirmar contraseña</label>
                            <div class="campo-contenedor">
                                <span class="campo-icono">&#128274;</span>
                                <input type="password" id="confirmar" name="confirmar" class="campo-input"
                                       placeholder="Repite la contraseña" autocomplete="new-password"
                                       required maxlength="100">
                                <button type="button" class="btn-ojo" id="btnOjo2"
                                        aria-label="Mostrar u ocultar contraseña"
                                        onclick="toggleContrasena('confirmar','btnOjo2')">&#128065;</button>
                            </div>
                            <span class="campo-error" id="errConfirmar"></span>
                        </div>

                        <button type="submit" class="btn-ingresar" id="btnGuardar">
                            <span class="btn-texto">Guardar contraseña</span>
                            <span class="btn-spinner" id="spinner" hidden>&#9696;</span>
                        </button>
                    </form>
                <% } %>

                <p class="login-pie" style="margin-top:20px;">SGRO &copy; 2026 · Hospedaje Barlen</p>

            </div>
        </div>

    </div>

    <script src="<%= request.getContextPath() %>/js/restablecer.js"></script>
</body>
</html>
