package pe.edu.barlen.sgro.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL      = "jdbc:mysql://localhost:3306/sgro_barlen?useSSL=false&serverTimezone=America/Lima";
    private static final String USUARIO  = "root";
    private static final String CLAVE    = "12345";

    public static Connection obtener() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage());
        }
    }
}
