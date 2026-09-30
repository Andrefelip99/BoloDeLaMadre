<script setup>
import { ref, nextTick } from 'vue'
import { Bot, X, Send, Sparkles, Plus, MessageSquare } from 'lucide-vue-next'
import { api, friendlyError } from '../services/api'

const open = ref(false); const busy = ref(false); const text = ref(''); const conversations = ref([]); const activeId = ref(null); const messages = ref([]); const error = ref('')
async function loadConversations() { try { const { data } = await api.get('/api/ai-conversas'); conversations.value = data || [] } catch (e) { error.value = friendlyError(e) } }
async function newConversation() {
  try { const title = 'Conversa ' + new Date().toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' }); const { data } = await api.post('/api/ai-conversas', null, { params: { titulo: title } }); activeId.value = data.id; messages.value = []; await loadConversations() } catch (e) { error.value = friendlyError(e) }
}
async function selectConversation(item) { activeId.value = item.id; try { const { data } = await api.get(`/api/ai-conversas/${item.id}/mensagens`); messages.value = data || [] } catch (e) { error.value = friendlyError(e) } }
async function send() {
  const content = text.value.trim(); if (!content || busy.value) return
  busy.value = true; error.value = ''
  try {
    if (!activeId.value) await newConversation()
    if (!activeId.value) return
    text.value = ''
    const { data } = await api.post('/api/ia/mensagens', { conversaId: activeId.value, content })
    messages.value.push({ role: 'user', content, createdAt: new Date().toISOString() }, data)
    await nextTick(); const el = document.querySelector('.chat-messages'); if (el) el.scrollTop = el.scrollHeight
  } catch (e) { error.value = friendlyError(e) } finally { busy.value = false }
}
function toggle() { open.value=!open.value; if (open.value && !conversations.value.length) loadConversations() }
</script>

<template>
  <button class="assistant-fab" @click="toggle" :aria-label="open ? 'Fechar assistente' : 'Abrir assistente IA'"><X v-if="open" :size="21"/><Bot v-else :size="22"/><span v-if="!open">Assistente</span></button>
  <Transition name="panel"><section v-if="open" class="assistant-panel" aria-label="Assistente BoloDeLaMadre">
    <header class="assistant-head"><div class="assistant-icon"><Sparkles :size="18"/></div><div><strong>Assistente</strong><small>Vendas, estoque e operação</small></div><button class="icon-button" @click="newConversation" title="Nova conversa"><Plus :size="18"/></button></header>
    <div class="conversation-strip"><button v-for="item in conversations.slice(0,5)" :key="item.id" class="conversation-pill" :class="{ selected:activeId===item.id }" @click="selectConversation(item)"><MessageSquare :size="13"/>{{ item.titulo || 'Conversa' }}</button></div>
    <div class="chat-messages">
      <div v-if="!messages.length" class="chat-welcome"><div class="assistant-icon large"><Sparkles :size="22"/></div><strong>Como posso ajudar?</strong><p>Pergunte sobre vendas, estoque ou desempenho. As respostas dependem dos dados disponíveis no sistema.</p></div>
      <article v-for="(message, index) in messages" :key="message.id || index" class="chat-message" :class="message.role"><span>{{ message.role==='assistant' ? 'Assistente' : 'Você' }}</span><p>{{ message.content }}</p></article>
      <div v-if="busy" class="typing"><i></i><i></i><i></i></div>
    </div>
    <p v-if="error" class="inline-error">{{ error }}</p>
    <form class="chat-compose" @submit.prevent="send"><textarea v-model="text" rows="2" maxlength="4000" placeholder="Escreva sua pergunta…" @keydown.enter.exact.prevent="send"/><button class="send-button" :disabled="busy || !text.trim()" aria-label="Enviar mensagem"><Send :size="17"/></button></form>
  </section></Transition>
</template>
