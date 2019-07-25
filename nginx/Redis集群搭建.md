---
title: Redis集群搭建
---
# Redis集群搭建
## 什么是 Redis
Redis 是用 C 语言开发的一个开源的高性能键值对（key-value）数据库。它通过提供多种键值数据类型来适应不同场景下的存储需求，目前为止 Redis 支持的键值数据类型如下：

1.字符串类型
2.散列类型
3.列表类型
4.集合类型
5.有序集合类型
## Redis 的应用场景
1.缓存（数据查询、短连接、新闻内容、商品内容等等）
2.分布式集群架构中的 session 分离
3.聊天室的在线好友列表
4.任务队列（秒杀、抢购、12306 等等）(时间戳加上redis生成3位数进行组合生成唯一)
5.应用排行榜
6.网站访问统计
7.数据过期处理（可以精确到毫秒）
## Redis Sentinel 集群部署
## 概述
Redis 集群可以在一组 redis 节点之间实现高可用性和 sharding。在集群中会有 1 个 master 和多个 slave 节点。当 master 节点失效时，应选举出一个 slave 节点作为新的 master。然而 Redis 本身(包括它的很多客户端)没有实现自动故障发现并进行主备切换的能力，需要外部的监控方案来实现自动故障恢复。

Redis Sentinel 是官方推荐的高可用性解决方案。它是 Redis 集群的监控管理工具，可以提供节点监控、通知、自动故障恢复和客户端配置发现服务。
![enter description here](./images/2019-07-25_185400.png)
## 搭建 Redis 集群
搭建一主两从环境，docker-compose.yml 配置如下：
```
version: '3.1'
services:
  master:
    image: redis
    container_name: redis-master
    ports:
      - 6379:6379

  slave1:
    image: redis
    container_name: redis-slave-1
    ports:
      - 6380:6379
    command: redis-server --slaveof redis-master 6379

  slave2:
    image: redis
    container_name: redis-slave-2
    ports:
      - 6381:6379
    command: redis-server --slaveof redis-master 6379
```
## 搭建 Sentinel 集群
我们至少需要创建三个 Sentinel 服务，docker-compose.yml 配置如下：
```
version: '3.1'
services:
  sentinel1:
    image: redis
    container_name: redis-sentinel-1
    ports:
      - 26379:26379
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
      - ./sentinel1.conf:/usr/local/etc/redis/sentinel.conf

  sentinel2:
    image: redis
    container_name: redis-sentinel-2
    ports:
      - 26380:26379
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
      - ./sentinel2.conf:/usr/local/etc/redis/sentinel.conf

  sentinel3:
    image: redis
    container_name: redis-sentinel-3
    ports:
      - 26381:26379
    command: redis-sentinel /usr/local/etc/redis/sentinel.conf
    volumes:
      - ./sentinel3.conf:/usr/local/etc/redis/sentinel.conf
  ```
## 修改 Sentinel 配置文件
需要三份 sentinel.conf 配置文件，分别为 sentinel1.conf，sentinel2.conf，sentinel3.conf，配置文件内容相同
```
port 26379
dir /tmp
# 自定义集群名，其中 127.0.0.1 为 redis-master 的 ip，6379 为 redis-master 的端口，2 为最小投票数（因为有 3 台 Sentinel 所以可以设置成 2）
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 30000
sentinel parallel-syncs mymaster 1
sentinel failover-timeout mymaster 180000
sentinel deny-scripts-reconfig yes
```
## 查看集群是否生效
进入 Sentinel 容器，使用 Sentinel API 查看监控情况：
```
docker exec -it redis-sentinel-1 /bin/bash
redis-cli -p 26379
sentinel master mymaster
sentinel slaves mymaster
```
