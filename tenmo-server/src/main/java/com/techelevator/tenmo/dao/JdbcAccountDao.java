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
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> listAll(int userId) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, tenmo_user.user_id, username, balance " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE account.user_id <> ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            Account account = mapRowToAccount(results);
            accounts.add(account);
        }
        return accounts;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = null;
        String sql = "SELECT account_id, tenmo_user.user_id, username, balance " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE account.user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountByAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, tenmo_user.user_id, username, balance " +
                "FROM account " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "WHERE account.account_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public void updateBalances(Transfer transfer) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?;";
        jdbcTemplate.update(sql, (transfer.getTransferAmount() * -1), transfer.getAccountFrom().getId());
        jdbcTemplate.update(sql, transfer.getTransferAmount(), transfer.getAccountTo().getId());
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        account.setId(rs.getInt("account_id"));
        account.setUser(user);
        account.setBalance(rs.getDouble("balance"));
        return account;
    }
}
