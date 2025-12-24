const { createServer } = require('vercel-java');
const app = require('./target/decrypt-1.0.jar');

module.exports.handler = createServer(app);