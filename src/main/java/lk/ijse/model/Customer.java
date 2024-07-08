package lk.ijse.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
public class Customer {
    private String cusId;
    private String cusName;
    private String cusAddress;
    private String cusNum;
}
