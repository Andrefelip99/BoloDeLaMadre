import { createRouter, createWebHistory } from 'vue-router'
import { readSession, isAdmin } from './services/session'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: () => readSession() ? '/dashboard' : '/login' },
    { path: '/login', name: 'login', component: () => import('./views/LoginView.vue'), meta: { guest: true } },
    { path: '/dashboard', name: 'dashboard', component: () => import('./views/DashboardView.vue'), meta: { auth: true } },
    { path: '/admin/financeiro', name: 'financeiro', component: () => import('./views/ModuleView.vue'), props: { moduleKey: 'financeiro' }, meta: { auth: true, admin: true } },
    { path: '/admin/funcionarios', name: 'funcionarios', component: () => import('./views/ModuleView.vue'), props: { moduleKey: 'funcionarios' }, meta: { auth: true, admin: true } },
    { path: '/admin/usuarios', name: 'usuarios', component: () => import('./views/ModuleView.vue'), props: { moduleKey: 'usuarios' }, meta: { auth: true, admin: true } },
    { path: '/:module(produtos|vendas|compras|clientes|fornecedores|categorias|ingredientes|receitas|estoque)', name: 'module', component: () => import('./views/ModuleView.vue'), props: (route) => ({ moduleKey: route.params.module }), meta: { auth: true } },
    { path: '/403', name: 'forbidden', component: () => import('./views/StatusView.vue'), props: { code: '403' }, meta: { auth: true } },
    { path: '/:pathMatch(.*)*', name: 'not-found', component: () => import('./views/StatusView.vue'), props: { code: '404' } },
  ],
})

router.beforeEach((to) => {
  const session = readSession()
  if (to.meta.auth && !session) return { name: 'login', query: { redirect: to.fullPath } }
  if (to.meta.guest && session) return { name: 'dashboard' }
  if (to.meta.admin && !isAdmin(session)) return { name: 'forbidden' }
  return true
})

export default router
