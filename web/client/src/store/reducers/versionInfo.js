import { combineReducers } from 'redux'
import { buildClientError } from './util'
import * as ActionConstants from '../actions/versionInfo'

export const VersionInfoDefaultState = { versionInfo: {buildVersion: 'Hämtar...'} }

const versionInfo = (state = { ...VersionInfoDefaultState }, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_VERSIONINFO_SUCCESS:
      return action.response
    default:
      return state
  }
}

const isFetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_VERSIONINFO_REQUEST:
      return true
    case ActionConstants.FETCH_VERSIONINFO_SUCCESS:
    case ActionConstants.FETCH_VERSIONINFO_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_VERSIONINFO_FAILURE:
      return buildClientError(action.payload, 'error.versionInfo').message
    case ActionConstants.FETCH_VERSIONINFO_REQUEST:
    case ActionConstants.FETCH_VERSIONINFO_SUCCESS:
      return null
    default:
      return state
  }
}

export default combineReducers({
  versionInfo,
  isFetching,
  errorMessage,
})

// Selectors

export const getVersionInfo = (state) => state.versionInfo

export const getIsFetching = (state) => state.isFetching

export const getErrorMessage = (state) => state.errorMessage
