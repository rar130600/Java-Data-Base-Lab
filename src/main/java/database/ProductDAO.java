package database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ProductDAO {
    private String URL = "jdbc:postgresql://localhost:5432/postgres?loggerLevel=OFF";
    private String TABLE_NAME = "Products";
    private PGSimpleDataSource dataSource;

    public ProductDAO(String user, String password) {
        dataSource = new PGSimpleDataSource();
        dataSource.setUrl(URL);
        dataSource.setServerName("localhost");
        dataSource.setPortNumber(5432);
        dataSource.setDatabaseName("postgres");
        dataSource.setUser(user);
        dataSource.setPassword(password);

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver is not found.", e);
        }
        try (Connection connection = dataSource.getConnection()) {
        } catch (SQLException e) {
            throw new RuntimeException("Connection to Database Failed!", e);
        }

        createTable();
    }

    private void createTable() {

    }

    public void clearTable() {

    }
}
