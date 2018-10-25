package com.warrior;



import com.warrior.controler.ServerController;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@SpringBootApplication
@MapperScan("com.warrior.mapper")  //配置mapper扫描
@EnableScheduling
public class Application   {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }


}