import { combineReducers } from 'redux'
import bestallningar from './bestallningar';
import user from "./user";
import { connectRouter } from 'connected-react-router'

const appReducer = (history) => combineReducers({
  bestallningar,
  user,
  router: connectRouter(history)
});



const reducers = (history) => (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(history)(state, action)
};

export default reducers;
