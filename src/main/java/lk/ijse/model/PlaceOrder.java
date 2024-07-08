package lk.ijse.model;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class PlaceOrder {
    private Order order;
    private List<OrderDetail> odList;
}
