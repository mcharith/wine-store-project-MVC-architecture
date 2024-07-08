package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.OrderDetail;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailRepo {
    public static boolean save(List<OrderDetail> odList) throws SQLException {
        for (OrderDetail od : odList) {
            //System.out.println("Hello");
            boolean isSaved = save(od);
            if(!isSaved) {
                return false;
            }
        }
        return true;
    }
    private static boolean save(OrderDetail od) throws SQLException {
        String sql = "INSERT INTO Item_order_details VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        System.out.println(pstm);

        pstm.setString(1, od.getOrder_id());
        pstm.setString(2, od.getItem_code());
        pstm.setInt(3, od.getQty());
        pstm.setDouble(4, od.getUnitPrice());

        System.out.println("1");
        boolean b = pstm.executeUpdate() > 0;
        return b;

    }
}
