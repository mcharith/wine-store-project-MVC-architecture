package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.OrderDetail;
import lk.ijse.model.PackageDetails;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class PackageDetailRepo {
    public static boolean save(List<PackageDetails> pdList) throws SQLException {
            for (PackageDetails pd : pdList) {
                //System.out.println("Hello");
                boolean isSaved = save(pd);
                if(!isSaved) {
                    return false;
                }
            }
            return true;
        }

    private static boolean save(PackageDetails pd) throws SQLException {
        String sql = "INSERT INTO Order_package_details VALUES(?, ?, ?, ?)";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        //System.out.println(pstm);

        pstm.setString(1, pd.getOrder_id());
        pstm.setString(2,pd.getPackage_id());
        pstm.setInt(3,pd.getQty());
        pstm.setDouble(4,pd.getUnitPrice());

        //System.out.println("1");
        boolean b = pstm.executeUpdate() > 0;
        return b;

    }
}
