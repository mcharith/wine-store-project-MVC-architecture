package lk.ijse.model.tm;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class ItemTm {
    private String Item_code;
    private String Description;
    private double unit_price ;
    private int qty_on_hand ;
    private double buying_price;
    private String Supplier_id;
}
