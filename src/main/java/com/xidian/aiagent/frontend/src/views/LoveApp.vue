<template>
  <div class="chat-container">
    <div class="chat-header">
      <button class="back-btn" @click="goHome">← 返回</button>
      <h2 class="header-title">AI 恋爱大师</h2>
      <div class="chat-id">会话ID: {{ chatId }}</div>
    </div>
    
    <div class="chat-messages" ref="messagesContainer">
      <div v-if="messages.length === 0" class="empty-state">
        <div class="empty-icon">💕</div>
        <p>开始与AI恋爱大师对话吧~</p>
      </div>
      <ChatMessage 
        v-for="(message, index) in messages" 
        :key="index" 
        :message="message" 
      />
      <div v-if="isLoading" class="loading-indicator">
        <div class="typing-dots">
          <span></span>
          <span></span>
          <span></span>
        </div>
      </div>
    </div>
    
    <div class="chat-input-container">
      <input
        v-model="inputMessage"
        @keyup.enter="sendMessage"
        :disabled="isLoading"
        class="chat-input"
        type="text"
        placeholder="输入您的问题..."
      />
      <button 
        @click="sendMessage" 
        :disabled="isLoading || !inputMessage.trim()"
        class="send-btn"
      >
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import ChatMessage from '../components/ChatMessage.vue'
import { createSSEConnection, closeSSEConnection } from '../utils/sse'

const router = useRouter()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const chatId = ref('')
const messagesContainer = ref(null)
let eventSource = null

// 生成聊天室ID
const generateChatId = () => {
  return 'chat_' + Date.now() + '_' + Math.random().toString(36).substr(2, 9)
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 发送消息
const sendMessage = () => {
  if (!inputMessage.value.trim() || isLoading.value) return
  
  const userMessage = {
    role: 'user',
    content: inputMessage.value,
    timestamp: Date.now()
  }
  
  messages.value.push(userMessage)
  const message = inputMessage.value
  inputMessage.value = ''
  isLoading.value = true
  scrollToBottom()
  
  // 创建AI消息占位符
  let aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'ai',
    content: '',
    timestamp: Date.now()
  })
  
  // 关闭之前的连接
  if (eventSource) {
    closeSSEConnection(eventSource)
  }
  
  // 创建SSE连接
  eventSource = createSSEConnection(
    '/api/ai/love_app/chat/sse',
    {
      message: message,
      chatId: chatId.value
    },
    (data) => {
      // 累积接收到的数据
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].content += data
        scrollToBottom()
      }
    },
    (error) => {
      console.error('SSE Error:', error)
      isLoading.value = false
      if (messages.value[aiMessageIndex] && !messages.value[aiMessageIndex].content) {
        messages.value[aiMessageIndex].content = '抱歉，发生了错误，请重试。'
      }
    }
  )
  
  // 监听连接关闭
  eventSource.addEventListener('close', () => {
    isLoading.value = false
    closeSSEConnection(eventSource)
    eventSource = null
  })
}

// 返回主页
const goHome = () => {
  if (eventSource) {
    closeSSEConnection(eventSource)
  }
  router.push('/')
}

onMounted(() => {
  chatId.value = generateChatId()
})

onUnmounted(() => {
  if (eventSource) {
    closeSSEConnection(eventSource)
  }
})
</script>

<style scoped>
.chat-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f5f5f5;
}

.chat-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.back-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
  padding: 8px 16px;
  border-radius: 8px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s;
}

.back-btn:hover {
  background: rgba(255, 255, 255, 0.3);
}

.header-title {
  font-size: 20px;
  font-weight: 600;
}

.chat-id {
  font-size: 12px;
  opacity: 0.9;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px;
  background: #f5f5f5;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #999;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
}

.loading-indicator {
  display: flex;
  justify-content: flex-start;
  margin-bottom: 20px;
}

.typing-dots {
  display: flex;
  gap: 4px;
  padding: 12px 16px;
  background: white;
  border-radius: 12px;
  border: 1px solid #e0e0e0;
}

.typing-dots span {
  width: 8px;
  height: 8px;
  background: #999;
  border-radius: 50%;
  animation: typing 1.4s infinite;
}

.typing-dots span:nth-child(2) {
  animation-delay: 0.2s;
}

.typing-dots span:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes typing {
  0%, 60%, 100% {
    transform: translateY(0);
    opacity: 0.7;
  }
  30% {
    transform: translateY(-10px);
    opacity: 1;
  }
}

.chat-input-container {
  display: flex;
  gap: 12px;
  padding: 16px 24px;
  background: white;
  border-top: 1px solid #e0e0e0;
}

.chat-input {
  flex: 1;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 24px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s;
}

.chat-input:focus {
  border-color: #667eea;
}

.chat-input:disabled {
  background: #f5f5f5;
  cursor: not-allowed;
}

.send-btn {
  padding: 12px 32px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  border-radius: 24px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: opacity 0.3s;
}

.send-btn:hover:not(:disabled) {
  opacity: 0.9;
}

.send-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
</style>


