package lk.ijse.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class Item {
    private String Code;
    private String Description;
    private double unit_price ;
    private int qty_on_hand ;
    private double buying_price;
    private String Supplier_id;
}
