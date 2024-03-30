import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index'
import metaManager from './components/vue-meta/meta-manager.js'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(metaManager)

app.mount('#app')
