<template>
  <div class="nav-head float-block">
      <label class="icon-label">
        <router-link :to="'/home'" title="Home" target="self">
          <el-button size="small" color="#626aef">Home</el-button>
        </router-link>
      </label>
      <label class="icon-label">
        <router-link :to="'/emoji'" title="Emoji">
          <el-button size="small" color="#626aef">Emoji</el-button>
        </router-link>
      </label>
      <label class="icon-label" title="小工具">
        <router-link :to="'/tools'">
          <el-button size="small" color="#626aef"> 小工具</el-button>
        </router-link>
      </label>
    
    <!--
      <label class="right-pos">
        <el-button size="small" @click="goBack" title="返回">
            <el-icon><ArrowLeftBold /></el-icon></el-button>
        <el-button size="small" type="warning" @click="min" title="最小化">
          <el-icon><Minus /></el-icon></el-button>
        <el-button size="small" type="danger" @click="exit" title="关闭">
          <el-icon><Close /></el-icon></el-button>
      </label>
    -->
  </div>
</template>

<script setup lang="ts" >
import { useRouter } from "vue-router"

const router = useRouter()

const goBack = () => {
  // this.$router.back()
  router.back()
}
const exit = () =>  {
  // Exit()
}
const min= () => {
  // Minimises()
}
</script>

<style scoped lang="scss">
@use './head-nav-var.scss' as *;
.nav-head{
  border-radius: unset;
  padding: 0rem 0.4rem;
  height: $head-nav-height;
  line-height: $head-nav-height;
  position: relative;

  padding-left: 10rem;
  padding-right: 10rem;

  text-align: center;
  background-color: rgba($color: #000000, $alpha: 0.3);
  a {
    color: #606266 !important;
  }
  a:hover {
    color: #409eff !important;
  }

  .right-pos{
    position: absolute;
    right: 1px;
    top: 0;
    width: rem;
  }
}

.icon-label:nth-child(n+2){
  margin-left: 1rem;
}

:deep(.el-button+.el-button) {
  margin-left: 1px;
}
</style>
