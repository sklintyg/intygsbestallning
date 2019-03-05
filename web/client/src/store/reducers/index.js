import { combineReducers } from 'redux'

const appReducer = combineReducers({});

const reducers = (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(state, action)
};

export default reducers;
