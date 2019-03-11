import { Schema, arrayOf } from 'normalizr';

export const bestallning = new Schema('bestallningar');
export const arrayOfBestallningar = arrayOf(bestallning);
