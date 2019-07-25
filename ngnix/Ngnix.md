---
title: Ngnix
tags: 基于docker Ngnix
grammar_mindmap: true
renderNumberedHeading: true
---

# Docker安装
1. root账户登录，查看内核版本如下
```
[root@localhost ~]# uname -a
Linux localhost.qgc 3.10.0-862.11.6.el7.x86_64 #1 SMP Tue Aug 14 21:49:04 UTC 2018 x86_64 x86_64 x86_64 GNU/Linux
```
2. 把yum包更新到最新
```
[root@localhost ~]# uname -a
[root@localhost ~]# yum update
已加载插件：fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: centos.ustc.edu.cn
 * extras: mirrors.aliyun.com
 * updates: centos.ustc.edu.cn
正在解决依赖关系
--> 正在检查事务
---> 软件包 bind-libs.x86_64.32.9.9.4-61.el7 将被 升级
---> 软件包 bind-libs.x86_64.32.9.9.4-61.el7_5.1 将被 更新
---> 软件包 bind-libs-lite.x86_64.32.9.9.4-61.el7 将被 升级
---> 软件包 bind-libs-lite.x86_64.32.9.9.4-61.el7_5.1 将被 更新
---> 软件包 bind-license.noarch.32.9.9.4-61.el7 将被 升级
---> 软件包 bind-license.noarch.32.9.9.4-61.el7_5.1 将被 更新
...
...
验证中 : 32:bind-license-9.9.4-61.el7.noarch 8/8

更新完毕:
bind-libs.x86_64 32:9.9.4-61.el7_5.1 
bind-libs-lite.x86_64 32:9.9.4-61.el7_5.1 
bind-license.noarch 32:9.9.4-61.el7_5.1 
bind-utils.x86_64 32:9.9.4-61.el7_5.1

完毕！
[root@localhost ~]#
```
3. 安装需要的软件包， yum-util 提供yum-config-manager功能，另外两个是devicemapper驱动依赖的
```
[root@localhost ~]# yum install -y yum-utils device-mapper-persistent-data lvm2
已加载插件：fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: centos.ustc.edu.cn
 * extras: mirrors.aliyun.com
 * updates: centos.ustc.edu.cn
...
```
4. 设置yum源
```
[root@localhost ~]# yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
已加载插件：fastestmirror, langpacks
adding repo from: https://download.docker.com/linux/centos/docker-ce.repo
grabbing file https://download.docker.com/linux/centos/docker-ce.repo to /etc/yum.repos.d/docker-ce.repo
repo saved to /etc/yum.repos.d/docker-ce.repo
...
```
5.可以查看所有仓库中所有docker版本，并选择特定版本安装
```
[root@localhost ~]# yum list docker-ce --showduplicates | sort -r
已加载插件：fastestmirror, langpacks
可安装的软件包
 * updates: centos.ustc.edu.cn
Loading mirror speeds from cached hostfile
 * extras: mirrors.aliyun.com
docker-ce.x86_64            18.06.1.ce-3.el7                    docker-ce-stable
docker-ce.x86_64            18.06.0.ce-3.el7                    docker-ce-stable
docker-ce.x86_64            18.03.1.ce-1.el7.centos             docker-ce-stable
docker-ce.x86_64            18.03.0.ce-1.el7.centos             docker-ce-stable
docker-ce.x86_64            17.12.1.ce-1.el7.centos             docker-ce-stable
docker-ce.x86_64            17.12.0.ce-1.el7.centos             docker-ce-stable
...
```
6.安装Docker，命令：yum install docker-ce-版本号
```
[root@localhost ~]# yum install docker-ce-17.12.1.ce
已加载插件：fastestmirror, langpacks
Loading mirror speeds from cached hostfile
 * base: centos.ustc.edu.cn
 * extras: mirrors.aliyun.com
 * updates: centos.ustc.edu.cn
base                                                   | 3.6 kB     00:00     
docker-ce-stable                                       | 2.9 kB     00:00     
extras                                                 | 3.4 kB     00:00     
updates                                                | 3.4 kB     00:00     
正在解决依赖关系
--> 正在检查事务
---> 软件包 docker-ce.x86_64.0.17.12.1.ce-1.el7.centos 将被 安装
--> 正在处理依赖关系 container-selinux >= 2.9，它被软件包 docker-ce-17.12.1.ce-1.el7.centos.x86_64 需要
...
```
7.启动Docker，命令：systemctl start docker，然后加入开机启动，如下
```
root@localhost ~]# systemctl start docker
[root@localhost ~]# systemctl enable docker
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
...
```
8.验证安装是否成功(有client和service两部分表示docker安装启动都成功了)
```
[root@localhost ~]# docker version 
Client:
 Version:    17.12.1-ce
 API version:    1.35
 Go version:    go1.9.4
 Git commit:    7390fc6
 Built:    Tue Feb 27 22:15:20 2018
 OS/Arch:    linux/amd64

Server:
 Engine:
  Version:    17.12.1-ce
  API version:    1.35 (minimum version 1.12)
  Go version:    go1.9.4
  Git commit:    7390fc6
  Built:    Tue Feb 27 22:17:54 2018
  OS/Arch:    linux/amd64
  Experimental:    false
...
```
9.常用命令
```
 -----------------     docker ps 查看当前正在运行的容器
 -----------------    docker ps -a 查看所有容器的状态
 -----------------    docker start/stop id/name 启动/停止某个容器
 -----------------    docker attach id 进入某个容器(使用exit退出后容器也跟着停止运行)
 -----------------    docker exec -ti id 启动一个伪终端以交互式的方式进入某个容器（使用exit退出后容器不停止运行）
 -----------------    docker images 查看本地镜像
 -----------------    docker rm id/name 删除某个容器
-----------------     docker rmi id/name 删除某个镜像
 -----------------    docker run --name test -ti ubuntu /bin/bash  复制ubuntu容器并且重命名为test且运行，然后以伪终端交互式方式进入容器，运行bash
-----------------     docker build -t soar/centos:7.1 .  通过当前目录下的Dockerfile创建一个名为soar/centos:7.1的镜像
 -----------------    docker run -d -p 2222:22 --name test soar/centos:7.1  以镜像soar/centos:7.1创建名为test的容器，并以后台模式运行，并做端口映射到宿主机2222端口，P参数重启容器宿主机端口会发生改变
```
# Docker Compose安装

## Docker Compose 概述
1.前面我们使用 Docker 的时候，定义 Dockerfile 文件，然后使用 docker build、docker run 等命令操作容器。然而微服务架构的应用系统一般包含若干个微服务，每个微服务一般都会部署多个实例，如果每个微服务都要手动启停，那么效率之低，维护量之大可想而知
2.使用 Docker Compose 可以轻松、高效的管理容器，它是一个用于定义和运行多容器 Docker 的应用程序工具


### 安装 https://github.com/docker/compose/releases
```
sudo curl -L https://github.com/docker/compose/releases/download/1.24.1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```
###  查看安装是否成功
```
docker-compose -v
```
### 快速入门

1.打包项目，获得 jar 包 docker-demo-0.0.1-SNAPSHOT.jar
```
mvn clean package
```
2.在 jar 包所在路径创建 Dockerfile 文件，添加以下内容
```
FROM java:8
VOLUME /tmp
ADD docker-demo-0.0.1-SNAPSHOT.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 9000
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
```
3.在 jar 包所在路径创建文件 docker-compose.yml，添加以下内容
```
version: '2' # 表示该 Docker-Compose 文件使用的是 Version 2 file
services:
  docker-demo:  # 指定服务名称
    build: .  # 指定 Dockerfile 所在路径
    ports:    # 指定端口映射
      - "9000:8761"
```
4.在 docker-compose.yml 所在路径下执行该命令 Compose 就会自动构建镜像并使用镜像启动容器
```
docker-compose up
docker-compose up -d  // 后台启动并运行容器
```
5.访问 http://localhost:9000/hello 即可访问微服务接口
### 工程、服务、容器

Docker Compose 将所管理的容器分为三层，分别是工程（project）、服务（service）、容器（container）
Docker Compose 运行目录下的所有文件（docker-compose.yml）组成一个工程,一个工程包含多个服务，每个服务中定义了容器运行的镜像、参数、依赖，一个服务可包括多个容器实例
Docker Compose 常用命令与配置
常见命令
ps：列出所有运行容器
```
docker-compose ps
```
logs：查看服务日志输出
```
docker-compose logs
```
port：打印绑定的公共端口，下面命令可以输出 eureka 服务 8761 端口所绑定的公共端口
```
docker-compose port eureka 8761
```
build：构建或者重新构建服务
```
docker-compose build
```
start：启动指定服务已存在的容器
```
docker-compose start eureka
```
stop：停止已运行的服务的容器
```
docker-compose stop eureka
```
rm：删除指定服务的容器
```
docker-compose rm eureka
```
up：构建、启动容器
```
docker-compose up
```
kill：通过发送 SIGKILL 信号来停止指定服务的容器
```
docker-compose kill eureka
```
pull：下载服务镜像

scale：设置指定服务运气容器的个数，以 service=num 形式指定
```
docker-compose scale user=3 movie=3
```
run：在一个服务上执行一个命令
```
docker-compose run web bash
```
### docker-compose.yml 属性
1.version：指定 docker-compose.yml 文件的写法格式
2.services：多个容器集合
3.build：配置构建时，Compose 会利用它自动构建镜像，该值可以是一个路径，也可以是一个对象，用于指定 Dockerfile 参数
```
build: ./dir
---------------
build:
    context: ./dir
    dockerfile: Dockerfile
    args:
        buildno: 1
```
4.command：覆盖容器启动后默认执行的命令
```
command: bundle exec thin -p 3000
----------------------------------
command: [bundle,exec,thin,-p,3000]
```
5.dns：配置 dns 服务器，可以是一个值或列表
```
dns: 8.8.8.8
------------
dns:
    - 8.8.8.8
    - 9.9.9.9
```
6.dns_search：配置 DNS 搜索域，可以是一个值或列表
```
dns_search: example.com
------------------------
dns_search:
    - dc1.example.com
    - dc2.example.com
```


7.environment：环境变量配置，可以用数组或字典两种方式
```
environment:
    RACK_ENV: development
    SHOW: 'ture'
-------------------------
environment:
    - RACK_ENV=development
    - SHOW=ture
```
8.env_file：从文件中获取环境变量，可以指定一个文件路径或路径列表，其优先级低于 environment 指定的环境变量
```
env_file: .env
---------------
env_file:
    - ./common.env
```
9.expose：暴露端口，只将端口暴露给连接的服务，而不暴露给主机
```
expose:
    - "3000"
    - "8000"
```
10.image：指定服务所使用的镜像
```
image: java
```
11.network_mode：设置网络模式
```
network_mode: "bridge"
network_mode: "host"
network_mode: "none"
network_mode: "service:[service name]"
network_mode: "container:[container name/id]"
```
12.ports：对外暴露的端口定义，和 expose 对应
```
ports:   # 暴露端口信息  - "宿主机端口:容器暴露端口"
- "8763:8763"
- "8763:8763"
```
13.links：将指定容器连接到当前连接，可以设置别名，避免ip方式导致的容器重启动态改变的无法连接情况
```
links:    # 指定服务名称:别名 
    - docker-compose-eureka-server:compose-eureka
```
14.volumes：卷挂载路径
```
volumes:
  - /lib（物理机/容器）
  - /var
```
15.logs：日志输出信息
```
--no-color          单色输出，不显示其他颜.
-f, --follow        跟踪日志输出，就是可以实时查看日志
-t, --timestamps    显示时间戳
--tail              从日志的结尾显示，--tail=200
```
### Docker Compose 其它
#### 更新容器
当服务的配置发生更改时，可使用 docker-compose up 命令更新配置
此时，Compose 会删除旧容器并创建新容器，新容器会以不同的 IP 地址加入网络，名称保持不变，任何指向旧容起的连接都会被关闭，重新找到新容器并连接上去

#### links
服务之间可以使用服务名称相互访问，links 允许定义一个别名，从而使用该别名访问其它服务
```
version: '2'
services:
    web:
        build: .
        links:
            - "db:database"
    db:
        image: postgres
```
这样 Web 服务就可以使用 db 或 database 作为 hostname 访问 db 服务了

