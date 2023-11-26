package dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
    String userId;
    String role;
    String rut;
    String email;
}
