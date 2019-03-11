import { combineReducers } from 'redux';

const createBestallningarList = (filter) => {
  const ids = (state = [], action) => {
    switch (action.type) {
      case 'FETCH_BESTALLNINGAR_SUCCESS':
        return filter === action.filter ?
          action.response.result :
          state;
      default:
        return state;
    }
  };

  const isFetching = (state = false, action) => {
    if (filter !== action.filter) {
      return state;
    }
    switch (action.type) {
      case 'FETCH_BESTALLNINGAR_REQUEST':
        return true;
      case 'FETCH_BESTALLNINGAR_SUCCESS':
      case 'FETCH_BESTALLNINGAR_FAILURE':
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
      case 'FETCH_BESTALLNINGAR_FAILURE':
        return action.message;
      case 'FETCH_BESTALLNINGAR_REQUEST':
      case 'FETCH_BESTALLNINGAR_SUCCESS':
        return null;
      default:
        return state;
    }
  };

  return combineReducers({
    ids,
    isFetching,
    errorMessage,
  });
};

export default createBestallningarList;

export const getIds = (state) => state.ids;
export const getIsFetching = (state) => state.isFetching;
export const getErrorMessage = (state) => state.errorMessage;
