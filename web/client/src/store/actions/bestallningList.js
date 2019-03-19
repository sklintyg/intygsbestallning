import * as api from '../../api/bestallningListApi';
import { getIsFetching } from '../reducers/bestallningList';

export const fetchBestallningList = (categoryFilter, textFilter) => (dispatch, getState) => {
  if (getIsFetching(getState(), categoryFilter)) {
    return Promise.resolve();
  }

  dispatch({
    type: 'FETCH_BESTALLNINGAR_REQUEST',
    categoryFilter
  });

  return api.fetchBestallningList(categoryFilter, textFilter).then(
    response => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_SUCCESS',
        categoryFilter,
        response: response,
      });
    },
    error => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_FAILURE',
        categoryFilter,
        message: error.message || 'Something went wrong.',
      });
    }
  );
};
