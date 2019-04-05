import * as api from '../../api/statApi'
import { isFetching } from '../reducers/stat'

export const FETCH_STAT_REQUEST = 'FETCH_STAT_REQUEST'
export const FETCH_STAT_SUCCESS = 'FETCH_STAT_SUCCESS'
export const FETCH_STAT_FAILURE = 'FETCH_STAT_FAILURE'

export const fetchStat = () => (dispatch, getState) => {
  if (isFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_STAT_REQUEST,
  })

  return api.fetchStat().then(
    (response) => {
      dispatch({
        type: FETCH_STAT_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_STAT_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
