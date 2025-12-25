const { spawn } = require('child_process');
const httpProxy = require('http-proxy');

// 启动 Spring Boot 进程（只启动一次）
let javaProcess = null;
function startJava() {
  if (!javaProcess) {
    javaProcess = spawn('java', ['-jar', 'decrypt/target/decrypt-1.0.jar']);
    // 打印 Java 日志到 Vercel 控制台
    javaProcess.stdout.on('data', (data) => console.log(`Java: ${data}`));
    javaProcess.stderr.on('data', (data) => console.error(`Java Error: ${data}`));
  }
  return new Promise(resolve => setTimeout(resolve, 5000)); // 等待 Java 启动
}

// 创建代理（转发请求到 Spring Boot 的 8080 端口）
const proxy = httpProxy.createProxyServer({ target: 'http://localhost:8080' });

// Vercel Serverless 处理函数
module.exports = async (req, res) => {
  await startJava(); // 启动 Java 进程
  proxy.web(req, res);
};