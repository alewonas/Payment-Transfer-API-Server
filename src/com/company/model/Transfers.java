package com.company.model;

import java.math.BigDecimal;

public class Transfers {

    private int transferId;
    private int accountFrom;
    private int accountTo;
    @Positive(message = "Cannot be a negative or zero amount")
    private BigDecimal amount;
    private int transferTypeId;
    private int transferStatusId;
    public static final int STATUS_APPROVED = 2;
    public static final int STATUS_PENDING = 1;


    public Transfers (int transferId, int accountFrom, int accountToId, BigDecimal transferAmount, int transferTypeId, int transferStatusId){
        this.transferId = transferId;
        this.accountFrom = accountFrom;
        this.accountTo = accountToId;
        this.amount = transferAmount;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;

    }
    public Transfers(){

    }

    public void sendTransfer(int teBucks){

    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountToId(int accountToId) {
        this.accountTo = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.amount = transferAmount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

}
