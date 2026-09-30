<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { CakeSlice, Eye, EyeOff, ShieldCheck, Clock3, Sparkles, Info, LoaderCircle, ArrowUpRight } from 'lucide-vue-next'
import { verifyCredentials } from '../services/api'
import { saveSession } from '../services/session'

const username=ref(''); const password=ref(''); const reveal=ref(false); const showInfo=ref(false); const loading=ref(false); const error=ref(''); const waitingText=ref(false); let waitTimer
const route=useRoute(); const router=useRouter()
onMounted(()=>{if(route.query.expired)error.value='Sua sessão expirou. Entre novamente para continuar.'})
async function login() {
  error.value=''; if (!username.value.trim() || !password.value) { error.value='Informe seu login e sua senha.'; return }
  loading.value=true; waitingText.value=false; clearTimeout(waitTimer); waitTimer=setTimeout(()=>{waitingText.value=true},9000)
  try { const session=await verifyCredentials(username.value.trim(),password.value); saveSession(session); router.replace(route.query.redirect || '/dashboard') }
  catch (e) { error.value=e?.response?.status===401 ? 'Login ou senha inválidos.' : 'O backend está demorando ou indisponível. Aguarde e tente novamente. Se estiver no navegador, o servidor precisa permitir CORS.' }
  finally { clearTimeout(waitTimer); loading.value=false; waitingText.value=false }
}
</script>

<template>
  <main class="login-page">
    <div class="login-orbit orbit-one"></div><div class="login-orbit orbit-two"></div>
    <section class="login-layout">
      <div class="login-intro"><div class="brand login-brand"><div class="brand-mark"><CakeSlice :size="22"/></div><div><strong>BoloDeLaMadre</strong><small>GESTÃO EMPRESARIAL</small></div></div><div class="intro-copy"><span class="eyebrow"><Sparkles :size="14"/> PLATAFORMA DE GESTÃO</span><h1>Clareza para<br/><em>fazer acontecer.</em></h1><p>Um espaço único para acompanhar a operação, cuidar do estoque e tomar decisões com confiança.</p><div class="intro-points"><div><ShieldCheck :size="17"/><span>Acesso protegido por perfil</span></div><div><Clock3 :size="17"/><span>Indicadores em tempo real</span></div></div></div><div class="intro-bottom">GESTÃO INTEGRADA <span>·</span> CONFEITARIA</div></div>
      <div class="login-card-wrap"><form class="login-card" @submit.prevent="login"><div class="mobile-login-brand"><span class="brand-mark"><CakeSlice :size="21"/></span><strong>BoloDeLaMadre</strong></div><span class="eyebrow">BEM-VINDO DE VOLTA</span><h2>Acesse sua conta</h2><p class="login-subtitle">Entre com suas credenciais para continuar.</p>
        <label class="field-label" for="username">Login</label><div class="input-wrap"><span class="input-symbol">@</span><input id="username" v-model="username" autocomplete="username" placeholder="Seu nome de usuário" required/></div>
        <label class="field-label" for="password">Senha</label><div class="input-wrap"><ShieldCheck class="input-icon" :size="17"/><input id="password" v-model="password" :type="reveal?'text':'password'" autocomplete="current-password" placeholder="Sua senha" required/><button class="reveal-button" type="button" @click="reveal=!reveal" :aria-label="reveal?'Ocultar senha':'Mostrar senha'"><EyeOff v-if="reveal" :size="17"/><Eye v-else :size="17"/></button></div>
        <p v-if="error" class="form-error">{{ error }}</p><button class="button-primary login-submit" :disabled="loading"><LoaderCircle v-if="loading" class="spin" :size="18"/><span>{{ loading?'Conectando…':'Entrar no sistema' }}</span><ArrowUpRight v-if="!loading" :size="17"/></button><p v-if="loading && waitingText" class="wait-hint">O servidor gratuito pode levar até cerca de 2 minutos para despertar. Aguarde, estamos tentando conectar.</p>
        <button class="login-info-toggle" type="button" @click="showInfo=!showInfo"><Info :size="16"/> Informações para login <span>{{ showInfo?'−':'+' }}</span></button>
        <Transition name="fade"><div v-if="showInfo" class="demo-credentials"><div><strong>Administrador</strong><small>login <code>admin</code> <span>·</span> senha <code>BoloAdmin#2026</code></small></div><div><strong>Usuário operacional</strong><small>login <code>user</code> <span>·</span> senha <code>BoloUser#2026</code></small></div></div></Transition>
        <div class="login-notice"><Info :size="16"/><p>Projeto pessoal hospedado na versão gratuita do Render. O primeiro acesso pode levar aproximadamente 2 minutos. Os dados exibidos são fictícios e fazem parte de uma simulação.</p></div>
      </form><p class="login-credit">Desenvolvido por: André Felipe</p></div>
    </section>
  </main>
</template>
