<script setup>
import { ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { LayoutDashboard, Package, ShoppingBag, ShoppingCart, Users, Truck, Tags, Wheat, ChefHat, Boxes, Wallet, ContactRound, ShieldCheck, LogOut, Menu, X, CakeSlice } from 'lucide-vue-next'
import { clearSession, readSession, isAdmin } from '../services/session'
import AssistantPanel from './AssistantPanel.vue'

const route = useRoute(); const router = useRouter(); const drawerOpen = ref(false)
const session = readSession(); const admin = isAdmin(session)
const groups = [
  { title: 'Visão geral', items: [{ label: 'Painel', to: '/dashboard', icon: LayoutDashboard }] },
  { title: 'Operação', items: [
    { label: 'Produtos', to: '/produtos', icon: Package }, { label: 'Vendas', to: '/vendas', icon: ShoppingBag },
    { label: 'Compras', to: '/compras', icon: ShoppingCart }, { label: 'Clientes', to: '/clientes', icon: Users },
    { label: 'Fornecedores', to: '/fornecedores', icon: Truck }, { label: 'Categorias', to: '/categorias', icon: Tags },
    { label: 'Ingredientes', to: '/ingredientes', icon: Wheat }, { label: 'Receitas', to: '/receitas', icon: ChefHat },
    { label: 'Estoque', to: '/estoque', icon: Boxes },
  ] },
  ...(admin ? [{ title: 'Administração', items: [
    { label: 'Financeiro', to: '/admin/financeiro', icon: Wallet }, { label: 'Funcionários', to: '/admin/funcionarios', icon: ContactRound },
    { label: 'Usuários', to: '/admin/usuarios', icon: ShieldCheck },
  ] }] : []),
]
function logout() { clearSession(); router.replace('/login') }
</script>

<template>
  <div class="app-frame">
    <button class="mobile-scrim" v-if="drawerOpen" @click="drawerOpen=false" aria-label="Fechar menu"></button>
    <aside class="sidebar" :class="{ 'sidebar-open': drawerOpen }">
      <div class="brand"><div class="brand-mark"><CakeSlice :size="21" /></div><div><strong>BoloDeLaMadre</strong><small>GESTÃO EMPRESARIAL</small></div><button class="icon-button sidebar-close" @click="drawerOpen=false" aria-label="Fechar menu"><X :size="18" /></button></div>
      <div class="workspace-label"><span class="status-dot"></span> Ambiente {{ admin ? 'administrativo' : 'operacional' }}</div>
      <nav class="side-nav">
        <section v-for="group in groups" :key="group.title" class="nav-group">
          <p>{{ group.title }}</p>
          <RouterLink v-for="item in group.items" :key="item.to" :to="item.to" class="nav-link" :class="{ active: route.path===item.to }" @click="drawerOpen=false"><component :is="item.icon" :size="17" :stroke-width="1.8"/><span>{{ item.label }}</span></RouterLink>
        </section>
      </nav>
      <div class="sidebar-bottom"><div class="profile-chip"><div class="avatar">{{ session?.username?.slice(0,1)?.toUpperCase() || 'U' }}</div><div class="profile-copy"><strong>{{ session?.username }}</strong><small>{{ admin ? 'Administrador' : 'Operacional' }}</small></div><button class="icon-button" @click="logout" title="Sair" aria-label="Sair"><LogOut :size="17" /></button></div></div>
    </aside>
    <div class="main-column">
      <header class="topbar"><button class="icon-button mobile-menu" @click="drawerOpen=true" aria-label="Abrir menu"><Menu :size="20" /></button><div class="breadcrumbs"><span>Gestão</span><span class="crumb-sep">/</span><strong>{{ route.name==='dashboard' ? 'Painel' : route.name==='financeiro' ? 'Financeiro' : route.name==='funcionarios' ? 'Funcionários' : route.name==='usuarios' ? 'Usuários' : (route.params.module || 'Operação') }}</strong></div><div class="topbar-right"><span class="live-indicator"><i></i> Sessão ativa</span><div class="top-avatar">{{ session?.username?.slice(0,1)?.toUpperCase() || 'U' }}</div></div></header>
      <main class="page-content"><slot /></main>
      <footer class="app-footer"><span>Desenvolvido por: André Felipe</span><span>© {{ new Date().getFullYear() }} BoloDeLaMadre · Dados de demonstração</span></footer>
    </div>
    <AssistantPanel />
  </div>
</template>
