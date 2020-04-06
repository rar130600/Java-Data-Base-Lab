package database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ProductDAO {
    private String TABLE_NAME = "products";
    private PGSimpleDataSource dataSource;

    public ProductDAO(String user, String password) {
        dataSource = new PGSimpleDataSource();
        String URL = "jdbc:postgresql://localhost:5432/postgres?loggerLevel=OFF";
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
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS  " + TABLE_NAME + "(" +
                    "id SERIAL NOT NULL, " +
                    "prodid VARCHAR(36) NOT NULL, " +
                    "title VARCHAR(40) NOT NULL, " +
                    "cost INTEGER NOT NULL, " +
                    "PRIMARY KEY (id), UNIQUE(prodid), UNIQUE(title))");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table.", e);
        }
    }

    public void clearTable() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("TRUNCATE TABLE " + TABLE_NAME);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table.", e);
        }
    }

    public void addToTable(Product product) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO " + TABLE_NAME +
                    " (id, prodid, title, cost) VALUES " + "(" +
                    product.getId() + ", '" +
                    product.getProdId() + "', '" +
                    product.getTitle() + "', " +
                    product.getCost() + ")");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to add to table.", e);
        }
    }
}
