package models.lombok.register;

import lombok.Data;

@Data
public class RegisterUserResponseLombokModel {

    private String id;
    private String token;
    private String error;
}
