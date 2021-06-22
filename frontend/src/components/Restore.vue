<template>
  <el-container>
    <el-main>
      <el-tabs type="border-card">
        <el-tab-pane label="File System & NAS">
            <el-form ref="form" label-position="left" :rules="rules" :model="restoreForm" label-width="120px" size="mini">
              <el-row>
                  <el-col :span="12">
                  <el-form-item label="Restore Content" prop="content">
                      <el-input v-model="restoreForm.content"></el-input>
                  </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                  <el-form-item label="Client Name" prop="clientname">
                      <el-select v-model="restoreForm.clientname" @change="selectClientChange" placeholder="please select a client">
                          <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                      </el-select>
                  </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="12">
                      <el-form-item label="Restore target" prop="targetpath">
                          <el-input v-model="restoreForm.targetpath"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                      <el-form-item label="Target Client" prop="targetclient">
                          <el-select v-model="restoreForm.targetclient" @change="selectTargetChange" placeholder="please select a target client">
                              <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                          </el-select>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
              <el-form-item label="Backup Time" required>
                  <el-col :span="12">
                      <el-form-item prop="date">
                          <el-date-picker 
                            type="datetime" 
                            placeholder="Select date and time" 
                            v-model="restoreForm.date" 
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy-MM-dd HH:mm:ss"
                            default-time="00:00:00"
                            size="mini"
                            style="width: 100%;">
                          </el-date-picker>
                      </el-form-item>
                  </el-col>
              </el-form-item>
              </el-row>
              <el-form-item size="mini">
                  <el-button type="primary" @click="submitForm('form')">Submit</el-button>
                  <el-button @click="resetForm('form')">Reset</el-button>
              </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="DB2">
          <el-form ref="db2form" label-position="left" :model="db2RestoreForm" label-width="120px" size="mini">
              <el-row>
                  <el-col :span="12">
                  <el-form-item label="Database Name" required>
                      <el-input v-model="db2RestoreForm.srcBackupsetName"></el-input>
                  </el-form-item>
                  <el-form-item label="Instance Name" required>
                      <el-input v-model="db2RestoreForm.srcInstanceName"></el-input>
                  </el-form-item>
                  <el-form-item label="Subclient Name" required>
                      <el-input v-model="db2RestoreForm.srcSubclientName"></el-input>
                  </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                  <el-form-item label="Client Name" required>
                      <el-select v-model="db2RestoreForm.srcClientName" placeholder="please select a client">
                          <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                      </el-select>
                  </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="12">
                      <el-form-item label="Target Database Name" required>
                          <el-input v-model="db2RestoreForm.targetDbName"></el-input>
                      </el-form-item>
                      <el-form-item label="Target Instance Name" required>
                          <el-input v-model="db2RestoreForm.destInstanceName"></el-input>
                      </el-form-item>
                      <el-form-item label="Target Path(*should be an existing path*)" required>
                          <el-input v-model="db2RestoreForm.targetPath"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                      <el-form-item label="Target Client" required>
                          <el-select v-model="db2RestoreForm.destClientName" placeholder="please select a target client">
                              <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                          </el-select>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
              <el-form-item label="Backup Time" required>
                  <el-col :span="12">
                      <el-form-item prop="date">
                          <el-date-picker 
                            type="datetime" 
                            placeholder="Select date and time" 
                            v-model="db2RestoreForm.backupDate" 
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy-MM-dd HH:mm:ss"
                            default-time="00:00:00"
                            size="mini"
                            style="width: 100%;">
                          </el-date-picker>
                      </el-form-item>
                  </el-col>
              </el-form-item>
              </el-row>
              <el-form-item size="mini">
                  <el-button type="primary" @click="submitDb2Form('db2form')">Submit</el-button>
              </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Oracle">
          <el-form ref="oracleform" label-position="left" :model="oracleRestoreForm" label-width="120px" size="mini">
              <el-row>
                  <el-col :span="12">
                  <el-form-item label="Database Name" required>
                      <el-input v-model="oracleRestoreForm.srcInstanceName"></el-input>
                  </el-form-item>
                  <el-form-item label="Subclient Name" required>
                      <el-input v-model="oracleRestoreForm.srcSubclientName"></el-input>
                  </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                  <el-form-item label="Client Name" required>
                      <el-select v-model="oracleRestoreForm.srcClientName" placeholder="please select a client">
                          <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                      </el-select>
                  </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="12">
                      <el-form-item label="Target Instance Name" required>
                          <el-input v-model="oracleRestoreForm.destInstanceName"></el-input>
                      </el-form-item>
                      <el-form-item label="Target Path" required>
                          <el-input v-model="oracleRestoreForm.targetPath"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                      <el-form-item label="Target Client" required>
                          <el-select v-model="oracleRestoreForm.destClientName" placeholder="please select a target client">
                              <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                          </el-select>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
              <el-form-item label="Backup Time" required>
                  <el-col :span="12">
                      <el-form-item prop="date">
                          <el-date-picker 
                            type="datetime" 
                            placeholder="Select date and time" 
                            v-model="oracleRestoreForm.backupDate" 
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy-MM-dd HH:mm:ss"
                            default-time="00:00:00"
                            size="mini"
                            style="width: 100%;">
                          </el-date-picker>
                      </el-form-item>
                  </el-col>
              </el-form-item>
              </el-row>
              <el-form-item size="mini">
                  <el-button type="primary" @click="submitOracleForm('oracleform')">Submit</el-button>
              </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="SQL Server">
          <el-form ref="mssqlform" label-position="left" :model="mssqlRestoreForm" label-width="120px" size="mini">
              <el-row>
                  <el-col :span="12">
                  <el-form-item label="Instance Name" required>
                      <el-input v-model="mssqlRestoreForm.srcInstanceName"></el-input>
                  </el-form-item>
                  <el-form-item label="Database Name" required>
                      <el-input v-model="mssqlRestoreForm.srcDbName"></el-input>
                  </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                  <el-form-item label="Client Name" required>
                      <el-select v-model="mssqlRestoreForm.srcClientName" placeholder="please select a client">
                          <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                      </el-select>
                  </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="12">
                      <el-form-item label="Target Database Name" required>
                          <el-input v-model="mssqlRestoreForm.targetDbName"></el-input>
                      </el-form-item>
                      <el-form-item label="Target Instance Name" required>
                          <el-input v-model="mssqlRestoreForm.destInstanceName"></el-input>
                      </el-form-item>
                      <el-form-item label="Target Path(*should be an existing path*)" required>
                          <el-input v-model="mssqlRestoreForm.targetPath"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                      <el-form-item label="Target Client" required>
                          <el-select v-model="mssqlRestoreForm.destClientName" placeholder="please select a target client">
                              <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                          </el-select>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
              <el-form-item label="Backup Time" required>
                  <el-col :span="12">
                      <el-form-item prop="date">
                          <el-date-picker 
                            type="datetime" 
                            placeholder="Select date and time" 
                            v-model="mssqlRestoreForm.backupDate" 
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy-MM-dd HH:mm:ss"
                            default-time="00:00:00"
                            size="mini"
                            style="width: 100%;">
                          </el-date-picker>
                      </el-form-item>
                  </el-col>
              </el-form-item>
              </el-row>
              <el-form-item size="mini">
                  <el-button type="primary" @click="submitMssqlForm('mssqlform')">Submit</el-button>
              </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="MySQL">
          <el-form ref="mysqlform" label-position="left" :model="mysqlRestoreForm" label-width="120px" size="mini">
              <el-row>
                  <el-col :span="12">
                  <el-form-item label="Instance Name" required>
                      <el-input v-model="mysqlRestoreForm.srcInstanceName"></el-input>
                  </el-form-item>
                  <el-form-item label="Database Name" required>
                      <el-input v-model="mysqlRestoreForm.srcDbName"></el-input>
                  </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                  <el-form-item label="Client Name" required>
                      <el-select v-model="mysqlRestoreForm.srcClientName" placeholder="please select a client">
                          <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                      </el-select>
                  </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
                  <el-col :span="12">
                      <el-form-item label="Target Instance Name" required>
                          <el-input v-model="mysqlRestoreForm.destInstanceName"></el-input>
                      </el-form-item>
                  </el-col>
                  <el-col :span="1">-</el-col>
                  <el-col :span="11">
                      <el-form-item label="Target Client" required>
                          <el-select v-model="mysqlRestoreForm.destClientName" placeholder="please select a target client">
                              <el-option v-for="item in clientList" :key="item.id" :lable="item.id" :value="item.name"></el-option>
                          </el-select>
                      </el-form-item>
                  </el-col>
              </el-row>
              <el-row>
              <el-form-item label="Backup Time" required>
                  <el-col :span="12">
                      <el-form-item prop="date">
                          <el-date-picker 
                            type="datetime" 
                            placeholder="Select date and time" 
                            v-model="mysqlRestoreForm.backupDate" 
                            value-format="yyyy-MM-dd HH:mm:ss"
                            format="yyyy-MM-dd HH:mm:ss"
                            default-time="00:00:00"
                            size="mini"
                            style="width: 100%;">
                          </el-date-picker>
                      </el-form-item>
                  </el-col>
              </el-form-item>
              </el-row>
              <el-form-item size="mini">
                  <el-button type="primary" @click="submitMysqlForm('mysqlform')">Submit</el-button>
              </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-main>
    <el-footer style="text-align: left;">
    <el-form :inline="true" :model="formInline" size="mini">
      <el-form-item label="Job ID">
        <el-input type="number" v-model="formInline.jobId" placeholder="Restore job id"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmitFormInline">Query Status</el-button>
      </el-form-item>
    </el-form>
    <el-progress :text-inside="true" :stroke-width="26" :percentage="formInline.percentComplete"></el-progress>
    </el-footer>
  </el-container>    
</template>

<script>
  export default {
    data() {
      return {
        restoreForm: {
          clientname: '',
          clientId: '',
          date: '',
          content: '',
          targetclient: '',
          targetClientId: '',
          targetpath: ''
        },
        db2RestoreForm: {
          srcSubclientName: '',
          srcBackupsetName: '',
          srcInstanceName: '',
          srcClientName: '',
          destClientName: '',
          destInstanceName: '',
          targetDbName: '',
          targetPath: '',
          backupDate: ''
        },
        oracleRestoreForm: {
          srcSubclientName: '',
          srcInstanceName: '',
          srcClientName: '',
          destClientName: '',
          destInstanceName: '',
          targetPath: '',
          backupDate: ''
        },
        mssqlRestoreForm: {
          srcDbName: '',
          srcInstanceName: '',
          srcClientName: '',
          destClientName: '',
          destInstanceName: '',
          targetDbName: '',
          targetPath: '',
          backupDate: ''
        },
        mysqlRestoreForm: {
          srcDbName: '',
          srcInstanceName: '',
          srcClientName: '',
          destClientName: '',
          destInstanceName: '',
          backupDate: ''
        },
        formInline: {
          jobId: '',
          status: '',
          percentComplete: 0
        },
        rules: {
          clientname: [
            { required: true, message: 'please select a client', trigger: 'change' }
          ],
          date: [
            { required: true, message: 'Please select date and time', trigger: 'change' }
          ],
          content: [
            { required: true, message: 'Please input the content to be restored', trigger: 'blur' }
          ],
          targetclient: [
            { required: true, message: 'Please select a target', trigger: 'change' }
          ],
          targetpath: [
            { required: true, message: 'Please input a target path', trigger: 'blur' }
          ]
        },
        clientList: []
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
            });
            const axios = this.GLOBAL.getAxiosInstance(this);
            var that = this;
            axios.post(
              '/data_restore',
              {
                srcClientName: that.restoreForm.clientname,
                srcClientId: that.restoreForm.clientId,
                filePaths: [that.restoreForm.content],
                backupTime: that.restoreForm.date,
                targetClientName: that.restoreForm.targetclient,
                targetClientId: that.restoreForm.targetClientId,
                destPath: that.restoreForm.targetpath
              }
            )
            .then(function (response) {
              //console.log(response);
              if(response && response.data){
                var jobId = response.data.restoreJobId;
                that.$message({
                  type: 'success',
                  message: 'Restore job submitted success, id:' + jobId
                });
                that.formInline.jobId = String(jobId);
                that.formInline.status = '';
                that.formInline.percentComplete = 0;
              }
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 0,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
              that.formInline.jobId = '';
              that.formInline.status = '';
              that.formInline.percentComplete = 0;
            }).then(function () {
              // always executed
              loading.close();
            });
          } else {
            //console.log('error submit!!');
            return false;
          }
        });
      },
      submitDb2Form(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
            });
            const axios = this.GLOBAL.getAxiosInstance(this);
            var that = this;
            axios.post(
              '/data_restore/db2',
              {
                srcSubclientName: that.db2RestoreForm.srcSubclientName,
                srcBackupsetName: that.db2RestoreForm.srcBackupsetName,
                srcInstanceName: that.db2RestoreForm.srcInstanceName,
                srcClientName: that.db2RestoreForm.srcClientName,
                destClientName: that.db2RestoreForm.destClientName,
                destInstanceName: that.db2RestoreForm.destInstanceName,
                targetDbName: that.db2RestoreForm.targetDbName,
                targetPath: that.db2RestoreForm.targetPath,
                backupDate: that.db2RestoreForm.backupDate
              }
            )
            .then(function (response) {
              //console.log(response);
              if(response && response.data){
                var jobId = response.data.restoreJobId;
                that.$message({
                  type: 'success',
                  message: 'Restore job submitted success, id:' + jobId
                });
                that.formInline.jobId = String(jobId);
                that.formInline.status = '';
                that.formInline.percentComplete = 0;
              }
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 0,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
              that.formInline.jobId = '';
              that.formInline.status = '';
              that.formInline.percentComplete = 0;
            }).then(function () {
              // always executed
              loading.close();
            });
          } else {
            //console.log('error submit!!');
            return false;
          }
        });
      },
      submitOracleForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
            });
            const axios = this.GLOBAL.getAxiosInstance(this);
            var that = this;
            axios.post(
              '/data_restore/oracle',
              {
                srcSubclientName: that.oracleRestoreForm.srcSubclientName,
                srcInstanceName: that.oracleRestoreForm.srcInstanceName,
                srcClientName: that.oracleRestoreForm.srcClientName,
                destClientName: that.oracleRestoreForm.destClientName,
                destInstanceName: that.oracleRestoreForm.destInstanceName,
                targetPath: that.oracleRestoreForm.targetPath,
                backupDate: that.oracleRestoreForm.backupDate
              }
            )
            .then(function (response) {
              //console.log(response);
              if(response && response.data){
                var jobId = response.data.restoreJobId;
                that.$message({
                  type: 'success',
                  message: 'Restore job submitted success, id:' + jobId
                });
                that.formInline.jobId = String(jobId);
                that.formInline.status = '';
                that.formInline.percentComplete = 0;
              }
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 0,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
              that.formInline.jobId = '';
              that.formInline.status = '';
              that.formInline.percentComplete = 0;
            }).then(function () {
              // always executed
              loading.close();
            });
          } else {
            //console.log('error submit!!');
            return false;
          }
        });
      },
      submitMssqlForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
            });
            const axios = this.GLOBAL.getAxiosInstance(this);
            var that = this;
            axios.post(
              '/data_restore/mssql',
              {
                srcDbName: that.mssqlRestoreForm.srcDbName,
                srcInstanceName: that.mssqlRestoreForm.srcInstanceName,
                srcClientName: that.mssqlRestoreForm.srcClientName,
                destClientName: that.mssqlRestoreForm.destClientName,
                destInstanceName: that.mssqlRestoreForm.destInstanceName,
                targetDbName: that.mssqlRestoreForm.targetDbName,
                targetPath: that.mssqlRestoreForm.targetPath,
                backupDate: that.mssqlRestoreForm.backupDate
              }
            )
            .then(function (response) {
              //console.log(response);
              if(response && response.data){
                var jobId = response.data.restoreJobId;
                that.$message({
                  type: 'success',
                  message: 'Restore job submitted success, id:' + jobId
                });
                that.formInline.jobId = String(jobId);
                that.formInline.status = '';
                that.formInline.percentComplete = 0;
              }
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 0,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
              that.formInline.jobId = '';
              that.formInline.status = '';
              that.formInline.percentComplete = 0;
            }).then(function () {
              // always executed
              loading.close();
            });
          } else {
            //console.log('error submit!!');
            return false;
          }
        });
      },
      submitMysqlForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
            });
            const axios = this.GLOBAL.getAxiosInstance(this);
            var that = this;
            axios.post(
              '/data_restore/mysql',
              {
                srcDbName: that.mysqlRestoreForm.srcDbName,
                srcInstanceName: that.mysqlRestoreForm.srcInstanceName,
                srcClientName: that.mysqlRestoreForm.srcClientName,
                destClientName: that.mysqlRestoreForm.destClientName,
                destInstanceName: that.mysqlRestoreForm.destInstanceName,
                backupDate: that.mysqlRestoreForm.backupDate
              }
            )
            .then(function (response) {
              //console.log(response);
              if(response && response.data){
                var jobId = response.data.restoreJobId;
                that.$message({
                  type: 'success',
                  message: 'Restore job submitted success, id:' + jobId
                });
                that.formInline.jobId = String(jobId);
                that.formInline.status = '';
                that.formInline.percentComplete = 0;
              }
            })
            .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 0,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
              that.formInline.jobId = '';
              that.formInline.status = '';
              that.formInline.percentComplete = 0;
            }).then(function () {
              // always executed
              loading.close();
            });
          } else {
            //console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      },
      selectClientChange(val) {//回显关键性代码，每一次选择后执行，val是上面dom的value值
        var obj = {};
        obj = this.clientList.find(function(item) {//obj是选中的对象
            return item.name === val;
        });
        this.restoreForm.clientId = obj.id;//提交数据使用，此处可忽略
        this.restoreForm.clientname = obj.name;//用于回显名称
      },
      selectTargetChange(val) {
        var obj = {};
        obj = this.clientList.find(function(item) {
            return item.name === val;
        });
        this.restoreForm.targetClientId = obj.id;
        this.restoreForm.targetclient = obj.name;
      },
      onSubmitFormInline(){
        if(this.GLOBAL.isEmpty(this.formInline.jobId) || this.GLOBAL.isEmpty(this.formInline.jobId.trim()) || this.formInline.jobId < 0){
          this.$message({
            type: 'error',
            message: "please input a valid Job ID",
            showClose: true
          });
          return;
        }
        const loading = this.$loading({
          lock: true,                             // 是否锁屏
          text: 'Loading...',                     // 加载动画的文字
          spinner: 'el-icon-loading'              // 引入的loading图标
        });
        const axios = this.GLOBAL.getAxiosInstance(this);
        var that = this;
        axios.get('/job/' + that.formInline.jobId)
        .then(function (response) {
          // console.log(response);
          if(response && response.data){
            that.formInline.status = response.data.status;
            that.formInline.percentComplete = response.data.percentComplete;
            that.$message({
              type: 'info',
              message: "Complete percentage: " + that.formInline.percentComplete + "%",
              showClose: true
            });
          }
        })
        .catch(function (error) {
          // console.log(error);
          that.$message({
            type: 'error',
            message: that.GLOBAL.getAxiosErrorMessage(error),
            showClose: true
          });
        }).then(function () {
          // always executed
          loading.close();
        });
      },
      async getClient(){
        let result = [];
        const loading = this.$loading({
              lock: true,                             // 是否锁屏
              text: 'Loading...',                     // 加载动画的文字
              spinner: 'el-icon-loading'              // 引入的loading图标
        });
        const axios = this.GLOBAL.getAxiosInstance(this);
        var that = this;
        await axios.get('/client')
        .then(function (response) {
              //console.log(response);
              if(response && response.data){
                result = response.data.clientList;
              }
        })
        .catch(function (error) {
              that.$message({
                type: 'error',
                duration: 5000,
                message: that.GLOBAL.getAxiosErrorMessage(error),
                showClose: true
              });
        }).then(function () {
              // always executed
              loading.close();
        });

        this.clientList = result;
        return result;
      }
    },
    created() {
        this.getClient();
    }
  };
</script>