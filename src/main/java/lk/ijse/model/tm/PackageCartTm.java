package lk.ijse.model.tm;

import com.jfoenix.controls.JFXButton;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PackageCartTm {
    private String Id;
    private String description;
    private int qty;
    private double price;
    private double total;
    private JFXButton btnRemove;
}
