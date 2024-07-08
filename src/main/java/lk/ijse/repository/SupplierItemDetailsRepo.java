package lk.ijse.repository;

import lk.ijse.db.DbConnection;
import lk.ijse.model.SupplierItemDetails;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierItemDetailsRepo {
    public static List<SupplierItemDetails> getAllSupplierItemDetails() throws SQLException {
        String sql = "SELECT * FROM Supplier_item_details";
        ResultSet resultSet = DbConnection.getInstance()
                .getConnection()
                .prepareStatement(sql)
                .executeQuery();

        List<SupplierItemDetails> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(
                    SupplierItemDetails.builder()
                            .supplier_id(resultSet.getString(1))
                            .item_code(resultSet.getString(2))
                            .build()
            );
        }
        return list;
    }
}
