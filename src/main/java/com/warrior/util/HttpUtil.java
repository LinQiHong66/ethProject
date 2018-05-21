package com.warrior.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSON;

import jodd.http.HttpConnection;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.http.net.SocketHttpConnection;

/**
 * @author: lqh
 * @description: jodd HttpUtil
 * @program: ETH
 * @create: 2018-05-09 17:22
 **/
public class HttpUtil {


    /**
     * 发送Get请求
     *
     * @param url    : 请求的连接
     * @param params ： 请求参数，无参时传null
     * @return
     */
    public static String sendGet(String url, Map<String, String> params) {
        HttpRequest request = HttpRequest.get(url);
        if (params != null) {
            request.query(params);
        }
        HttpResponse response = request.send();
        String respJson = response.bodyText();
        return respJson;
    }


    /**
     * 发送Post请求-json数据
     *
     * @param url    : 请求的连接
     * @param params ：  请求参数，无参时传null
     * @return
     */
    public static String sendPostToJson(String url, Map<String, Object> params) {
        HttpRequest request = HttpRequest.post(url);
        request.contentType("application/json");
        request.charset("utf-8");

        //参数详情
        if (params != null) {
            request.body(JSON.toJSONString(params));
        }

        HttpResponse response = request.send();
        String respJson = response.bodyText();
        return respJson;
    }

    /**
     * 发送Post请求
     *
     * @param url           : 请求的连接
     * @param params        ：  请求参数，无参时传null
     * @param paramsDatails : 参数详情，没有时传null
     * @return
     */
    public static String sendPost(String url, Map<String, Object> params) {
        HttpRequest request = HttpRequest.post(url);

        //参数详情
        if (params != null) {
            request.form(params);
        }
        HttpResponse response = request.send();
        String respJson = response.bodyText();
        return respJson;
    }


    /**
     * 发送Delete请求
     *
     * @param url    : 请求的连接
     * @param params ：  请求参数，无参时传null
     * @return
     */
    public static String sendDelete(String url, Map<String, Object> params) {
        HttpRequest request = HttpRequest.delete(url);

        if (params != null) {
            request.form(params);
        }
        HttpResponse response = request.send();
        String respJson = response.bodyText();
        return respJson;
    }


    public static void main(String[] args) {

    }


    public static boolean telnet(String server, Integer port) {
        Socket target = null;
        boolean result = true;
        try {
            target = new Socket();
            InetSocketAddress address = new InetSocketAddress(server, port);
            target.connect(address, 1000);
        } catch (UnknownHostException e) {
            result = false;
        } catch (IOException e) {
            result = false;
        } finally {
            if (target != null)
                try {
                    target.close();
                } catch (IOException e) {

                }
        }
        return result;
    }

    public static boolean ping(String server) {
        try {
            InetAddress address = InetAddress.getByName(server);
            return address.isReachable(5000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}