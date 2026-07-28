import { defineStore } from 'pinia'
import { ref } from 'vue'

const STORAGE_KEY = 'nepg_user'

export const useUserStore = defineStore('user', () => {

  const token = ref('')
  const gmId = ref(null)
  const gmName = ref('')
  const gmCode = ref('')


  // 恢复登录状态
  try {

    const saved = localStorage.getItem(STORAGE_KEY)

    if (saved) {

      const parsed = JSON.parse(saved)

      token.value = parsed.token || ''
      gmId.value = parsed.gmId || null
      gmName.value = parsed.gmName || ''
      gmCode.value = parsed.gmCode || ''

    }

  } catch {

    localStorage.removeItem(STORAGE_KEY)

  }



  function setUser(payload) {

    token.value = payload.token
    gmId.value = payload.gmId
    gmName.value = payload.gmName
    gmCode.value = payload.gmCode


    localStorage.setItem(
      STORAGE_KEY,
      JSON.stringify({

        token: payload.token,
        gmId: payload.gmId,
        gmName: payload.gmName,
        gmCode: payload.gmCode,

      })
    )

  }



  function clearUser(){

    token.value=''
    gmId.value=null
    gmName.value=''
    gmCode.value=''

    localStorage.removeItem(STORAGE_KEY)

  }



  return {

    token,
    gmId,
    gmName,
    gmCode,

    setUser,
    clearUser

  }

})