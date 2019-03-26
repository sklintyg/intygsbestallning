const proxy = require('http-proxy-middleware');

module.exports = function(app) {
  const url = 'http://localhost:8080/';

  app.use(proxy('/services', { target: url, changeOrigin: false }));
  app.use(proxy('/fake', { target: url, changeOrigin: false }));
  app.use(proxy('/api', { target: url, changeOrigin: false }));
};
