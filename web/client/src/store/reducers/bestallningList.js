import { combineReducers } from 'redux';
import { buildClientError } from './util';
import * as ActionConstants from '../actions/bestallningList'

export const BestallningListDefaultState = { bestallningar: [], 'pageIndex': 0, 'totalPages': 0, 'numberOfElements': 0, 'totalElements': 0, 'sortColumn': 'ID', 'sortDirection': 'ASC' }

const createBestallningarList = filter => {
  const bestallningList = (
    state = {...BestallningListDefaultState},
    action
  ) => {
    switch (action.type) {
      case ActionConstants.FETCH_BESTALLNINGAR_SUCCESS:
        return filter === action.categoryFilter ? action.response : state;
      default:
        return state;
    }
  };

  const isFetching = (state = false, action) => {
    if (filter !== action.categoryFilter) {
      return state;
    }
    switch (action.type) {
      case ActionConstants.FETCH_BESTALLNINGAR_REQUEST:
        return true;
      case ActionConstants.FETCH_BESTALLNINGAR_SUCCESS:
      case ActionConstants.FETCH_BESTALLNINGAR_FAILURE:
        return false;
      default:
        return state;
    }
  };

  const errorMessage = (state = null, action) => {
    if (filter !== action.categoryFilter) {
      return state;
    }
    switch (action.type) {
      case ActionConstants.FETCH_BESTALLNINGAR_FAILURE:
        return buildClientError(action.payload, 'error.bestallninglist').message;
      case ActionConstants.FETCH_BESTALLNINGAR_REQUEST:
      case ActionConstants.FETCH_BESTALLNINGAR_SUCCESS:
        return null;
      default:
        return state;
    }
  };

  return combineReducers({
    bestallningList,
    isFetching,
    errorMessage
  });
};

const listBestallningarByFilter = combineReducers({
  AKTUELLA: createBestallningarList('AKTUELLA'),
  KLARA: createBestallningarList('KLARA'),
  AVVISADE: createBestallningarList('AVVISADE'),
});

const bestallningarReducer = combineReducers({
  listBestallningarByFilter
});

export default bestallningarReducer;

// Selectors

export const getVisibleBestallningList = (state, filter) =>
  state.bestallningList.listBestallningarByFilter[filter].bestallningList;

export const getIsFetching = (state, filter) =>
  state.bestallningList.listBestallningarByFilter[filter].isFetching;

export const getErrorMessage = (state, filter) =>
  state.bestallningList.listBestallningarByFilter[filter].errorMessage;
