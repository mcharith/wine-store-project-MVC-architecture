package lk.ijse.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class Register {
    private String User_id;
    private String First_name;
    private String Last_name;
    private String Email;
    private String Password;
}
