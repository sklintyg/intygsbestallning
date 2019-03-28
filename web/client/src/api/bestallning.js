let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallning');
} else {
  api = require('./mock/bestallning')
}

export const fetchBestallning = id => api.fetchBestallning(id);

export const accepteraBestallning = id => api.accepteraBestallning(id);

export const rejectBestallning = id => api.rejectBestallning(id);
