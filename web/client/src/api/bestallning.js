let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallningApi');
} else {
  api = require('./mock/bestallningApi')
}

export const fetchBestallning = id => api.fetchBestallning(id);

export const accepteraBestallning = (id, fritextForklaring) =>
  api.accepteraBestallning(id, fritextForklaring);

export const rejectBestallning = (id, fritextForklaring) =>
  api.rejectBestallning(id, fritextForklaring);

export const completeBestallning = (id) =>
  api.completeBestallning(id);
