let api;

if (process.env.NODE_ENV === 'production' || true) {
  api = require('./real/bestallningListApi')
} else {
  api = require('./mock/bestallningListApi');
}

export const fetchBestallningList = (bestallningRequest) => {

  let {pageIndex, sortColumn, sortDirection} = bestallningRequest

  if(!pageIndex) {
    pageIndex = 0
  }

  if(!sortColumn) {
    sortColumn = 'ANKOMST_DATUM'
  }

  if(!sortDirection) {
    sortDirection = 'DESC'
  }

  return api.fetchBestallningList({...bestallningRequest, pageIndex, sortColumn, sortDirection})
}