package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    Transfer createTransfer(Transfer transfer);

    Transfer getTransferByTransferId(int id);

    List<Transfer> getTransferListByUserId(int id);

    //List<Transfer> getTransferListByUserId(int id);



}
