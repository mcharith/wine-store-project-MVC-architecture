package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.PlaceOrder;
import lk.ijse.model.PlacePackage;

import java.sql.Connection;
import java.sql.SQLException;

public class PlacePackageRepo {
    public static boolean placePackage(PlacePackage pd) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        try {
            boolean isOrderSaved = OrderRepo.save(pd.getOrder());
            if (isOrderSaved){
                boolean isQtyUpdated = PackageRepo.updateNow(pd.getPdList());
                //  System.out.println(isQtyUpdated);
                if (isQtyUpdated){
                    boolean isOrderDetailSaved = PackageDetailRepo.save(pd.getPdList());
                    //System.out.println(isOrderDetailSaved);
                    if (isOrderDetailSaved){
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
            return false;
        }catch (Exception e){
            connection.rollback();
            throw new RuntimeException(e);
//            return false;
        }finally {
            connection.setAutoCommit(true);
        }
    }
}
