package lk.ijse.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class ItemDetails {
    private String Supplier_id;
    private String Item_code;
    private int Qty;
    private double unitPrice;
}
