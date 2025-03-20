import { createApp } from 'vue'
import router from './router'
import './styles/style.css'
import './styles/auth.css'
import 'element-plus/theme-chalk/src/message.scss'
import 'element-plus/theme-chalk/src/message-box.scss'
import App from './App.vue'

createApp(App).use(router).mount('#app')
