package lk.ijse.controller;

import com.jfoenix.controls.JFXComboBox;
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
import lk.ijse.db.DbConnection;
import lk.ijse.model.*;
import lk.ijse.model.tm.ItemTm;
import lk.ijse.model.tm.StaffTm;
import lk.ijse.model.tm.SupplierItemDetailsTm;
import lk.ijse.repository.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFormController {
    public TextField txtCode;

    public TextField txtCount;
    public TextField txtUnitPrice;
    public TextField txtCategory;

    public AnchorPane rootNode;
    public TableColumn<?, ?> colItemCode;

    public TableColumn<?, ?> colCount;
    public TableColumn<?, ?> colDescription;
    public TableColumn <?,?>colBuyingPice;
    public TableColumn <?,?>colunitPrice;


    public Label lblTime;
    public Label lblDate;
    public TableView<ItemTm> tblItem;
    public Label lblSupplierId;
    public JFXComboBox <String>cmbSupplierName;
    public TextField txtBuyingPrice;

    public void initialize(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateTime();
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Repeat indefinitely
        timeline.play();
        setCellValueFactory();
        loadAllItem();
        getSupplierName();
        setTableAction();
        setDate();
    }
    private void updateTime() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }
    private void setTableAction() {
        tblItem.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) -> {
            if (newSelection != null) {
                txtCode.setText(newSelection.getItem_code());
                txtCategory.setText(newSelection.getDescription());
                txtCount.setText(String.valueOf(newSelection.getQty_on_hand()));
                txtBuyingPrice.setText(String.valueOf(newSelection.getBuying_price()));
                txtUnitPrice.setText(String.valueOf(newSelection.getUnit_price()));
                lblSupplierId.setText(String.valueOf(newSelection.getSupplier_id()));
            }
        });
    }
    public void setDate(){
        LocalDate now = LocalDate.now();
        lblDate.setText(String.valueOf(now));
    }

    private void setCellValueFactory() {
        colItemCode.setCellValueFactory(new PropertyValueFactory<>("item_code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colunitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colCount.setCellValueFactory(new PropertyValueFactory<>("qty_on_hand"));
        colBuyingPice.setCellValueFactory(new PropertyValueFactory<>("buying_price"));
    }

    private void loadAllItem() {
        ObservableList<ItemTm> obList = FXCollections.observableArrayList();

        try {
            List<Item> itemList = ItemRepo.getAll();
            for (Item item : itemList) {
                ItemTm tm = new ItemTm();
                tm.setItem_code(item.getCode());
                tm.setDescription(item.getDescription());
                tm.setBuying_price(item.getBuying_price());
                tm.setUnit_price(item.getUnit_price());
                tm.setQty_on_hand(item.getQty_on_hand());

                obList.add(tm);
            }

            tblItem.setItems(obList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void btnAddOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();
        String description = txtCategory.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtCount.getText());
        double buyingPrice = Double.parseDouble(txtBuyingPrice.getText());
        String SupplierId = lblSupplierId.getText();

        Item item = new Item();
        item.setCode(code);
        item.setDescription(description);
        item.setUnit_price(unitPrice);
        item.setQty_on_hand(qtyOnHand);
        item.setBuying_price(buyingPrice);
        item.setSupplier_id(SupplierId);

        try {
            boolean isSaved = ItemRepo.save(item);
            if(isSaved) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item saved!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllItem();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();

        try {
            boolean isDeleted = ItemRepo.delete(code);
            if(isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item deleted!").show();
                clearFields();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        loadAllItem();
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
    }
    private void clearFields(){
        txtCode.setText("");
        txtCategory.setText("");
        txtCount.setText("");
        txtUnitPrice.setText("");
        txtBuyingPrice.setText("");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String code = txtCode.getText();
        String description = txtCategory.getText();
        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtCount.getText());
        double buyingPrice = Double.parseDouble(txtBuyingPrice.getText());
        String SupplierId = lblSupplierId.getText();


        Item item = new Item();
        item.setCode(code);
        item.setDescription(description);
        item.setUnit_price(unitPrice);
        item.setQty_on_hand(qtyOnHand);
        item.setBuying_price(buyingPrice);
        item.setSupplier_id(SupplierId);

        try {
            boolean isUpdated = ItemRepo.updateItem(item);
            if(isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, "Item updated!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void getSupplierName(){
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<String> supplierNameList = SupplierRepo.getSupplierName();

            for (String supplierName: supplierNameList){
                obList.add(supplierName);
            }
            cmbSupplierName.setItems(obList);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
    public void cmbSupplierNameOnAction(ActionEvent actionEvent) {
        String supplierName = cmbSupplierName.getValue();
        try {
            Supplier supplier = SupplierRepo.searchBySupplierName(supplierName);
            if(supplier != null) {
                lblSupplierId.setText(supplier.getSupId());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void btnAddNewCusOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/supplier_form.fxml"));
        rootNode.getChildren().clear();
        rootNode.getChildren().add(anchorPane);
    }

    public void btnItemReportOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/Itemlist.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("ItemCode",txtCode.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }

    public void btnViweProfitOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load("src/main/resources/Report/ItemProfit.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

        Map<String,Object> data = new HashMap<>();
        data.put("Description",txtCategory.getText());

        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, data, DbConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint,false);
    }
}
