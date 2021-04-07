<script>
    // 定义一些公共的属性和方法

    // 根据 process.env.HOST 的值判断当前是什么环境
    // 命令：npm run build -- test ，process.env.HOST就设置为：'test'
    let HOST = process.env.HOST;
    let baseUrl = 'http://localhost:8081';//'/api';//发布时改为空
    if(HOST === 'prod'){
        baseUrl = ''
    }
    
    function getCvUserName() {
      return localStorage.getItem("cv_user");
    }
    function getAxiosInstance(that) {
      const axios = require('axios');
      var instance = axios.create({
        baseURL: baseUrl,
        withCredentials: true
        // timeout: 1000
      });
      
      return instance;
    }
    function getAxiosErrorMessage(error) {
      var message = "";

      if (error.response) {
        // console.log(error.response);
        // 发送请求后，服务端返回的响应码不是 2xx
        if(error.response.status == 401){
          if(isEmpty(getCvUserName())){
            message = "You don't have permission to access this page, please login first";
          } else {
            message = "Your session has been timeout, please login again";
            localStorage.clear();
          }
          return message;
        } 

        if(error.response.data){
          if(error.response.data.error){
            return error.response.data.error;
          } else {
            return error.response.data;
          }
        }
      } 

      return message;
    } 
    //判断字符是否为空的方法
    function isEmpty(obj){
        if(typeof obj == "undefined" || obj == null || obj == ""){
            return true;
        }else{
            return false;
        }
    }
    // 暴露出这些属性和方法
    export default {
        baseUrl,
        getCvUserName,
        getAxiosInstance,
        getAxiosErrorMessage,
        isEmpty
    }
</script>