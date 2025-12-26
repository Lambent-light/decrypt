import { useState } from 'react'
import { decryptData } from './utils/crypto-utils'
import './index.css' // 确保使用最新的 index.css

function App() {
  const [input, setInput] = useState('')
  const [output, setOutput] = useState('')

  const formatJSON = (text: string) => {
    try {
      // 尝试解析并格式化 JSON
      const obj = JSON.parse(text)
      return JSON.stringify(obj, null, 2)
    } catch (e) {
      // 如果不是有效的 JSON，直接返回原文本
      return text
    }
  }

  const handleDecrypt = () => {
    if (!input.trim()) {
      setOutput('请在左侧输入需要解密的内容...')
      return
    }
    const decrypted = decryptData(input)
    const formatted = formatJSON(decrypted)
    setOutput(formatted)
  }

  return (
    <div className="main-container">
      {/* 左侧面板：输入 */}
      <div className="left-panel">
        <h1>解密工具</h1>
        <div className="input-section">
          <label htmlFor="encrypt-input">加密内容</label>
          <textarea
            id="encrypt-input"
            placeholder='此处粘贴加密内容 (自动处理 "data": 前缀和引号)...'
            value={input}
            onChange={(e) => setInput(e.target.value)}
          />
        </div>
        <button onClick={handleDecrypt}>
          立即解密
        </button>
        <div style={{ marginTop: 'auto', fontSize: '0.75rem', color: '#94a3b8' }}>

        </div>
      </div>

      {/* 右侧面板：结果 */}
      <div className="right-panel">
        <div className="result-header">
          <h2>解密结果</h2>
          {output && (
            <button
              style={{ padding: '0.4rem 1rem', fontSize: '0.8rem', width: 'auto', margin: 0 }}
              onClick={() => {
                navigator.clipboard.writeText(output)
                alert('已复制到剪贴板')
              }}
            >
              复制结果
            </button>
          )}
        </div>
        <div className="result-box">
          {output || '等待输入并点击解密按钮...'}
        </div>
      </div>
    </div>
  )
}

export default App
