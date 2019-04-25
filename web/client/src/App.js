import React, {Fragment} from 'react'
import {HashRouter, Switch} from 'react-router-dom'
import HomePage from './pages/IndexPage'
import SelectEnhetPage from './pages/SelectEnhetPage'
import BestallningarIndexPage from './pages/BestallningarIndexPage'
import BestallningarPage from './pages/BestallningarPage'
import BestallningPage from './pages/BestallningPage'
import Header from './components/header'
import {getUser} from './store/actions/user'
import {connect} from 'react-redux'
import {compose, lifecycle} from 'recompose'
import SecuredRoute from './components/auth/SecuredRoute'
import UnsecuredRoute from './components/auth/UnsecuredRoute'
import {history} from './store/configureStore'
import {ConnectedRouter} from 'connected-react-router'
import {closeAllModals} from './store/actions/modal'
import ErrorPage from './pages/ErrorPage'
import ErrorModal from './components/errorModal'
import TestLinks from './components/TestLinks/TestLinks'
import SessionPoller from './components/sessionPoller'
import CookieBanner from './components/cookieBanner/CookieBanner'
import CookieModal from './components/cookieModal/CookieModal'

const App = () => {
  return (
    <ConnectedRouter history={history}>
      <HashRouter>
        <Fragment>
          <SessionPoller/>
          {process.env.NODE_ENV !== 'production' && <TestLinks />}
          <Header />
          <CookieBanner />
          <ErrorModal />
          <CookieModal />
          <Switch>
            <UnsecuredRoute exact path="/" component={HomePage} />
            <UnsecuredRoute path="/loggedout/:method" component={HomePage} />
            <SecuredRoute allowMissingUnit={true} path="/valj-enhet" component={SelectEnhetPage} />
            <SecuredRoute path="/bestallningar/:filter" component={BestallningarPage} />
            <SecuredRoute path="/bestallningar" component={BestallningarIndexPage} />
            <SecuredRoute path="/bestallning/:id" component={BestallningPage} />
            <UnsecuredRoute path="/exit/:errorCode/:logId?" isErrorPage={true} component={ErrorPage} />
          </Switch>
        </Fragment>
      </HashRouter>
    </ConnectedRouter>
  )
}

const lifeCycleValues = {
  componentWillMount() {
    this.props.getUser()
  },
  componentDidMount() {
    this.unlisten = history.listen((location) => {
      console.log('Route path changed to: ' + location.pathname)
      this.props.closeAllModals()
    })
  },
  componentWillUnmount() {
    this.unlisten()
  },
}

// expose selected dispachable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getUser: () => dispatch(getUser()),
    closeAllModals: () => dispatch(closeAllModals()),
  }
}

// enhance APP using compose with connect and lifecycle so we can use them in APp
export default compose(
  connect(
    null,
    mapDispatchToProps
  ),
  lifecycle(lifeCycleValues)
)(App)
