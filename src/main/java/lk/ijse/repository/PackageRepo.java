package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.Item;
import lk.ijse.model.OrderDetail;
import lk.ijse.model.Package;
import lk.ijse.model.PackageDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static lk.ijse.repository.ItemRepo.updateQty;

public class PackageRepo {
    public static boolean save(Package packages) throws SQLException {
        String sql = "INSERT INTO Package VALUES(?,?,?,?)";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,packages.getPackageId());
        pstm.setObject(2,packages.getDescription());
        pstm.setObject(3,packages.getPrice());
        pstm.setObject(4,packages.getQty());

        return pstm.executeUpdate()>0;
    }

    public static List<Package> getAll() throws SQLException {
        String sql = "SELECT * FROM Package";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        ResultSet resultSet = pstm.executeQuery();

        List<Package> PackList = new ArrayList<>();

        while (resultSet.next()) {
            String packageId = resultSet.getString(1);
            String Description = resultSet.getString(2);
            double Price = resultSet.getDouble(3);
            int Qty = resultSet.getInt(4);

            Package packages = new Package(packageId,Description,Price,Qty);
            PackList.add(packages);
        }
        return PackList;
    }

    public static boolean update(Package packages) throws SQLException {
        String sql = "UPDATE Package SET Description = ?, Price = ?, Qty = ? WHERE Package_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1,packages.getDescription());
        pstm.setObject(2,packages.getPrice());
        pstm.setObject(3,packages.getQty());
        pstm.setObject(4,packages.getPackageId());

        return pstm.executeUpdate() > 0;
    }

    public static boolean delete(String packageId) throws SQLException {
        String sql = "DELETE FROM Package WHERE Package_id = ?";

        Connection connection = DbConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, packageId);

        return pstm.executeUpdate() > 0;
    }

    public static Package searchByDescription(String desc) throws SQLException {
        String sql = "SELECT * FROM Package WHERE Description = ?";
        PreparedStatement pstm = DbConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setObject(1,desc);
        ResultSet resultSet = pstm.executeQuery();
        if(resultSet.next()){
            return new Package(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getDouble(3),
                    resultSet.getInt(4)
            );
        }
        return null;
    }

    public static List<String> getDescription() throws SQLException {
        String sql = "SELECT Description FROM Package";
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

    public static boolean updateNow(List<PackageDetails> pdList) throws SQLException {
        for (PackageDetails pd : pdList) {
            boolean isUpdateQty = updatePackage(pd.getPackage_id(),pd.getQty());
            if(!isUpdateQty) {
                return false;
            }
        }
        return true;
    }

    private static boolean updatePackage(String packageId, int qty) throws SQLException {
        String sql = "UPDATE Package SET Qty = Qty - ? WHERE Package_id = ?";

        PreparedStatement pstm = DbConnection.getInstance().getConnection()
                .prepareStatement(sql);

        pstm.setInt(1, qty);
        pstm.setString(2, packageId);

        return pstm.executeUpdate() > 0;
    }
}
