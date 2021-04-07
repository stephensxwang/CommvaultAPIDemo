<template>
    <el-row type="flex" justify="center">
        <el-form ref="loginForm" :model="user" :rules="rules" status-icon label-width="100px" @submit.native.prevent @keyup.enter.native="login">
            <el-form-item label="User Name" prop="name">
                <el-input v-model="user.name"></el-input>
            </el-form-item>
            <el-form-item label="Password" prop="pass">
                <el-input v-model="user.pass" type="password"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-s-promotion" @click="login">Log in</el-button>
            </el-form-item>
        </el-form>
    </el-row>
</template>

<script>
    export default {
        name: 'Login',
        methods: {
            login () {
                this.$refs.loginForm.validate((valid) => {
                    if (valid) {
                        var that = this;
                        const axios = require('axios');
                        let base64 = require('js-base64').Base64

                        var username = this.user.name;
                        var password = this.user.pass;
                        const loading = this.$loading({
                            lock: true,                             // 是否锁屏
                            text: 'Loading...',                     // 加载动画的文字
                            spinner: 'el-icon-loading'              // 引入的loading图标
                        });
                        axios({
                            method: 'post',
                            url: that.GLOBAL.baseUrl + "/login",
                            data: {
                                username: username,
                                password: base64.encode(password)
                            },
                            transformRequest: [function(data) {
                                let ret = ''
                                for(let it in data) {
                                    ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
                                }
                                return ret
                            }],
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                        })
                        .then(function (response) {
                            // console.log(response);
                            that.$notify({
                                type: 'success',
                                message: 'Welcome,' + username + '!',
                                duration: 3000 
                            });
                            if(response && response.data){
                                localStorage.setItem("cv_user", username);
                            }
                            window.location.href = '/content.html';
                        })
                        .catch(function (error) {
                            // console.log(error);
                            if(error && error.response && error.response.data && error.response.data.message){
                                that.$message({
                                    type: 'error',
                                    message: error.response.data.message,
                                    showClose: true
                                });
                            } else {
                                that.$message({
                                    type: 'error',
                                    message: 'User Name or Password is incorrect',
                                    showClose: true
                                });
                            }
                        }).then(function () {
                            // always executed
                            loading.close();
                        });
                    }
                    else {
                        return false
                    }
                });
            }
        },
        data () {
            return {
                user: {},
                rules: {
                    name: [
                        {required: true, message: 'User Name is required', trigger: 'blur'}
                    ],
                    pass: [
                        {required: true, message: 'Password is required', trigger: 'blur'}
                    ]
                }
            }
        }
    }
</script>