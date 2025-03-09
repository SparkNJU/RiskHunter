import { createRouter, createWebHashHistory } from "vue-router"
import Layout from "../components/layout/Layout.vue"
import Home from "../views/home/Home.vue"
import Login from "../views/user/Login.vue"
import Register from "../views/user/Register.vue"
import Profile from "../views/user/Profile.vue"
import RiskSignal from "../views/signal/RiskSignal.vue"
import ForexData from "../views/forex/ForexData.vue"
import News from "../views/news/News.vue"
import Chat from "../views/chat/Chat.vue"

const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
        path: '/',
        component: Layout,
        children: [
            {
                path: '',
                redirect: '/home'
            },
            {
                path: '/home',
                name: 'Home',
                component: Home,
                meta: { title: '关于我们', requiresAuth: false }
            },
            {
                path: '/login',
                name: 'Login',
                component: Login,
                meta: { title: '用户登录', requiresAuth: false }
            },
            {
                path: '/register',
                name: 'Register',
                component: Register,
                meta: { title: '用户注册', requiresAuth: false }
            },
            {
                path: '/profile',
                name: 'Profile',
                component: Profile,
                meta: { title: '个人信息', requiresAuth: true }
            },
            {
                path: '/risk-signal',
                name: 'RiskSignals',
                component: RiskSignal,
                meta: { title: '风险信号', requiresAuth: true }
            },
            {
                path: '/forex-data',
                name: 'ForexData',
                component: ForexData,
                meta: { title: '外汇数据', requiresAuth: true }
            },
            {
                path: '/news',
                name: 'News',
                component: News,
                meta: { title: '新闻资讯', requiresAuth: true }
            },
            {
                path: '/chat',
                name: 'Chat',
                component: Chat,
                meta: { title: '智能体', requiresAuth: true }
            }
        ]
    }, {
        path: '/404',
        name: '404',
        component: () => import('../views/NotFound.vue'),
        meta: { title: '404' }
    }, {
        path: '/:catchAll(.*)',
        redirect: '/404'
    }]
})

// 路由守卫
//router.beforeEach((to, _, next) => {
    // const token: string | null = sessionStorage.getItem('token');
    // const role: string | null = sessionStorage.getItem('role')

    // if (to.meta.title) {
    //     document.title = to.meta.title
    // }

    // if (token) {
    //     if (to.meta.permission) {
    //         if (to.meta.permission.includes(role!)) {
    //             next()
    //         } else {
    //             next('/404')
    //         }
    //     } else {
    //         next()
    //     }
    // } else {
    //     if (to.path === '/login') {
    //         next();
    //     } else if (to.path === '/register') {
    //         next()
    //     } else {
    //         next('/login')
    //     }
    // }
    //next()
//})

export default router