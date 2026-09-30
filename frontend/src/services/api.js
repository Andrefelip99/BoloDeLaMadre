import axios from 'axios'
import { clearSession, readSession } from './session'

export const api = axios.create({
  baseURL: (import.meta.env.DEV ? '' : (import.meta.env.VITE_API_URL || 'https://bolodelamadre.onrender.com')).replace(/\/$/, ''),
  timeout: 150000,
  headers: { Accept: 'application/json' },
})

api.interceptors.request.use((config) => {
  const session = readSession()
  if (session?.basic) config.headers.Authorization = `Basic ${session.basic}`
  return config
})

api.interceptors.response.use((response) => response, (error) => {
  if (error.response?.status === 401) {
    clearSession()
    if (window.location.pathname !== '/login') window.location.assign('/login?expired=1')
  }
  return Promise.reject(error)
})

export function friendlyError(error) {
  if (error?.code === 'ECONNABORTED') return 'O servidor demorou mais que o esperado. O primeiro acesso pode levar cerca de 2 minutos; tente novamente.'
  if (!error?.response) return 'Não foi possível conectar ao backend. Verifique a rede e a URL da API; em produção, o backend também precisa liberar CORS para o domínio do frontend.'
  const { status, data } = error.response
  const message = typeof data === 'string' ? data : data?.message
  if (status === 400) return message || 'Confira os dados informados.'
  if (status === 401) return 'Login ou senha inválidos. A sessão também pode ter expirado.'
  if (status === 403) return 'Seu perfil não tem permissão para executar esta ação.'
  if (status === 404) return message || 'O registro solicitado não foi encontrado.'
  if (status === 409) return message || 'Este registro já existe ou conflita com outro.'
  if (status === 422) return message || 'Não foi possível validar os dados enviados.'
  if (status >= 500) return 'O backend encontrou um erro. Tente novamente mais tarde.'
  return message || 'Ocorreu um erro inesperado.'
}

export const endpoints = {
  kpis: (mes, ano) => api.get('/api/v1/relatorios/kpis', { params: { mes, ano } }),
  dre: (mes, ano) => api.get('/api/v1/relatorios/dre', { params: { mes, ano } }),
  insights: (mes, ano) => api.get('/api/v1/relatorios/insights', { params: { mes, ano } }),
  vendas: () => api.get('/api/vendas'),
  produtos: () => api.get('/api/produtos'),
  compras: () => api.get('/api/compras'),
  clientes: () => api.get('/api/clientes'),
  fornecedores: () => api.get('/api/fornecedores'),
  categorias: () => api.get('/api/categorias'),
  ingredientes: () => api.get('/api/ingredientes'),
  receitas: () => api.get('/api/receitas'),
  movimentacoes: () => api.get('/api/movimentacoes-estoque'),
  funcionarios: () => api.get('/api/funcionarios'),
  financeiro: (mes, ano) => api.get('/api/lancamentos-financeiros', { params: { mes, ano } }),
  resumoFinanceiro: (mes, ano) => api.get('/api/lancamentos-financeiros/resumo', { params: { mes, ano } }),
}

export async function verifyCredentials(username, password) {
  const basic = btoa(`${username}:${password}`)
  // Ask the backend for the authenticated user's actual role; never infer it from the login name.
  const response = await axios.get(`${api.defaults.baseURL}/api/usuarios/me`, {
    headers: { Authorization: `Basic ${basic}` }, timeout: 150000,
    validateStatus: (status) => status === 200 || status === 401,
  })
  if (response.status === 401) throw { response: { status: 401 } }
  return {
    basic,
    username: response.data?.username || username,
    role: response.data?.role === 'ADMIN' ? 'ADMIN' : 'USER',
  }
}
