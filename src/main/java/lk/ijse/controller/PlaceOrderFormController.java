package lk.ijse.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.sun.jdi.StackFrame;
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
import lk.ijse.model.tm.CartTm;
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

public class PlaceOrderFormController {
    public JFXComboBox <String>cmbItemCode;
    public TextField txtQty;
    public TableView <CartTm>tblOrder;
    public TableColumn <?,?>colItemCode;
    public TableColumn <?,?>colDescription;
    public TableColumn <?,?>colQty;
    public TableColumn <?,?>colUnitPrice;
    public TableColumn <?,?>colTotal;
    public TableColumn <?,?>colAction;
    public Label lblOrderId;
    public Label lblCusName;
    public Label lblDescription;
    public Label lblUnitPrice;
    public Label lblQtyOnHand;
    public AnchorPane rootNode;
    public Label lblNetTotal;
    public Label lblDate;
    public TextField txtTele;
    public Label lblItemCode;
    public Label lblCusId;
    public JFXComboBox <String>cmbDescription;
    public JFXComboBox <String>cmbUserName;
    public Label lblUserId;
    public Label lblTime;

    private ObservableList<CartTm> obList = FXCollections.observableArrayList();

   public void initialize() {
        getCurrentOrderId();
        setDate();
        setTime();
        getDescription();
        getuserName();
        //getCustomerIds();
       //getItemCodes();
       setCellValueFactory();
    }
    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

    }
    private void setDate() {
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    private void setTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = now.format(formatter);
        lblTime.setText(formattedTime);
    }

    private void getCurrentOrderId() {
        try {
            String currentId = OrderRepo.genarateOrderId();
            String nextOrderId = nextOrderId(currentId);

            lblOrderId.setText(nextOrderId);

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
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

    public void btnAddNewOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/addNewCus_form.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(anchorPane));
        stage.setTitle("Customer Form");
        stage.centerOnScreen();
        stage.show();
        stage.setAlwaysOnTop(true);
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String code = lblItemCode.getText();
        String description = cmbDescription.getValue();
        int qty = Integer.parseInt(txtQty.getText());
        double unitPrice = Double.parseDouble(lblUnitPrice.getText());
        double total = qty * unitPrice;
        JFXButton btnRemove = new JFXButton("remove");
        btnRemove.setCursor(Cursor.HAND);

        btnRemove.setOnAction((e) -> {
            ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> type = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

            if(type.orElse(no) == yes) {
                int selectedIndex = tblOrder.getSelectionModel().getSelectedIndex();
                if (selectedIndex<0){

                    new Alert(Alert.AlertType.ERROR,"Please select item").show();

                }else {
                    //System.out.println(selectedIndex);
                    obList.remove(selectedIndex);

                    tblOrder.refresh();
                    calculateNetTotal();
                }
            }
        });

        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            if(code.equals(colItemCode.getCellData(i))) {

                CartTm tm = obList.get(i);
                qty += tm.getQty();
                total = qty * unitPrice;

                tm.setQty(qty);
                tm.setTotal(total);

                tblOrder.refresh();

                calculateNetTotal();
                return;
            }
        }

        CartTm tm = new CartTm(code, description, qty, unitPrice, total, btnRemove);
        //System.out.println(tm);
        obList.add(tm);

        tblOrder.setItems(obList);
        calculateNetTotal();
        txtQty.setText("");
    }

    private void calculateNetTotal() {
        int netTotal = 0;
        for (int i = 0; i < tblOrder.getItems().size(); i++) {
            netTotal += (double) colTotal.getCellData(i);
        }
        lblNetTotal.setText(String.valueOf(netTotal));
    }

   public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String orderID = lblOrderId.getText();
        String cusId = lblCusId.getText();
        Date date = Date.valueOf(LocalDate.now());

        var order = new Order(orderID,cusId,date);
        List<OrderDetail>odList = new ArrayList<>();

        for (int i = 0; i < tblOrder.getItems().size();i++){
            CartTm tm = obList.get(i);

            OrderDetail od = new OrderDetail(
                    orderID,
                    tm.getCode(),
                    tm.getQty(),
                    tm.getUnitPrice()
            );
            odList.add(od);
        }
        PlaceOrder po = new PlaceOrder(order,odList);
       // System.out.println(po);

        try {
            boolean isPlaced = PlaceOrderRepo.placeOrder(po);
            if(isPlaced){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Placed!").show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Order Placed Unsuccessfully!").show();
            }
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

   public void cmbItemCodeOnAction(ActionEvent actionEvent) {
       String code = cmbItemCode.getValue();

       try {
           Item item = ItemRepo.searchById(code);
           if(item != null) {
               lblDescription.setText(item.getDescription());
               lblUnitPrice.setText(String.valueOf(item.getUnit_price()));
               lblQtyOnHand.setText(String.valueOf(item.getQty_on_hand()));
           }

           txtQty.requestFocus();

       } catch (SQLException e) {
           throw new RuntimeException(e);
       }
   }

    public void txtContactOnAction(ActionEvent actionEvent) {

        //System.out.println("1");
        String tele = txtTele.getText();
        if (isValid()) {
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

    private void getDescription(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> descriptionList = ItemRepo.getDescription();

            for (String description: descriptionList){
                obList.add(description);
            }
            cmbDescription.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void cmbDescriptionOnAction(ActionEvent actionEvent) {
        String desc = cmbDescription.getValue();
        try {
            Item item = ItemRepo.searchByDescription(desc);
            if(item != null) {
                lblItemCode.setText(item.getCode());
                lblUnitPrice.setText(String.valueOf(item.getUnit_price()));
                lblQtyOnHand.setText(String.valueOf(item.getQty_on_hand()));
            }

            txtQty.requestFocus();

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
    private boolean isValid() {
        if(!Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele))return false;
        return true;
    }

    public void txtCusTeleOnKeyReleased(KeyEvent keyEvent) {
        Regex.setTextColour(lk.ijse.util.TextField.CONTACT,txtTele);
    }

    public void btnPrintBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/CustomerOrderBill.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        String tot = lblNetTotal.getText();

        Map<String,Object> data = new HashMap<>();
        data.put("Total",tot);

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

}

