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
            "/add", this::add,
            "/delete", this::delete,
            "/show_all", this::showAll,
            "/price", this::showPrice,
            "/change_price", this::changePrice,
            "/filter_by_price", this::filterByPrice
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
        while (!login()) {}

        try {
            reset();
        } catch (Exception e) {
            out.println(e.getMessage());
            out.println("Failed to reset table! :с\n");
        }

        out.println("Please enter commands");
        out.println("Type \"/help\" for more information about commands");
        while (in.hasNextLine()) {
            String cmd = in.nextLine().trim();
            if (cmd.equals("/exit")) {
                break;
            }
            if (cmd.equals("/help")) {
                help();
                continue;
            }
            execute(cmd);
        }
    }

    public void execute(String line) {
        Scanner scanner = new Scanner(line);
        if (scanner.hasNext()) {
            try {
                commands.getOrDefault(scanner.next(),
                        (a) -> out.println("Unknown command.")).accept(scanner);
            } catch (Exception e) {
                out.println("Error executing command: " + e.getMessage());
            }
        }
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
        } catch (Exception e) {
            out.println(e.getMessage());
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

    private void help() {
        out.println("/add product_name price - add to table");
        out.println("/delete product_name - delete product from table");
        out.println("/show_all - show all product in table");
        out.println("/price product_name - show price product");
        out.println("/change_price product_name price - change price product");
        out.println("/filter_by_price price_from price_to - show product in price range");
        out.println("/exit - for exit");
    }

    private void add(Scanner args) {
        String productTitle = getParseTitle(args);
        int productPrice = getParsePrice(args);

        dao.addToTable(new Product(0, productTitle, productPrice));
        out.println("Product successfully added!");
    }

    private void delete(Scanner args) {
        String productTitle = getParseTitle(args);

        dao.deleteFromTable(productTitle);
        out.println("Product successfully deleted!");
    }

    private void showAll(Scanner args) {
        dao.list().forEach(out::println);
    }

    private void showPrice(Scanner args) {
        String productTitle = getParseTitle(args);
        out.println(dao.selectByTitle(productTitle).getCost());
    }

    private void changePrice(Scanner args) {
        String productTitle = getParseTitle(args);
        int productPrice = getParsePrice(args);

        dao.updatePrice(productTitle, productPrice);
        out.println("Product successfully update!");
    }

    private void filterByPrice(Scanner args) {
        int priceFrom = getParsePrice(args);
        int priceTo = getParsePrice(args);

        dao.list(priceFrom, priceTo).forEach(out::println);
    }

    private String getParseTitle(Scanner line) {
        if (!line.hasNext()) {
            throw new IllegalArgumentException("Incorrect product name.");
        }
        return line.next();
    }

    private int getParsePrice(Scanner line) {
        if (!line.hasNextInt()) {
            throw new IllegalArgumentException("Incorrect product price.");
        }
        return line.nextInt();
    }
}
