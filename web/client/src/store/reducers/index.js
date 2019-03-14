import { combineReducers } from 'redux'
import bestallningar from './bestallningar';
import user from "./user";
import { connectRouter } from 'connected-react-router'
import bestallning from './bestallning';

const appReducer = (history) => combineReducers({
  bestallningar,
  user,
  router: connectRouter(history),
  bestallning,
});



const reducers = (history) => (state, action) => {
  if ((action.payload && action.payload.response && action.payload.response.status === 401)){
    state = undefined;
  }
  return appReducer(history)(state, action)
};

export default reducers;
