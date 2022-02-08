import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'Home',
    component: Home
  },
  {
    path: '/leaderboard',
    name: 'Leaderboard',
    component: () => import('../views/Leaderboard.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/daily',
    name: 'DailyLeaderboard',
    component: () => import('../views/DailyLeaderboard.vue')
  },
  {
    path: '/noDailyChallenge',
    name: 'NoDailyChallenge',
    component: () => import('../views/NoDailyChallenge.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
