package console;

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
    }

    private boolean login() {
        out.println("Database login:");
        final String user = in.next();
        out.println("Database password:");
        final String password = in.next();

        try {
            dao = new ProductDAO(user, password);
            out.println("Successful connection to database.");
        } catch (RuntimeException e) {
            out.println(e.getMessage());
            out.println(e.getCause().getMessage());
            return false;
        }
        return true;
    }

    private void add(Scanner args){}
}
