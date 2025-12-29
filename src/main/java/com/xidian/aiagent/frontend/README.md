# AI Agent Frontend

Vue3 前端项目，包含AI恋爱大师和AI超级智能体两个聊天应用。

## 功能特性

- 🏠 主页：应用切换中心
- 💕 AI恋爱大师：专业的恋爱咨询助手
- 🤖 AI超级智能体：强大的智能助手
- 💬 实时SSE流式对话
- 🎨 现代化的聊天界面

## 技术栈

- Vue 3
- Vue Router 4
- Axios
- Vite

## 安装和运行

### 安装依赖

```bash
npm install
```

### 开发模式

```bash
npm run dev
```

项目将在 http://localhost:3000 启动

### 构建生产版本

```bash
npm run build
```

## 项目结构

```
src/
├── components/          # 组件
│   └── ChatMessage.vue # 聊天消息组件
├── views/              # 页面
│   ├── Home.vue        # 主页
│   ├── LoveApp.vue     # AI恋爱大师
│   └── ManusApp.vue    # AI超级智能体
├── router/             # 路由配置
│   └── index.js
├── utils/              # 工具函数
│   ├── axios.js        # Axios配置
│   └── sse.js          # SSE工具函数
├── App.vue             # 根组件
└── main.js             # 入口文件
```

## 接口说明

### AI恋爱大师
- 接口：`GET /api/ai/love_app/chat/sse`
- 参数：`message`（消息内容）、`chatId`（会话ID）
- 返回：SSE流式数据

### AI超级智能体
- 接口：`GET /api/ai/manus/chat`
- 参数：`message`（消息内容）
- 返回：SSE流式数据

## 注意事项

1. 确保后端服务运行在 `http://localhost:8123`
2. Vite开发服务器已配置代理，会自动转发 `/api` 请求到后端
3. 每个聊天会话会自动生成唯一的会话ID（仅恋爱大师应用）


