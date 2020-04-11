package ui.controller;

import database.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewProductWindowController {
    private Product product;
    private Stage stage;

    @FXML
    private TextField fieldTitle;

    @FXML
    private TextField fieldPrice;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAdd;

    @FXML
    void onClickAdd() {
        if (fieldTitle.getText().isEmpty()) {
            fieldTitle.requestFocus();
            return;
        }
        if (fieldPrice.getText().isEmpty()) {
            fieldPrice.requestFocus();
            return;
        }
        int price;
        try {
            price = Integer.parseInt(fieldPrice.getText());
            if (price < 0) {
                throw new IllegalArgumentException("Number can't be negative.");
            }
            product.setTitle(fieldTitle.getText());
            product.setCost(price);
            stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field");
            alert.setHeaderText("Please correct the invalid field.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            fieldPrice.requestFocus();
        }
    }

    @FXML
    void onClickCancel() {
        product = null;
        stage.close();
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
