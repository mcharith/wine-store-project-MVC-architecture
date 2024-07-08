package lk.ijse.model;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PlacePackage {
    private Order order;
    private List<PackageDetails> pdList;
}
