package com.company.dao;

import java.util.List;

public interface TransferDao {

    void createTransfer(Transfers transfers, int accountFromId);
    List<Transfers> getListOfTransfers(int accountFromId);
    Transfers getAnyTransfer(int transferId);
    void createRequest(Transfers transfers, int accountFromId);
    void updateTransfer(Transfers transfers, int accountToId);
    List<Transfers> getPendingTransactions(int accountFromId);
}
