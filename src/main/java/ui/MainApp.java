package ui;

import database.ProductDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.controller.DataBaseWindowController;
import ui.controller.LoginWindowController;

import java.io.IOException;

public class MainApp extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        showLoginWindow();
        primaryStage.show();
    }

    public void showLoginWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ui/view/LoginWindow.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Login to DataBase");
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            LoginWindowController controller = fxmlLoader.getController();
            controller.provideApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDataBaseWindow(ProductDAO dao) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ui/view/DataBaseWindow.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("DataBase");
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            DataBaseWindowController controller = fxmlLoader.getController();
            controller.provideApp(this);
            controller.setDao(dao);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
