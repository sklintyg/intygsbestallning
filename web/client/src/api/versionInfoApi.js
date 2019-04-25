let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/versionInfoApi');
} else {
  api = require('./mock/versionInfoApi')
}

export const fetchVersionInfo = () => api.fetchVersionInfo();
