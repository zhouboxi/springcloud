---
title: vuex 状态管理
---

# vuex 状态管理
## 简介
Vuex 是一个专为 Vue.js 应用程序开发的 状态管理模式。它采用集中式存储管理应用的所有组件的状态，并以相应的规则保证状态以一种可预测的方式发生变化。

## 目标
继续之前 vue-router 章节做的案例项目，我们通过完善登录功能将用户信息保存至 Vuex 中来体会它的作用；

## 安装
在项目根目录执行如下命令来安装 Vuex
```
npm install vuex --save
```
修改 main.js 文件，导入 Vuex，关键代码如下：
```
import Vuex from 'vuex'
Vue.use(Vuex);
```
## 判断用户是否登录
我们利用路由钩子 beforeEach 来判断用户是否登录，期间会用到 sessionStorage 存储功能

## 修改 Login.vue
在表单验证成功方法内增加如下代码：
```
// 设置用户登录成功
sessionStorage.setItem('isLogin', 'true');
```
## 修改 main.js
利用路由钩子 beforeEach 方法判断用户是否成功登录，关键代码如下：
```
// 在跳转前执行
router.beforeEach((to, form, next) => {
  // 获取用户登录状态
  let isLogin = sessionStorage.getItem('isLogin');

  // 注销
  if (to.path == '/logout') {
    // 清空
    sessionStorage.clear();

    // 跳转到登录
    next({path: '/login'});
  }

  // 如果请求的是登录页
  else if (to.path == '/login') {
    if (isLogin != null) {
      // 跳转到首页
      next({path: '/main'});
    }
  }

  // 如果为非登录状态
  else if (isLogin == null) {
    // 跳转到登录页
    next({path: '/login'});
  }

  // 下一个路由
  next();
});
```
## 配置 vuex
## 创建 Vuex 配置文件
在 src 目录下创建一个名为 store 的目录并新建一个名为 index.js 文件用来配置 Vuex，代码如下：
```
import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);

// 全局 state 对象，用于保存所有组件的公共数据
const state = {
  // 定义一个 user 对象
  // 在组件中是通过 this.$store.state.user 来获取
  user: {
    username: ''
  }
};

// 实时监听 state 值的最新状态，注意这里的 getters 可以理解为计算属性
const getters = {
  // 在组件中是通过 this.$store.getters.getUser 来获取
  getUser(state) {
    return state.user;
  }
};

// 定义改变 state 初始值的方法，这里是唯一可以改变 state 的地方，缺点是只能同步执行
const mutations = {
  // 在组件中是通过 this.$store.commit('updateUser', user); 方法来调用 mutations
  updateUser(state, user) {
    state.user = user;
  }
};

// 定义触发 mutations 里函数的方法，可以异步执行 mutations 里的函数
const actions = {
  // 在组件中是通过 this.$store.dispatch('asyncUpdateUser', user); 来调用 actions
  asyncUpdateUser(context, user) {
    context.commit('updateUser', user);
  }
};

export default new Vuex.Store({
  state,
  getters,
  mutations,
  actions
});
```
修改 main.js 增加刚才配置的 store/index.js，关键代码如下：
```
import Vue from 'vue'
import Vuex from 'vuex'
import store from './store'

Vue.use(Vuex);

new Vue({
  el: '#app',
  store
});
```
## 解决浏览器刷新后 Vuex 数据消失问题
## 问题描述
Vuex 的状态存储是响应式的，当 Vue 组件从 store 中读取状态的时候，若 store 中的状态发生变化，那么相应的组件也会相应地得到高效更新。但是有一个问题就是：vuex 的存储的数据只是在页面的中，相当于我们定义的全局变量，刷新之后，里边的数据就会恢复到初始化状态。但是这个情况有时候并不是我们所希望的。

## 解决方案
监听页面是否刷新，如果页面刷新了，将 state 对象存入到 sessionStorage 中。页面打开之后，判断 sessionStorage 中是否存在 state 对象，如果存在，则说明页面是被刷新过的，将 sessionStorage 中存的数据取出来给 vuex 中的 state 赋值。如果不存在，说明是第一次打开，则取 vuex 中定义的 state 初始值。

## 修改代码
在 App.vue 中增加监听刷新事件
```
  export default {
    name: 'App',
    mounted() {
      window.addEventListener('unload', this.saveState);
    },
    methods: {
      saveState() {
        sessionStorage.setItem('state', JSON.stringify(this.$store.state));
      }
    }
  }
  ```
修改 store/index.js 中的 state
```
const state = sessionStorage.getItem('state') ? JSON.parse(sessionStorage.getItem('state')) : {
  user: {
    username: ''
  }
};
```
## 模块化
由于使用单一状态树，应用的所有状态会集中到一个比较大的对象。当应用变得非常复杂时，store 对象就有可能变得相当臃肿。为了解决以上问题，Vuex 允许我们将 store 分割成模块（module）。每个模块拥有自己的 state、mutation、action、getter、甚至是嵌套子模块——从上至下进行同样方式的分割

## 创建 user 模块
在 store 目录下创建一个名为 modules 的目录并创建一个名为 user.js 的文件，代码如下：
```
const user = {
  // 因为模块化了，所以解决刷新问题的代码需要改造一下
  state: sessionStorage.getItem('userState') ? JSON.parse(sessionStorage.getItem('userState')) : {
    user: {
      username: ''
    }
  },
  getters: {
    getUser(state) {
      return state.user;
    }
  },
  mutations: {
    updateUser(state, user) {
      state.user = user;
    }
  },
  actions: {
    asyncUpdateUser(context, user) {
      context.commit('updateUser', user);
    }
  }
};

export default user;
```
## 修改 store/index.js
```
import Vue from 'vue'
import Vuex from 'vuex'

import user from './modules/user'

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    // this.$store.state.user
    user
  }
});
备注：由于组件中使用的是 getters 和 actions 处理，所以调用代码不变

# 修改 App.vue
  export default {
    name: 'App',
    mounted() {
      window.addEventListener('unload', this.saveState);
    },
    methods: {
      saveState() {
        // 模块化后，调用 state 的代码修改为 this.$store.state.user
        sessionStorage.setItem('userState', JSON.stringify(this.$store.state.user));
      }
    }
  }
```