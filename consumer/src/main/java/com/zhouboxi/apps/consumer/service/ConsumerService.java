package com.zhouboxi.apps.consumer.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConsumerService {
    @Autowired
    RestTemplate restTemplate;

    //断路由配置，当无法调用下面方法后自动调用consumerError方法
    @HystrixCommand(fallbackMethod="consumerError")
    public Map<String, String> consumerService(String name) {
        //调用生产者
        Map<String, String> map = new HashMap<>();
        map.put("object",restTemplate.getForObject("http://PROVIDER/provider?name=" + name, Object.class).toString());
        map.put("user",restTemplate.getForEntity("http://PROVIDER/provider?name=" + name, Object.class).toString());
        return map;
    }

    public Map<String, String> consumerError(String name){
        Map<String, String> map = new HashMap<>();
        map.put("object",name+"操作异常，自动调该方法！");
        map.put("user",name+"操作异常，自动调该方法！");
        return map;
    }
}
