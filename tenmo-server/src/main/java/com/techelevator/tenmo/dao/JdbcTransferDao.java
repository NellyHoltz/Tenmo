package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Transfer createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?, ?, ?, ?, ?) RETURNING transfer_id;";
        Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getTransferTypeId(), transfer.getTransferStatusId(),
                transfer.getAccountFrom().getId(), transfer.getAccountTo().getId(), transfer.getTransferAmount());

       return getTransferByTransferId(newTransferId);
    }

    @Override
    public Transfer getTransferByTransferId(int id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, amount, af.account_id AS from_account_id, af.user_id AS from_user_id, uf.username AS from_username, af.balance AS from_balance, at.account_id AS to_account_id, at.user_id AS to_user_id, ut.username AS to_username, at.balance AS to_balance " +
                "FROM transfer " +
                "JOIN account AS af ON transfer.account_from = af.account_id " +
                "JOIN tenmo_user AS uf ON af.user_id = uf.user_id " +
                "JOIN account AS at ON transfer.account_to = at.account_id " +
                "JOIN tenmo_user AS ut ON at.user_id = ut.user_id " +
                "WHERE transfer_id = ?";
        Transfer newTransfer = null;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
        if (results.next()) {
           newTransfer = mapRowToTransfer(results);
        }
        return newTransfer;
    }



    @Override
    public List<Transfer> getTransferListByUserId(int id) {
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, amount, af.account_id AS from_account_id, af.user_id AS from_user_id, uf.username AS from_username, af.balance AS from_balance, at.account_id AS to_account_id, at.user_id AS to_user_id, ut.username AS to_username, at.balance AS to_balance " +
                "FROM transfer " +
                "JOIN account AS af ON transfer.account_from = af.account_id " +
                "JOIN tenmo_user AS uf ON af.user_id = uf.user_id " +
                "JOIN account AS at ON transfer.account_to = at.account_id " +
                "JOIN tenmo_user AS ut ON at.user_id = ut.user_id " +
                "WHERE uf.user_id = ? OR ut.user_id = ?;";
        List<Transfer> transfers = new ArrayList<>();
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            transfers.add(transfer);

        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        Account toAccount = new Account();
        Account fromAccount = new Account();
        User toUser = new User();
        User fromUser = new User();
        toUser.setId(rs.getInt("to_user_id"));
        toUser.setUsername(rs.getString("to_username"));
        fromUser.setId(rs.getInt("from_user_id"));
        fromUser.setUsername(rs.getString("from_username"));
        toAccount.setId(rs.getInt("to_account_id"));
        toAccount.setUser(toUser);
        toAccount.setBalance(rs.getDouble("to_balance"));
        fromAccount.setId(rs.getInt("from_account_id"));
        fromAccount.setBalance(rs.getDouble("from_balance"));
        fromAccount.setUser(fromUser);
        transfer.setTransferId(rs.getInt("transfer_id"));
        transfer.setTransferTypeId(rs.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rs.getInt("transfer_status_id"));
        transfer.setAccountFrom(fromAccount);
        transfer.setAccountTo(toAccount);
        transfer.setTransferAmount(rs.getDouble("amount"));
        return transfer;
    }

}
