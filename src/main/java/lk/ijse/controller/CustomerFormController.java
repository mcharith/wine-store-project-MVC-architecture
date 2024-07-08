package lk.ijse.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.Supplier;
import lk.ijse.model.tm.CustomerTm;
import lk.ijse.model.tm.SupplierTm;
import lk.ijse.repository.CustomerRepo;
import lk.ijse.repository.SupplierRepo;
import lk.ijse.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerFormController {

    public AnchorPane rootNode;
    public TextField txtCusId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtNumber;
    public TableView <CustomerTm>tblCustomer;
    public TableColumn<?,?> cusId;
    public TableColumn<?,?> cusName;
    public TableColumn<?,?>cusAddress;
    public TableColumn <?,?>cusTele;
    public Label lblTime;
    public Label lblDate;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        setDate();
       setCellValuefactory();
       loadAllCstomer();
       setTableAction();
    }
    public void setCellValuefactory(){
        cusId.setCellValueFactory(new PropertyValueFactory<>("CusId"));
        cusName.setCellValueFactory(new PropertyValueFactory<>("cusName"));
        cusAddress.setCellValueFactory(new PropertyValueFactory<>("cusAddress"));
        cusTele.setCellValueFactory(new PropertyValueFactory<>("cusNum"));
    }

    private void loadAllCstomer(){
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();

        try {
            List<Customer> CustomerList = CustomerRepo.getAll();
            for (Customer customer : CustomerList) {
                CustomerTm tm = new CustomerTm(
                        customer.getCusId(),
                        customer.getCusName(),
                        customer.getCusAddress(),
                        customer.getCusNum()
                );
                obList.add(tm);
            }

            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    private void setTableAction() {
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if (newSelection != null) {
                txtCusId.setText(newSelection.getCusId());
                txtName.setText(newSelection.getCusName());
                txtAddress.setText(newSelection.getCusAddress());
                txtNumber.setText(newSelection.getCusNum());
            }
        });
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String cusId = txtCusId.getText();
        String cusName = txtName.getText();
        String cusAddress = txtAddress.getText();
        String cusNum = txtNumber.getText();
        if(isValid()) {
            Customer customer = new Customer(cusId, cusName, cusAddress, cusNum);
            try {
                boolean isSaved = CustomerRepo.save(customer);
                if (isSaved) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved!!!").show();
                    clearFields();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
            loadAllCstomer();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String cusId = txtCusId.getText();

        try {
            boolean isDeleted = CustomerRepo.delete(cusId);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCstomer();
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String cusId = txtCusId.getText();
        String cusName = txtName.getText();
        String cusAddress = txtAddress.getText();
        String cusNum = txtNumber.getText();

        Customer customer = new Customer(cusId,cusName,cusAddress,cusNum);
        try {
            boolean isUpdated = CustomerRepo.update(customer);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Customer updated!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllCstomer();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
       clearFields();
    }

    private void clearFields(){
        txtCusId.setText("");
        txtName.setText("");
        txtAddress.setText("");
        txtNumber.setText("");
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
    }

    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException {
        String id = txtCusId.getText();

        Customer customer = CustomerRepo.searchById(id);
        if (customer != null) {
            txtCusId.setText(customer.getCusId());
            txtName.setText(customer.getCusNum());
            txtAddress.setText(customer.getCusAddress());
            txtNumber.setText(customer.getCusNum());
        } else {
            new Alert(Alert.AlertType.INFORMATION, "customer not found!").show();
        }
    }
    private boolean isValid() {
        if(!Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtNumber))return false;
        return true;
    }
    public void txtCustomerNumberOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtNumber);
    }

    public void btnGetReportOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Customerlist.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("CustomerID",txtCusId.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);

    }
}
