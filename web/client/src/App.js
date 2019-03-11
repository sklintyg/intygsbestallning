import React, {Fragment} from 'react';
import {HashRouter, Switch, NavLink} from 'react-router-dom'
import Route from 'react-router-dom/Route';
import HomePage from "./pages/IndexPage";
import ValjEnhetPage from "./pages/ValjEnhetPage";
import BestallningarPage from "./pages/BestallningarPage";
import BestallningPage from "./pages/BestallningPage";

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
            <Route replace path="/bestallningar" component={BestallningarPage} />
            <Route path="/bestallning/:id" component={BestallningPage} />
          </Switch>
        </Fragment>
    </HashRouter>
  )
};

export default App;
