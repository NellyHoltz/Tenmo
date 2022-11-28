package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface AccountDao {
    List<Account> listAll(int userId);

    Account getAccountByUserId(int userId);

    void updateBalances(Transfer transfer);

    Account getAccountByAccountId(int accountId);

}
