package models;

import lombok.Getter;

@Getter
public class LoginUserResponse extends BaseModel {

    private String role;
    private String username;
}