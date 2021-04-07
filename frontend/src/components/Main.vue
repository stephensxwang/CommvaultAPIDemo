<template>
    <el-carousel :interval="4000" type="card">
      <el-carousel-item v-for="item in menus" :key="item.name">
        <h3 class="medium">
          <router-link :to="item.link">{{ item.name }}</router-link>
        </h3>
      </el-carousel-item>
    </el-carousel>
</template>

<script>
export default {
  name: 'Main',
  data() {
    return {
      menus: [
        {name: 'Client Group', link: 'client_group'}, 
        {name: 'Batch Configuration', link: 'batchConfig'}, 
        {name: 'Batch Install', link: 'batchInstall'}, 
        {name: 'Restore', link: 'restore'}, 
        {name: 'Setting', link: 'sys_config'}
      ]
    }
  }
}
</script>

<style>
  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }
</style>