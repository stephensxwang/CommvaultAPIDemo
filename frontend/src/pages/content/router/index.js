import Vue from 'vue'
import Router from 'vue-router'
import Main from '../../../components/Main.vue'
import ClientGroup from '../../../components/ClientGroup.vue'
import BatchConfig from '../../../components/BatchConfig.vue'
import BatchInstall from '../../../components/BatchInstall.vue'
import Restore from '../../../components/Restore.vue'
import SysConfig from '../../../components/SysConfig.vue'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Main',
      component: Main
    },
    {
      path: '/client_group',
      name: 'ClientGroup',
      component: ClientGroup
    },
    {
      path: '/batchConfig',
      name: 'BatchConfig',
      component: BatchConfig
    },
    {
      path: '/batchInstall',
      name: 'BatchInstall',
      component: BatchInstall
    },
    {
      path: '/restore',
      name: 'Restore',
      component: Restore
    },
    {
      path: '/sys_config',
      name: 'SysConfig',
      component: SysConfig
    }
  ]
})

import global from '../../../components/Global'//引用文件
router.beforeEach((to, from, next) => {
  // 取到用户名
  let userName = global.getCvUserName();

  // 如果没登录
  if (global.isEmpty(userName)) {
    alert('Please login first');
    window.location.href = '/index.html';
  } else {
    next();
  }
})

export default router