import { combineReducers } from 'redux'
import * as ActionConstants from '../actions/stat'

const stat = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_STAT_SUCCESS:
      return action.response
    default:
      return state
  }
}

const fetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_STAT_REQUEST:
      return true
    case ActionConstants.FETCH_STAT_SUCCESS:
    case ActionConstants.FETCH_STAT_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_STAT_FAILURE:
      return action.payload
    default:
      return state
  }
}

export default combineReducers({
  stat,
  fetching,
  errorMessage,
})

export const getStat = (state) => state.stat.stat

export const isFetching = (state) => state.stat.fetching

export const getErrorMessage = (state) => state.stat.errorMessage
