// recuperar.js — validación del correo para solicitar el enlace · RF-03

(function () {
    "use strict";

    const form = document.getElementById("formRecuperar");
    if (!form) return; // si ya se generó el enlace, no hay formulario

    const correo = document.getElementById("correo");
    const err    = document.getElementById("errCorreo");
    const btn    = document.getElementById("btnEnviar");
    const spinner = document.getElementById("spinner");

    function valido(v) {
        return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.trim());
    }

    function revisar() {
        if (correo.value.trim() === "") {
            correo.classList.add("invalido");
            err.textContent = "El correo es obligatorio.";
            return false;
        }
        if (!valido(correo.value)) {
            correo.classList.add("invalido");
            err.textContent = "Ingresa un correo electrónico válido.";
            return false;
        }
        correo.classList.remove("invalido");
        err.textContent = "";
        return true;
    }

    correo.addEventListener("input", revisar);

    form.addEventListener("submit", function (e) {
        if (!revisar()) {
            e.preventDefault();
            return;
        }
        btn.disabled = true;
        spinner.hidden = false;
        document.querySelector("#btnEnviar .btn-texto").textContent = "Generando...";
    });
})();
