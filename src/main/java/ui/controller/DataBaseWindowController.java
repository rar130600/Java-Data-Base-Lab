package ui.controller;

import database.Product;
import database.ProductDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        idColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("ID"));
        prodIdColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("ProdID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("Title"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("Price"));

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

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickDelete() {

    }

    @FXML
    void onClickFilter() {

    }

    @FXML
    void onClickGenerate() {
        try {
            int amount = Integer.parseInt(fieldNumber.getText());
            if (amount < 0) {
                createAlert("Amount can't be negative.");
                fieldNumber.requestFocus();
                return;
            }
            dao.clearTable();
            for (int i = 1; i < amount + 1; i++) {
                dao.addToTable(new Product(i, "product" + i, i * 10));
            }
            products.setAll(dao.list());
            fieldNumber.clear();
        } catch (NumberFormatException e) {
            createAlert(e.getMessage());
            fieldNumber.requestFocus();
        }
    }

    @FXML
    void onClickShowAll() {

    }

    @FXML
    void onClickUpdatePrice() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(mainApp.getClass().getResource("/ui/view/UpdatePriceWindow.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            dialogStage.setScene(new Scene(fxmlLoader.load()));
            dialogStage.setTitle("Update Price");
            dialogStage.setResizable(false);

            UpdatePriceWindowController controller = fxmlLoader.getController();

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private void createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("Invalid field");
        alert.setHeaderText("Please correct the invalid field.");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
