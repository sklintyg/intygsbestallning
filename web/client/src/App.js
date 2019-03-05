import React from 'react';
import {HashRouter, Switch} from 'react-router-dom'
import Route from 'react-router-dom/Route';
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import HomePage from "./pages/IndexPage";

const theme = createMuiTheme({
  typography: {
    useNextVariants: true,
    h3: {
      fontSize: '2rem'
    }
  }
});

const App = () => {
  return (
    <HashRouter>
      <MuiThemeProvider theme={theme}>
        <CssBaseline></CssBaseline>
        <Switch>
          <Route component={HomePage} />
        </Switch>
      </MuiThemeProvider>
    </HashRouter>
  )
};

export default App;
