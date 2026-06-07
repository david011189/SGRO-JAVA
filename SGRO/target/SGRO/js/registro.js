// registro.js — validación en el cliente del registro de clientes · RF-01, RF-42

(function () {
    "use strict";

    const form = document.getElementById("formRegistro");

    const campos = {
        nombre:     { input: g("nombre"),     err: g("errNombre"),     val: requerido("El nombre es obligatorio.") },
        apellido:   { input: g("apellido"),   err: g("errApellido"),   val: requerido("El apellido es obligatorio.") },
        correo:     { input: g("correo"),     err: g("errCorreo"),     val: validarCorreo },
        telefono:   { input: g("telefono"),   err: g("errTelefono"),   val: validarTelefono },
        contrasena: { input: g("contrasena"), err: g("errContrasena"), val: validarClave },
        confirmar:  { input: g("confirmar"),  err: g("errConfirmar"),  val: validarConfirmar }
    };

    const btn     = document.getElementById("btnRegistrar");
    const spinner = document.getElementById("spinner");

    function g(id) { return document.getElementById(id); }

    function requerido(msg) {
        return (v) => v.trim() === "" ? msg : "";
    }

    function validarCorreo(v) {
        if (v.trim() === "") return "El correo es obligatorio.";
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v.trim())) return "Ingresa un correo electrónico válido.";
        return "";
    }

    function validarTelefono(v) {
        if (v.trim() === "") return ""; // opcional
        if (!/^[0-9+()\-\s]{6,20}$/.test(v.trim())) return "El teléfono ingresado no es válido.";
        return "";
    }

    function validarClave(v) {
        if (v === "") return "La contraseña es obligatoria.";
        if (v.length < 6) return "La contraseña debe tener al menos 6 caracteres.";
        return "";
    }

    function validarConfirmar(v) {
        if (v === "") return "Confirma tu contraseña.";
        if (v !== campos.contrasena.input.value) return "Las contraseñas no coinciden.";
        return "";
    }

    function pintar(campo) {
        const msg = campo.val(campo.input.value);
        if (msg) {
            campo.input.classList.add("invalido");
            campo.err.textContent = msg;
            return false;
        }
        campo.input.classList.remove("invalido");
        campo.err.textContent = "";
        return true;
    }

    // Validación en tiempo real
    Object.values(campos).forEach((campo) => {
        campo.input.addEventListener("input", () => pintar(campo));
    });
    // Revalidar confirmación cuando cambia la contraseña
    campos.contrasena.input.addEventListener("input", () => {
        if (campos.confirmar.input.value !== "") pintar(campos.confirmar);
    });

    // Validación final
    form.addEventListener("submit", function (e) {
        let valido = true;
        Object.values(campos).forEach((campo) => {
            if (!pintar(campo)) valido = false;
        });

        if (!valido) {
            e.preventDefault();
            return;
        }

        btn.disabled = true;
        spinner.hidden = false;
        document.querySelector("#btnRegistrar .btn-texto").textContent = "Creando cuenta...";
    });
})();

// Alternar visibilidad de un campo de contraseña
function toggleContrasena(idInput, idBoton) {
    const input = document.getElementById(idInput);
    const btn   = document.getElementById(idBoton);
    const visible = input.type === "text";
    input.type = visible ? "password" : "text";
    btn.textContent = visible ? "👁" : "🙈"; // 👁 / 🙈
    btn.setAttribute("aria-label", visible ? "Mostrar contraseña" : "Ocultar contraseña");
}
