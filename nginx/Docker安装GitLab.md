---
title: Docker安装GitLab
---
基于 Docker 安装 GitLab
我们使用 Docker 来安装和运行 GitLab 中文版，由于新版本问题较多，这里我们使用目前相对稳定的 10.5 版本，docker-compose.yml 配置如下：
```
version: '3'
services:
    web:
      image: 'twang2218/gitlab-ce-zh:10.5'
      restart: always
      hostname: '192.168.75.145'
      environment:
        TZ: 'Asia/Shanghai'
        GITLAB_OMNIBUS_CONFIG: |
          external_url 'http://192.168.75.145:8080'
          gitlab_rails['gitlab_shell_ssh_port'] = 2222
          unicorn['port'] = 8888
          nginx['listen_port'] = 8080
      ports:
        - '8080:8080'
        - '8443:443'
        - '2222:22'
      volumes:
        - /usr/local/docker/gitlab/config:/etc/gitlab
        - /usr/local/docker/gitlab/data:/var/opt/gitlab
        - /usr/local/docker/gitlab/logs:/var/log/gitlab
 ```
# 安装完成后的工作
访问地址：http://ip:8080
端口 8080 是因为我在配置中设置的外部访问地址为 8080，默认是 80
设置管理员初始密码，这里的密码最好是 字母 + 数字 组合，并且 大于等于 8 位
配置完成后登录，管理员账号是 root
# 虚拟机内存设置3G+不然启动502 密码设置字母 + 数字 组合，并且 大于等于 8 位不然500
