package com.zhouboxi.apps.provider;

import com.zhouboxi.apps.provider.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class ProviderApplication {
    /**
     * @EnableEurekaClient 声明是一个生产者
     * @RestController  声明为一个控制器
     */
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    /**
     * 读取配置文件
     */
    @Value("${spring.application.name}")
    String appName;

    @RequestMapping("/provider")
    public Object home(@RequestParam String name){
        Map<String, Object> map = new HashMap<>();
        User user = new User("1", "zhouboxi");
        map.put("str",name + "正在访问名为" + appName +"的应用！");
        map.put("user",user);
        return map;
    }

}
