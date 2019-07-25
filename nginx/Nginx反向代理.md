---
title: Nginx反向代理
---
# 使用 Nginx 反向代理 Tomcat
## 需求
两个 tomcat 服务通过 nginx 反向代理
nginx 服务器：192.168.75.145:80
tomcat1 服务器：192.168.75.145:9090
tomcat2 服务器：192.168.75.145:9091
## 启动 Tomcat 容器
启动两个 Tomcat 容器，映射端口为 9090 和 9091，docker-compose.yml 如下：
```
version: '3'
services:
  tomcat1:
    image: tomcat
    container_name: tomcat1
    ports:
      - 9090:8080

  tomcat2:
    image: tomcat
    container_name: tomcat2
    ports:
      - 9091:8080
```
# 配置 Nginx 反向代理
修改 /usr/local/docker/nginx/conf 目录下的 nginx.conf 配置文件：
```
user  nginx;
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;

    sendfile        on;

    keepalive_timeout  65;
	
	# 配置一个代理即 tomcat1 服务器
	upstream tomcatServer1 {
		server 192.168.75.145:9090;
	}

	# 配置一个代理即 tomcat2 服务器
	upstream tomcatServer2 {
		server 192.168.75.145:9091;
	}

	# 配置一个虚拟主机
	server {
		listen 80;
		server_name admin.service.itoken.funtl.com;
		location / {
				# 域名 admin.service.itoken.funtl.com 的请求全部转发到 tomcat_server1 即 tomcat1 服务上
				proxy_pass http://tomcatServer1;
				# 欢迎页面，按照从左到右的顺序查找页面
				index index.jsp index.html index.htm;
		}
	}

	server {
		listen 80;
		server_name admin.web.itoken.funtl.com;

		location / {
			# 域名 admin.web.itoken.funtl.com 的请求全部转发到 tomcat_server2 即 tomcat2 服务上
			proxy_pass http://tomcatServer2;
			index index.jsp index.html index.htm;
		}
	}
}
```
注意：新版 Nginx 的 upstream 配置中的名称不可以有下划线("_")，否则会报 400 错误
