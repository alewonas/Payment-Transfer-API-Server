package com.company.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class jdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate ){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTransfer(Transfers transfers, int accountFromId){
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao(jdbcTemplate);
        Account account = jdbcAccountDao.getAccount(accountFromId);

        if (transfers.getTransferTypeId() == 2 && (account.getBalance().compareTo(transfers.getAmount()) == 1
                || account.getBalance().compareTo(transfers.getAmount()) == 0)){
            String sql = "INSERT INTO transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                    " VALUES (?, ?, ?, ?, ?);";
            jdbcTemplate.update(sql, transfers.getTransferTypeId(), transfers.getTransferStatusId(), accountFromId, transfers.getAccountTo(), transfers.getAmount());
            updateBalanceSent(accountFromId, transfers.getAmount());
            updateBalanceReceived(transfers.getAccountTo(), transfers.getAmount());
        }
        else {
            throw new RuntimeException("Not a valid option");
        }


    }
    public void updateBalanceSent(int id, BigDecimal amount){
        String sql = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, id);

    }
    public void updateBalanceReceived(int id, BigDecimal amount){
        String sql = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, id);

    }

    @Override
    public List<Transfers> getListOfTransfers(int accountFromId){
        List<Transfers> transfersList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                "FROM tenmo_user AS tu " +
                "JOIN account AS a ON tu.user_id = a.user_id " +
                "JOIN transfer AS t ON a.account_id = t.account_from " +
                "WHERE (t.account_from IN (SELECT account_id FROM account WHERE user_id =? ) " +
                "OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?));";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountFromId, accountFromId);
        while (results.next()){
            transfersList.add(mapToRowSet(results));
        }
        return transfersList;
    }
    @Override
    public List<Transfers>  getPendingTransactions(int accountFromId){
        List<Transfers> pendingTransfersList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                "FROM tenmo_user AS tu " +
                "JOIN account AS a ON tu.user_id = a.user_id " +
                "JOIN transfer AS t ON a.account_id = t.account_from " +
                "WHERE (t.account_from IN (SELECT account_id FROM account WHERE user_id =? ) " +
                "OR t.account_to IN (SELECT account_id FROM account WHERE user_id = ?)) AND t.transfer_status_id = ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountFromId, accountFromId,  STATUS_PENDING);
        while (rowSet.next()){
            pendingTransfersList.add((mapToRowSet(rowSet)));
        }
        return pendingTransfersList;
    }

    @Override
    public Transfers getAnyTransfer(int transferId){
        Transfers transfers = new Transfers();
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()){
            transfers = mapToRowSet(results);
        }
        return transfers;
    }

    @Override
    public void createRequest(Transfers transfers, int accountFromId){
        if (transfers.getTransferTypeId() == 1){
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (?, ?, ?, ?, ?);";
            jdbcTemplate.update(sql, transfers.getTransferTypeId(), STATUS_PENDING, accountFromId, transfers.getAccountTo(), transfers.getAmount());
        }
    }



    @Override
    public void updateTransfer(Transfers transfers, int accountToId){
        JdbcAccountDao jdbcAccountDao = new JdbcAccountDao(jdbcTemplate);
        Account account = jdbcAccountDao.getAccount(accountToId);

        if (transfers.getTransferStatusId() == 2 && accountToId == transfers.getAccountTo() &&
                (account.getBalance().compareTo(transfers.getAmount()) == 1 || account.getBalance().compareTo(transfers.getAmount()) == 0)){
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ? AND account_to = ?;";
            jdbcTemplate.update(sql, STATUS_APPROVED, transfers.getTransferId(), accountToId);
            updateBalanceSent(accountToId, transfers.getAmount());
            int id = transfers.getAccountFrom();
            updateBalanceReceived(id, transfers.getAmount());
        } else if (transfers.getTransferStatusId() == 3){
            String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ? AND account_to = ?;";
            jdbcTemplate.update(sql, transfers.getTransferStatusId(), transfers.getTransferId(), accountToId);

        }  else {
            throw new RuntimeException("Not a valid option");
        }

    }

    private Transfers mapToRowSet(SqlRowSet rowSet){
        Transfers transfers = new Transfers();
        transfers.setTransferId(rowSet.getInt("transfer_id"));
        transfers.setAccountFrom(rowSet.getInt("account_from"));
        transfers.setAccountToId(rowSet.getInt("account_to"));
        transfers.setTransferAmount(rowSet.getBigDecimal("amount"));
        transfers.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfers.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        return transfers;
    }

}
