const { createServer } = require('vercel-java');
// 路径改为 decrypt/target 下的 jar 包（注意替换为你实际的 jar 包名）
const app = require('./decrypt/target/decrypt-1.0.jar');

module.exports.handler = createServer(app);