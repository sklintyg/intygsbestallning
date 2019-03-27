import * as utils from "./utils";

export const fetchBestallningList = ({categoryFilter, textFilter, pageIndex}) =>
    utils.makeServerRequest(utils.buildUrlFromParams('bestallningar', {
      category: categoryFilter,
      textSearch: textFilter,
      pageIndex: pageIndex,
      limit: 4,
      sortColumn: 'ANKOMST_DATUM',
      sortDirection: 'DESC'
    }));
