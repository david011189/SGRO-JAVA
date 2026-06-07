// restablecer.js — validación de la nueva contraseña · RF-03

(function () {
    "use strict";

    const form = document.getElementById("formRestablecer");
    if (!form) return; // enlace inválido: no hay formulario

    const clave     = document.getElementById("contrasena");
    const confirmar = document.getElementById("confirmar");
    const errClave  = document.getElementById("errContrasena");
    const errConf   = document.getElementById("errConfirmar");
    const btn       = document.getElementById("btnGuardar");
    const spinner   = document.getElementById("spinner");

    function revisarClave() {
        if (clave.value === "") {
            marcar(clave, errClave, "La contraseña es obligatoria.");
            return false;
        }
        if (clave.value.length < 6) {
            marcar(clave, errClave, "Debe tener al menos 6 caracteres.");
            return false;
        }
        limpiar(clave, errClave);
        return true;
    }

    function revisarConfirmar() {
        if (confirmar.value === "") {
            marcar(confirmar, errConf, "Confirma tu contraseña.");
            return false;
        }
        if (confirmar.value !== clave.value) {
            marcar(confirmar, errConf, "Las contraseñas no coinciden.");
            return false;
        }
        limpiar(confirmar, errConf);
        return true;
    }

    function marcar(input, span, msg) { input.classList.add("invalido"); span.textContent = msg; }
    function limpiar(input, span)     { input.classList.remove("invalido"); span.textContent = ""; }

    clave.addEventListener("input", revisarClave);
    confirmar.addEventListener("input", revisarConfirmar);

    form.addEventListener("submit", function (e) {
        const ok1 = revisarClave();
        const ok2 = revisarConfirmar();
        if (!ok1 || !ok2) {
            e.preventDefault();
            return;
        }
        btn.disabled = true;
        spinner.hidden = false;
        document.querySelector("#btnGuardar .btn-texto").textContent = "Guardando...";
    });
})();

// Reutiliza el patrón de mostrar/ocultar contraseña del registro
function toggleContrasena(idInput, idBoton) {
    const input = document.getElementById(idInput);
    const btn   = document.getElementById(idBoton);
    const visible = input.type === "text";
    input.type = visible ? "password" : "text";
    btn.textContent = visible ? "👁" : "🙈"; // 👁 / 🙈
    btn.setAttribute("aria-label", visible ? "Mostrar contraseña" : "Ocultar contraseña");
}
