<template>
  <div class="home-container">
    <div class="home-content">
      <h1 class="title">AI Agent 应用中心</h1>
      <p class="subtitle">选择您想要使用的AI应用</p>
      
      <div class="app-cards">
        <div class="app-card" @click="goToLoveApp">
          <div class="card-icon">💕</div>
          <h2 class="card-title">AI 恋爱大师</h2>
          <p class="card-description">专业的恋爱咨询助手，为您提供情感建议</p>
        </div>
        
        <div class="app-card" @click="goToManusApp">
          <div class="card-icon">🤖</div>
          <h2 class="card-title">AI 超级智能体</h2>
          <p class="card-description">强大的智能助手，帮您解决各种问题</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

const goToLoveApp = () => {
  router.push('/love-app')
}

const goToManusApp = () => {
  router.push('/manus-app')
}
</script>

<style scoped>
.home-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.home-content {
  text-align: center;
  color: white;
}

.title {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 16px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.subtitle {
  font-size: 20px;
  margin-bottom: 60px;
  opacity: 0.9;
}

.app-cards {
  display: flex;
  gap: 40px;
  justify-content: center;
  flex-wrap: wrap;
}

.app-card {
  width: 320px;
  padding: 40px 30px;
  background: white;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.app-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.3);
}

.card-icon {
  font-size: 64px;
  margin-bottom: 20px;
}

.card-title {
  font-size: 24px;
  color: #333;
  margin-bottom: 12px;
  font-weight: 600;
}

.card-description {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}
</style>


