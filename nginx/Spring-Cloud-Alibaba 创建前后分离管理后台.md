---
title: Spring-Cloud-Alibaba 创建前后分离管理后台
---

# Spring-Cloud-Alibaba 创建前后分离管理后台
## 概述
我们利用 VUE ELEMENT ADMIN 来快速搭建属于自己的产品后台

备注：使用该后台模板开发需要掌握 NodeJS、ES2015+、vue、vuex、vue-router 、axios 和 element-ui 等前端技术栈相关知识。如果不具备相关基础知识请先学习我的 Vue 渐进式 JavaScript 框架 课程

## 克隆模板到本地
克隆 vue-element-admin 完整模板到本地，主要作用是方便我们直接拿组件到项目中使用
git clone https://github.com/PanJiaChen/vue-element-admin.git
克隆 vue-admin-template 基础模板到本地，主要作用是创建一个最简单的项目后台，再根据需求慢慢完善功能
git clone https://github.com/PanJiaChen/vue-admin-template.git
## 创建项目管理后台
将 vue-admin-template 项目模板复制一份出来修改目录名为 vue-admin-myshop，进入 vue-admin-myshop 目录，修改 package.json 配置文件为如下内容：
```
{
  "name": "vue-admin-myshop",
  "version": "1.0.0",
  "description": "",
  "author": "Lusifer <lusifer.lee@gmail.com>",
  "scripts": {
    "dev": "webpack-dev-server --inline --progress --config build/webpack.dev.conf.js",
    "start": "npm run dev",
    "build": "node build/build.js",
    "build:report": "npm_config_report=true npm run build",
    "lint": "eslint --ext .js,.vue src",
    "test": "npm run lint",
    "svgo": "svgo -f src/icons/svg --config=src/icons/svgo.yml"
  },
  "dependencies": {
    "axios": "0.18.0",
    "element-ui": "2.4.6",
    "js-cookie": "2.2.0",
    "mockjs": "1.0.1-beta3",
    "normalize.css": "7.0.0",
    "nprogress": "0.2.0",
    "vue": "2.5.17",
    "vue-router": "3.0.1",
    "vuex": "3.0.1"
  },
  "devDependencies": {
    "autoprefixer": "8.5.0",
    "babel-core": "6.26.0",
    "babel-eslint": "8.2.6",
    "babel-helper-vue-jsx-merge-props": "2.0.3",
    "babel-loader": "7.1.5",
    "babel-plugin-syntax-jsx": "6.18.0",
    "babel-plugin-transform-runtime": "6.23.0",
    "babel-plugin-transform-vue-jsx": "3.7.0",
    "babel-preset-env": "1.7.0",
    "babel-preset-stage-2": "6.24.1",
    "chalk": "2.4.1",
    "compression-webpack-plugin": "2.0.0",
    "copy-webpack-plugin": "4.5.2",
    "css-loader": "1.0.0",
    "eslint": "4.19.1",
    "eslint-friendly-formatter": "4.0.1",
    "eslint-loader": "2.0.0",
    "eslint-plugin-vue": "4.7.1",
    "eventsource-polyfill": "0.9.6",
    "file-loader": "1.1.11",
    "friendly-errors-webpack-plugin": "1.7.0",
    "html-webpack-plugin": "4.0.0-alpha",
    "mini-css-extract-plugin": "0.4.1",
    "node-notifier": "5.2.1",
    "node-sass": "^4.7.2",
    "optimize-css-assets-webpack-plugin": "5.0.0",
    "ora": "3.0.0",
    "path-to-regexp": "2.4.0",
    "portfinder": "1.0.16",
    "postcss-import": "12.0.0",
    "postcss-loader": "2.1.6",
    "postcss-url": "7.3.2",
    "rimraf": "2.6.2",
    "sass-loader": "7.0.3",
    "script-ext-html-webpack-plugin": "2.0.1",
    "semver": "5.5.0",
    "shelljs": "0.8.2",
    "svg-sprite-loader": "3.8.0",
    "svgo": "1.0.5",
    "uglifyjs-webpack-plugin": "1.2.7",
    "url-loader": "1.0.1",
    "vue-loader": "15.3.0",
    "vue-style-loader": "4.1.2",
    "vue-template-compiler": "2.5.17",
    "webpack": "4.16.5",
    "webpack-bundle-analyzer": "2.13.1",
    "webpack-cli": "3.1.0",
    "webpack-dev-server": "3.1.14",
    "webpack-merge": "4.1.4"
  },
  "engines": {
    "node": ">= 6.0.0",
    "npm": ">= 3.0.0"
  },
  "browserslist": [
    "> 1%",
    "last 2 versions",
    "not ie <= 8"
  ]
}
```
说明：主要是将项目信息修改为自己的

## 构建并运行
## 建议不要用 cnpm  安装有各种诡异的 bug 可以通过如下操作解决 npm 速度慢的问题
```
npm install --registry=https://registry.npm.taobao.org
```
## Serve with hot reload at localhost:9528
```
npm run dev
```