package com.company.dao;

import java.math.BigDecimal;

@Component
public class jdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao( JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;

    }

    @Override
    public BigDecimal getUserBalance(int userId){
        Account account;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            account = mapRowToAccount(results);
            return account.getBalance();
        }
        return null;
    }

    @Override
    public Account getAccount(int accountId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);

        }
        return account;
    }

    @Override
    public int getAccountId(int userId){
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            account = mapRowToAccount(results);
            return account.getAccountId();
        }

        return account.getAccountId();

    }


    private Account mapRowToAccount(SqlRowSet rs){
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }

}
