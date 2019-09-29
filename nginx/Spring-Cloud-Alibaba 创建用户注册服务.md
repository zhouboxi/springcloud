---
title: Spring-Cloud-Alibaba 创建用户注册服务
---

# Spring-Cloud-Alibaba 创建用户注册服务
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

    <artifactId>myshop-service-reg</artifactId>
    <packaging>jar</packaging>

    <name>myshop-service-reg</name>
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
        <!-- Spring Cloud End -->

        <!-- Projects Begin -->
        <dependency>
            <groupId>com.funtl</groupId>
            <artifactId>myshop-commons-service</artifactId>
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
                    <mainClass>com.funtl.myshop.service.reg.MyShopServiceRegApplication</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```
## Application
```
package com.funtl.myshop.service.reg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.funtl.myshop.commons.mapper")
public class MyShopServiceRegApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyShopServiceRegApplication.class, args);
    }
}
```
### Controller
```
package com.funtl.myshop.service.reg.controller;

import com.funtl.myshop.commons.domain.TbUser;
import com.funtl.myshop.commons.mapper.TbUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "reg")
public class RegController {

    @Autowired
    private TbUserMapper tbUserMapper;

    /**
     * 根据 ID 测试查询用户信息
     * @param id
     * @return
     */
    @GetMapping(value = {"{id}"})
    public String reg(@PathVariable long id) {
        TbUser tbUser = tbUserMapper.selectByPrimaryKey(id);
        return tbUser.getUsername();
    }
}
```
## 配置文件
```
bootstrap.properties
spring.application.name=myshop-service-reg-config
spring.cloud.nacos.config.file-extension=yaml
spring.cloud.nacos.config.server-addr=192.168.10.151:8848
bootstrap-prod.properties
spring.profiles.active=prod
spring.application.name=myshop-service-reg-config
spring.cloud.nacos.config.file-extension=yaml
spring.cloud.nacos.config.server-addr=192.168.10.151:8848
# Nacos Config
spring:
  application:
    name: myshop-service-reg
  datasource:
    druid:
      url: jdbc:mysql://192.168.10.150:3306/myshop?useUnicode=true&characterEncoding=utf-8&useSSL=false
      username: root
      password: 123456
      initial-size: 1
      min-idle: 1
      max-active: 20
      test-on-borrow: true
      driver-class-name: com.mysql.cj.jdbc.Driver
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.10.151:8848
    sentinel:
      transport:
        port: 8719
        dashboard: 192.168.10.151:8080

server:
  port: 9501

mybatis:
    type-aliases-package: com.funtl.myshop.commons.domain
    mapper-locations: classpath:mapper/*.xml

management:
  endpoints:
    web:
      exposure:
        include: "*"
```
## SkyWalking
```
-javaagent:D:\Workspace\Others\spring-cloud-alibaba-my-shop\myshop-external-skywalking\agent\skywalking-agent.jar
-Dskywalking.agent.service_name=myshop-service-reg
-Dskywalking.collector.backend_service=192.168.10.148:11800
```