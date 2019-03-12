import { combineReducers } from 'redux'
import byId, * as fromById from './byId';
import createBestallningarList, * as fromList from './createBestallningarList';

const listBestallningarByFilter = combineReducers({
  active: createBestallningarList('active'),
  completed: createBestallningarList('completed'),
  rejected: createBestallningarList('rejected'),
});

const bestallningarReducer = combineReducers({
  listBestallningarByFilter,
  byId,
});

export default bestallningarReducer;

// Selectors

export const getVisibleBestallningar = (state, filter) => {
  const ids = fromList.getIds(state.bestallningar.listBestallningarByFilter[filter]);
  return ids.map(id => fromById.getBestallning(state.bestallningar.byId, id));
};

export const getIsFetching = (state, filter) =>
  fromList.getIsFetching(state.bestallningar.listBestallningarByFilter[filter]);

export const getErrorMessage = (state, filter) =>
  fromList.getErrorMessage(state.bestallningar.listBestallningarByFilter[filter]);
