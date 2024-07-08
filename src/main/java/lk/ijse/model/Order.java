package lk.ijse.model;

import lombok.*;

import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Setter
@Getter
@Data
@ToString
public class Order {
    private String Order_id;
    private String Customer_id;
    private Date Date;
}
