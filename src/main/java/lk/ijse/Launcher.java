package lk.ijse;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Launcher extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws IOException {
        stage.setScene(new Scene(FXMLLoader
                .load(this.getClass().getResource("/view/login_form.fxml"))));
        stage.setTitle("Login Page");
        stage.centerOnScreen();
        stage.show();

    }
}
