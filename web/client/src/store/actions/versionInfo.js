import * as api from '../../api/versionInfoApi'
import { getIsFetching } from '../reducers/versionInfo'

export const FETCH_VERSIONINFO_REQUEST = 'FETCH_VERSIONINFO_REQUEST'
export const FETCH_VERSIONINFO_SUCCESS = 'FETCH_VERSIONINFO_SUCCESS'
export const FETCH_VERSIONINFO_FAILURE = 'FETCH_VERSIONINFO_FAILURE'

export const fetchVersionInfo = () => (dispatch, getState) => {
  if (getIsFetching(getState())) {
    return Promise.resolve()
  }

  dispatch({
    type: FETCH_VERSIONINFO_REQUEST
  })

  return api.fetchVersionInfo().then(
    (response) => {
      dispatch({
        type: FETCH_VERSIONINFO_SUCCESS,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: FETCH_VERSIONINFO_FAILURE,
        payload: errorResponse,
      })
    }
  )
}
