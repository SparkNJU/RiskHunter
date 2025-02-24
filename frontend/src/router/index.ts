import { createRouter, createWebHashHistory } from "vue-router"

const router = createRouter({
    history: createWebHashHistory(),
    routes: [{
        path: '/',
        component: () => import('../components/Layout.vue'),
        children: [
            {
                path: '',
                redirect: '/about'
            },
            {
                path: '/about',
                name: 'About',
                component: () => import('../views/About.vue'),
                meta: { title: '关于我们', requiresAuth: false }
            },
            {
                path: '/login',
                name: 'Login',
                component: () => import('../views/user/Login.vue'),
                meta: { title: '用户登录', requiresAuth: false }
            },
            {
                path: '/register',
                name: 'Register',
                component: () => import('../views/user/Register.vue'),
                meta: { title: '用户注册', requiresAuth: false }
            },
            {
                path: '/dashboard',
                name: 'Dashboard',
                component: () => import('../views/user/Dashboard.vue'),
                meta: { title: '个人信息', requiresAuth: true }
            },
            {
                path: '/risk-signal',
                name: 'RiskSignals',
                component: () => import('../views/RiskSignal.vue'),
                meta: { title: '风险信号', requiresAuth: true }
            },
            {
                path: '/fx-data',
                name: 'FXData',
                component: () => import('../views/FXData.vue'),
                meta: { title: '外汇数据', requiresAuth: true }
            },
            {
                path: '/news',
                name: 'News',
                component: () => import('../views/News.vue'),
                meta: { title: '新闻资讯', requiresAuth: true }
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

router.beforeEach((to, _, next) => {
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
    next()
})

export { router }