<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, LockKeyhole, SearchX } from 'lucide-vue-next'
import AppShell from '../components/AppShell.vue'
import { isAdmin } from '../services/session'
const props=defineProps({code:{type:String,default:'404'}});const router=useRouter();const forbidden=computed(()=>props.code==='403')
function back(){router.replace(forbidden.value?'/dashboard':'/')}
</script>
<template><AppShell v-if="forbidden"><section class="status-page"><div class="status-illustration"><LockKeyhole :size="31"/></div><span class="eyebrow">ERRO {{ code }}</span><h1>Acesso não autorizado</h1><p>Seu perfil não possui permissão para acessar este recurso.</p><button class="button-primary" @click="back"><ArrowLeft :size="16"/> Voltar ao painel</button></section></AppShell><main v-else class="standalone-status"><div class="status-illustration"><SearchX :size="31"/></div><span class="eyebrow">ERRO {{ code }}</span><h1>Página não encontrada</h1><p>O endereço acessado não corresponde a uma área deste sistema.</p><button class="button-primary" @click="back"><ArrowLeft :size="16"/> Ir para o início</button></main></template>
