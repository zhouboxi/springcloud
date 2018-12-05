package com.zhouboxi.apps.consumer.controller;

import com.zhouboxi.apps.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    @RequestMapping(value = "/consumer")
    public Map<String, String> consumer(@RequestParam String name) {
        Map<String, String> map = consumerService.consumerService(name);
        return map;
    }
}
