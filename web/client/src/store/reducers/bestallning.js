import { combineReducers } from 'redux'
import * as ActionConstants from '../actions/bestallning'
import {buildClientError} from "./util"

export const FAIL_MODAL_HEADER = 'Ett tekniskt fel uppstod'
export const DELETE_FAIL_MODAL_HEADER = 'Ett fel uppstod vid radering.'

const bestallning = (state = {}, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BESTALLNING_SUCCESS:
      return action.response
    case ActionConstants.ACCEPTERA_BESTALLNING_SUCCESS:
      return { ...state, status: 'Accepterad' }
    case ActionConstants.REJECT_BESTALLNING_SUCCESS:
      return { ...state, status: 'Avvisad' }
    case ActionConstants.COMPLETE_BESTALLNING_SUCCESS:
      return { ...state, status: 'Klar' }
    case ActionConstants.FETCH_BESTALLNING_REQUEST:
      return {}
    default:
      return state
  }
}

const fetching = (state = false, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BESTALLNING_REQUEST:
      return true
    case ActionConstants.FETCH_BESTALLNING_SUCCESS:
    case ActionConstants.FETCH_BESTALLNING_FAILURE:
      return false
    default:
      return state
  }
}

const errorMessage = (state = null, action) => {
  switch (action.type) {
    case ActionConstants.FETCH_BESTALLNING_FAILURE:
      return buildClientError(action.payload)
    case ActionConstants.REJECT_BESTALLNING_FAILURE:
    case ActionConstants.ACCEPTERA_BESTALLNING_FAILURE:
    case ActionConstants.COMPLETE_BESTALLNING_FAILURE:
      return { ...action.payload, modal: buildClientError(action.payload) }
    case ActionConstants.DELETE_BESTALLNING_FAILURE:
      if (action.payload.error && action.payload.error.logId) {
        return { ...action.payload, modal: { ...action.payload.error, title: DELETE_FAIL_MODAL_HEADER, message:'' } }
      }
      return {...action.payload, modal: {...buildClientError(action.payload), title: DELETE_FAIL_MODAL_HEADER}}
    case ActionConstants.FETCH_BESTALLNING_REQUEST:
    case ActionConstants.FETCH_BESTALLNING_SUCCESS:
    case ActionConstants.REJECT_BESTALLNING_SUCCESS:
    case ActionConstants.REJECT_BESTALLNING_REQUEST:
    case ActionConstants.ACCEPTERA_BESTALLNING_REQUEST:
    case ActionConstants.ACCEPTERA_BESTALLNING_SUCCESS:
    case ActionConstants.DELETE_BESTALLNING_REQUEST:
    case ActionConstants.DELETE_BESTALLNING_SUCCESS:
      return null
    default:
      return state
  }
}

export default combineReducers({
  bestallning,
  fetching,
  errorMessage,
})

export const getBestallning = (state) => state.bestallning.bestallning

export const isFetching = (state) => state.bestallning.fetching

export const getErrorMessage = (state) => state.bestallning.errorMessage
