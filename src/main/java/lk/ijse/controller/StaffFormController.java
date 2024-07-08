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
import lk.ijse.model.Staff;
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.StaffTm;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.StaffRepo;
import lk.ijse.repository.SupplierRepo;
import lk.ijse.util.Regex;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StaffFormController {
    public TableView <StaffTm>tblStaffTable;
    public TableColumn <?,?>colStaffId;
    public TableColumn <?,?>colName;
    public TableColumn <?,?>colAddress;
    public TableColumn <?,?>colAge;
    public TableColumn <?,?>colNumber;
    public TableColumn <?,?>colPosition;
    public TextField txtStaffId;
    public TextField txtAddress;
    public TextField txtName;
    public TextField txtAge;
    public TextField txtContactNumber;
    public TextField txtJobPosition;
    public AnchorPane rootNode;
    public Label lblTime;
    public Label lblDate;

    public void initialize(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Repeat indefinitely
        timeline.play();
        setDate();
        setCellValueFactory();
        loadAllStaff();
        setTableAction();
    }
    private void updateTime() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }
    public void setDate(){
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }
    private void loadAllStaff() {
        ObservableList<StaffTm>obList = FXCollections.observableArrayList();

        try {
            List<Staff> staffList = StaffRepo.getAll();
            for (Staff staff : staffList) {
               StaffTm tm = new StaffTm(
                        staff.getStaff_id(),
                        staff.getName(),
                        staff.getAddress(),
                        staff.getAge(),
                        staff.getContact_number(),
                        staff.getJob_Role()
                );
                obList.add(tm);
            }

            tblStaffTable.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setTableAction() {
        tblStaffTable.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if (newSelection != null) {
                txtStaffId.setText(newSelection.getStaff_id());
                txtName.setText(newSelection.getName());
                txtAge.setText(newSelection.getAge());
                txtAddress.setText(newSelection.getAddress());
                txtJobPosition.setText(newSelection.getJob_Role());
                txtContactNumber.setText(newSelection.getContact_number());
            }
        });
    }
    private void setCellValueFactory() {
        colStaffId.setCellValueFactory(new PropertyValueFactory<>("Staff_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        colNumber.setCellValueFactory(new PropertyValueFactory<>("Contact_number"));
        colPosition.setCellValueFactory(new PropertyValueFactory<>("Job_Role"));
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String StaffID = txtStaffId.getText();
        String Name = txtName.getText();
        String Address = txtAddress.getText();
        String Age = txtAge.getText();
        String ContactNumber = txtContactNumber.getText();
        String JobPosition = txtJobPosition.getText();
        if (isValid()) {
            Staff staff = new Staff(StaffID, Name, Address, Age, ContactNumber, JobPosition);

            try {
                boolean isSaved = StaffRepo.save(staff);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Staff Member Saved!!!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
        loadAllStaff();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String StaffID = txtStaffId.getText();
        String Name = txtName.getText();
        String Address = txtAddress.getText();
        String Age = txtAge.getText();
        String ContactNumber = txtContactNumber.getText();
        String JobPosition = txtJobPosition.getText();

        Staff staff = new Staff(StaffID,Name,Address,Age,ContactNumber,JobPosition);

        try {
            boolean isSaved = StaffRepo.update(staff);
            if (isSaved){
                new Alert(Alert.AlertType.CONFIRMATION,"Staff Member Detail update!!!").show();
                clearFields();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllStaff();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String StaffID = txtStaffId.getText();

        try {
            boolean isDeleted = StaffRepo.delete(StaffID);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllStaff();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/dashboard_form.fxml"));
        Stage stage = (Stage) rootNode.getScene().getWindow();

        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Dashboard Form");
        stage.centerOnScreen();
    }
    private void clearFields() {
        txtStaffId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtAge.setText("");
        txtContactNumber.setText("");
        txtJobPosition.setText("");
    }
    private boolean isValid() {
        if(!Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtContactNumber))return false;
        return true;
    }

    public void txtStaffNumberOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtContactNumber);
    }
}
