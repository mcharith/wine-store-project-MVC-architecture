package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddNewCusRepo {
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
}
