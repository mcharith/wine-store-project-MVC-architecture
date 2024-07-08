package lk.ijse.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@EqualsAndHashCode
public class Package {
    private String packageId;
    private String Description;
    private  double Price;
    private int Qty ;
}
