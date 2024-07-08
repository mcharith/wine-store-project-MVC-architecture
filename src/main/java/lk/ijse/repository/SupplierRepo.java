package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;
import lk.ijse.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepo {

    public static boolean Add(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO Supplier VALUES(?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplier.getSupId());
        pstm.setObject(2,supplier.getName());
        pstm.setObject(3,supplier.getAddress());
        pstm.setObject(4,supplier.getEmail());
        pstm.setObject(5,supplier.getTele());

        return pstm.executeUpdate() > 0;
    }
    public static boolean update(Supplier supplier) throws SQLException {
        String sql = "UPDATE customers SET Supplier_name = ?, Supplier_address = ?, Supplier_tel = ?,Supplier_email WHERE Supplier_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplier.getSupId());
        pstm.setObject(2,supplier.getName());
        pstm.setObject(3,supplier.getAddress());
        pstm.setObject(4,supplier.getEmail());
        pstm.setObject(5,supplier.getTele());

        return pstm.executeUpdate() > 0;
    }
    public static boolean delete(String id) throws SQLException {
        String sql = "DELETE FROM Supplier WHERE Supplier_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        return pstm.executeUpdate() > 0;
    }
    public static Supplier searchById(String id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE Supplier_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String userID = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String Tele = resultSet.getString(5);

            Supplier supplier = new Supplier(userID, Name, Address,Email,Tele);

            return supplier;
        }

        return null;
    }
    public static List<Supplier> getAll() throws SQLException {
        String sql = "SELECT * FROM Supplier";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Supplier> supList = new ArrayList<>();

        while (resultSet.next()) {
            String userId = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String Tele = resultSet.getString(5);

            Supplier supplier = new Supplier(userId,Name,Address,Email,Tele);
            supList.add(supplier);
        }
        return supList;
    }

    public static Supplier searchBySupplierName(String supplierName) throws SQLException {
        String sql = "SELECT * FROM Supplier WHERE Supplier_name = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,supplierName);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            String userId = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Email = resultSet.getString(4);
            String Tele = resultSet.getString(5);

            Supplier supplier = new Supplier(userId, Name, Address,Email,Tele);
            return supplier;
        }
        return null;
    }

    public static List<String> getSupplierName() throws SQLException {
        String sql = "SELECT Supplier_name FROM Supplier";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> supplierNameList = new ArrayList<>();
        while (resultSet.next()) {
            supplierNameList.add(resultSet.getString(1));
        }
        return supplierNameList;
    }
}
