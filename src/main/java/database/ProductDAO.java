package database;

import org.postgresql.ds.PGSimpleDataSource;

import java.sql.*;
import java.util.ArrayList;

public class ProductDAO {
    private final String TABLE_NAME = "products";
    private final PGSimpleDataSource dataSource;

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
            statement.execute("TRUNCATE TABLE " + TABLE_NAME + " RESTART IDENTITY");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clear table.", e);
        }
    }

    public void addToTable(Product product) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE_NAME +
                    " (prodid, title, cost) VALUES (?, ?, ?)");
            preparedStatement.setString(1, product.getProdId());
            preparedStatement.setString(2, product.getTitle());
            preparedStatement.setInt(3, product.getCost());
            preparedStatement.execute();
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                throw new IllegalArgumentException("Product already exist.");
            } else {
                throw new RuntimeException("Failed to add to table.", e);
            }
        }
    }

    public void deleteFromTable(String productTitle) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM " + TABLE_NAME +
                    " WHERE title = ?");
            preparedStatement.setString(1, productTitle);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No such product.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete from table.", e);
        }
    }

    public void updatePrice(String productTitle, int newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price can't be negative.");
        }
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + TABLE_NAME +
                    " SET cost = ? WHERE title = ?");
            preparedStatement.setInt(1, newPrice);
            preparedStatement.setString(2, productTitle);
            if (preparedStatement.executeUpdate() == 0) {
                throw new IllegalArgumentException("No such product.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update table.", e);
        }
    }

    public ArrayList<Product> list() {
        ArrayList<Product> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME)) {
                while (resultSet.next()) {
                    result.add(extractProduct(resultSet));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get data from table.", e);
        }
    }

    public ArrayList<Product> list(int priceFrom, int priceTo) {
        ArrayList<Product> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME +
                    " WHERE cost BETWEEN ? AND ?");
            preparedStatement.setInt(1, priceFrom);
            preparedStatement.setInt(2, priceTo);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(extractProduct(resultSet));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get data from table.", e);
        }
    }

    public Product selectByTitle(String productTitle) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + TABLE_NAME +
                    " WHERE title = ?");
            preparedStatement.setString(1, productTitle);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractProduct(resultSet);
                }
                throw new IllegalArgumentException("No such product.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get data from table.", e);
        }
    }

    private Product extractProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("prodid"),
                resultSet.getString("title"),
                resultSet.getInt("cost")
        );
    }

    public String getUser() {
        return dataSource.getUser();
    }
}
