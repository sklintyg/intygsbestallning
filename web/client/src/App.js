import React, {Fragment} from 'react';
import {HashRouter, NavLink, Switch} from 'react-router-dom'
import Route from 'react-router-dom/Route';
import HomePage from "./pages/IndexPage";
import ValjEnhetPage from "./pages/ValjEnhetPage";
import BestallningarPage from "./pages/BestallningarPage";
import BestallningPage from "./pages/BestallningPage";
import {getUser} from "./store/actions/UserActions";
import {connect} from "react-redux";
import {compose, lifecycle} from "recompose";


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
    <HashRouter>
        <Fragment>
          <TestLinks />
          <Switch>
            <Route exact path="/" component={HomePage} />
            <Route replace path="/valj-enhet" component={ValjEnhetPage} />
            <Route replace path="/bestallningar/(:filter)" component={BestallningarPage} />
            <Route path="/bestallning/:id" component={BestallningPage} />
          </Switch>
        </Fragment>
    </HashRouter>
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
