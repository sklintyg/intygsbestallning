import * as utils from './utils'

export const fetchBestallning = id => utils.makeServerRequest("bestallningar/1");

export const setStatus = (id, status) => Promise.resolve({id});
