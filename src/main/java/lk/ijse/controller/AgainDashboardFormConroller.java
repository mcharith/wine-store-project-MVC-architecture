package lk.ijse.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.db.DbConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AgainDashboardFormConroller {
    public AnchorPane rootNode;
    public Label lblTime;
    public Label lblDate;
    public Label lblItemCount;
    public Label lblSupplierCount;
    private int itemCount;
    private int supplierCount;

    public void initialize() {
        try {
            itemCount = getItemCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setItemCount(itemCount);

        try {
            supplierCount = getSupplierCount();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        setsupplierCount(supplierCount);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        setDate();
    }

    private void setsupplierCount(int supplierCount) {
        lblSupplierCount.setText(String.valueOf(supplierCount));
    }

    private int getSupplierCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS supplier_count FROM Supplier";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("supplier_count");
        }
        return 0;
    }

    private int getItemCount() throws SQLException {
        String sql = "SELECT COUNT(*) AS item_count FROM Items";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()) {
            return resultSet.getInt("item_count");
        }
        return 0;
    }

    private void setItemCount(int itemCount) {
        lblItemCount.setText(String.valueOf(itemCount));
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    private void updateTime() {

        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }

    public void btnAgainPlaceOrderOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/place_order_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(anchorPane);
    }

    public void btnAgainPlacePackageOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/place_package_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(anchorPane);
    }

    public void btnAgainAddCusOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/customer_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(anchorPane);
    }
}
