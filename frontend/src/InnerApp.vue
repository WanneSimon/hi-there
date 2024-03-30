
<template>
  <router-view :key="'outer_route_' + fullPath"></router-view>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const fullPath = computed(() => route.fullPath)
</script>