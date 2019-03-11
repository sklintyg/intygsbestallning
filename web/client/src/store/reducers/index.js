import { combineReducers } from 'redux'
import byId, * as fromById from './byId';
import createBestallningarList, * as fromList from './createBestallningarList';
import user from "./user";

const listBestallningarByFilter = combineReducers({
  activeBestallningar: createBestallningarList('active'),
  completedBestallningar: createBestallningarList('completed'),
  rejectedBestallningar: createBestallningarList('rejected'),
});

const appReducer = combineReducers({
  listBestallningarByFilter,
  byId,
  user,
});

const reducers = (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(state, action)
};

export default reducers;

export const getVisibleBestallningar = (state, filter) => {
  const ids = fromList.getIds(state.listByFilter[filter]);
  return ids.map(id => fromById.getBestallning(state.byId, id));
};

export const getIsFetching = (state, filter) =>
  fromList.getIsFetching(state.listByFilter[filter]);

export const getErrorMessage = (state, filter) =>
  fromList.getErrorMessage(state.listByFilter[filter]);
