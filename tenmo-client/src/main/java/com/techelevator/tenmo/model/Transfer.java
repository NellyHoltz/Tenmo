package com.techelevator.tenmo.model;

public class Transfer {
    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private Account accountFrom;
    private Account accountTo;
    private double transferAmount;


    public Transfer() {
    }

    public Transfer( int transferTypeId, int transferStatusId, Account accountFrom, Account accountTo, double transferAmount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
    }

    public Transfer(int transferId, int transferTypeId, int transferStatusId, Account accountFrom, Account accountTo, double transferAmount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
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

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }


    public String toSummaryString() {
        return String.format("%-5s %10s %10s %15s%n", getTransferId(), getAccountFrom().getUser().getUsername(), getAccountTo().getUser().getUsername(), getTransferAmount());
    }

    @Override
    public String toString() {
        String transferTypeName = null;
        String transferStatusName = null;

        switch (transferTypeId) {
            case 1: transferTypeName = "Request"; break;
            case 2: transferTypeName = "Sent"; break;
        }

        switch (transferStatusId) {
            case 1: transferStatusName = "Pending"; break;
            case 2: transferStatusName = "Approved"; break;
            case 3: transferStatusName = "Rejected"; break;
        }

        return "transferId: " + transferId +
                "\nTransfer Type: " + transferTypeName +
                "\nTransfer Status: " + transferStatusName +
                "\nSent From: " + accountFrom.getUser().getUsername() +
                "\nSent To: " + accountTo.getUser().getUsername() +
                "\nAmount: " + transferAmount;
    }
}
