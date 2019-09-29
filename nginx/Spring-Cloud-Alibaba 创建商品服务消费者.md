---
title: Spring-Cloud-Alibaba 创建商品服务消费者
---

# Spring-Cloud-Alibaba 创建商品服务消费者
## POM
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.funtl</groupId>
        <artifactId>myshop-dependencies</artifactId>
        <version>1.0.0-SNAPSHOT</version>
        <relativePath>../myshop-dependencies/pom.xml</relativePath>
    </parent>

    <artifactId>myshop-service-consumer-item</artifactId>
    <packaging>jar</packaging>

    <name>myshop-service-consumer-item</name>
    <url>http://www.funtl.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <!-- Spring Boot Begin -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!-- Spring Boot End -->

        <!-- Spring Cloud Begin -->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!-- Spring Cloud End -->

        <!-- Projects Begin -->
        <dependency>
            <groupId>com.funtl</groupId>
            <artifactId>myshop-commons-consumer</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
        <!-- Projects End -->
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.funtl.myshop.service.consumer.item.MyShopServiceConsumerItemApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
## Application
```
package com.funtl.myshop.service.consumer.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = "com.funtl.myshop", exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableSwagger2
@EnableFeignClients
public class MyShopServiceConsumerItemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceConsumerItemApplication.class, args);
    }
}
```
## Service
```
package com.funtl.myshop.service.consumer.item.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "myshop-service-provider-item")
public interface TbItemService {

    @GetMapping(value = "/item/page/{num}/{size}")
    String page(@PathVariable(name = "num") int pageNum, @PathVariable(name = "size") int pageSize);
}
```
## Controller
```
package com.funtl.myshop.service.consumer.item.controller;

import com.fasterxml.jackson.databind.JavaType;
import com.funtl.myshop.commons.domain.TbItem;
import com.funtl.myshop.commons.dto.AbstractBaseResult;
import com.funtl.myshop.commons.utils.MapperUtils;
import com.funtl.myshop.commons.web.AbstractBaseController;
import com.funtl.myshop.service.consumer.item.service.TbItemService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping(value = "item")
public class TbItemController extends AbstractBaseController<TbItem> {

    @Autowired
    private TbItemService tbItemService;

    @ApiOperation(value = "商品分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "num", value = "页码", required = true, paramType = "path", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "笔数", required = true, paramType = "path", dataType = "int")
    })
    @GetMapping(value = "page/{num}/{size}")
    public AbstractBaseResult page(@PathVariable int num, @PathVariable  int size) {
        String json = tbItemService.page(num, size);
        try {
            JavaType javaType = MapperUtils.getCollectionType(PageInfo.class, TbItem.class);
            PageInfo<TbItem> pageInfo = MapperUtils.getInstance().readValue(json, javaType);
            return success(request.getRequestURI(), pageInfo.getNextPage(), pageInfo.getPages(), pageInfo.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
```
## properties
```
spring.application.name=myshop-service-consumer-item-config
spring.cloud.nacos.config.file-extension=yaml
spring.cloud.nacos.config.server-addr=192.168.10.151:8848
```
## YAML
```
spring:
  application:
    name: myshop-service-consumer-item
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.10.151:8848
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.10.151:8080

server:
  port: 10205

management:
  endpoints:
    web:
      exposure:
        include: "*"
```