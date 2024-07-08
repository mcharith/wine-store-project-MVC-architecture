package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Order;
import lk.ijse.model.OrderDetail;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OrderRepo {

    public static boolean save(Order order) throws SQLException {
        String sql = "INSERT INTO Orders VALUES(?,?,?)";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);

        pstm.setString(1,order.getOrder_id());
        pstm.setString(2,order.getCustomer_id());
        pstm.setDate(3, (Date) order.getDate());

        return pstm.executeUpdate() > 0;
    }

    public static String genarateOrderId() throws SQLException {
        String sql = "SELECT MAX(CAST(SUBSTRING(Order_id, 2) AS UNSIGNED)) AS HighestOrder_id FROM Orders;";
        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        if(resultSet.next()){
            return resultSet.getString(1);
        }
        return splitOrderId(null);
    }

    private static String splitOrderId(String currentId) {
        if(currentId != null) {
            String[] strings = currentId.split("O0");
            int id = Integer.parseInt(strings[1]);
            id++;
            String ID = String.valueOf(id);
            int length = ID.length();
            if (length < 2){
                return "O00"+id;
            }else {
                if (length < 3){
                    return "O0"+id;
                }else {
                    return "O"+id;
                }
            }
        }
        return "O001";
    }
}