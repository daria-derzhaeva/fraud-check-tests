package database.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDao {

    private long id;
    private String accountNumber;
    private double balance;
    private long customerId;
}