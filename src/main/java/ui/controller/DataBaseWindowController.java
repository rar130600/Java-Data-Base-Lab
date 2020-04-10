package ui.controller;

import database.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import ui.MainApp;

public class DataBaseWindowController {
    private MainApp mainApp;
    private ProductDAO dao;

    @FXML
    private TableView<?> tableView;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> prodIdColumn;

    @FXML
    private TableColumn<?, ?> titleColumn;

    @FXML
    private TableColumn<?, ?> priceColumn;

    @FXML
    private TextField fieldNumber;

    @FXML
    private TextField fieldFrom;

    @FXML
    private TextField fieldTo;

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
    void onClickAdd() {

    }

    @FXML
    void onClickDelete() {

    }

    @FXML
    void onClickFilter() {

    }

    @FXML
    void onClickGenerate() {

    }

    @FXML
    void onClickShowAll() {

    }

    @FXML
    void onClickUpdatePrice() {

    }

    public void provideApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setDao(ProductDAO dao) {
        this.dao = dao;
    }
}
