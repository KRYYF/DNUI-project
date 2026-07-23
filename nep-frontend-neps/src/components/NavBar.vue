<template>
  <van-nav-bar
    :title="title"
    :left-arrow="showBack"
    fixed
    placeholder
    @click-left="onBack"
  >
    <template v-if="rightText" #right>
      <span class="nav-right" @click="$emit('click-right')">{{ rightText }}</span>
    </template>
  </van-nav-bar>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  title: { type: String, default: '' },
  showBack: { type: Boolean, default: true },
  rightText: { type: String, default: '' },
  backTo: { type: String, default: '' },
})

defineEmits(['click-right'])

const router = useRouter()

function onBack() {
  if (props.backTo) {
    router.replace(props.backTo)
    return
  }
  if (window.history.length > 1) {
    router.back()
  } else {
    router.replace('/login')
  }
}
</script>

<style scoped>
.nav-right {
  color: #1989fa;
  font-size: 14px;
}
</style>
