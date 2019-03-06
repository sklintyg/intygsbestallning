import React from 'react';
import {HashRouter, Switch, NavLink} from 'react-router-dom'
import Route from 'react-router-dom/Route';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import HomePage from "./pages/IndexPage";
import ValjEnhetPage from "./pages/ValjEnhetPage";
import BestallningarPage from "./pages/BestallningarPage";
import BestallningPage from "./pages/BestallningPage";

const theme = createMuiTheme({
  typography: {
    useNextVariants: true,
    h3: {
      fontSize: '2rem'
    }
  }
});

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
      <MuiThemeProvider theme={theme}>
        <CssBaseline></CssBaseline>
        <TestLinks />
        <Switch>
          <Route exact path="/" component={HomePage} />
          <Route replace path="/valj-enhet" component={ValjEnhetPage} />
          <Route replace path="/bestallningar" component={BestallningarPage} />
          <Route path="/bestallning/:id" component={BestallningPage} />
        </Switch>
      </MuiThemeProvider>
    </HashRouter>
  )
};

export default App;
