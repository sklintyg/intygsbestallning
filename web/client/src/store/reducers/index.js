import { combineReducers } from 'redux'
import user from "./user";

const appReducer = combineReducers({user});

const reducers = (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(state, action)
};

export default reducers;
