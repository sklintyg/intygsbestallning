import * as utils from './utils'

export const fetchBestallning = id => utils.makeServerRequest('bestallningar/' + id);

export const accepteraBestallning = id => utils.makeServerPost('bestallningar/' + id + '/acceptera');

export const rejectBestallning = id => utils.makeServerPost('bestallningar/' + id + '/avvisa');
