import * as utils from './utils'

export const fetchBestallning = id => utils.makeServerRequest('bestallningar/' + id);

export const accepteraBestallning = (id, fritextForklaring) => 
  utils.makeServerPost('bestallningar/' + id + '/acceptera', fritextForklaring, {emptyBody:true});

export const rejectBestallning = (id, fritextForklaring) => 
  utils.makeServerPost('bestallningar/' + id + '/avvisa', fritextForklaring, {emptyBody:true});
