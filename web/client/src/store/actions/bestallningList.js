import * as api from '../../api/bestallningListApi';
import { getIsFetching } from '../reducers/bestallningList';

export const fetchBestallningList = (bestallningRequest) => (dispatch, getState) => {
  const {categoryFilter} = bestallningRequest

  if (getIsFetching(getState(), categoryFilter)) {
    return Promise.resolve();
  }

  dispatch({
    type: 'FETCH_BESTALLNINGAR_REQUEST',
    categoryFilter
  });

  return api.fetchBestallningList(bestallningRequest).then(
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
