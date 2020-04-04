package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ProductDAO {
    private String URL = "jdbc:postgresql://localhost:5432/postgres?loggerLevel=OFF";
    private String TABLE_NAME = "Products";

    public ProductDAO(String user, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver is not found.", e);
        }
        try (Connection connection = DriverManager.getConnection(URL, user, password)) {
        } catch (SQLException e) {
            throw new RuntimeException("Connection to Database Failed!", e);
        }
    }

}
