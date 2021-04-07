<template>
  <el-container>
    <el-header class="header" height="100px" direction="vertical">
      <el-row>
          <div style="float:left;">
            <h3>
              <img :src="logoUrl" style="position:absolute;height:30px;">
              <span style="position:absolute;top:60px">Commvault Cloud Platform</span>
            </h3>
          </div>

          <el-dropdown style="top:60px">
            <i class="el-icon-user-solid" style="margin-right: 15px"></i>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">{{checkStatus()}}</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>      
          <span style="position:relative;top:60px">{{getUserName()}}</span>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <el-menu
          :default-active="$route.path"
          :router="true"
          class="el-menu-vertical-demo"
          background-color="black"
          text-color="#fff"
          active-text-color="#ffd04b">
          <el-menu-item index="/client_group">
            <i class="el-icon-menu"></i>
            <span>Client Group</span>
          </el-menu-item>
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-s-order"></i>
              <span>Batch Job</span>
            </template>
            <el-menu-item index="/batchConfig">Batch Configuration</el-menu-item>
            <el-menu-item index="/batchInstall">Batch Install</el-menu-item>
          </el-submenu>
          <el-menu-item index="/restore">
            <i class="el-icon-aim"></i>
            <span>Restore</span>
          </el-menu-item>
          <el-menu-item index="/sys_config">
            <i class="el-icon-setting"></i>
            <span>Setting</span>
          </el-menu-item>
          <el-menu-item-group title="Others">
            <el-menu-item index="/about">
              <i class="el-icon-question"></i>
              <span>About Us</span>
            </el-menu-item>
          </el-menu-item-group>
        </el-menu>
      </el-aside>
      <el-main><router-view/></el-main>
    </el-container>
  </el-container>
</template>

<script>
import logo from "../../assets/logo.png"

export default {
  name: 'Content',
  // mounted () {
  //   console.log(this.$route.path);
  // }
  data(){
    return {
      logoUrl: logo
    }
  },
  methods: {
    getUserName() {
      return this.GLOBAL.getCvUserName();
    },
    logout() {
      var that = this;
      const axios = require('axios');
      
      const loading = this.$loading({
          lock: true,                             // 是否锁屏
          text: 'Loading...',                     // 加载动画的文字
          spinner: 'el-icon-loading'              // 引入的loading图标
      });
      axios({
          method: 'post',
          url: that.GLOBAL.baseUrl + "/logout"
      })
      .then(function (response) {
          // console.log(response);
      })
      .catch(function (error) {
          // console.log(error);
      }).then(function () {
          localStorage.clear();
          window.location.href = '/';
          // always executed
          loading.close();
      });
    },
    checkStatus(){
      if(this.GLOBAL.isEmpty(this.GLOBAL.getCvUserName())){
        return "Log in";
      } else {
        return "Log out";
      }
    }
  }
}
</script>

<style>
  .header{
    background-color: black;
    color: white;
    text-align: right;
  }
    
  .el-container {
    height: 100%;
  }
  
  .el-aside {
    background-color: black;
    color: white;
    /* line-height: 200px; */
  }
  
  .el-main {
    background-color: #E9EEF3;
    color: #333;
    text-align: center;
    /* line-height: 160px; */
  }
</style>