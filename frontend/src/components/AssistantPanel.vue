<script setup>
import { nextTick, ref } from 'vue'
import { Bot, X, Sparkles, Plus, MessageSquare } from 'lucide-vue-next'
import { api, friendlyError } from '../services/api'

const options = [
  { label: 'Resumo das vendas deste mês', prompt: 'resumo de vendas deste mês', description: 'Faturamento, quantidade e comparação' },
  { label: 'Produtos mais vendidos', prompt: 'produtos mais vendidos deste mês', description: 'Ranking com base nas vendas registradas' },
  { label: 'Ver estoque de ingredientes', prompt: 'ver estoque atual de ingredientes', description: 'Estoque atual e mínimos cadastrados' },
  { label: 'Ingredientes abaixo do mínimo', prompt: 'ingredientes abaixo do mínimo', description: 'Itens que precisam de reposição' },
  { label: 'Sugerir produtos', prompt: 'sugestão de produtos com base nas vendas', description: 'Sugestões baseadas no histórico' },
  { label: 'Estimar ingredientes para produção', production: true, description: 'Escolha um produto e a quantidade' },
]

const open = ref(false)
const busy = ref(false)
const conversations = ref([])
const activeId = ref(null)
const messages = ref([])
const error = ref('')
const productionMode = ref(false)
const products = ref([])
const selectedProduct = ref('')
const quantity = ref(1)

async function loadConversations() {
  try {
    const { data } = await api.get('/api/ai-conversas')
    conversations.value = data || []
  } catch (e) {
    error.value = friendlyError(e)
  }
}

async function newConversation() {
  try {
    const title = 'Conversa ' + new Date().toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' })
    const { data } = await api.post('/api/ai-conversas', null, { params: { titulo: title } })
    activeId.value = data.id
    messages.value = []
    productionMode.value = false
    await loadConversations()
    return true
  } catch (e) {
    error.value = friendlyError(e)
    return false
  }
}

async function selectConversation(item) {
  activeId.value = item.id
  productionMode.value = false
  error.value = ''
  try {
    const { data } = await api.get(`/api/ai-conversas/${item.id}/mensagens`)
    messages.value = data || []
    await scrollToBottom()
  } catch (e) {
    error.value = friendlyError(e)
  }
}

async function send(prompt, displayText = prompt) {
  if (busy.value) return
  busy.value = true
  error.value = ''
  try {
    if (!activeId.value && !(await newConversation())) return
    const { data } = await api.post('/api/ia/mensagens', { conversaId: activeId.value, content: prompt })
    messages.value.push(
      { role: 'user', content: displayText, createdAt: new Date().toISOString() },
      data,
    )
    productionMode.value = false
    await scrollToBottom()
  } catch (e) {
    error.value = friendlyError(e)
  } finally {
    busy.value = false
  }
}

async function chooseOption(option) {
  if (option.production) {
    productionMode.value = true
    error.value = ''
    if (!products.value.length) {
      try {
        const { data } = await api.get('/api/ia/opcoes/produtos')
        products.value = (data || []).map(nome => ({ nome }))
      } catch (e) {
        error.value = friendlyError(e)
      }
    }
    if (!selectedProduct.value && products.value.length) selectedProduct.value = products.value[0].nome
    return
  }
  await send(option.prompt, option.label)
}

async function estimateProduction() {
  const amount = Number(quantity.value)
  if (!selectedProduct.value || !Number.isFinite(amount) || amount <= 0) {
    error.value = 'Selecione um produto e informe uma quantidade maior que zero.'
    return
  }
  await send(`estimar ingredientes para ${amount} unidades de ${selectedProduct.value}`,
    `Estimar ${amount} unidade(s) de ${selectedProduct.value}`)
}

async function scrollToBottom() {
  await nextTick()
  const el = document.querySelector('.chat-messages')
  if (el) el.scrollTop = el.scrollHeight
}

function toggle() {
  open.value = !open.value
  if (open.value && !conversations.value.length) loadConversations()
}
</script>

<template>
  <button class="assistant-fab" @click="toggle" :aria-label="open ? 'Fechar assistente' : 'Abrir assistente de gestão'">
    <X v-if="open" :size="21" />
    <Bot v-else :size="22" />
    <span v-if="!open">Assistente</span>
  </button>

  <Transition name="panel">
    <section v-if="open" class="assistant-panel" aria-label="Assistente de gestão BoloDeLaMadre">
      <header class="assistant-head">
        <div class="assistant-icon"><Sparkles :size="18" /></div>
        <div><strong>Assistente de gestão</strong><small>Escolha uma opção para consultar</small></div>
        <button class="icon-button" @click="newConversation" title="Nova conversa"><Plus :size="18" /></button>
      </header>

      <div class="conversation-strip">
        <button v-for="item in conversations.slice(0, 5)" :key="item.id" class="conversation-pill"
          :class="{ selected: activeId === item.id }" @click="selectConversation(item)">
          <MessageSquare :size="13" />{{ item.titulo || 'Conversa' }}
        </button>
      </div>

      <div class="chat-messages">
        <div v-if="!messages.length" class="chat-welcome">
          <div class="assistant-icon large"><Sparkles :size="22" /></div>
          <strong>O que você quer consultar?</strong>
          <p>Escolha uma opção. As respostas são automáticas e usam os registros do sistema.</p>
        </div>
        <article v-for="(message, index) in messages" :key="message.id || index" class="chat-message" :class="message.role">
          <span>{{ message.role === 'assistant' ? 'Assistente' : 'Você' }}</span>
          <p>{{ message.content }}</p>
        </article>
        <div v-if="busy" class="typing"><i></i><i></i><i></i></div>

        <section class="assistant-options" aria-label="Opções do assistente">
          <p>Escolha uma opção</p>
          <button v-for="option in options" :key="option.label" class="assistant-option"
            :disabled="busy" @click="chooseOption(option)">
            <strong>{{ option.label }}</strong><small>{{ option.description }}</small>
          </button>
        </section>
      </div>

      <form v-if="productionMode" class="production-form" @submit.prevent="estimateProduction">
        <label>Produto
          <select v-model="selectedProduct" required>
            <option v-for="product in products" :key="product.nome" :value="product.nome">{{ product.nome }}</option>
          </select>
        </label>
        <label>Quantidade
          <input v-model.number="quantity" type="number" min="0.01" step="0.01" required />
        </label>
        <button class="production-submit" :disabled="busy || !products.length">Calcular ingredientes</button>
        <small v-if="!products.length && !error">Não há produtos disponíveis para selecionar.</small>
      </form>

      <p v-if="error" class="inline-error">{{ error }}</p>
      <footer class="assistant-disclaimer">Respostas automáticas • Não usa modelo de IA</footer>
    </section>
  </Transition>
</template>
