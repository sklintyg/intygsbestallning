import { combineReducers } from 'redux'

const welcomeStats = (state = { unread: 0, active: 0, completed: 0 }, action) => {
  switch (action.type) {
    case 'FETCH_WELCOMESTATS_SUCCESS':
      return action.response
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case 'FETCH_WELCOMESTATS_REQUEST':
      return true
    case 'FETCH_WELCOMESTATS_SUCCESS':
    case 'FETCH_WELCOMESTATS_FAILURE':
      return false
    default:
      return state
  }
}

const welcomeStatsReducer = combineReducers({
  welcomeStats,
  isFetching,
})

export default welcomeStatsReducer

// Selectors

export const getWelcomeStats = (state) => {
  return state.welcomeStats.welcomeStats
}

export const getIsFetching = (state) => {
  return state.welcomeStats.isFetching
}
