import { defineStore } from 'pinia'
import { ref } from 'vue'

const STORAGE_KEY = 'neps_user'

export const useUserStore = defineStore('user', () => {
  const token = ref('')
  const supervisorId = ref(null)
  const realName = ref('')

  const saved = localStorage.getItem(STORAGE_KEY)
  if (saved) {
    try {
      const data = JSON.parse(saved)
      token.value = data.token || ''
      supervisorId.value = data.supervisorId ?? null
      realName.value = data.realName || ''
    } catch {
      localStorage.removeItem(STORAGE_KEY)
    }
  }

  function setUser(payload) {
    token.value = payload.token || ''
    supervisorId.value = payload.supervisorId ?? null
    realName.value = payload.realName || ''
    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({
        token: token.value,
        supervisorId: supervisorId.value,
        realName: realName.value,
      })
    )
  }

  function clearUser() {
    token.value = ''
    supervisorId.value = null
    realName.value = ''
    localStorage.removeItem(STORAGE_KEY)
  }

  return { token, supervisorId, realName, setUser, clearUser }
})
