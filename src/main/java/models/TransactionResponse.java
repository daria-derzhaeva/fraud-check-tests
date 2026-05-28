package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionResponse extends BaseModel {

    private long id;
    private double amount;
    private String type;
    private String timestamp;
    private Long relatedAccountId;
}