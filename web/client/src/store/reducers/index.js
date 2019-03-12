import { combineReducers } from 'redux'
import bestallningar from './bestallningar';
import user from "./user";

const appReducer = combineReducers({
  bestallningar,
  user,
});

const reducers = (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(state, action)
};

export default reducers;