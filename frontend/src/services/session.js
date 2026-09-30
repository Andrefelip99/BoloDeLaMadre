const KEY = 'bolodelamadre.session'

export function readSession() {
  try { return JSON.parse(sessionStorage.getItem(KEY) || 'null') } catch { return null }
}

export function saveSession(session) {
  sessionStorage.setItem(KEY, JSON.stringify(session))
}

export function clearSession() {
  sessionStorage.removeItem(KEY)
}

export function userRole(session = readSession()) {
  return session?.role || 'USER'
}

export function isAdmin(session = readSession()) {
  return userRole(session) === 'ADMIN'
}
