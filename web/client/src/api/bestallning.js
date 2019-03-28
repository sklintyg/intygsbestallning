let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallning');
} else {
  api = require('./mock/bestallning')
}

export const fetchBestallning = id => api.fetchBestallning(id);

export const accepteraBestallning = (id, fritextForklaring) =>
  api.accepteraBestallning(id, fritextForklaring);

export const rejectBestallning = (id, fritextForklaring) =>
  api.rejectBestallning(id, fritextForklaring);
