package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Item;
import lk.ijse.model.Staff;
import lk.ijse.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StaffRepo {
    public static boolean save(Staff staff) throws SQLException {
        String sql = "INSERT INTO Staff VALUES(?,?,?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,staff.getStaff_id());
        pstm.setObject(2,staff.getName());
        pstm.setObject(3,staff.getAddress());
        pstm.setObject(4,staff.getAge());
        pstm.setObject(5,staff.getContact_number());
        pstm.setObject(6,staff.getJob_Role());

        return pstm.executeUpdate()>0;
    }

    public static boolean update(Staff staff) throws SQLException {
        String sql = "UPDATE Staff SET Name = ?, Address = ?, Age = ?,Contact_number =?, Job_Role =? WHERE Staff_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,staff.getName());
        pstm.setObject(2,staff.getAddress());
        pstm.setObject(3,staff.getAge());
        pstm.setObject(4,staff.getContact_number());
        pstm.setObject(5,staff.getJob_Role());
        pstm.setObject(6,staff.getStaff_id());
        return pstm.executeUpdate()>0;
    }

    public static boolean delete(String staffID) throws SQLException {
        String sql = "DELETE FROM Staff WHERE Staff_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, staffID);

        return pstm.executeUpdate() > 0;
    }

    public static List<Staff> getAll() throws SQLException {
        String sql = "SELECT * FROM Staff";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Staff> staffList = new ArrayList<>();

        while (resultSet.next()) {
            String StaffId = resultSet.getString(1);
            String Name = resultSet.getString(2);
            String Address = resultSet.getString(3);
            String Age = resultSet.getString(4);
            String contactNumber = resultSet.getString(5);
            String jobRole = resultSet.getString(6);

            Staff staff = new Staff(StaffId,Name,Address,Age,contactNumber,jobRole);
            staffList.add(staff);
        }
        return staffList;
    }

    public static List<String> getuserName() throws SQLException {
        String sql = "SELECT Name FROM Staff";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<String> userNameList = new ArrayList<>();
        while (resultSet.next()) {
            userNameList.add(resultSet.getString(1));
        }
        return userNameList;
    }

    public static Staff searchByUserName(String userName) throws SQLException {
        String sql = "SELECT * FROM Staff WHERE Name = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1,userName);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            return new Staff(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
        }
        return null;
    }
}
