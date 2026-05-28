package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
@JsonIgnoreProperties(value = {"transactions"}, ignoreUnknown = true)
public class AccountResponse extends BaseModel {

    private long id;
    private String accountNumber;
    private double balance;

    private List<TransactionResponse> transactions = Collections.emptyList();
}