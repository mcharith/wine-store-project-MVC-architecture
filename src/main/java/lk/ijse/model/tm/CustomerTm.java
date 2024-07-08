package lk.ijse.model.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
@EqualsAndHashCode
public class CustomerTm {
    private String cusId;
    private String cusName;
    private String cusAddress;
    private String cusNum;
}
