package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Item;
import lk.ijse.model.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepo {

    public static boolean save(Item item) throws SQLException {
        String sql = "INSERT INTO items VALUES(?, ?, ?, ?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, item.getCode());
        pstm.setObject(2, item.getDescription());
        pstm.setObject(3, item.getUnit_price());
        pstm.setObject(4, item.getQty_on_hand());
        pstm.setObject(5,item.getBuying_price());
        pstm.setObject(6,item.getSupplier_id());

        return pstm.executeUpdate() > 0;
    }

    public static List<String> getCodes() throws SQLException {
        String sql = "SELECT Item_code FROM Items";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> codeList = new ArrayList<>();
        while (resultSet.next()) {
            codeList.add(resultSet.getString(1));
        }
        return codeList;
    }

    public static Item searchById(String code) throws SQLException {
        String sql = "SELECT * FROM items WHERE Item_code = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, code);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }

    public static List<Item> getAll() throws SQLException {
        String sql = "SELECT * FROM Items";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Item> itemList = new ArrayList<>();

        while (resultSet.next()) {
                    String Item_code =resultSet.getString(1);
                    String Description = resultSet.getString(2);
                    double unit_price= resultSet.getDouble(3);
                    int qty_on_hand = resultSet.getInt(4);
                    double buying_price = resultSet.getDouble(5);
                    String supplierId = resultSet.getString(6);

            Item item = new Item(Item_code,Description,unit_price,qty_on_hand,buying_price, supplierId);
            itemList.add(item);
        }
        return itemList;
    }

    public static boolean update(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            boolean isUpdateQty = updateQty(od.getItem_code(), od.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    protected static boolean updateQty(String itemCode, int qty) throws SQLException {
        String sql = "UPDATE Items SET qty_on_hand = qty_on_hand - ? WHERE Item_code = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, itemCode);

        return pstm.executeUpdate() > 0;
    }

    public static Item searchByDesc(String description) throws SQLException {
        String sql = "SELECT * FROM items WHERE Description = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setObject(1, description);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()) {
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }

    public static Item searchByDescription(String desc) throws SQLException {
        String sql = "SELECT * FROM Items WHERE Description = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1,desc);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            return new Item(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }

    public static List<String> getDescription() throws SQLException {
        String sql = "SELECT Description FROM items";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> descriptionList = new ArrayList<>();
        while (resultSet.next()) {
            descriptionList.add(resultSet.getString(1));
        }
        return descriptionList;
    }


    public static boolean updateItem(Item item) throws SQLException {
        String sql = "UPDATE Items SET Description = ?, unit_price = ?, qty_on_hand = ? ,buying_price=? ,Supplier_id =? WHERE Item_code = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,item.getDescription());
        pstm.setObject(2,item.getUnit_price());
        pstm.setObject(3,item.getQty_on_hand());
        pstm.setObject(4,item.getBuying_price());
        pstm.setObject(5,item.getSupplier_id());
        pstm.setObject(1,item.getCode());


        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String code) throws SQLException {
        String sql = "DELETE FROM Items WHERE Item_code = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, code);

        return pstm.executeUpdate() > 0;
    }
}
