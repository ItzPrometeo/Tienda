package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/tosta_cafe?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver JDBC cargado.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el Driver JDBC.");
            e.printStackTrace();
        }
    }

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    public static void cerrar(AutoCloseable ac) {
    try {
        if (ac != null) ac.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
