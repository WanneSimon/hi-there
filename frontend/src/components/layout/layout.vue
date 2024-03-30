<template>
    <div class="app-main">
      <!-- menu sidebar, 左侧菜单 -->
      <!-- <div v-for="child in menuOptions" :key="'submenu_wrapper_' + child.key">
      </div> -->

      <div class="app-content">
        <content-header></content-header>
        <common-checker ref="layout_checker"></common-checker>
        <router-view></router-view>
      </div>
    </div>
</template>

<script>
import ContentHeader from './content-header.vue'
import CommonChecker from './common-checker.vue'
import routes from '/src/router/router-options.js'
import globalStore from '/src/components/pinia/store.js'

// import { CaretDown24Filled } from '@vicons/fluent'
// 筛选是菜单的路由
const filterMenu = (routes) => {
  if(!routes) {
    return routes
  }
  return routes.filter(e => e && e.co_menu && (!e.co_menu.disabled))
}

// 路由转菜单 （注：菜单中路由中不允许出现动态路由）
const routeToMenuOption = (store, basekey, routes, basePath) => {
  let arr = []
  basePath = basePath ? basePath : ''

  for(let i=0; i<routes.length; i++) {
    let t = routes[i]
    let opt = t.co_menu
    
    if(!opt) {
      continue
    }

    // 检查权限
    //console.log("opt.permission", opt.permission)
    if(opt.permission && store && !store.hasPermission(opt.permission)) {
      continue
    }
    
    // 设置一个key
    // opt.key = basekey + i
    // opt.key = t.path.startsWith("/") ? t.path : basekey + "/" + t.path

    // debugger
    let currPath = basePath
    if(t.path) {
      currPath = t.path.startsWith('/') ? t.path : basePath +"/"+ t.path
    }

    // 没有子节点的菜单设置跳转 url
    if(!t.children) {
        opt.href = currPath
    } else if(t.children && t.children.length>0) {
      opt.__href = currPath + i // 虚拟一个假路径

      // 只有一个子节点的情况，提升子节点为单个菜单栏
      if(t.children.length == 1 && !opt.alwaysShow && t.children[0].co_menu) {
        let fchild = t.children[0]

        opt = fchild.co_menu
        // opt.key = basekey + i
        // opt.key = fchild.path.startsWith("/") ? fchild.path : basekey + "/" + fchild.path
        // 忘记检查权限
        if(opt.permission && store && !store.hasPermission(opt.permission)) {
          continue
        }

        opt.href = fchild.path.startsWith("/") ? fchild.path : currPath + "/" + fchild.path
        // opt.key = opt.href
      } else {
        const childrenOption = routeToMenuOption(store, opt.key, t.children, currPath)
        opt.children = childrenOption
      }
    }

    // console.log('p', opt.label, opt.permission)
    opt.key = opt.href ||opt.__href
    //console.log("key", opt.key)
    // 存在子节点，并且子节点为空的情况，舍弃
    if( opt.children ) {
      if(opt.children.length==0) {
        continue
      } else if(opt.children.length==1 && !opt.alwaysShow ) {
        // 再次检查是否只有一个子节点（因为加入权限后，多了一种情况）
        arr.push(opt.children[0])
      } else {
        arr.push(opt)
      }
    } else {
      // 没有子节点，直接并入
      arr.push(opt)
    }
  }
  return arr
}
//=====

export default {
  components: { ContentHeader, CommonChecker },
  setup() {
    const store = globalStore()
    return {
      store
    }
  },
  data () {
    return {
      collapsed: false,
      defaultExpandedKey: this.$route.path,
    }
  },
  computed: {
    menuOptions() {
      return this.renderMenu()
    }
  }, 
  created() {
  },
  mounted() {
  },
  methods: {
    renderMenu() {
      let menuRoutes =  filterMenu(routes)
      const _menuOptions = routeToMenuOption(this.store, "", menuRoutes)
      return _menuOptions
    },
    //=====
  }
    
}
</script>

<style lang="scss" scoped>
.app-main {
  display: flex;
  height: 100vh;

  :deep(.el-layout-scroll-container){
    overflow-x: auto;
  }
  .app-content{
    min-width: 400px;
    flex: 1;
  }
}
</style>