---
title: Spring-Cloud-Alibaba 创建通用的业务逻辑
---


# Spring-Cloud-Alibaba 创建通用的业务逻辑
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

    <artifactId>myshop-commons-service</artifactId>
    <packaging>jar</packaging>

    <name>myshop-commons-service</name>
    <url>http://www.funtl.com</url>
    <inceptionYear>2018-Now</inceptionYear>

    <dependencies>
        <dependency>
            <groupId>com.funtl</groupId>
            <artifactId>myshop-commons-mapper</artifactId>
            <version>${project.parent.version}</version>
        </dependency>
    </dependencies>
</project>
```