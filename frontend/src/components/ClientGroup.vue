<template>
  <el-container>
    <el-header style="text-align: left;">
      <el-button @click="handleAdd">Add Client Group</el-button>
    </el-header>
    <el-main>
    <div>
      <h3>Client Group List</h3>
      <div style="border: 1px solid;
                  border-radius: 3px;
                  transition: .2s;
                  display: block;">
        <div>
          <el-table
            :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
            style="width: 100%">
            <el-table-column
              prop="id"
              label="Id"
              sortable>
            </el-table-column>
            <el-table-column
              prop="name"
              label="Name"
              sortable>
            </el-table-column>
            <el-table-column
              align="right">
              <template slot="header">
                <input
                  v-model="search"
                  size="mini"
                  placeholder="Type name to search"/>
              </template>
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
                <el-button
                  size="mini"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <el-dialog title="Add Client Group" :visible.sync="dialogFormVisible" :close-on-click-modal="false">
        <el-form ref="clientGroupForm" :model="form" :rules="rules">
          <el-form-item label="Group name" :label-width="formLabelWidth" prop="name">
            <el-input v-model="form.name" autocomplete="on"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="addClientGroup()">Confirm</el-button>
        </span>
      </el-dialog>
      <el-dialog title="Edit Client Group" :visible.sync="editFormVisible" :close-on-click-modal="false">
        <el-form ref="clientGroupEditForm" :model="form" :rules="rules">
          <el-form-item label="Group name" :label-width="formLabelWidth" prop="name">
            <el-input v-model="form.name" autocomplete="on"></el-input>
          </el-form-item>
          <el-input v-model="form.id" type="hidden"></el-input>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="editFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="editClientGroup()">Confirm</el-button>
        </span>
      </el-dialog>
    </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'ClientGroup',
  // mounted 方法为钩子，在模板渲染成html后调用，通常是初始化页面完成后，再对html的dom节点进行一些需要的操作
  mounted() {
    this.getClientGroupList();
  },
  data () {
    return {
      tableData:[],
      search: '',
      dialogFormVisible: false,
      editFormVisible: false,
      form: {
        id: '',
        name: ''
      },
      entry: {},
      formLabelWidth: '120px',
      rules: {
        name: [
          {required: true, message: 'Group name is required', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    addClientGroup() {
      this.$refs.clientGroupForm.validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,                             // 是否锁屏
            text: 'Loading...',                     // 加载动画的文字
            spinner: 'el-icon-loading'              // 引入的loading图标
          });
          const axios = this.GLOBAL.getAxiosInstance(this);
          var that = this;
          axios.post(
            '/client_group',
            {
              name: that.form.name
            }
          )
          .then(function (response) {
            //console.log(response);
            if(response && response.data){
              that.tableData.push(response.data.clientGroup);
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
          this.dialogFormVisible = false;
        }
      });
    },
    editClientGroup() {
      this.$refs.clientGroupEditForm.validate((valid) => {
        if (valid) {
          const loading = this.$loading({
            lock: true,                             // 是否锁屏
            text: 'Loading...',                     // 加载动画的文字
            spinner: 'el-icon-loading'              // 引入的loading图标
          });
          const axios = this.GLOBAL.getAxiosInstance(this);
          var that = this;
          axios.post(
            '/client_group/' + that.form.id,
            {
              name: that.form.name
            }
          )
          .then(function (response) {
            //console.log(response);
            that.tableData.splice(that.tableData.indexOf(that.entry), 1, {id: that.form.id, name: that.form.name});
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
    getClientGroupList() {
      const loading = this.$loading({
        lock: true,                             // 是否锁屏
        text: 'Loading...',                     // 加载动画的文字
        spinner: 'el-icon-loading'              // 引入的loading图标
      });
      const axios = this.GLOBAL.getAxiosInstance(this);
      var that = this;
      axios.get('/client_group')
      .then(function (response) {
        // console.log(response);
        if(response && response.data){
          that.tableData = response.data.clientGroupList;
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
    handleAdd(){
      this.form.name = '';
      if(this.$refs.clientGroupForm) {
        this.resetForm(this.$refs.clientGroupForm);
      }
      this.dialogFormVisible = true;
    },
    handleEdit(index, row) {
      // console.log(index, row);
      if(this.$refs.clientGroupEditForm) {
        this.resetForm(this.$refs.clientGroupEditForm);
      }
      this.editFormVisible = true;
      this.form.id = row.id;
      this.form.name = row.name;
      this.entry = row;
    },
    handleDelete(index, row) {
      // console.log(index, row);
      this.$confirm('Permanently delete the client group "' + row.name + '". Continue?', 'Warning', {
          confirmButtonText: 'OK',
          cancelButtonText: 'Cancel',
          closeOnClickModal: false,
          type: 'warning'
        }).then(() => {
          const loading = this.$loading({
            lock: true,                             // 是否锁屏
            text: 'Loading...',                     // 加载动画的文字
            spinner: 'el-icon-loading'              // 引入的loading图标
          });
          const axios = this.GLOBAL.getAxiosInstance(this);
          var that = this;
          axios.delete('/client_group/' + row.id)
          .then(function (response) {
            that.tableData.splice(that.tableData.indexOf(row), 1);
            that.$message({
              type: 'success',
              message: 'Delete completed'
            });
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
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: 'Delete canceled'
          // });          
        });
    }
  }
}
</script>

<style>
</style>