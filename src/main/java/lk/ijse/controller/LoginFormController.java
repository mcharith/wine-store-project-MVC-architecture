package lk.ijse.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    public TextField txtUserId;
    public AnchorPane rootNode;
    public PasswordField txtPassword;
    public AnchorPane pane;


    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        String userId = txtUserId.getText();
        String Pw = txtPassword.getText();
        if (isValid()) {
            try {
                checkCredintial(userId, Pw);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
    private void checkCredintial(String userId,String Pw) throws SQLException, IOException {
        String sql ="SELECT User_id, Password FROM User WHERE User_id = ?";
        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,userId);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            String dbPw = resultSet.getString("Password");
            if (Pw.equals(dbPw)){
                navigateToTheDashboard();
            }else{
                new Alert(Alert.AlertType.ERROR,"Sorry!!! Password,Incorrect").show();
            }
        }else{
            new Alert(Alert.AlertType.INFORMATION,"Sorry!!! User Id Can't be find").show();
        }
    }

    public void navigateToTheDashboard() throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/dashboard_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage)this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }

    public void linkRegistrationOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane rootNode = FXMLLoader.load(this.getClass().getResource("/view/registration_form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage)this.rootNode.getScene().getWindow();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Dashboard Form");
    }
    private boolean isValid() {
        if ((!Regex.setTextColour(lk.ijse.util.TextField.ID,txtUserId)))return false;
       // if ((!Regex.setTextColour(lk.ijse.util.TextField.PASSWORD,txtPassword)))return false;
        return true;
    }

    public void txtUserIdOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.ID,txtUserId);
    }

    public void txtPasswordOnKeyReleased(KeyEvent keyEvent) {
        //Regex.setTextColour(lk.ijse.util.TextField.PASSWORD,txtPassword);
    }

    public void btnEnterOnKeyPress(KeyEvent keyEvent) {
    }
}
