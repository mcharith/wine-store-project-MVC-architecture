package lk.ijse.model.tm;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class StaffTm {
    private String Staff_id;
    private String Name;
    private String Address;
    private String Age;
    private String Contact_number;
    private String Job_Role;
}