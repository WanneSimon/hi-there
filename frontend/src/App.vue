<template>
  <client-only>
    <inner-app></inner-app>
  </client-only>
</template>

<style lang="scss">
@use './assets/css/global.scss' as *; 
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}
</style>

<script setup>
import InnerApp from './InnerApp.vue';
import ClientOnly from '@duannx/vue-client-only';
</script>