// const componentModules = import.meta.globEager('/src/views/**/*.vue') // 立即引入
const componentModules = import.meta.glob('/src/views/**/*.vue') // 懒加载

//import Layout from '/src/components/layout/layout.vue'
//import EmptyView from '/src/views/components/empty-view.vue'
import FrontLayout from '/src/views/components/front-layout.vue'

function _importView(_path){
  return componentModules[`/src/views/${_path}`]
}
function _importFrontView(_path){
  return componentModules[`/src/views/front/${_path}`]
}
function _import(_path){
  return componentModules[`/src/${_path}`]
}

const routes = [
    { path: '/', redirect: '/home' },
    { 
      path: "/",
      component: FrontLayout, 
      children: [
        { path: "/404", name: "notFound", component: _importFrontView('404.vue') },
        { path: '/home', component: _importFrontView("home/home-v3.vue") },
        { path: '/tools', component: _importFrontView("tools/tools.vue") },
        { path: '/emoji', component: _importFrontView("emoji/emoji.vue") },
        // flv拉流 (flv.js)
        { name: 'live_', path: 'live',
          components: {
            default: _importFrontView('tools/live/live.vue')
          }
        },
        // 随机猫猫
        { name: 'randomCats', path: 'cats',
          components: {
            default: _importFrontView('tools/the-cat-api/the-cat-api.vue')
          }
        },
      ]
    },
    { 
      path: "/tool",
      component: FrontLayout, 
      children: [
        // flv拉流 (flv.js)
        { name: 'live_', path: 'live',
          components: {
            default: _importFrontView('tools/live/live.vue')
          }
        },
        // 随机猫猫 
        { name: 'randomCats', path: 'cats',
          components: {
            default: _importFrontView('tools/the-cat-api/the-cat-api.vue')
          }
        },
      ]
    },

    // others
    // ========
    // { path: "/404", name: "notFound", redirect: '/home' },
    {
      path: "/:pathMatch(.*)", 
      redirect: "/404",
    }
  ]

export default routes