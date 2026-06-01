package iteration2.api.fixtures;

import database.dao.AccountDao;
import models.AccountResponse;
import requests.steps.UserSteps;

public class FraudTransferFixture {

    private final UserSteps senderSteps;
    private final UserSteps receiverSteps;
    private final AccountResponse senderAccount;
    private final AccountResponse receiverAccount;
    private final AccountDao senderBeforeTransfer;
    private final AccountDao receiverBeforeTransfer;

    public FraudTransferFixture(UserSteps senderSteps,
                                UserSteps receiverSteps,
                                AccountResponse senderAccount,
                                AccountResponse receiverAccount,
                                AccountDao senderBeforeTransfer,
                                AccountDao receiverBeforeTransfer) {
        this.senderSteps = senderSteps;
        this.receiverSteps = receiverSteps;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.senderBeforeTransfer = senderBeforeTransfer;
        this.receiverBeforeTransfer = receiverBeforeTransfer;
    }

    public UserSteps getSenderSteps() {
        return senderSteps;
    }

    public UserSteps getReceiverSteps() {
        return receiverSteps;
    }

    public AccountResponse getSenderAccount() {
        return senderAccount;
    }

    public AccountResponse getReceiverAccount() {
        return receiverAccount;
    }

    public AccountDao getSenderBeforeTransfer() {
        return senderBeforeTransfer;
    }

    public AccountDao getReceiverBeforeTransfer() {
        return receiverBeforeTransfer;
    }
}