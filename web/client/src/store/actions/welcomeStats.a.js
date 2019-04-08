import * as api from '../../api/welcomeStatsApi'
import { getIsFetching } from '../reducers/welcomeStats.r'

export const fetchWelcomeStats = () => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: 'FETCH_WELCOMESTATS_REQUEST',
  })

  return api.fetchWelcomeStats().then(
    (response) => {
      dispatch({
        type: 'FETCH_WELCOMESTATS_SUCCESS',
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'FETCH_WELCOMESTATS_FAILURE',
        payload: errorResponse,
      })
    }
  )
}
