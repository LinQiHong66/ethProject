import com.alibaba.fastjson.JSON;
import com.warrior.util.EthcoinAPI;
import com.warrior.util.HttpUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * @author: lqh
 * @description: 获取开了某个端口的主机
 * @program: ETH
 * @create: 2018-05-05 17:43
 **/
public class GetServerIp {

    //final static EthcoinAPI eth = new EthcoinAPI("", "", "52.0.75.110", "8545", "");

    final static EthcoinAPI eth = new EthcoinAPI("193.112.77.111", "8545", "", "0", "1");
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int a = 256;
        int b = 256;
        int c = 256;
        int d = 256;
        int i = 0;
        for (int k1 = 0; k1 < a; k1++) {
            for (int k2 = 0; k2 < b; k2++) {
                for (int k3 = 0; k3 < c; k3++) {
                    for (int k4 = 0; k4 < d; k4++) {
                        i++;
                        System.out.println(i + "--" + k1 + "." + k2 + "." + k3 + "." + k4);
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("time:" + (end - start));
    }

    @Test
    public void test1() {
        HttpUtil.sendGet("localhost:666/warrior/server/getCoinHost", null);
    }


    @Test
    public void createAccount() {
        long t1 = System.currentTimeMillis();
        System.out.println("address： " + eth.newAccount("123456789"));
        long t2 = System.currentTimeMillis();
        System.out.println("time:" + (t2 - t1));
    }

    @Test
    public void getBalance() {
        BigDecimal balance = eth.getBalance("0xfd502a8e20cb597a3b20e376ed94aa7abfa631ab");
        System.out.println("balance:" + balance.divide(EthcoinAPI.WEI));
    }


    @Test
    public void getAllAccounts() {
        String allAccount = eth.getAllAccounts();
        List<String> accountList = JSON.parseObject(allAccount, List.class);
        System.out.println("accountList" + accountList);
    }

    @Test
    public void blockNumber() {
        String blockNumber = eth.eth_blockNumber();
        System.out.println("blockNumber:" + blockNumber);
    }

    @Test
    public void eth_syncing() {
        String eth_syncing = eth.eth_syncing();
        System.out.println("eth_syncing:" + eth_syncing);
    }


    @Test
    public void xx(){
      String address = eth.newAccount("1");
        System.out.println("address:"+address);
    }

}
