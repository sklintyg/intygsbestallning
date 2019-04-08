let api;

if (process.env.NODE_ENV === 'production' || false) {
  //api = require('./real/welcomeStatsApi')
} else {
  api = require('./mock/welcomeStatsApi');
}

export const fetchWelcomeStats = () => {
  return api.fetchWelcomeStats()
}