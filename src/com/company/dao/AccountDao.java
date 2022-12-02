package com.company.dao;

import java.math.BigDecimal;

public interface AccountDao {

    BigDecimal getUserBalance(int user_id);

    Account getAccount(int accountId);

    int getAccountId(int userId);

}
