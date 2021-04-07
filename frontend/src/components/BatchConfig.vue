<template>
  <el-container>
    <el-header style="text-align: left;">
      <el-upload
        class="upload-demo"
        accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
        ref="upload"
        action=""
        :http-request='handleFile'
        :on-change="changeFile"
        :multiple="false" 
        :limit="1" 
        :file-list="file_path"
        :auto-upload="false">
        <el-button slot="trigger" size="small">select file</el-button>
        <el-button style="margin-left: 10px;" size="small" type="primary" @click="submitUpload">upload to server</el-button>
        <el-button size="small" @click="handleSubmit" type="primary">Submit Configuration</el-button>
        <el-link type="info" href="/file/subclientconfig.xlsx">Configuration Template</el-link>
        <div class="el-upload__tip" slot="tip">excel file with subclient configuration info</div>
      </el-upload>
    </el-header>
    <el-main>
      <el-table highlight-current-row :data="tableData" empty-text="Please upload the configuration" style="top: 20px;width: 100%">
        <template v-for="(col, index) in cols">
          <el-table-column v-if="col.type==='normal'" :prop="col.prop" :label="col.label" v-bind:key="index" width="180">
          </el-table-column>
          <el-table-column v-if="col.type==='sort'" :prop="col.prop" sortable :label="col.label" v-bind:key="index" width="180">
          </el-table-column>
        </template>
      </el-table>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'BatchConfig',
  data() {
    return {
      file_path:[],
      cols: [],
      tableData: []
    }
  },
  methods: {
    handleFile(){},
    changeFile(file, fileList){
      this.file_path = fileList;
    },
    submitUpload() {
      // this.$refs.upload.submit();
      const loading = this.$loading({
        lock: true,                             // 是否锁屏
        text: 'Loading...',                     // 加载动画的文字
        spinner: 'el-icon-loading'              // 引入的loading图标
      });
      const axios = this.GLOBAL.getAxiosInstance(this);
      var that = this;
          
      let formData = new FormData();
      formData.append("file", this.file_path[0] ? this.file_path[0].raw : '');

      axios.post(
        '/batch_config/fileupload',
        formData, 
        { headers: { 'Content-Type': 'multipart/form-data' } }
      )
      .then(function (response) {
        //console.log(response);
        if(response && response.data){
          that.cols = response.data.titleList;
          that.tableData = response.data.contentList;
          that.$refs.upload.clearFiles();
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
    },
    handleSubmit() {
      if(this.tableData && this.tableData.length == 0){
        this.$message({
          type: 'error',
          duration: 5000,
          message: "Please upload the configuration first",
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
      axios.post(
        '/batch_config/config',
        {
          contentList: that.tableData
        }
      )
      .then(function (response) {
        //console.log(response);
        that.$message({
          type: 'success',
          message: 'Update success'
        });
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
    }
  }
}
</script>

<style>
</style>