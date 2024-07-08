package lk.ijse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Staff {
    private String Staff_id;
    private String Name;
    private String Address;
    private String Age;
    private String Contact_number;
    private String Job_Role;
}
