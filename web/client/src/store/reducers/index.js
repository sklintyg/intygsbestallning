import {combineReducers} from 'redux'
import bestallningList from './bestallningList'
import user from './user'
import {connectRouter} from 'connected-react-router'
import bestallning from './bestallning'
import modal from './modal'
import sessionPoll from './sessionPoll'
import cookieBanner from './cookieBanner'

const appReducer = (history) =>
  combineReducers({
    bestallningList,
    user,
    router: connectRouter(history),
    bestallning,
    modal,
    sessionPoll,
    cookieBanner,
  })

const reducers = (history) => (state, action) => {
  if (action.payload && action.payload.response && action.payload.response.status === 401) {
    state = undefined
  }
  return appReducer(history)(state, action)
}

export default reducers
