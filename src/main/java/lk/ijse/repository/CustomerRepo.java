package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepo {
    public static boolean save(Customer customer) throws SQLException {
        String sql = "INSERT INTO Customer VALUES(?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, customer.getCusId());
        pstm.setObject(2, customer.getCusName());
        pstm.setObject(3, customer.getCusAddress());
        pstm.setObject(4, customer.getCusNum());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String cusId) throws SQLException {
        String sql = "DELETE FROM Customer WHERE Customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, cusId);

        return pstm.executeUpdate() > 0;
    }

    public static boolean update(Customer customer) throws SQLException {
        String sql = "UPDATE Customer SET Name = ?, Address = ?, Contact_number  WHERE Customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1, customer.getCusName());
        pstm.setObject(2, customer.getCusAddress());
        pstm.setObject(3, customer.getCusNum());
        pstm.setObject(4, customer.getCusId());

        return pstm.executeUpdate() > 0;
    }

    public static List<Customer> getAll() throws SQLException {
        String sql = "SELECT * FROM Customer";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Customer> cusList = new ArrayList<>();

        while (resultSet.next()) {
            String cusId = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Tele = resultSet.getString(4);

            Customer customer = new Customer(cusId, Name, Address, Tele);
            cusList.add(customer);
        }
        return cusList;
    }
    public static Customer searchByContact(String tele) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Contact_number = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,tele);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()){
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String tel = resultSet.getString(4);

            Customer customer = new Customer(cus_id,name,address,tel);
            return customer;
        }
        return null;
    }
    public static Customer searchById(String id) throws SQLException {
        String sql = "SELECT * FROM Customer WHERE Customer_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, id);

        ResultSet resultSet = pstm.executeQuery();
        if (resultSet.next()) {
            String cus_id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            String tel = resultSet.getString(4);

            Customer customer = new Customer(cus_id, name, address, tel);

            return customer;
        }
        return null;
    }

    public static List<String> getIds() throws SQLException {
        String sql = "SELECT Customer_id FROM Customer";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        List<String> idList = new ArrayList<>();

        ResultSet resultSet = pstm.executeQuery();
        while (resultSet.next()) {
            String id = resultSet.getString(1);
            idList.add(id);
        }
        return idList;
    }

}
