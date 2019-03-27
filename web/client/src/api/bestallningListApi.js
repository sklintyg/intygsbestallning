let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallningListApi')
} else {
  api = require('./mock/bestallningListApi');
}

export const fetchBestallningList = (bestallningRequest) => api.fetchBestallningList(bestallningRequest)
