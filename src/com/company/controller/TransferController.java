package com.company.controller;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/transfer")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;
    private TransferDao transferDao;

    public TransferController(JdbcUserDao user, JdbcAccountDao account, JdbcTransferDao transferDao) {
        this.userDao = user;
        this.accountDao = account;
        this.transferDao = transferDao;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public void create(@Valid @RequestBody Transfers transfers, Principal principal){
        String loggedInUser = principal.getName();
        int userId = userDao.findIdByUsername(loggedInUser);
        int accountId = accountDao.getAccountId(userId);
        transferDao.createTransfer(transfers, accountId);


    }

    @RequestMapping ( method = RequestMethod.GET)
    public List transfers (Principal principal){

        String loggedInUser = principal.getName();
        int id = userDao.findIdByUsername(loggedInUser);
        return transferDao.getListOfTransfers(id);

    }
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public Transfers anyTransfer (@PathVariable int id){
        return transferDao.getAnyTransfer(id);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/request", method = RequestMethod.POST)
    public void createRequest(@Valid @RequestBody Transfers transfers, Principal principal){
        String loggedInUser = principal.getName();
        int userId = userDao.findIdByUsername(loggedInUser);
        int accountId = accountDao.getAccountId(userId);
        transferDao.createRequest(transfers, accountId);

    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @RequestMapping(path = "/response", method = RequestMethod.PUT)
    public void respond(@Valid @RequestBody Transfers transfers, Principal principal){
        String loggedInUser = principal.getName();
        int userId = userDao.findIdByUsername(loggedInUser);
        int accountId = accountDao.getAccountId(userId);
        transferDao.updateTransfer(transfers, accountId);
    }
    @RequestMapping(path = "/response/pending", method = RequestMethod.GET)
    public List pendingTransfers(Principal principal ){
        String loggedInUser = principal.getName();
        int id = userDao.findIdByUsername(loggedInUser);
        return transferDao.getPendingTransactions(id);
    }

}
