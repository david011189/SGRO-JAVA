// login.js — validación en el cliente · RF-42, CA-02, CA-33

(function () {
    "use strict";

    const form        = document.getElementById("formLogin");
    const inputCorreo = document.getElementById("correo");
    const inputClave  = document.getElementById("contrasena");
    const errCorreo   = document.getElementById("errCorreo");
    const errClave    = document.getElementById("errContrasena");
    const btnIngresar = document.getElementById("btnIngresar");
    const spinner     = document.getElementById("spinner");

    // Valida formato de correo electrónico
    function esCorreoValido(valor) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(valor.trim());
    }

    function mostrarError(input, span, mensaje) {
        input.classList.add("invalido");
        span.textContent = mensaje;
    }

    function limpiarError(input, span) {
        input.classList.remove("invalido");
        span.textContent = "";
    }

    // Validación en tiempo real del correo
    inputCorreo.addEventListener("input", function () {
        if (this.value.trim() === "") {
            mostrarError(this, errCorreo, "El correo es obligatorio.");
        } else if (!esCorreoValido(this.value)) {
            mostrarError(this, errCorreo, "Ingresa un correo electrónico válido.");
        } else {
            limpiarError(this, errCorreo);
        }
    });

    // Validación en tiempo real de la contraseña
    inputClave.addEventListener("input", function () {
        if (this.value.trim() === "") {
            mostrarError(this, errClave, "La contraseña es obligatoria.");
        } else if (this.value.length < 6) {
            mostrarError(this, errClave, "La contraseña debe tener al menos 6 caracteres.");
        } else {
            limpiarError(this, errClave);
        }
    });

    // Validación final antes de enviar el formulario (RF-42)
    form.addEventListener("submit", function (e) {
        let valido = true;

        if (inputCorreo.value.trim() === "") {
            mostrarError(inputCorreo, errCorreo, "El correo es obligatorio.");
            valido = false;
        } else if (!esCorreoValido(inputCorreo.value)) {
            mostrarError(inputCorreo, errCorreo, "Ingresa un correo electrónico válido.");
            valido = false;
        } else {
            limpiarError(inputCorreo, errCorreo);
        }

        if (inputClave.value.trim() === "") {
            mostrarError(inputClave, errClave, "La contraseña es obligatoria.");
            valido = false;
        } else if (inputClave.value.length < 6) {
            mostrarError(inputClave, errClave, "La contraseña debe tener al menos 6 caracteres.");
            valido = false;
        } else {
            limpiarError(inputClave, errClave);
        }

        if (!valido) {
            e.preventDefault();
            return;
        }

        // Mostrar spinner mientras se procesa
        btnIngresar.disabled = true;
        spinner.hidden = false;
        document.querySelector(".btn-texto").textContent = "Verificando...";
    });
})();

// Alternar visibilidad de la contraseña
function toggleContrasena() {
    const input  = document.getElementById("contrasena");
    const btn    = document.getElementById("btnOjo");
    const visible = input.type === "text";
    input.type   = visible ? "password" : "text";
    btn.textContent = visible ? "👁" : "🙈"; // 👁 / 🙈
    btn.setAttribute("aria-label", visible ? "Mostrar contraseña" : "Ocultar contraseña");
}
