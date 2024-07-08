package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.db.DbConnection;
import lk.ijse.model.*;
import lk.ijse.model.Package;
import lk.ijse.model.tm.CartTm;
import lk.ijse.model.tm.PackageCartTm;
import lk.ijse.repository.*;
import lk.ijse.util.Regex;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PlacePackageFormController {
    public AnchorPane rootNode;
    public Label lblOrderId;
    public Label lblDate;
    public Label lblTime;
    public Label lblCusName;
    public Label lblCusId;
    public JFXComboBox <String>cmbUserName;
    public Label lblUserId;
    public TextField txtQty;
    public Label lbltPrice;
    public Label lblQtyOnHand;
    public Label lblPackageId;
    public JFXComboBox <String>cmbDescription;
    public TableView <PackageCartTm>tblPackageOrder;
    public TableColumn <?,?>colCode;
    public TableColumn <?,?>colDescription;
    public TableColumn <?,?>colQty;
    public TableColumn <?,?>colUnitPrice;
    public TableColumn <?,?>colTotal;
    public TableColumn <?,?>colAction;
    public TextField txtTele;
    public Label lblNetTotal;
    public AnchorPane pane;

    private ObservableList<PackageCartTm> obList = FXCollections.observableArrayList();

    public void initialize() {
        setDate();
        setTime();
        getCurrentOrderId();
        getuserName();
        getDescription();
        setCellValueFactory();
    }
    private void setCellValueFactory() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("total"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
    }
    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.genarateOrderId();
            String nextOrderId = nextOrderId(currentId);
            lblOrderId.setText(nextOrderId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private String nextOrderId(String currentId) {
        if (currentId != null){
            int id = Integer.parseInt(currentId);
            ++id;

            return "O"+id;
        }
        return "O1";
    }

    private void setTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);
        lblTime.setText(formattedTime);
    }

    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    public void txtContactOnAction(ActionEvent actionEvent) {
        String tele = txtTele.getText();
        if(isValid()) {
            try {
                Customer customer = CustomerRepo.searchByContact(tele);

                assert customer != null;
                lblCusName.setText(customer.getCusName());
                lblCusId.setText(customer.getCusId());

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void cmbUserNameOnAction(ActionEvent actionEvent) {
        String userName = cmbUserName.getValue();
        try {
            Staff staff = StaffRepo.searchByUserName(userName);
            if(staff != null) {
                lblUserId.setText(staff.getStaff_id());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getuserName() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> userNameList = StaffRepo.getuserName();

            for (String userName : userNameList) {
                obList.add(userName);
            }
            cmbUserName.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cmbDescriptionOnAction(ActionEvent actionEvent) {
        String desc = cmbDescription.getValue();
        try {
            Package packages = PackageRepo.searchByDescription(desc);
            if(packages != null) {
                lblPackageId.setText(packages.getPackageId());
                lbltPrice.setText(String.valueOf(packages.getPrice()));
                lblQtyOnHand.setText(String.valueOf(packages.getQty()));
            }

            txtQty.requestFocus();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void getDescription(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> descriptionList = PackageRepo.getDescription();

            for (String description: descriptionList){
                obList.add(description);
            }
            cmbDescription.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void btnAddNewOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/addNewCus_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Customer Form");
        stage.centerOnScreen();
        stage.show();
        stage.setAlwaysOnTop(true);
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String orderID = lblOrderId.getText();
        String cusId = lblCusId.getText();
        Date date = Date.valueOf(LocalDate.now());

        var order = new Order(orderID,cusId,date);
        List<PackageDetails>pdList = new ArrayList<>();

        for (int i = 0; i < tblPackageOrder.getItems().size();i++){
            PackageCartTm tm = obList.get(i);

            PackageDetails pd = new PackageDetails(
                    orderID,
                    tm.getId(),
                    tm.getQty(),
                    tm.getPrice()
            );
            pdList.add(pd);
        }
        PlacePackage pd = new PlacePackage(order,pdList);
        //System.out.println(po);

        try {
            boolean isPlaced = PlacePackageRepo.placePackage(pd);
            if(isPlaced){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Placed!").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Order Placed Unsuccessfully!").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    public void btnAddCart(ActionEvent actionEvent) {
        String code = lblPackageId.getText();
        String description = cmbDescription.getValue();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lbltPrice.getText());
        double total = qty * unitPrice;
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(type.orElse(no) == yes) {
                int selectedIndex = tblPackageOrder.getSelectionModel().getSelectedIndex();
                obList.remove(selectedIndex);

                tblPackageOrder.refresh();
                calculateNetTotal();
            }
        });

        for (int i = 0; i < tblPackageOrder.getItems().size(); i++) {
            if(code.equals(colCode.getCellData(i))) {

                PackageCartTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblPackageOrder.refresh();

                calculateNetTotal();
                return;
            }
        }

        PackageCartTm tm = new PackageCartTm(code, description, qty, unitPrice, total, btnRemove);
        obList.add(tm);

        tblPackageOrder.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");
    }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblPackageOrder.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

    public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/package_form.fxml"));
        pane.getChildren().clear();
        pane.getChildren().add(anchorPane);
    }
    private boolean isValid() {
        if(!Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele))return false;
        return true;
    }
    public void txtCustomerTelephoneOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele);
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/OrderPackageBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        String tot = lblNetTotal.getText();

        Map<String,Object> data = new HashMap<>();
        data.put("Total",tot);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
