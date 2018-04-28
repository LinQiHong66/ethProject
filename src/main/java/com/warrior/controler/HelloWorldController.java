package com.warrior.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author: lqh
 * @description:
 * @program: ETH
 * @create: 2018-04-28 15:23
 **/
@Controller
public class HelloWorldController {
    @RequestMapping("/")
    @ResponseBody
    String hello() {
        return "Hello World!";
    }
}
