package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

    private void showLoginWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/ui/view/LoginWindow.fxml"));
            primaryStage.setScene(new Scene(fxmlLoader.load()));
            primaryStage.setTitle("Login to DataBase");
            primaryStage.setResizable(false);

            LoginWindowController controller = fxmlLoader.getController();
            controller.provideApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
