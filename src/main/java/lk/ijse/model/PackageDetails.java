package lk.ijse.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
@Data
public class PackageDetails {
    private String Order_id;
    private String Package_id;
    private int qty;
    private double unitPrice;
}
