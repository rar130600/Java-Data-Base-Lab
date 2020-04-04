import console.DBManager;


public class Main {

    public static void main(String[] args) {
        /*String login = "";
        String pass = "";
        String url = "jdbc:postgresql://localhost:5432/postgres";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found.");
            e.printStackTrace();
            return;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try (Connection connection = DriverManager.getConnection(url, login, pass)){
            System.out.println("Successfully connected to Database");
        } catch (SQLException e) {
            System.err.println("Connection Failed");
            e.printStackTrace();
            return;
        }*/
        new DBManager(System.in, System.out).start();
    }
}
