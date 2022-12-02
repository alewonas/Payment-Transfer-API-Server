package com.company.controller;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserAccountsController {

    private UserDao userDao;
    private AccountDao accountDao;

    public UserAccountsController(JdbcUserDao userDao, JdbcAccountDao accountDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
    }
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal){
        String loggedInUser = principal.getName();
        int id = userDao.findIdByUsername(loggedInUser);
        BigDecimal balance = accountDao.getUserBalance(id);

        return balance;
    }
    @RequestMapping ( method = RequestMethod.GET)
    public List users (Principal principal){
        String loggedInUser = principal.getName();
        int id = userDao.findIdByUsername(loggedInUser);
        return userDao.getListOfUsers(id);

    }

}
