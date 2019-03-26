let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallningListApi')
} else {
  api = require('./mock/bestallningListApi');
}

export const fetchBestallningList = (categoryFilter, textFilter) => api.fetchBestallningList(categoryFilter, textFilter);
