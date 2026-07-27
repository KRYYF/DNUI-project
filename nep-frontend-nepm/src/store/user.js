import { defineStore } from 'pinia'
import { ref } from 'vue'

const STORAGE_KEY = 'nepm_user'

export const useUserStore = defineStore('user', () => {
  const token = ref('')
  const adminId = ref(null)
  const adminCode = ref('')

  // 从 localStorage 恢复
  try {
    const saved = localStorage.getItem(STORAGE_KEY)
    if (saved) {
      const parsed = JSON.parse(saved)
      token.value = parsed.token || ''
      adminId.value = parsed.adminId || null
      adminCode.value = parsed.adminCode || ''
    }
  } catch {
    localStorage.removeItem(STORAGE_KEY)
  }

  function setUser(payload) {
    token.value = payload.token
    adminId.value = payload.adminId
    adminCode.value = payload.adminCode
    localStorage.setItem(STORAGE_KEY, JSON.stringify({
      token: payload.token,
      adminId: payload.adminId,
      adminCode: payload.adminCode,
    }))
  }

  function clearUser() {
    token.value = ''
    adminId.value = null
    adminCode.value = ''
    localStorage.removeItem(STORAGE_KEY)
  }

  return { token, adminId, adminCode, setUser, clearUser }
})
