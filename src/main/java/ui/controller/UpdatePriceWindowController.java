package ui.controller;

import database.Product;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UpdatePriceWindowController {
    private Product product;
    private Stage stage;
    private boolean wasUpdated = false;

    @FXML
    private Text textProductTitle;

    @FXML
    private TextField fieldNewPrice;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnUpdate;

    @FXML
    void onClickUpdate() {
        if (fieldNewPrice.getText().isEmpty()) {
            fieldNewPrice.requestFocus();
            return;
        }
        try {
            int newPrice = Integer.parseInt(fieldNewPrice.getText());
            if (newPrice < 0) {
                throw new IllegalArgumentException("Number can't be negative.");
            }
            product.setCost(newPrice);
            wasUpdated = true;
            stage.close();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(stage);
            alert.setTitle("Invalid field");
            alert.setHeaderText("Please correct the invalid field.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            fieldNewPrice.requestFocus();
        }
    }

    @FXML
    void onClickCancel() {
        wasUpdated = false;
        stage.close();
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onClickUpdate();
        } else if (event.getCode() == KeyCode.ESCAPE) {
            onClickCancel();
        }
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        textProductTitle.setText(product.getTitle());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public boolean isWasUpdated() {
        return wasUpdated;
    }
}
