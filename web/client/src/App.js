import React, {Fragment} from 'react';
import {HashRouter, NavLink, Switch} from 'react-router-dom'
import HomePage from "./pages/IndexPage";
import ValjEnhetPage from "./pages/ValjEnhetPage";
import BestallningarIndexPage from "./pages/BestallningarIndexPage";
import BestallningarPage from "./pages/BestallningarPage";
import BestallningPage from "./pages/BestallningPage";
import Header from "./components/header";
import {getUser} from "./store/actions/UserActions";
import {connect} from "react-redux";
import {compose, lifecycle} from "recompose";
import SecuredRoute from "./components/auth/securedRoute/SecuredRoute";
import UnsecuredRoute from "./components/auth/unsecuredRoute/UnsecuredRoute";
import {history} from "./store/configureStore";
import {ConnectedRouter} from "connected-react-router";

// TEST
const TestLinks = () => (
  <nav>
    <NavLink exact to="/">
      start
    </NavLink> | <NavLink to="/valj-enhet">
      valj-enhet
    </NavLink> | <NavLink to="/bestallningar">
      bestallningar
    </NavLink>
  </nav>
);


const App = () => {
  return (
    <ConnectedRouter history={history}>
      <HashRouter>
          <Fragment>
            <TestLinks />
            <Header/>
            <Switch>
              <UnsecuredRoute exact path="/" component={HomePage}/>
              <SecuredRoute allowMissingUnit={true} path="/valj-enhet" component={ValjEnhetPage} />
              <SecuredRoute path="/bestallningar/:filter" component={BestallningarPage} />
              <SecuredRoute path="/bestallningar" component={BestallningarIndexPage} />
              <SecuredRoute path="/bestallning/:id" component={BestallningPage} />
            </Switch>
          </Fragment>
      </HashRouter>
    </ConnectedRouter>
  )
};

const lifeCycleValues = {
  componentWillMount() {
    this.props.getUser();
  }
};

// expose selected dispachable methods to App props
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getUser: () => dispatch(getUser())
  }
};

// enhance APP using compose with connect and lifecycle so we can use them in APp
export default compose(
  connect(null, mapDispatchToProps),
  lifecycle(lifeCycleValues)
)(App);
