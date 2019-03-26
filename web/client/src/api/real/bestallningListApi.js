import * as utils from "./utils";

export const fetchBestallningList = (categoryFilter, textFilter) =>
    utils.makeServerRequest(utils.buildUrlFromParams('bestallningar', {
      category: categoryFilter,
      textSearch: textFilter
    }));
