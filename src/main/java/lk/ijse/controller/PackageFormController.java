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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.model.Package;
import lk.ijse.model.tm.PackageTm;
import lk.ijse.repository.PackageRepo;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PackageFormController {
    public AnchorPane rootNode;
    public TextField txtPackageId;
    public TextField txtPackageType;
    public TextField txtQty;
    public TextField txtPrice;
    public TableView <PackageTm>tblPackage;
    public TableColumn <?,?>colId;
    public TableColumn <?,?>colQty;
    public TableColumn <?,?>colPrice;
    public TableColumn <?,?>colDescription;
    public TextField txtDescription;
    public Label lblTime;
    public Label lblDate;

    public void initialize() {
        setCellValueFactory();
        loadAllPackages();
        setTableAction();
        setDate();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Repeat indefinitely
        timeline.play();
    }
    private void setTableAction() {
        tblPackage.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if (newSelection != null) {
                txtPackageId.setText(newSelection.getPackageId());
                txtDescription.setText(newSelection.getDescription());
                txtQty.setText(String.valueOf(newSelection.getQty()));
                txtPrice.setText(String.valueOf(newSelection.getPrice()));
            }
        });
    }
    public void setDate(){
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }
    private void updateTime() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }
    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("packageId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("Qty"));
    }

    private void loadAllPackages() {
        ObservableList<PackageTm> obList = FXCollections.observableArrayList();

        try {
            List<Package> PackageList = PackageRepo.getAll();
            for (Package packages : PackageList) {
                PackageTm tm = new PackageTm(
                        packages.getPackageId(),
                        packages.getDescription(),
                        packages.getPrice(),
                        packages.getQty()
                );

                obList.add(tm);
            }

            tblPackage.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String packageId = txtPackageId.getText();
        String Description = txtDescription.getText();
        double Price = Double.parseDouble(txtPrice.getText());
        int Qty = Integer.parseInt(txtQty.getText());

        Package packages = new Package();
        packages.setPackageId(packageId);
        packages.setDescription(Description);
        packages.setPrice(Price);
        packages.setQty(Qty);

        try {
            boolean isSaved = PackageRepo.save(packages);
            if (isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Package saved!").show();
                clearFields();
                loadAllPackages();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String packageId = txtPackageId.getText();
        String Description = txtDescription.getText();
        double Price = Double.parseDouble(txtPrice.getText());
        int Qty = Integer.parseInt(txtQty.getText());

        Package packages = new Package(packageId,Description,Price,Qty);
        try {
            boolean isUpdated = PackageRepo.update(packages);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Package updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllPackages();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String packageId = txtPackageId.getText();
        try {
            boolean isDeleted = PackageRepo.delete(packageId);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Package deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
       loadAllPackages();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields(){
        txtPackageId.setText("");
        txtDescription.setText("");
        txtQty.setText("");
        txtPrice.setText("");
    }

    public void btnOrderPackageOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/place_package_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(anchorPane);
    }
}
