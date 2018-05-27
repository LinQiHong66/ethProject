package com.warrior.controler;

import com.warrior.entity.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lqh
 * @since 2018-05-18
 */
@Controller
@RequestMapping("/warrior/log")
public class LogController {
    @Value("${server.username}")
    public String username;

    @Value("${server.password}")
    public String password;


    @RequestMapping("readConfig")
    @ResponseBody
    public Map readConfig() {
        LinkedHashMap resultMap = new LinkedHashMap();
        resultMap.put("username", username);
        resultMap.put("password", password);
        Log log = new Log();
        log.setDate(new Date())
                .setLogContent("xxdfsdfs")
                .setLogName("lin") ;
        log.insert();
        return resultMap;
    }
}
