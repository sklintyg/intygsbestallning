import { applyMiddleware, compose, createStore } from 'redux'

import rootReducer from './reducers'

export default function configureStore(preloadedState) {
  const middlewareEnhancer = applyMiddleware();

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = compose(...enhancers);

  const store = createStore(rootReducer, preloadedState, composedEnhancers);

  return store
}
