// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Index from './Index.vue'
import router from './router'
import '../../element'
import global from '../../components/Global'//引用文件

Vue.prototype.GLOBAL = global//挂载到Vue实例上面
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { Index },
  template: '<Index/>'
})
