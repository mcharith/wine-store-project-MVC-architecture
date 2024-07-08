package lk.ijse.model;

import lombok.*;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Data
public class OrderDetail {
    private String Order_id;
    private String Item_code;
    private int qty;
    private double unitPrice;
}


