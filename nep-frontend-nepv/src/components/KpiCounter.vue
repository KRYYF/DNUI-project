<template>
  <div class="kpi-counter">
    <div class="kpi-title">{{ title }}</div>
    <div class="kpi-value">
      <span ref="valueRef" class="value-number">{{ displayValue }}</span>
      <span class="value-unit" v-if="unit">{{ unit }}</span>
    </div>
    <div class="kpi-icon">
      <el-icon :size="28"><component :is="icon" /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'

const props = defineProps({
  title: { type: String, required: true },
  value: { type: Number, default: 0 },
  unit: { type: String, default: '' },
  duration: { type: Number, default: 1500 },
  icon: { type: String, default: 'DataAnalysis' }
})

const displayValue = ref(0)

function animateValue(from, to, duration) {
  const start = performance.now()
  function step(now) {
    const elapsed = now - start
    const progress = Math.min(elapsed / duration, 1)
    const eased = 1 - Math.pow(1 - progress, 3)
    displayValue.value = Math.round(from + (to - from) * eased)
    if (progress < 1) {
      requestAnimationFrame(step)
    }
  }
  requestAnimationFrame(step)
}

watch(() => props.value, (newVal, oldVal) => {
  animateValue(oldVal || 0, newVal, props.duration)
}, { immediate: false })

onMounted(() => {
  animateValue(0, props.value, props.duration)
})
</script>

<style scoped>
.kpi-counter {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 6px 12px;
  position: relative;
}

.kpi-title {
  color: #b0bcc8;
  font-size: 12px;
  margin-bottom: 4px;
}

.kpi-value {
  display: flex;
  align-items: baseline;
  gap: 3px;
}

.value-number {
  font-size: 28px;
  font-weight: 700;
  color: #00e5ff;
  text-shadow: 0 0 16px rgba(0, 229, 255, 0.5);
  font-family: 'DIN', 'Helvetica Neue', sans-serif;
}

.value-unit {
  font-size: 12px;
  color: #a8b8cc;
}

.kpi-icon {
  position: absolute;
  top: 8px;
  right: 12px;
  color: rgba(0, 229, 255, 0.35);
}
</style>
