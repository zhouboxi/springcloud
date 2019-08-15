---
title: 安装 Docker Registry 私服
---

## 安装 Docker Registry 私服
#### 概述
官方的 Docker Hub 是一个用于管理公共镜像的地方，我们可以在上面找到我们想要的镜像，也可以把我们自己的镜像推送上去。但是，有时候我们的服务器无法访问互联网，或者你不希望将自己的镜像放到公网当中，那么你就需要 Docker Registry，它可以用来存储和管理自己的镜像。

#### 安装
在之前的 Docker 私有仓库 章节中已经提到过如何配置和使用容器运行私有仓库，这里我们使用 docker-compose 来安装，配置如下：
```
version: '3.1'
services:
  registry:
    image: registry
    restart: always
    container_name: registry
    ports:
      - 5000:5000
    volumes:
      - /usr/local/docker/registry/data:/var/lib/registry
 ```
 测试
启动成功后需要测试服务端是否能够正常提供服务，有两种方式：

1.浏览器端访问
 ```
http://ip:5000/v2/
 ```
2.终端访问
 ```
 curl http://ip:5000/v2/
  ```
  ## 配置 Docker Registry 客户端
概述
我们的教学案例使用的是 Ubuntu Server 16.04 LTS 版本，属于 systemd 系统，需要在 /etc/docker/daemon.json 中增加如下内容（如果文件不存在请新建该文件）
 ```
{
  "registry-mirrors": [
    "https://registry.docker-cn.com"
  ],
  "insecure-registries": [
    "ip:5000"
  ]
}
 ```
注意：该文件必须符合 json 规范，否则 Docker 将不能启动。

之后重新启动服务。
 ```
$ sudo systemctl daemon-reload
$ sudo systemctl restart docker
 ```
#### 检查客户端配置是否生效
使用 docker info 命令手动检查，如果从配置中看到如下内容，说明配置成功（192.168.75.133 为教学案例 IP）
 ```
Insecure Registries:
 192.168.75.133:5000
 127.0.0.0/8
 ```
#### 测试镜像上传
我们以 Nginx 为例测试镜像上传功能
 ```
## 拉取一个镜像
docker pull nginx

## 查看全部镜像
docker images

## 标记本地镜像并指向目标仓库（ip:port/image_name:tag，该格式为标记版本号）
docker tag nginx 192.168.75.133:5000/nginx

## 提交镜像到仓库
docker push 192.168.75.133:5000/nginx
 ```
#### 查看全部镜像
 ```
curl -XGET http://192.168.75.133:5000/v2/_catalog
 ```
#### 查看指定镜像
以 Nginx 为例，查看已提交的列表
 ```
curl -XGET http://192.168.75.133:5000/v2/nginx/tags/list
 ```
#### 测试拉取镜像
先删除镜像
 ```
docker rmi nginx
docker rmi 192.168.75.133:5000/nginx
 ```
再拉取镜像
 ```
docker pull 192.168.75.133:5000/nginx
 ```
##  部署 Docker Registry WebUI
私服安装成功后就可以使用 docker 命令行工具对 registry 做各种操作了。然而不太方便的地方是不能直观的查看 registry 中的资源情况。如果可以使用 UI 工具管理镜像就更好了。这里介绍两个 Docker Registry WebUI 工具

docker-registry-frontend
docker-registry-web
两个工具的功能和界面都差不多，我们以 docker-registry-fontend 为例讲解

# docker-registry-frontend
我们使用 docker-compose 来安装和运行，docker-compose.yml 配置如下：
 ```
version: '3.1'
services:
  frontend:
    image: konradkleine/docker-registry-frontend:v2
    ports:
      - 8080:80
    volumes:
      - ./certs/frontend.crt:/etc/apache2/server.crt:ro
      - ./certs/frontend.key:/etc/apache2/server.key:ro
    environment:
      - ENV_DOCKER_REGISTRY_HOST=192.168.75.133
      - ENV_DOCKER_REGISTRY_PORT=5000
 ```
注意：请将配置文件中的主机和端口换成自己仓库的地址

运行成功后在浏览器访问：http://192.168.75.133:5000