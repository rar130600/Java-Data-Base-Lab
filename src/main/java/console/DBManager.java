package console;

import database.Product;
import database.ProductDAO;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class DBManager {
    private Scanner in;
    private PrintStream out;
    private ProductDAO dao;
    private final Map<String, Consumer<Scanner>> commands = Map.of(
            "/add", DBManager.this::add
    );

    public DBManager() {
        in = new Scanner(System.in);
        out = System.out;
    }

    public DBManager(InputStream in, PrintStream out) {
        this.in = new Scanner(in);
        this.out = out;
    }

    public void start() {
        while(!login()){}

        reset();
    }

    private boolean login() {
        out.println("Database login:");
        final String user = in.next();
        out.println("Database password:");
        final String password = in.next();

        try {
            dao = new ProductDAO(user, password);
            out.println("Successful connection to database.\n");
        } catch (RuntimeException e) {
            out.println(e.getMessage());
            out.println(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    public void reset() {
        out.println("Enter the number of products to generate:");
        int n;
        while (true) {
            n = in.nextInt();
            if (n < 0) {
                out.println("This number can't be negative.");
                continue;
            }
            break;
        }
        dao.clearTable();
        for (int i = 1; i < n + 1; i++) {
            dao.addToTable(new Product(i, "Товар" + i, i * 10));
        }
        out.println("Table is successfully filled!\n");
    }

    private void add(Scanner args){}
}
