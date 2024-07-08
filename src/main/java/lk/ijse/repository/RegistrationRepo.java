package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Register;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegistrationRepo {
    public static boolean register(Register register) throws SQLException {
        String sql = "INSERT INTO User VALUES(?, ?, ?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);

        pstm.setObject(1,register.getUser_id());
        pstm.setObject(2,register.getFirst_name());
        pstm.setObject(3,register.getLast_name());
        pstm.setObject(4,register.getEmail());
        pstm.setObject(5,register.getPassword());

        return pstm.executeUpdate() > 0;
    }
}
