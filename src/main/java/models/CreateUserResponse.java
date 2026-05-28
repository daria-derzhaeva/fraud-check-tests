package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserResponse extends BaseModel {

    private int id;
    private String username;
    private String password;
    private String name;
    private String role;
}