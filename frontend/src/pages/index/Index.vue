<template>
  <el-container>
    <el-header class="header" height="100px">
      <h1>Commvault Cloud Platform</h1>
      <p>Please login with CommServe credential</p>
    </el-header>
    <el-main>
      <router-view/>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: 'Index'
}
</script>

<style>
  .header{
    background-color: black;
    color: white;
    text-align: center;
  }
</style>