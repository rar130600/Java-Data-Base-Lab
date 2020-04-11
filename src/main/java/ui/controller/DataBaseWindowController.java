package ui.controller;

import database.Product;
import database.ProductDAO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.MainApp;

import java.io.IOException;

public class DataBaseWindowController {
    private MainApp mainApp;
    private ProductDAO dao;
    private ObservableList<Product> products = FXCollections.observableArrayList();

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> prodIdColumn;

    @FXML
    private TableColumn<Product, String> titleColumn;

    @FXML
    private TableColumn<Product, Integer> priceColumn;

    @FXML
    private TextField fieldNumber;

    @FXML
    private TextField fieldFrom;

    @FXML
    private TextField fieldTo;

    @FXML
    private Text textLogin;

    @FXML
    private Button btnGenerate;

    @FXML
    private Button btnUpdatePrice;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnFilter;

    @FXML
    private Button btnShowAll;

    @FXML
    private Button btnSignOut;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        prodIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProdId()));
        titleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        priceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCost()).asObject());

        tableView.setItems(products);
    }

    @FXML
    void onClickAdd() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(mainApp.getClass().getResource("/ui/view/NewProductWindow.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(fxmlLoader.load()));
            dialogStage.setTitle("New Product");
            dialogStage.setResizable(false);

            NewProductWindowController controller = fxmlLoader.getController();
            controller.setProduct(new Product());
            controller.setStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.isWasAdded()) {
                dao.addToTable(controller.getProduct());
                products.setAll(dao.list());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            createAlertError(e.getMessage());
        }
    }

    @FXML
    void onClickUpdatePrice() {
        try {
            Product product = tableView.getSelectionModel().getSelectedItem();
            if (product == null) {
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(mainApp.getClass().getResource("/ui/view/UpdatePriceWindow.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(fxmlLoader.load()));
            dialogStage.setTitle("Update Price");
            dialogStage.setResizable(false);

            UpdatePriceWindowController controller = fxmlLoader.getController();
            controller.setProduct(product);
            controller.setStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.isWasUpdated()) {
                dao.updatePrice(controller.getProduct().getTitle(), controller.getProduct().getCost());
                products.setAll(dao.list());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            createAlertError(e.getMessage());
        }
    }

    @FXML
    void onClickDelete() {
        Product product = tableView.getSelectionModel().getSelectedItem();
        if (product == null) {
            return;
        }
        dao.deleteFromTable(product.getTitle());
        products.setAll(dao.list());
    }

    @FXML
    void onClickFilter() {
        int from;
        try {
            from = extractInteger(fieldFrom);
        } catch (IllegalArgumentException e) {
            createAlertWarning(e.getMessage());
            fieldFrom.requestFocus();
            return;
        } catch (RuntimeException e) {
            fieldFrom.requestFocus();
            return;
        }

        int to;
        try {
            to = extractInteger(fieldTo);
        } catch (IllegalArgumentException e) {
            createAlertWarning(e.getMessage());
            fieldTo.requestFocus();
            return;
        } catch (RuntimeException e) {
            fieldTo.requestFocus();
            return;
        }

        if (from > to) {
            createAlertWarning("From must be lower then To.");
            fieldFrom.requestFocus();
            return;
        }

        try {
            products.setAll(dao.list(from, to));
            fieldFrom.clear();
            fieldTo.clear();
        } catch (RuntimeException e) {
            createAlertError(e.getMessage() + "\n" + e.getCause().getMessage());
        }
    }

    @FXML
    void onClickGenerate() {
        try {
            int amount = extractInteger(fieldNumber);
            dao.clearTable();
            for (int i = 1; i < amount + 1; i++) {
                dao.addToTable(new Product(i, "product" + i, i * 10));
            }
            products.setAll(dao.list());
            fieldNumber.clear();
        } catch (IllegalArgumentException e) {
            createAlertWarning(e.getMessage());
            fieldNumber.requestFocus();
        } catch (RuntimeException e) {
            fieldNumber.requestFocus();
        }
    }

    private int extractInteger(TextField field) {
        if (field.getText().isEmpty()) {
            throw new RuntimeException();
        }
        int result = Integer.parseInt(field.getText());
        if (result < 0) {
            field.requestFocus();
            throw new IllegalArgumentException("Number can't be negative.");
        }
        return result;
    }

    @FXML
    void onClickShowAll() {
        products.setAll(dao.list());
    }

    @FXML
    void onClickSignOut() {
        mainApp.showLoginWindow();
    }

    public void provideApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDao(ProductDAO dao) {
        this.dao = dao;
        textLogin.setText(dao.getUser());
    }

    private void createAlertError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Error");
        alert.setHeaderText("Something went wrong.");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void createAlertWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Warning");
        alert.setHeaderText("Maybe you made a mistake somewhere.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
