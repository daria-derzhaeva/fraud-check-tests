package models;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DepositMoneyRequest extends BaseModel {

    private long accountId;
    private double amount;
}