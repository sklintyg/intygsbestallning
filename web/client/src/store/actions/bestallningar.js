import { normalize } from 'normalizr';
import * as schema from './schema';
import * as api from '../../api/bestallningList';
import { getIsFetching } from '../reducers/bestallningar';

export const fetchBestallningar = (filter) => (dispatch, getState) => {
  if (getIsFetching(getState(), filter)) {
    return Promise.resolve();
  }

  dispatch({
    type: 'FETCH_BESTALLNINGAR_REQUEST',
    filter,
  });

  return api.fetchBestallningar(filter).then(
    response => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_SUCCESS',
        filter,
        response: normalize(response, schema.arrayOfBestallningar),
      });
    },
    error => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_FAILURE',
        filter,
        message: error.message || 'Something went wrong.',
      });
    }
  );
};
