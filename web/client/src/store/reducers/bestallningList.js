import { combineReducers } from "redux";

const createBestallningarList = filter => {
  const bestallningList = (
    state = { bestallningList: [], totalCount: 0 },
    action
  ) => {
    switch (action.type) {
      case "FETCH_BESTALLNINGAR_SUCCESS":
        return filter === action.filter ? action.response : state;
      default:
        return state;
    }
  };

  const isFetching = (state = false, action) => {
    if (filter !== action.filter) {
      return state;
    }
    switch (action.type) {
      case "FETCH_BESTALLNINGAR_REQUEST":
        return true;
      case "FETCH_BESTALLNINGAR_SUCCESS":
      case "FETCH_BESTALLNINGAR_FAILURE":
        return false;
      default:
        return state;
    }
  };

  const errorMessage = (state = null, action) => {
    if (filter !== action.filter) {
      return state;
    }
    switch (action.type) {
      case "FETCH_BESTALLNINGAR_FAILURE":
        return action.message;
      case "FETCH_BESTALLNINGAR_REQUEST":
      case "FETCH_BESTALLNINGAR_SUCCESS":
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
  active: createBestallningarList('active'),
  completed: createBestallningarList('completed'),
  rejected: createBestallningarList('rejected'),
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
