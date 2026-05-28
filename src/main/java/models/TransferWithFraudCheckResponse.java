package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferWithFraudCheckResponse extends BaseModel {

    private String status;
    private String message;
    private double amount;
    private long senderAccountId;
    private long receiverAccountId;
    private double fraudRiskScore;
    private String fraudReason;
    private boolean requiresManualReview;
    private boolean requiresVerification;
}