<template>
  <div class="app-header">
    <HeadNav></HeadNav>
  </div>
  <div class="app-main">
    <div class="app-content">
      <router-view></router-view>
    </div>
  </div>
</template>

<script setup>
import HeadNav from '@/views/components/head-nav/head-nav.vue'

</script>

<style lang="scss" scoped>
@use './head-nav/head-nav-var.scss' as *;

.app-main {
  display: flex;
  height: calc( 100vh - $head-nav-height);

  :deep(.el-layout-scroll-container){
    overflow-x: auto;
  }
  .app-content{
    min-width: 400px;
    flex: 1;
  }
}
</style>