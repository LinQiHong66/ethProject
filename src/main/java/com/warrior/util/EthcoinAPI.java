/**
 * decodescript "hex"
 * dumpwallet "filename"
 * getaccount "bitcoinaddress"
 * getaccountaddress "account"
 * backupwallet "destination"
 * importwallet "filename"
 * validateaddress "bitcoinaddress"
 * settxfee amount
 * addnode "node" "add|remove|onetry"
 * createrawtransaction [{"txid":"id","vout":n},...] {"address":amount,...}
 * getaddednodeinfo dns ( "node" )
 * getbalance ( "account" minconf )
 * getblocktemplate ( "jsonrequestobject" )
 * getnetworkhashps ( blocks height )
 * getreceivedbyaccount "account" ( minconf )
 * getreceivedbyaddress "bitcoinaddress" ( minconf )
 * gettxout "txid" n ( includemempool )
 * getwork ( "data" )
 * help ( "command" )
 * importprivkey "bitcoinprivkey" ( "label" rescan )
 * keypoolrefill ( newsize )
 * listaccounts ( minconf )
 * listreceivedbyaccount ( minconf includeempty )
 * listsinceblock ( "blockhash" target-confirmations )
 * listtransactions ( "account" count from )
 * listunspent ( minconf maxconf ["address",...] )
 * lockunspent unlock [{"txid":"txid","vout":n},...]
 * move "fromaccount" "toaccount" amount ( minconf "comment" )
 * sendfrom "fromaccount" "tobitcoinaddress" amount ( minconf "comment" "comment-to" )
 * sendmany "fromaccount" {"address":amount,...} ( minconf "comment" )
 * sendtoaddress "bitcoinaddress" amount ( "comment" "comment-to" )
 * setaccount "bitcoinaddress" "account"
 * setgenerate generate ( genproclimit )
 * signmessage "bitcoinaddress" "message"
 * signrawtransaction "hexstring" ( [{"txid":"id","vout":n,"scriptPubKey":"hex","redeemScript":"hex"},...] ["privatekey1",...] sighashtype )
 * submitblock "hexdata" ( "jsonparametersobject" )
 * verifychain ( checklevel numblocks )
 * verifymessage "bitcoinaddress" "signature" "message"
 */
package com.warrior.util;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EthcoinAPI {
    private static Logger logger = LoggerFactory.getLogger(EthcoinAPI.class);

    public static Long GAS = 90000l;

    public static BigDecimal WEI = new BigDecimal("1000000000000000000");

    private String rpcuser;
    private String rpcpassword;
    private String rpcurl;
    private String rpcport;
    private String walletpassphrase;


    /**
     *
     */
    public EthcoinAPI() {

    }

    public EthcoinAPI(String rpcuser, String rpcpassword, String rpcurl, String rpcport, String walletpassphrase) {
        this.rpcuser = rpcuser;
        this.rpcpassword = rpcpassword;
        this.rpcurl = rpcurl;
        this.rpcport = rpcport;
        this.walletpassphrase = walletpassphrase;
    }

    private static final Charset QUERY_CHARSET = Charset.forName("ISO8859-1");

    @SuppressWarnings({"rawtypes", "unchecked", "serial"})
    private byte[] prepareRequest(final String method, final Object... params) {
        return JSON.toJSONString(new LinkedHashMap() {
            {
                put("jsonrpc", "1.0");
                put("id", "1");
                put("method", method);
                put("params", params);
            }
        }).getBytes(QUERY_CHARSET);
    }

    private String generateRequest(String method, Object... param) {

        String requestBody = new String(this.prepareRequest(method, param));
       // System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< requestBody： " + requestBody);
/*        if (method.equals("dumpprivkey"))
            logger.info("dumpprivkey so no logging");
        else if (method.equals("getblock"))
            logger.info("It's to long to log an entire block");
        else if (method.equals("walletpassphrasechange"))
            logger.info("walletpassphrasechange so no logging");
        else
            logger.info("请求参数：" + requestBody);*/

        final PasswordAuthentication temp = new PasswordAuthentication(this.rpcuser, this.rpcpassword.toCharArray());
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return temp;
            }
        });
        String uri = "http://" + this.rpcurl + ":" + this.rpcport;
        //logger.info("请求url:---------" + uri);

        String contentType = "application/json";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection(Proxy.NO_PROXY);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", contentType);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(requestBody.getBytes().length));
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(1000);
            OutputStream out = connection.getOutputStream();
            out.write(requestBody.getBytes());
            out.flush();
            out.close();
        } catch (Exception ioE) {
            connection.disconnect();
            // ioE.printStackTrace();
            //logger.info("connection error");
        }

        try {
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                String responseToString = response.toString();
                //logger.info("responseToString：" + responseToString);
                try {
                    JSONObject jsonObject = JSON.parseObject(responseToString);
                    String returnAnswer = jsonObject.get("result").toString();
                    //logger.info("响应结果：" + returnAnswer);
                    //System.out.println("响应结果：" + returnAnswer);

/*                    if (method.equals("dumpprivkey"))
                        logger.info("dumpprivkey so no logging");
                    else if (method.equals("getblock"))
                        logger.info("It's to long to log an entire block");
                    else if (method.equals("walletpassphrasechange"))
                        logger.info("walletpassphrasechange so no logging");
                    else
                        logger.info(returnAnswer);*/

                    return returnAnswer;
                } catch (Exception e) {
                    return "";
                }
            } else {
/*                System.out.println("Coudln't connet to Bitcoind!");
                logger.info("Coudln't connet to Bitcoind!");*/
                connection.disconnect();
            }
        } catch (Exception e) {
        }
     //   logger.info("Couldn't get a decent answer");
        return "";
    }

    /**
     * 获取余额
     *
     * @param address : 地址
     * @return
     */
    public BigDecimal getBalance(String address) {
        String returnAnswer = generateRequest("eth_getBalance", address, "latest");
        return returnAnswer.equals("") ? new BigDecimal(0) : EthcoinAPI.unit16To10(returnAnswer);
    }

    /**
     * 新建账户
     *
     * @param pwd : 密码
     * @return
     */
    public String newAccount(String pwd) {
        logger.info(pwd);
        String returnAnswer = generateRequest("personal_newAccount", pwd);
        logger.info(returnAnswer);
        return returnAnswer;
    }

    public boolean unlockAccount(String account, String pwd) {
        // 账户解锁
        String unlockAccountRes = generateRequest("personal_unlockAccount", account, pwd);
        logger.info(unlockAccountRes);
        return unlockAccountRes.equals("") ? false : true;
    }


    /**
     * 转账
     *
     * @param from  ： 转入账户
     * @param to    ： 转出账户
     * @param value ： 金额
     * @return
     */
    public String sendTransaction(String from, String to, String value) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("from", from);
        reqMap.put("to", to);
        reqMap.put("value", value);
        String returnAnswer = generateRequest("eth_sendTransaction", reqMap);
        logger.info(returnAnswer);
        return returnAnswer;
    }

    /**
     * 获取主账户
     *
     * @return
     */
    public String getMainaccount() {

        String returnAnswer = generateRequest("eth_coinbase");
        logger.info(returnAnswer);
        return returnAnswer;
    }


    /**
     * 获取所有账户
     *
     * @return
     */
    public String getAllAccounts() {
        String res = generateRequest("eth_accounts");
        return res;
    }


    /**
     * 获取所有账户
     *
     * @return
     */
    public String adminNodeInfo() {

        String res = generateRequest("admin_nodeInfo");
        return res;
    }

    /**
     * 获取所有账户
     *
     * @return
     */
    public String eth_getTransactionByHash(String hash) {

        String res = generateRequest("eth_getTransactionByHash", hash);
        return res;
    }

    public String eth_gasPrice() {
        String res = generateRequest("eth_gasPrice");
        return res;
    }

    public BigDecimal getGasAndGasPrice() {
        BigDecimal gas = new BigDecimal(GAS);
        BigDecimal gasPrice = new BigDecimal(0);
        String gasPriceStr = generateRequest("eth_gasPrice");
        if (gasPriceStr.equals("")) {
            gasPrice = new BigDecimal(18000000000l);
        } else {
            gasPriceStr = gasPriceStr.substring(gasPriceStr.indexOf("0x") + "0x".length());
            BigInteger transValue_16 = new BigInteger(gasPriceStr, 16);
            // 转成十进制bigDecimal
            gasPrice = new BigDecimal(transValue_16.toString(10));
        }
        return gas.multiply(gasPrice);
    }


    /**
     * 根据钱包地址获取交易记录
     *
     * @param address ： 钱包地址
     * @return
     */
/*	public List<Map<String, String>> getTrans(String address) {
        String responbody = HttpUtil.sendGet(
				"http://api.etherscan.io/api?module=account&action=txlist&address=" + address + "&sort=asc", null);
		Map<String, String> resultMap = (Map<String, String>) JSON.parseObject(responbody, Map.class);
		Object result = resultMap.get("result");
		if(result==null) {
			return null;
		}
		 
		List<Map<String, String>> trans = JSON.parseObject(result.toString(), List.class);
        return trans;
	}*/
    public static BigDecimal unit16To10(String num_16) {
        num_16 = num_16.substring(num_16.indexOf("0x") + "0x".length());
        String bigInteger = new BigInteger(num_16, 16).toString(10);
        return new BigDecimal(bigInteger);
    }

    public static String unit10To16(BigDecimal num_10) {
        String bigInteger = "0x" + new BigInteger(num_10.toString(), 10).toString(16);
        return bigInteger;
    }

    public BigDecimal getMinerFee() {
        BigDecimal gas = new BigDecimal(GAS);
        BigDecimal gasPrice = new BigDecimal(0);
        String gasPriceStr = generateRequest("eth_gasPrice");
        gasPrice = gasPriceStr.equals("") ? new BigDecimal(18000000000l) : EthcoinAPI.unit16To10(gasPriceStr);
        return gas.multiply(gasPrice);
    }

    public BigDecimal eth_blockNumber() {
        BigDecimal bigDecimal = BigDecimal.ZERO;
        String res = generateRequest("eth_blockNumber");
        if (!res.equals("")) {
            bigDecimal = EthcoinAPI.unit16To10(res);
        }
        return bigDecimal;
    }

    public String eth_syncing() {
        String res = generateRequest("eth_syncing");
        return res;
    }

    public String eth_getBlockByNumber(Integer blockHeight) {
        String bigInteger = "0x" + new BigInteger(blockHeight.toString(), 10).toString(16);
        String res = generateRequest("eth_getBlockByNumber", bigInteger, false);
        return res;
    }

    //是否为公链
    public Boolean isPublicChain() {
        try {
            String res = generateRequest("eth_getBlockByNumber", "earliest", false);
            Map resultMap = JSON.parseObject(res, Map.class);
            String hash = resultMap.get("hash").toString();
            if (hash.equalsIgnoreCase("0xd4e56740f876aef8c010b86a40d5f56745a118d0906a34e69aec8c0db1cb8fa3")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

}
