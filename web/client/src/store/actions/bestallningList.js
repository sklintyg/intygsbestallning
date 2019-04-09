import * as api from '../../api/bestallningListApi'
import { getIsFetching } from '../reducers/bestallningList'

export const FETCH_BESTALLNINGAR_REQUEST = 'FETCH_BESTALLNINGAR_REQUEST'
export const FETCH_BESTALLNINGAR_SUCCESS = 'FETCH_BESTALLNINGAR_SUCCESS'
export const FETCH_BESTALLNINGAR_FAILURE = 'FETCH_BESTALLNINGAR_FAILURE'

export const fetchBestallningList = (bestallningRequest) => (dispatch, getState) => {
  const { categoryFilter } = bestallningRequest

  if (getIsFetching(getState(), categoryFilter)) {
    return Promise.resolve()
  }

  dispatch({
    type: 'FETCH_BESTALLNINGAR_REQUEST',
    categoryFilter,
  })

  return api.fetchBestallningList(bestallningRequest).then(
    (response) => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_SUCCESS',
        categoryFilter,
        response: response,
      })
    },
    (errorResponse) => {
      dispatch({
        type: 'FETCH_BESTALLNINGAR_FAILURE',
        categoryFilter,
        payload: errorResponse,
      })
    }
  )
}
