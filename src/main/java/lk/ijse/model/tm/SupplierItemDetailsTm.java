package lk.ijse.model.tm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierItemDetailsTm {
    private String item_code;
    private String description;
    private double unit_price ;
    private int qty_on_hand ;
    private String supplier_id;
}
