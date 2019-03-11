import { applyMiddleware, compose, createStore } from 'redux'
import thunk from 'redux-thunk';
import rootReducer from './reducers'

export default function configureStore(preloadedState) {
  const middlewares = [thunk];
  const middlewareEnhancer = applyMiddleware(...middlewares);

  const enhancers = [middlewareEnhancer];
  const composedEnhancers = compose(...enhancers);

  const store = createStore(rootReducer, preloadedState, composedEnhancers);

  return store
}
