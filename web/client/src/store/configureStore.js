import { applyMiddleware, compose, createStore } from 'redux'

import rootReducer from './reducers'
import thunk from "redux-thunk";

export default function configureStore(preloadedState) {
  const middlewares = [thunk];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = compose(...enhancers);

  const store = createStore(rootReducer, preloadedState, composedEnhancers);

  return store
}
