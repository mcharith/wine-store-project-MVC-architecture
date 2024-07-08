package lk.ijse.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.SupplierRepo;
import lk.ijse.util.Regex;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SupplierFormController {

    public TextField txtId;
    public TextField txtTele;
    public TextField txtName;
    public TextField txtEmail;
    public TextField txtAddress;
    public AnchorPane rootNode;
    public Label lblDate;
    public Label lblTime;
    public TableView <SupplierTm>tblSupplier;
    public TableColumn <?,?>colId;
    public TableColumn <?,?>colName;
    public TableColumn <?,?>colAddress;
    public TableColumn <?,?>colEmail;
    public TableColumn <?,?>colTelephone;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        setDate();
        setCellValuefactory();
        loadAllSupplier();
        setTableAction();
    }
    private void setTableAction() {
        tblSupplier.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if (newSelection != null) {
                txtId.setText(newSelection.getUserId());
                txtName.setText(newSelection.getName());
                txtEmail.setText(String.valueOf(newSelection.getEmail()));
                txtTele.setText(String.valueOf(newSelection.getTelephone()));
                txtAddress.setText(String.valueOf(newSelection.getAddress()));
            }
        });
    }
    private void updateTime() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }

    public void setCellValuefactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colTelephone.setCellValueFactory(new PropertyValueFactory<>("Telephone"));
    }

    private void loadAllSupplier(){
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();

        try {
            List<Supplier> supplierList = SupplierRepo.getAll();
            for (Supplier supplier : supplierList) {
                SupplierTm tm = new SupplierTm(
                        supplier.getSupId(),
                        supplier.getName(),
                        supplier.getAddress(),
                        supplier.getEmail(),
                        supplier.getTele()
                );
                obList.add(tm);
            }

            tblSupplier.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    public void btnAddOnAction(ActionEvent actionEvent) {
        String userID = txtId.getText();
        String Name = txtName.getText();
        String Address = txtAddress.getText();
        String Email = txtEmail.getText();
        String Tele = txtTele.getText();
        if (isValid()) {
            Supplier supplier = new Supplier(userID, Name, Address, Email, Tele);
            try {
                boolean isSaved = SupplierRepo.Add(supplier);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier Saved!!!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
        loadAllSupplier();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String userID = txtId.getText();
        String Name = txtName.getText();
        String Address = txtAddress.getText();
        String Email = txtEmail.getText();
        String Tele = txtTele.getText();

        Supplier supplier = new Supplier(userID,Name,Address,Email,Tele);

            try {
                boolean isUpdated = SupplierRepo.update(supplier);
                if(isUpdated) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Supplier updated!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            loadAllSupplier();
        }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();

        try {
            boolean isDeleted = SupplierRepo.delete(id);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllSupplier();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtId.getText();

        Supplier supplier = SupplierRepo.searchById(id);
        if (supplier != null) {
            txtId.setText(supplier.getSupId());
            txtName.setText(supplier.getName());
            txtAddress.setText(supplier.getAddress());
            txtEmail.setText(supplier.getEmail());
            txtTele.setText(supplier.getTele());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Supplier not found!").show();
        }
    }
    public void btnClearOnOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
    }
    private void clearFields() {
        txtId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtEmail.setText("");
        txtTele.setText("");
    }
    private boolean isValid() {
        if(!Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele))return false;
        return true;
    }

    public void txtSupplierNumberOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele);
    }
}
