/**
 * 创建SSE连接
 * @param {string} url - SSE接口地址
 * @param {Object} params - 请求参数
 * @param {Function} onMessage - 消息回调函数
 * @param {Function} onError - 错误回调函数
 * @returns {EventSource} EventSource实例
 */
export function createSSEConnection(url, params, onMessage, onError) {
  // 构建查询字符串
  const queryString = new URLSearchParams(params).toString()
  const fullUrl = `${url}?${queryString}`
  
  const eventSource = new EventSource(fullUrl)
  
  eventSource.onmessage = (event) => {
    if (onMessage) {
      onMessage(event.data)
    }
  }
  
  eventSource.onerror = (error) => {
    if (onError) {
      onError(error)
    }
    eventSource.close()
  }
  
  return eventSource
}

/**
 * 关闭SSE连接
 * @param {EventSource} eventSource - EventSource实例
 */
export function closeSSEConnection(eventSource) {
  if (eventSource) {
    eventSource.close()
  }
}


