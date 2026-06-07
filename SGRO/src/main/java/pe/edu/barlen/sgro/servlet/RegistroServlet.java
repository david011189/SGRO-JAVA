package pe.edu.barlen.sgro.servlet;

import pe.edu.barlen.sgro.dao.UsuarioDAO;
import pe.edu.barlen.sgro.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

// RF-01: Registro de clientes nuevos
@WebServlet("/RegistroServlet")
public class RegistroServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nombre    = trim(request.getParameter("nombre"));
        String apellido  = trim(request.getParameter("apellido"));
        String correo    = trim(request.getParameter("correo"));
        String telefono  = trim(request.getParameter("telefono"));
        String clave     = request.getParameter("contrasena");
        String confirmar = request.getParameter("confirmar");

        // Conservar lo escrito para repintar el formulario si hay error
        request.setAttribute("nombre",   nombre);
        request.setAttribute("apellido", apellido);
        request.setAttribute("correo",   correo);
        request.setAttribute("telefono", telefono);

        // ---- Validación de campos obligatorios (RF-42) ----
        if (vacio(nombre) || vacio(apellido) || vacio(correo) || vacio(clave) || vacio(confirmar)) {
            error(request, response, "Por favor, completa todos los campos obligatorios.");
            return;
        }
        if (nombre.length() > 80 || apellido.length() > 80) {
            error(request, response, "El nombre y el apellido no pueden superar los 80 caracteres.");
            return;
        }
        if (correo.length() > 100 || !PATRON_CORREO.matcher(correo).matches()) {
            error(request, response, "Ingresa un correo electrónico válido.");
            return;
        }
        if (telefono != null && !telefono.isBlank()) {
            if (telefono.length() > 20 || !telefono.matches("[0-9+()\\-\\s]{6,20}")) {
                error(request, response, "El teléfono ingresado no es válido.");
                return;
            }
        }
        if (clave.length() < 6 || clave.length() > 100) {
            error(request, response, "La contraseña debe tener entre 6 y 100 caracteres.");
            return;
        }
        if (!clave.equals(confirmar)) {
            error(request, response, "Las contraseñas no coinciden.");
            return;
        }

        // ---- Persistencia ----
        try {
            UsuarioDAO dao = new UsuarioDAO();

            // CA-01: correo ya registrado
            if (dao.existeCorreo(correo)) {
                error(request, response, "Ya existe una cuenta registrada con ese correo electrónico.");
                return;
            }

            Usuario nuevo = new Usuario();
            nuevo.setNombre(nombre);
            nuevo.setApellido(apellido);
            nuevo.setCorreo(correo);
            nuevo.setTelefono(telefono);
            nuevo.setRol("cliente");

            boolean creado = dao.registrar(nuevo, clave);

            if (creado) {
                // RF-01 escenario exitoso → al login con aviso de éxito
                response.sendRedirect(request.getContextPath() + "/login.jsp?registrado=1");
            } else {
                error(request, response, "No se pudo crear la cuenta. Intenta de nuevo.");
            }

        } catch (SQLException e) {
            // Captura también la violación de UNIQUE por carrera entre dos registros
            String msg = e.getMessage() != null && e.getMessage().toLowerCase().contains("duplicate")
                    ? "Ya existe una cuenta registrada con ese correo electrónico."
                    : "Error interno del sistema. Por favor, intenta más tarde.";
            error(request, response, msg);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Acceso directo por GET → mostrar el formulario
        request.getRequestDispatcher("/registro.jsp").forward(request, response);
    }

    // ---- Utilidades ----
    private void error(HttpServletRequest req, HttpServletResponse resp, String mensaje)
            throws ServletException, IOException {
        req.setAttribute("error", mensaje);
        req.getRequestDispatcher("/registro.jsp").forward(req, resp);
    }

    private static String trim(String s) { return s == null ? null : s.trim(); }
    private static boolean vacio(String s) { return s == null || s.isBlank(); }
}
