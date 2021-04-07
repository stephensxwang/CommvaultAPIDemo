<template>
  <el-container>
    <el-main>
    <div>
      <h3>Property List</h3>
      <div style="border: 1px solid;
                  border-radius: 3px;
                  transition: .2s;
                  display: block;">
        <div>
          <el-table
            :data="tableData.filter(data => !searchId || data.id.toLowerCase().includes(searchId.toLowerCase()))"
            style="width: 100%">
            <el-table-column
              prop="id"
              label="Id"
              sortable>
            </el-table-column>
            <el-table-column
              label="Value">
              <template slot-scope="scope">
                <input v-model="form.value" v-if="form.editIndex === scope.$index"/>
                <span v-else>{{scope.row.value}}</span>
              </template>
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header">
                <input
                  v-model="searchId"
                  size="mini"
                  placeholder="Type id to searchId"/>
              </template>
              <template slot-scope="scope">
                <div v-if="form.editIndex === scope.$index">
                  <el-button
                  size="mini"
                  type="primary"
                  @click="handleSave(scope.$index, scope.row)">Save</el-button>
                  <el-button
                  size="mini"
                  type="danger"
                  @click="form.editIndex = -1">Cancel</el-button>
                </div>
                <el-button
                  size="mini"
                  @click="handleEditInline(scope.$index, scope.row)" v-else>Edit</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <el-dialog title="Edit Property Value" :visible.sync="editFormVisible" :close-on-click-modal="false">
        <el-form ref="propertyEditForm" :model="form" :rules="rules">
          <el-form-item label="Value" :label-width="formLabelWidth" prop="value">
            <el-input v-model="form.value" autocomplete="on"></el-input>
          </el-form-item>
          <el-input v-model="form.id" type="hidden"></el-input>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="editCvProperty()">Confirm</el-button>
        </span>
      </el-dialog>
    </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'SysConfig',
  // mounted 方法为钩子，在模板渲染成html后调用，通常是初始化页面完成后，再对html的dom节点进行一些需要的操作
  mounted() {
    this.getCvPropertyList();
  },
  data () {
    return {
      tableData:[],
      searchId: '',
      editFormVisible: false,
      form: {
        id: '',
        value: '',
        editIndex: -1
      },
      entry: {},
      formLabelWidth: '120px',
      rules: {
        value: [
          {required: true, message: 'Value is required', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    editCvProperty() {
      this.$refs.propertyEditForm.validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,                             // 是否锁屏
            text: 'Loading...',                     // 加载动画的文字
            spinner: 'el-icon-loading'              // 引入的loading图标
          });
          const axios = this.GLOBAL.getAxiosInstance(this);
          var that = this;
          axios.post(
            '/cv_property/' + that.form.id,
            {
              id: that.form.id,
              value: that.form.value
            }
          )
          .then(function (response) {
            //console.log(response);
            that.tableData.splice(that.tableData.indexOf(that.entry), 1, {id: that.form.id, value: that.form.value});
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
          this.editFormVisible = false;
        }
      });
    },
    getCvPropertyList() {
      const loading = this.$loading({
        lock: true,                             // 是否锁屏
        text: 'Loading...',                     // 加载动画的文字
        spinner: 'el-icon-loading'              // 引入的loading图标
      });
      const axios = this.GLOBAL.getAxiosInstance(this);
      var that = this;
      axios.get('/cv_property')
      .then(function (response) {
        // console.log(response);
        if(response && response.data){
          that.tableData = response.data.cvPropertyList;
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
    resetForm(form) {
      form.resetFields();
    },
    handleEdit(index, row) {
      // console.log(index, row);
      if(this.$refs.propertyEditForm) {
        this.resetForm(this.$refs.propertyEditForm);
      }
      this.editFormVisible = true;
      this.form.id = row.id;
      this.form.value = row.value;
      this.entry = row;
    },
    handleEditInline(index, row){
      this.form.id = row.id;
      this.form.value = row.value;
      this.form.editIndex = index;
      this.entry = row;
    },
    handleSave(index, row){
      const loading = this.$loading({
        lock: true,                             // 是否锁屏
        text: 'Loading...',                     // 加载动画的文字
        spinner: 'el-icon-loading'              // 引入的loading图标
      });
      const axios = this.GLOBAL.getAxiosInstance(this);
      var that = this;
      axios.post(
        '/cv_property/' + that.form.id,
        {
          id: that.form.id,
          value: that.form.value
        }
      )
      .then(function (response) {
        //console.log(response);
        that.tableData.splice(that.tableData.indexOf(that.entry), 1, {id: that.form.id, value: that.form.value});
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
        that.form.editIndex = -1;
      });
    }
  }
}
</script>

<style>
</style>