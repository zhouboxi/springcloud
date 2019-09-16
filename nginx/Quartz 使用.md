---
title: Quartz 使用
---

# Quartz 使用
### 概述
Quartz 是 OpenSymphony 开源组织在 Job Scheduling 领域又一个开源项目，它可以与 J2EE 与 J2SE 应用程序相结合也可以单独使用。Quartz 可以用来创建简单或为运行十个，百个，甚至是好几万个 Jobs 这样复杂的程序。Jobs 可以做成标准的 Java 组件或 EJBs。

### 为什么使用 Quartz？
Quartz 是一个任务调度框架。比如你遇到这样的问题：

1.每天 02:00 发送一份工作邮件给工作组成员并抄送给老板（假装自己很努力的工作到深夜）
2.每月 2 号提醒自己还信用卡或自动还款
3.每秒钟发 N 笔脏数据给竞争对手公司的服务器（！←_←）
这些问题总结起来就是：在某一个有规律的时间点干某件事。并且时间的触发的条件可以非常复杂，复杂到需要一个专门的框架来干这个事。Quartz 就是来干这样的事，你给它一个触发条件的定义，它负责到了时间点，触发相应的 Job 起来干活（睡 NMB 起来嗨！）。

### 什么是 cron 表达式？
cron 是 Linux 系统用来设置计划任务的，比如：每天晚上 12 点重启服务器。

### 格式
一个 cron 表达式具体表现就是一个字符串，这个字符串中包含 6~7 个字段，字段之间是由空格分割的，每个字段可以由任何允许的值以及允许的特殊字符所构成，下面表格列出了每个字段所允许的值和特殊字符

字段	允许值	允许的特殊字符
秒	0-59	, - * /
分	0-59	, - * /
小时	0-23	, - * /
日期	1-31	, - * / L W C
月份	1-12 或者 JAN-DEC	, - * /
星期	1-7 或者 SUN-SAT	, - * / L C #(1是星期天 ,7是星期六)
年（可选）	留空, 1970-2099	, - * /
1.* 字符被用来指定所有的值。如：* 在分钟的字段域里表示“每分钟”。

2.- 字符被用来指定一个范围。如：10-12 在小时域意味着“10点、11点、12点”

3., 字符被用来指定另外的值。如：MON,WED,FRI 在星期域里表示“星期一、星期三、星期五”.

4.? 字符只在日期域和星期域中使用。它被用来指定“非明确的值”。当你需要通过在这两个域中的一个来指定一些东西的时候，它是有用的。

5.L 字符指定在月或者星期中的某天（最后一天）。即 “Last” 的缩写。但是在星期和月中 “Ｌ” 表示不同的意思，如：在月字段中 “L” 指月份的最后一天 “1月31日，2月28日”，如果在星期字段中则简单的表示为“7”或者“SAT”。如果在星期字段中在某个 value 值得后面，则表示 “某月的最后一个星期 value” ,如 “6L” 表示某月的最后一个星期五。

6.W 字符只能用在月份字段中，该字段指定了离指定日期最近的那个星期日。

7.# 字符只能用在星期字段，该字段指定了第几个星期 value 在某月中

每一个元素都可以显式地规定一个值（如 6），一个区间（如 9-12 ），一个列表（如 9，11，13 ）或一个通配符（如 *）。“月份中的日期”和“星期中的日期”这两个元素是互斥的，因此应该通过设置一个问号（?）来表明你不想设置的那个字段。

表达式	意义
0 0 12 * * ?	每天中午 12 点触发
0 15 10 ? * *	每天上午 10:15 触发
0 15 10 * * ?	每天上午 10:15 触发
0 15 10 * * ? *	每天上午 10:15 触发
0 15 10 * * ? 2005	2005 年的每天上午 10:15 触发
0 * 14 * * ?	在每天下午 2 点到下午 2:59 期间的每 1 分钟触发
0 0/5 14 * * ?	在每天下午 2 点到下午 2:55 期间的每 5 分钟触发
0 0/5 14,18 * * ?	在每天下午 2 点到 2:55 期间和下午 6 点到 6:55 期间的每 5 分钟触发
0 0-5 14 * * ?	在每天下午 2 点到下午 2:05 期间的每 1 分钟触发
0 10,44 14 ? 3 WED	每年三月的星期三的下午 2:10 和 2:44 触发
0 15 10 ? * MON-FRI	周一至周五的上午 10:15 触发
0 15 10 15 * ?	每月 15 日上午 10:15 触发
0 15 10 L * ?	每月最后一日的上午 10:15 触发
0 15 10 ? * 6L	每月的最后一个星期五上午 10:15 触发
0 15 10 ? * 6L 2002-2005	2002 年至 2005 年的每月的最后一个星期五上午 10:15 触发
0 15 10 ? * 6#3	每月的第三个星期五上午 10:15 触发
### Spring Boot 集成 Quartz
### 创建项目
创建一个名为 hello-quartz 的项目

### POM
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.funtl</groupId>
    <artifactId>hello-quartz</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>jar</packaging>

    <name>hello-quartz</name>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.4.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-quartz</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>
```
主要增加了 org.springframework.boot:spring-boot-starter-quartz 依赖

### Application
```
package com.funtl.hello.quatrz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class HelloQuatrzApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloQuatrzApplication.class, args);
    }
}
```
使用 @EnableScheduling 注解来开启计划任务功能

### 创建任务
我们创建一个每 5 秒钟打印当前时间的任务来测试 Quartz
```
package com.funtl.hello.quatrz.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PrintCurrentTimeTask {
    @Scheduled(cron = "0/5 * * * * ? ")
    public void printCurrentTime() {
        System.out.println("Current Time is:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
```
启动服务，控制台打印效果如下：

