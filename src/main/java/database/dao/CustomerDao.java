package database.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDao {

    private long id;
    private String username;
    private String password;
    private String name;
    private String role;
}