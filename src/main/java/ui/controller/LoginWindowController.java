package ui.controller;

import database.ProductDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ui.MainApp;

public class LoginWindowController {
    private MainApp mainApp;


    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void onClickButton() {
        try {
            if (loginField.getText().isEmpty()) {
                loginField.requestFocus();
                return;
            } else if (passwordField.getText().isEmpty()) {
                passwordField.requestFocus();
                return;
            }

            ProductDAO dao = new ProductDAO(loginField.getText(), passwordField.getText());
            dao.clearTable(); // new session every time
            mainApp.showDataBaseWindow(dao);
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(mainApp.getPrimaryStage());
            alert.setTitle("Error");
            alert.setHeaderText("Failed to connect to DataBase.");
            alert.setContentText(e.getCause().getMessage());
            alert.showAndWait();

            passwordField.clear();
            if (loginField.getText().isEmpty()) {
                loginField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            onClickButton();
        }
    }

    public void provideApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
