
import {createRouter, createWebHistory } from 'vue-router'
import routes from './router-options'

const basePath = import.meta.env.VITE_FRONT_BASE

const router = createRouter({
  history: createWebHistory(basePath),
  routes,
})

router.beforeEach( async (to, from, next) => {
  next()
})

export default router