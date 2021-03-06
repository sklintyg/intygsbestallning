import bestallningarDb, {decorateForBestallning, bestallningsConfig} from './bestallningarFakeDb';
import { delay } from './util';

export const fetchBestallning = id =>
  delay(500).then(() => {
    return bestallningsConfig(decorateForBestallning([...bestallningarDb]).find(t => t.id === id));
  });

export const accepteraBestallning = id =>
  delay(500).then(() => {
    return {status: 'oklart'};
  });
