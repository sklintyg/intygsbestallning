import bestallningarDb, {decorateForBestallning, bestallningsConfig} from "./bestallningarFakeDb";
import * as utils from '../store/actions/utils'

const delay = ms => new Promise(resolve => setTimeout(resolve, ms));
const useFake = false;

export const fetchBestallning = id => {
    if (useFake) {
      return delay(500).then(() => {
        return bestallningsConfig(decorateForBestallning([...bestallningarDb]).find(t => t.id === id));
      });
    } else {
      return utils.makeServerRequest("bestallningar/1");
    }
  }
export const setStatus = (id, status) =>
    delay(500).then(() => {
        return status;
    });