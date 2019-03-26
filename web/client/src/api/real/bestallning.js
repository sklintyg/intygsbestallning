import * as utils from './utils'

export const fetchBestallning = id => utils.makeServerRequest("bestallningar/" + id);

export const setStatus = (id, status) => Promise.resolve({id});
